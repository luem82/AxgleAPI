package com.example.axgleapi.data.video


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Video(
    @SerializedName("private")
    val private: Boolean,
    @SerializedName("hd")
    val hd: Boolean,
    @SerializedName("framerate")
    val framerate: Double,
    @SerializedName("dislikes")
    val dislikes: Long,
    @SerializedName("likes")
    val likes: Long,
    @SerializedName("viewnumber")
    val viewnumber: Long,
    @SerializedName("addtime")
    val addtime: Long,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("embedded_url")
    val embeddedUrl: String,
    @SerializedName("preview_url")
    val previewUrl: String,
    @SerializedName("preview_video_url")
    val previewVideoUrl: String,
    @SerializedName("title")
    val title: String,
    var isFavorite: Boolean = false
) : Serializable