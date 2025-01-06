package com.example.managementapp.data.model.mapper.dto

import com.example.managementapp.domain.model.Owner
import com.google.gson.annotations.SerializedName


data class OwnerDto(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("name") val name: String?

) {
    fun toOwner() = Owner(
        login = login,
        avatarUrl = avatarUrl,
        name = name ?:""
    )
}