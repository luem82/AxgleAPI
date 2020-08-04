package com.example.pexelsretrofitapi.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(FavoriteVideo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDatabase(): FavoriteVideoDAO
}