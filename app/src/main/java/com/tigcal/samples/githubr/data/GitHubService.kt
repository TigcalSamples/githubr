package com.tigcal.samples.githubr.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @GET("/search/users")
    suspend fun searchUser(@Query("q") username: String): UserSearchResponse

    @GET("/users/{username}")
    suspend fun getProfile(@Path("username") username: String): User

    @GET("/users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<User>

    @GET("/users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<User>
}