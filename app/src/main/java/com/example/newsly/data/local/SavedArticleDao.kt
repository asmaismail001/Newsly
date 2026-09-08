package com.example.newsly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {

    @Query("SELECT * FROM saved_articles ORDER BY savedAt DESC")
    fun getAllSavedArticles(): Flow<List<SavedArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE id = :id)")
    fun isArticleSaved(id: String): Flow<Boolean>

    @Query("SELECT * FROM saved_articles WHERE id = :id LIMIT 1")
    fun getSavedArticleById(id: String): SavedArticleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedArticle(article: SavedArticleEntity): Long

    @Query("DELETE FROM saved_articles WHERE id = :id")
    fun deleteSavedArticle(id: String): Int

    @Query("SELECT * FROM saved_articles WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY savedAt DESC")
    fun searchSavedArticles(query: String): Flow<List<SavedArticleEntity>>

    @Query("DELETE FROM saved_articles")
    fun clearAllSaved(): Int
}
