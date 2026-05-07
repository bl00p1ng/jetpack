package com.unilibre.taller05.di

import com.google.ai.client.generativeai.GenerativeModel
import com.unilibre.taller05.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides @Singleton
    fun provideGenerativeModel(): GenerativeModel? {
        val key = BuildConfig.GEMINI_API_KEY
        if (key.isBlank()) return null
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = key
        )
    }
}
