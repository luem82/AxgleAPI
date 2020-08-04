package com.example.axgleapi.data.category


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("response")
    val response: Response,
    @SerializedName("success")
    val success: Boolean
)