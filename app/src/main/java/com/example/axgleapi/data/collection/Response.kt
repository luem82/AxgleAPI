package com.example.axgleapi.data.collection


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("collections")
    val collections: List<Collection>,
    @SerializedName("current_offset")
    val currentOffset: Int,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("total_collections")
    val totalCollections: Int
)