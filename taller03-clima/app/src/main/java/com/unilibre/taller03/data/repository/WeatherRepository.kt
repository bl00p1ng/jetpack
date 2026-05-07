package com.unilibre.taller03.data.repository

import com.unilibre.taller03.BuildConfig
import com.unilibre.taller03.data.local.SearchHistoryDao
import com.unilibre.taller03.data.local.SearchHistoryEntity
import com.unilibre.taller03.data.remote.WeatherApi
import com.unilibre.taller03.data.remote.toDomain
import com.unilibre.taller03.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val historyDao: SearchHistoryDao
) {
    suspend fun fetchWeather(city: String): Result<WeatherData> {
        val key = BuildConfig.OPENWEATHER_API_KEY
        if (key.isBlank()) {
            return Result.failure(
                IllegalStateException("Configura OPENWEATHER_API_KEY en local.properties")
            )
        }
        return runCatching {
            val response = api.getCurrent(city = city.trim(), apiKey = key)
            val data = response.toDomain()
            historyDao.upsert(
                SearchHistoryEntity(city = data.city, timestamp = System.currentTimeMillis())
            )
            data
        }
    }

    fun observeHistory(): Flow<List<String>> =
        historyDao.observeRecent().map { list -> list.map { it.city } }
}
