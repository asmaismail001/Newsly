package com.example.newsly.data.model

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val category: String,
    val isRead: Boolean = false,
    val imageUrl: String? = null,
    val articleId: String? = null
) {
    fun getFormattedTimeAgo(): String {
        val diffMillis = System.currentTimeMillis() - timestamp
        val diffMinutes = diffMillis / (1000 * 60)
        val diffHours = diffMinutes / 60
        val diffDays = diffHours / 24

        return when {
            diffMinutes < 1 -> "Just now"
            diffMinutes < 60 -> "$diffMinutes min ago"
            diffHours < 24 -> "$diffHours hr ago"
            diffDays == 1L -> "Yesterday"
            else -> "$diffDays days ago"
        }
    }
}
