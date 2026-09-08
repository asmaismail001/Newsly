package com.example.newsly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsly.data.model.NewsArticle

@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val url: String,
    val imageUrl: String,
    val videoUrl: String = "",
    val videoDuration: String = "",
    val publishedAt: String,
    val sourceName: String,
    val author: String,
    val category: String,
    val isBreaking: Boolean,
    val savedAt: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): NewsArticle {
        return NewsArticle(
            id = id,
            title = title,
            description = description.ifEmpty { null },
            content = content.ifEmpty { null },
            url = url,
            imageUrl = imageUrl.ifEmpty { null },
            videoUrl = videoUrl.ifEmpty { null },
            videoDuration = videoDuration.ifEmpty { null },
            publishedAt = publishedAt,
            sourceName = sourceName,
            author = author.ifEmpty { null },
            category = category,
            isBreaking = isBreaking,
            isBookmarked = true
        )
    }
}
