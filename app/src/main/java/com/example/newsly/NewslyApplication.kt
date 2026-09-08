package com.example.newsly

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.newsly.di.AppContainer
import com.example.newsly.di.DefaultAppContainer

class NewslyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NEWS_CHANNEL_ID,
                "Breaking & Category News",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for breaking news and topic updates"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NEWS_CHANNEL_ID = "newsly_breaking_news"
    }
}
