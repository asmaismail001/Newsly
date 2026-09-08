package com.example.newsly.di

import android.content.Context
import com.example.newsly.data.local.NewsDatabase
import com.example.newsly.data.remote.RetrofitClient
import com.example.newsly.data.repository.NewsRepository
import com.example.newsly.data.repository.NotificationRepository
import com.example.newsly.data.repository.TranslationRepository

interface AppContainer {
    val newsRepository: NewsRepository
    val translationRepository: TranslationRepository
    val notificationRepository: NotificationRepository
    val themeManager: com.example.newsly.ui.theme.ThemeManager
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: NewsDatabase by lazy {
        NewsDatabase.getInstance(context)
    }

    override val themeManager: com.example.newsly.ui.theme.ThemeManager by lazy {
        com.example.newsly.ui.theme.ThemeManager(context)
    }

    override val newsRepository: NewsRepository by lazy {
        NewsRepository(
            apiService = RetrofitClient.newsApiService,
            savedArticleDao = database.savedArticleDao()
        )
    }

    override val translationRepository: TranslationRepository by lazy {
        TranslationRepository(
            apiService = RetrofitClient.translationApiService
        )
    }

    override val notificationRepository: NotificationRepository by lazy {
        NotificationRepository()
    }
}
