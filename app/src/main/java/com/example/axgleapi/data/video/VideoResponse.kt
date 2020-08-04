package com.example.axgleapi.data.video


import com.example.axgleapi.data.video.Response
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoResponse(
    @SerializedName("response")
    val response: Response,
    @SerializedName("success")
    val success: Boolean
) : Serializable