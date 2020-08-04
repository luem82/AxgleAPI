package com.example.pexelsretrofitapi.room

import androidx.room.*

@Dao
interface FavoriteVideoDAO {

    @Query("SELECT * FROM table_video")
    fun getAllVides(): List<FavoriteVideo>

    @Insert
    fun insertVideo(video: FavoriteVideo)

    @Delete
    fun deleteVideo(video: FavoriteVideo)

    @Update
    fun updatePhoto(video: FavoriteVideo)
}