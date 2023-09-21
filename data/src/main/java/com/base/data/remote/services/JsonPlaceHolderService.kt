package com.base.data.remote.services

import com.base.data.remote.dto.PostDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPlaceHolderService {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>

    @GET("posts/{id}")
    suspend fun getPostDetail(@Path("id") id: String): Response<PostDto>

    @POST("posts")
    suspend fun addPost(@Body body: Any): Response<PostDto>
}