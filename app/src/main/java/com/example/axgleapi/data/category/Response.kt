package com.example.axgleapi.data.category


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("categories")
    val categories: List<Category>
)