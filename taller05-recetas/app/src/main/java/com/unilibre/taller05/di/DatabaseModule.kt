package com.unilibre.taller05.di

import android.content.Context
import androidx.room.Room
import com.unilibre.taller05.data.local.RecetaFavoritaDao
import com.unilibre.taller05.data.local.RecetasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): RecetasDatabase =
        Room.databaseBuilder(ctx, RecetasDatabase::class.java, "recetas.db").build()

    @Provides
    fun provideDao(db: RecetasDatabase): RecetaFavoritaDao = db.recetaFavoritaDao()
}
