package com.andresillo.newsapp.repository

import android.util.Log
import com.andresillo.newsapp.db.NewsDao
import com.andresillo.newsapp.db.toEntity
import com.andresillo.newsapp.db.toNews
import com.andresillo.newsapp.model.News
import com.andresillo.newsapp.provider.NewsProvider
import javax.inject.Inject

class NewsRepositoryImp @Inject constructor(
    private val provider: NewsProvider,
    private val dao: NewsDao
) : NewsRepository {

    override suspend fun getNews(country: String): List<News> {
        return try {
            val response = provider.topHeadLines(country)
            Log.d(TAG, "API response code=${response.code()} success=${response.isSuccessful}")
            if (response.isSuccessful) {
                val articles = response.body()?.articles
                    ?.filter { it.title.isNotBlank() }
                    ?: emptyList()
                Log.d(TAG, "Got ${articles.size} articles from API")
                if (articles.isNotEmpty()) {
                    runCatching { dao.insertAll(articles.map { it.toEntity() }) }
                        .onFailure { Log.e(TAG, "Failed to cache", it) }
                }
                articles
            } else {
                Log.w(TAG, "API not successful, falling back to cache. body=${response.errorBody()?.string()}")
                dao.getAll().map { it.toNews() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching news, falling back to cache", e)
            dao.getAll().map { it.toNews() }
        }
    }

    override fun getNew(title: String): News {
        return dao.getByTitle(title)?.toNews()
            ?: News(title = title, content = null, author = null, url = "", urlToImage = null)
    }

    companion object {
        private const val TAG = "NewsRepositoryImp"
    }
}
