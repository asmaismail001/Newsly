package com.example.newsly.ui.news

import com.example.newsly.data.model.NewsArticle

sealed interface NewsUiState {
    data object Loading : NewsUiState
    data class Success(
        val featuredArticles: List<NewsArticle>,
        val articles: List<NewsArticle>,
        val isRefreshing: Boolean = false
    ) : NewsUiState
    data object Empty : NewsUiState
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : NewsUiState
}
