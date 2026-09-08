package com.example.newsly.data.repository

import com.example.newsly.data.local.NewsDatabase
import com.example.newsly.data.local.SavedArticleDao
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.data.remote.NewsApiService
import com.example.newsly.data.remote.NewsFallbackData
import com.example.newsly.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NewsRepository(
    private val apiService: NewsApiService = RetrofitClient.newsApiService,
    private val savedArticleDao: SavedArticleDao
) {

    suspend fun getNewsByCategory(category: String, page: Int = 1): Result<List<NewsArticle>> = withContext(Dispatchers.IO) {
        try {
            val apiCategory = if (category.equals("All", ignoreCase = true) || category.equals("Breaking News", ignoreCase = true)) {
                "general"
            } else {
                category.lowercase()
            }

            val response = apiService.getTopHeadlines(
                category = apiCategory,
                page = page,
                apiKey = RetrofitClient.newsApiKey
            )

            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.articles ?: response.body()?.results ?: emptyList()
                val isBreaking = category.contains("Breaking", ignoreCase = true)
                val articles = dtoList.mapIndexed { index, dto ->
                    dto.toDomainModel(
                        categoryFallback = category,
                        isBreaking = isBreaking && index == 0
                    )
                }

                if (articles.isNotEmpty()) {
                    return@withContext Result.success(attachBookmarkState(articles))
                }
            }

            // If API returns empty or error, use rich fallback data
            val fallbackArticles = NewsFallbackData.getFallbackArticles(category)
            Result.success(attachBookmarkState(fallbackArticles))
        } catch (e: Exception) {
            // Network error fallback
            val fallbackArticles = NewsFallbackData.getFallbackArticles(category)
            Result.success(attachBookmarkState(fallbackArticles))
        }
    }

    suspend fun searchNews(query: String, page: Int = 1): Result<List<NewsArticle>> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            return@withContext getNewsByCategory("All", page)
        }

        try {
            val response = apiService.searchNews(
                query = query,
                page = page,
                apiKey = RetrofitClient.newsApiKey
            )

            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.articles ?: response.body()?.results ?: emptyList()
                val articles = dtoList.map { dto ->
                    dto.toDomainModel(categoryFallback = "Search Result")
                }
                if (articles.isNotEmpty()) {
                    return@withContext Result.success(attachBookmarkState(articles))
                }
            }

            // Fallback filter
            val allArticles = NewsFallbackData.getFallbackArticles("All")
            val filtered = allArticles.filter {
                it.title.contains(query, ignoreCase = true) ||
                (it.description?.contains(query, ignoreCase = true) == true) ||
                (it.content?.contains(query, ignoreCase = true) == true) ||
                it.category.contains(query, ignoreCase = true)
            }
            Result.success(attachBookmarkState(filtered))
        } catch (e: Exception) {
            val allArticles = NewsFallbackData.getFallbackArticles("All")
            val filtered = allArticles.filter {
                it.title.contains(query, ignoreCase = true) ||
                (it.description?.contains(query, ignoreCase = true) == true) ||
                it.category.contains(query, ignoreCase = true)
            }
            Result.success(attachBookmarkState(filtered))
        }
    }

    suspend fun getArticleById(id: String): NewsArticle? = withContext(Dispatchers.IO) {
        // Check in saved articles first
        val saved = savedArticleDao.getSavedArticleById(id)
        if (saved != null) {
            return@withContext saved.toDomainModel()
        }

        // Check fallback cache
        val fallback = NewsFallbackData.getFallbackArticles("All").find { it.id == id }
        if (fallback != null) {
            val isBookmarked = isArticleSavedFlow(id).first()
            return@withContext fallback.copy(isBookmarked = isBookmarked)
        }

        null
    }

    private suspend fun attachBookmarkState(articles: List<NewsArticle>): List<NewsArticle> {
        val savedEntities = savedArticleDao.getAllSavedArticles().first()
        val savedIds = savedEntities.map { it.id }.toSet()
        return articles.map { it.copy(isBookmarked = savedIds.contains(it.id)) }
    }

    fun getAllSavedArticles(): Flow<List<NewsArticle>> {
        return savedArticleDao.getAllSavedArticles()
            .map { list -> list.map { it.toDomainModel() } }
            .flowOn(Dispatchers.IO)
    }

    fun isArticleSavedFlow(id: String): Flow<Boolean> {
        return savedArticleDao.isArticleSaved(id).flowOn(Dispatchers.IO)
    }

    suspend fun toggleBookmark(article: NewsArticle) = withContext(Dispatchers.IO) {
        val isSaved = savedArticleDao.getSavedArticleById(article.id) != null
        if (isSaved) {
            savedArticleDao.deleteSavedArticle(article.id)
        } else {
            savedArticleDao.insertSavedArticle(article.toSavedEntity())
        }
    }

    suspend fun removeBookmark(id: String) = withContext(Dispatchers.IO) {
        savedArticleDao.deleteSavedArticle(id)
    }

    fun searchSavedArticles(query: String): Flow<List<NewsArticle>> {
        return savedArticleDao.searchSavedArticles(query)
            .map { list -> list.map { it.toDomainModel() } }
            .flowOn(Dispatchers.IO)
    }
}
