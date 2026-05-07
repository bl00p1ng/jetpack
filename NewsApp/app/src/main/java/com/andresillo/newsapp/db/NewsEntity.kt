package com.andresillo.newsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andresillo.newsapp.model.News

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey val title: String,
    val content: String?,
    val author: String?,
    val url: String,
    val urlToImage: String?,
    val description: String?,
)

fun NewsEntity.toNews() = News(
    title = title,
    content = content,
    author = author,
    url = url,
    urlToImage = urlToImage,
    description = description,
)

fun News.toEntity() = NewsEntity(
    title = title,
    content = content,
    author = author,
    url = url,
    urlToImage = urlToImage,
    description = description,
)
