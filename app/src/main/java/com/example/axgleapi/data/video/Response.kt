package com.example.axgleapi.data.video


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(
    @SerializedName("current_offset")
    val currentOffset: Int,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("total_videos")
    val totalVideos: Int,
    @SerializedName("videos")
    val videos: List<Video>
): Serializable