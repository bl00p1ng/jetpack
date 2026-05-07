package com.andresillo.newsapp.provider

import com.andresillo.newsapp.BuildConfig
import com.andresillo.newsapp.model.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = BuildConfig.NEWS_API_KEY

interface NewsProvider {

    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun topHeadLines(
        @Query("country") country: String
    ): Response<NewsApiResponse>
}
