package com.example.newsly.data.model

import com.example.newsly.util.VideoUrlValidator
import com.google.gson.annotations.SerializedName

data class NewsApiResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("totalResults") val totalResults: Int? = null,
    @SerializedName("articles") val articles: List<ApiArticleDto>? = null,
    @SerializedName("results") val results: List<ApiArticleDto>? = null,
    @SerializedName("data") val data: List<ApiArticleDto>? = null,
    @SerializedName("message") val message: String? = null
)

data class ApiArticleDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("source") val source: ApiSourceDto? = null,
    @SerializedName("source_name") val sourceNameRaw: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("urlToImage") val urlToImage: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("image_url") val imageUrlSnake: String? = null,
    @SerializedName("video_url") val videoUrlSnake: String? = null,
    @SerializedName("videoUrl") val videoUrlCamel: String? = null,
    @SerializedName("video") val video: String? = null,
    @SerializedName("publishedAt") val publishedAt: String? = null,
    @SerializedName("published_at") val publishedAtSnake: String? = null,
    @SerializedName("pubDate") val pubDate: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("category") val category: Any? = null
) {
    fun toDomainModel(categoryFallback: String = "General", isBreaking: Boolean = false): NewsArticle {
        val resolvedImage = urlToImage ?: image ?: imageUrl ?: imageUrlSnake
        val resolvedDate = publishedAt ?: publishedAtSnake ?: pubDate ?: ""
        val resolvedSource = source?.name ?: sourceNameRaw ?: "Newsly Feed"
        val resolvedCategory = when (category) {
            is List<*> -> category.firstOrNull()?.toString() ?: categoryFallback
            is String -> category
            else -> categoryFallback
        }
        val safeId = id ?: url?.hashCode()?.toString() ?: title?.hashCode()?.toString() ?: System.currentTimeMillis().toString()

        val rawVideo = videoUrlSnake ?: videoUrlCamel ?: video
        val resolvedVideo = if (VideoUrlValidator.isValidPlayableVideoUrl(rawVideo)) {
            VideoUrlValidator.sanitizeVideoUrl(rawVideo)
        } else {
            null
        }

        return NewsArticle(
            id = safeId,
            title = title ?: "Headline unavailable",
            description = description ?: content ?: "",
            content = content ?: description ?: "",
            url = url ?: "",
            imageUrl = resolvedImage,
            videoUrl = resolvedVideo,
            publishedAt = resolvedDate,
            sourceName = resolvedSource,
            author = author,
            category = resolvedCategory,
            isBreaking = isBreaking,
            isBookmarked = false
        )
    }
}

data class ApiSourceDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null
)
