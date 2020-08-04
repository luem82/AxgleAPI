package com.example.axgleapi.data.category


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("CHID")
    val cHID: String,
    @SerializedName("category_url")
    val categoryUrl: String,
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("shortname")
    val shortname: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("total_videos")
    val totalVideos: Int
)