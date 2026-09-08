package com.example.newsly.data.repository

import com.example.newsly.data.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationRepository {

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif-1",
                title = "Breaking: Global AI Summit",
                description = "World leaders reach landmark quantum safety accords in Geneva.",
                timestamp = System.currentTimeMillis() - (15 * 60 * 1000), // 15 mins ago
                category = "Technology",
                isRead = false,
                imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=400&q=80",
                articleId = "art-breaking-1"
            ),
            NotificationItem(
                id = "notif-2",
                title = "Markets Surge on Inflation Report",
                description = "Global indices post best single-day rally this quarter.",
                timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000), // 2 hours ago
                category = "Business",
                isRead = false,
                imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=400&q=80",
                articleId = "art-business-1"
            ),
            NotificationItem(
                id = "notif-3",
                title = "Deep Space Discovery",
                description = "Atmospheric water signatures detected on habitable zone exoplanet.",
                timestamp = System.currentTimeMillis() - (5 * 60 * 60 * 1000),
                category = "Science",
                isRead = true,
                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=400&q=80",
                articleId = "art-science-1"
            ),
            NotificationItem(
                id = "notif-4",
                title = "Champions League Thriller",
                description = "Stoppage-time goal sends stadium into absolute frenzy.",
                timestamp = System.currentTimeMillis() - (8 * 60 * 60 * 1000),
                category = "Sports",
                isRead = true,
                imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=400&q=80",
                articleId = "art-sports-1"
            ),
            NotificationItem(
                id = "notif-5",
                title = "10 Million Trees Planted by Drones",
                description = "Innovative autonomous reforestation initiative completes phase one.",
                timestamp = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                category = "Environment",
                isRead = true,
                imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=400&q=80",
                articleId = "art-environment-1"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _categorySettings = MutableStateFlow<Map<String, Boolean>>(
        mapOf(
            "Breaking News" to true,
            "World News" to true,
            "Politics" to true,
            "Business" to true,
            "Technology" to true,
            "Science" to true,
            "Health" to true,
            "Sports" to true,
            "Entertainment" to false
        )
    )
    val categorySettings: StateFlow<Map<String, Boolean>> = _categorySettings.asStateFlow()

    fun markAsRead(notificationId: String) {
        _notifications.update { list ->
            list.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
        }
    }

    fun markAllAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    fun toggleCategorySetting(category: String) {
        _categorySettings.update { map ->
            val current = map[category] ?: false
            map.toMutableMap().apply { put(category, !current) }
        }
    }

    fun setAllCategories(enabled: Boolean) {
        _categorySettings.update { map ->
            map.keys.associateWith { enabled }
        }
    }

    fun addNotification(item: NotificationItem) {
        _notifications.update { listOf(item) + it }
    }
}
