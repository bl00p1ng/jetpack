package com.andresillo.newsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    suspend fun getAll(): List<NewsEntity>

    @Query("SELECT * FROM news_table WHERE title = :title LIMIT 1")
    fun getByTitle(title: String): NewsEntity?
}
