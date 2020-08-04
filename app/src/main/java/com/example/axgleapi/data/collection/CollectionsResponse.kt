package com.example.axgleapi.data.collection


import com.google.gson.annotations.SerializedName

data class CollectionsResponse(
    @SerializedName("response")
    val response: Response,
    @SerializedName("success")
    val success: Boolean
)