package com.dicoding.githubuserapp.network

import com.dicoding.githubuserapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsers(
    ): Call<List<UsersResponseItem>>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserFollowersResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserFollowingResponseItem>>

    @GET("search/users")
    fun getSearchResult(
        @Query("q") username: String
    ): Call<UserSearchResponse>

}