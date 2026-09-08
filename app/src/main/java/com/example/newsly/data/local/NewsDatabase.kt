package com.example.newsly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [SavedArticleEntity::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun savedArticleDao(): SavedArticleDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS saved_articles")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS saved_articles (
                        id TEXT NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        content TEXT NOT NULL,
                        url TEXT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        videoUrl TEXT NOT NULL DEFAULT '',
                        videoDuration TEXT NOT NULL DEFAULT '',
                        publishedAt TEXT NOT NULL,
                        sourceName TEXT NOT NULL,
                        author TEXT NOT NULL,
                        category TEXT NOT NULL,
                        isBreaking INTEGER NOT NULL,
                        savedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "newsly_database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
