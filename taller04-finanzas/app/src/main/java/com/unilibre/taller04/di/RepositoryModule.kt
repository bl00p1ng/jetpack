package com.unilibre.taller04.di

import com.unilibre.taller04.data.repository.TransaccionRepositoryImpl
import com.unilibre.taller04.domain.repository.TransaccionRepository
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
    abstract fun bindTransaccionRepository(impl: TransaccionRepositoryImpl): TransaccionRepository
}
