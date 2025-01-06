package com.example.managementapp.data.model.mapper.dto

import com.example.managementapp.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("name") val name: String?,
    @SerializedName("company") val company: String?,
    @SerializedName("blog") val blog: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("html_url") val htmlUrl: String,
) {

    fun toUser() = User(
        username = login,
        token = ""
    )
}