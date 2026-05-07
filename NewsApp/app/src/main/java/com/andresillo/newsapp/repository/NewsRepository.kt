package com.andresillo.newsapp.repository

import com.andresillo.newsapp.model.News

interface NewsRepository {
    suspend fun getNews(country: String): List<News>
    fun getNew(title: String): News
}
