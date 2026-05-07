package com.andresillo.newsapp.di

import com.andresillo.newsapp.repository.NewsRepository
import com.andresillo.newsapp.repository.NewsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImp): NewsRepository
}
