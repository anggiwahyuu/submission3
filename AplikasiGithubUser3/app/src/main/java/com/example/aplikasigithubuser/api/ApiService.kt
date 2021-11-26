package com.example.aplikasigithubuser.api

import com.example.aplikasigithubuser.model.DetailResponse
import com.example.aplikasigithubuser.model.ItemsItem
import com.example.aplikasigithubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>
}