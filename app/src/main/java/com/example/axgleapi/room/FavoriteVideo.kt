package com.example.pexelsretrofitapi.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_video")
class FavoriteVideo : Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    var id: Long
    @ColumnInfo(name = "title")
    var title: String
    @ColumnInfo(name = "duration")
    var duration: Double
    @ColumnInfo(name = "cover")
    var cover: String
    @ColumnInfo(name = "embed")
    var embed: String


    constructor(id: Long, cover: String, title: String, embed: String, duration: Double) {
        this.id = id
        this.cover = cover
        this.title = title
        this.embed = embed
        this.duration = duration
    }


}