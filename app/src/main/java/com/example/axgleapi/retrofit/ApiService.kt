package com.example.axgleapi.retrofit

import com.example.axgleapi.data.category.CategoryResponse
import com.example.axgleapi.data.collection.CollectionsResponse
import com.example.axgleapi.data.video.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v1/videos/{offset}")
    fun getAllVideo(
        @Path("offset") page: String,
        @Query("limit") limit: Int
    ): Call<VideoResponse>


    @GET("v1/videos/{offset}")
    fun getVideoByOrdering(
        @Path("offset") page: String, @Query("o") order: String,
        @Query("limit") limit: Int
    ): Call<VideoResponse>

    // query = keyword    https://api.avgle.com/v1/search/三上悠亜/0
    @GET("v1/search/{query}/{page}")
    fun getVideoByKeySearch(
        @Path("query") query: String, @Path("page") page: String,
        @Query("o") order: String, @Query("limit") limit: Int
    ): Call<VideoResponse>

    // c=CHID   https://api.avgle.com/v1/videos/0?c=7
    @GET("v1/videos/{offset}")
    fun getVideoByCategoryID(
        @Path("offset") page: String, @Query("c") c: Int,
        @Query("o") order: String, @Query("limit") limit: Int
    ): Call<VideoResponse>

    @GET("v1/categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("v1/collections/{offset}")
    fun getColections(
        @Path("offset") page: String, @Query("limit") limit: Int
    ): Call<CollectionsResponse>

}