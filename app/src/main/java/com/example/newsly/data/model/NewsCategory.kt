package com.example.newsly.data.model

enum class NewsCategory(val displayName: String, val apiQuery: String) {
    ALL("All", "general"),
    BREAKING("Breaking News", "breaking"),
    WORLD("World", "world"),
    POLITICS("Politics", "politics"),
    BUSINESS("Business", "business"),
    TECHNOLOGY("Technology", "technology"),
    SCIENCE("Science", "science"),
    HEALTH("Health", "health"),
    SPORTS("Sports", "sports"),
    ENTERTAINMENT("Entertainment", "entertainment"),
    LIFESTYLE("Lifestyle", "lifestyle"),
    EDUCATION("Education", "education"),
    ENVIRONMENT("Environment", "environment"),
    LOCAL("Local News", "local");

    companion object {
        fun fromDisplayName(name: String): NewsCategory {
            return entries.find { it.displayName.equals(name, ignoreCase = true) } ?: ALL
        }
    }
}
