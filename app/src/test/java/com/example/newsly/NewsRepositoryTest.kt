package com.example.newsly

import com.example.newsly.data.local.SavedArticleDao
import com.example.newsly.data.local.SavedArticleEntity
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.data.remote.NewsFallbackData
import com.example.newsly.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeSavedArticleDao : SavedArticleDao {
    private val savedMap = mutableMapOf<String, SavedArticleEntity>()

    override fun getAllSavedArticles(): Flow<List<SavedArticleEntity>> {
        return flowOf(savedMap.values.toList())
    }

    override fun isArticleSaved(id: String): Flow<Boolean> {
        return flowOf(savedMap.containsKey(id))
    }

    override fun getSavedArticleById(id: String): SavedArticleEntity? {
        return savedMap[id]
    }

    override fun insertSavedArticle(article: SavedArticleEntity): Long {
        savedMap[article.id] = article
        return 1L
    }

    override fun deleteSavedArticle(id: String): Int {
        return if (savedMap.remove(id) != null) 1 else 0
    }

    override fun searchSavedArticles(query: String): Flow<List<SavedArticleEntity>> {
        return flowOf(savedMap.values.filter { it.title.contains(query, true) })
    }

    override fun clearAllSaved(): Int {
        val count = savedMap.size
        savedMap.clear()
        return count
    }
}

class NewsRepositoryTest {

    private lateinit var repository: NewsRepository
    private lateinit var fakeDao: FakeSavedArticleDao

    @Before
    fun setup() {
        fakeDao = FakeSavedArticleDao()
        repository = NewsRepository(savedArticleDao = fakeDao)
    }

    @Test
    fun getFallbackArticles_returnsArticlesForCategory() {
        val articles = NewsFallbackData.getFallbackArticles("Technology")
        assertTrue(articles.isNotEmpty())
        assertTrue(articles.any { it.category.equals("Technology", ignoreCase = true) })
    }

    @Test
    fun toggleBookmark_savesAndRemovesArticle() = runBlocking {
        val article = NewsArticle(
            id = "test-1",
            title = "Test Article",
            description = "Test Description",
            content = "Test Content",
            url = "https://test.com",
            imageUrl = null,
            publishedAt = "2026-09-06T12:00:00Z",
            sourceName = "Test Source",
            author = "Author",
            category = "Technology"
        )

        // Save
        repository.toggleBookmark(article)
        val saved = fakeDao.getSavedArticleById("test-1")
        assertNotNull(saved)
        assertEquals("Test Article", saved?.title)

        // Remove
        repository.toggleBookmark(article)
        val removed = fakeDao.getSavedArticleById("test-1")
        assertEquals(null, removed)
    }

    @Test
    fun searchNews_filtersFallbackDataProperly() = runBlocking {
        val result = repository.searchNews("Quantum")
        assertTrue(result.isSuccess)
        val articles = result.getOrNull()
        assertNotNull(articles)
        assertTrue(articles!!.isNotEmpty())
    }
}
