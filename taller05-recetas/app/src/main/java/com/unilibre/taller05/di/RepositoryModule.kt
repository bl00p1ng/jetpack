package com.unilibre.taller05.di

import com.unilibre.taller05.data.repository.RecetasRepositoryImpl
import com.unilibre.taller05.domain.repository.RecetasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindRecetasRepository(impl: RecetasRepositoryImpl): RecetasRepository
}
