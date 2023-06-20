package com.tigcal.samples.githubr.data

import com.squareup.moshi.Json

data class User(
    val id: Long = 0,
    @field:Json(name = "login")
    val username: String = "",
    @field:Json(name = "avatar_url")
    val avatar: String = "",
    val name: String? = "",
    @field:Json(name = "bio")
    val description: String? = "",
    val followers: Int = 0,
    val following: Int = 0,
)

data class UserSearchResponse(
    val total_count: Int = 0,
    val incomplete_results: Boolean = false,
    val items: List<User> = emptyList()
)
