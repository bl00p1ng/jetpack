package com.unilibre.taller03.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * El repositorio se inyecta por constructor con @Inject + @Singleton, asi que
 * este modulo queda como punto de extension si se quieren agregar bindings adicionales.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
