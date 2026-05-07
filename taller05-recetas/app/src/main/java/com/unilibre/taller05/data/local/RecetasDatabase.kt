package com.unilibre.taller05.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecetaFavoritaEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecetasDatabase : RoomDatabase() {
    abstract fun recetaFavoritaDao(): RecetaFavoritaDao
}
