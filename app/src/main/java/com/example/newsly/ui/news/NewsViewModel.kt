package com.example.newsly.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.data.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    init {
        // Load initial news feed
        loadNews()

        // Debounce search query changes
        _searchQuery
            .debounce(400)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isNotBlank()) {
                    performSearch(query)
                } else if (_isSearchActive.value) {
                    loadNews(_selectedCategory.value)
                }
            }
            .launchIn(viewModelScope)

        // Observe bookmark changes from Room DB to keep UI state in sync
        newsRepository.getAllSavedArticles()
            .onEach { savedArticles ->
                val savedIds = savedArticles.map { it.id }.toSet()
                val current = _uiState.value
                if (current is NewsUiState.Success) {
                    _uiState.value = current.copy(
                        featuredArticles = current.featuredArticles.map {
                            it.copy(isBookmarked = savedIds.contains(it.id))
                        },
                        articles = current.articles.map {
                            it.copy(isBookmarked = savedIds.contains(it.id))
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        if (_selectedCategory.value == category) return
        _selectedCategory.value = category
        _searchQuery.value = ""
        _isSearchActive.value = false
        loadNews(category)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            _isSearchActive.value = true
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _isSearchActive.value = false
        loadNews(_selectedCategory.value)
    }

    fun toggleSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
            loadNews(_selectedCategory.value)
        }
    }

    fun refreshNews() {
        val current = _uiState.value
        if (current is NewsUiState.Success) {
            _uiState.value = current.copy(isRefreshing = true)
        }
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        } else {
            loadNews(_selectedCategory.value)
        }
    }

    fun retry() {
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        } else {
            loadNews(_selectedCategory.value)
        }
    }

    fun toggleBookmark(article: NewsArticle) {
        viewModelScope.launch {
            newsRepository.toggleBookmark(article)
        }
    }

    private fun loadNews(category: String = _selectedCategory.value) {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            val result = newsRepository.getNewsByCategory(category)
            result.onSuccess { articles ->
                if (articles.isEmpty()) {
                    _uiState.value = NewsUiState.Empty
                } else {
                    val featured = articles.filter { it.isBreaking }.ifEmpty { articles.take(1) }
                    val remaining = if (featured.isNotEmpty()) articles.filter { it.id != featured.first().id } else articles
                    _uiState.value = NewsUiState.Success(
                        featuredArticles = featured,
                        articles = remaining,
                        isRefreshing = false
                    )
                }
            }.onFailure { error ->
                _uiState.value = NewsUiState.Error(
                    message = error.localizedMessage ?: "Unable to load news. Please check your connection.",
                    canRetry = true
                )
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            val result = newsRepository.searchNews(query)
            result.onSuccess { articles ->
                if (articles.isEmpty()) {
                    _uiState.value = NewsUiState.Empty
                } else {
                    _uiState.value = NewsUiState.Success(
                        featuredArticles = emptyList(),
                        articles = articles,
                        isRefreshing = false
                    )
                }
            }.onFailure { error ->
                _uiState.value = NewsUiState.Error(
                    message = error.localizedMessage ?: "Search failed. Please try again.",
                    canRetry = true
                )
            }
        }
    }

    companion object {
        fun provideFactory(newsRepository: NewsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NewsViewModel(newsRepository) as T
                }
            }
    }
}
