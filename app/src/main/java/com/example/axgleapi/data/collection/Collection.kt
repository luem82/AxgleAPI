package com.example.axgleapi.data.collection


import com.google.gson.annotations.SerializedName

data class Collection(
    @SerializedName("collection_url")
    val collectionUrl: String,
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("total_views")
    val totalViews: Int,
    @SerializedName("video_count")
    val videoCount: Int
)