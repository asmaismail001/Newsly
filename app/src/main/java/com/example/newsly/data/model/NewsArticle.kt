package com.example.newsly.data.model

import com.example.newsly.data.local.SavedArticleEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val imageUrl: String?,
    val videoUrl: String? = null,
    val videoDuration: String? = null,
    val publishedAt: String,
    val sourceName: String,
    val author: String?,
    val category: String = "General",
    val isBreaking: Boolean = false,
    val isBookmarked: Boolean = false
) {
    fun getFormattedTimeAgo(): String {
        if (publishedAt.isBlank()) return "Recently"
        
        if (publishedAt.contains("ago", ignoreCase = true) || publishedAt.contains("just now", ignoreCase = true)) {
            return publishedAt
        }

        return try {
            val formats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd"
            )
            var parsedDate: Date? = null
            for (format in formats) {
                try {
                    val sdf = SimpleDateFormat(format, Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    parsedDate = sdf.parse(publishedAt)
                    if (parsedDate != null) break
                } catch (_: Exception) { }
            }

            if (parsedDate == null) return publishedAt

            val diffMillis = System.currentTimeMillis() - parsedDate.time
            val diffMinutes = diffMillis / (1000 * 60)
            val diffHours = diffMinutes / 60
            val diffDays = diffHours / 24

            when {
                diffMinutes < 1 -> "Just now"
                diffMinutes < 60 -> "$diffMinutes min ago"
                diffHours < 24 -> "$diffHours hr ago"
                diffDays == 1L -> "Yesterday"
                diffDays < 7 -> "$diffDays days ago"
                else -> {
                    val displayFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    displayFormat.format(parsedDate)
                }
            }
        } catch (_: Exception) {
            publishedAt
        }
    }

    fun toSavedEntity(): SavedArticleEntity {
        return SavedArticleEntity(
            id = id,
            title = title,
            description = description ?: "",
            content = content ?: "",
            url = url,
            imageUrl = imageUrl ?: "",
            videoUrl = videoUrl ?: "",
            videoDuration = videoDuration ?: "",
            publishedAt = publishedAt,
            sourceName = sourceName,
            author = author ?: "",
            category = category,
            isBreaking = isBreaking,
            savedAt = System.currentTimeMillis()
        )
    }
}
