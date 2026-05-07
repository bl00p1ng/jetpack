package com.unilibre.taller03.di

import android.content.Context
import androidx.room.Room
import com.unilibre.taller03.data.local.SearchHistoryDao
import com.unilibre.taller03.data.local.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): WeatherDatabase =
        Room.databaseBuilder(ctx, WeatherDatabase::class.java, "weather.db").build()

    @Provides
    fun provideSearchHistoryDao(db: WeatherDatabase): SearchHistoryDao = db.searchHistoryDao()
}
