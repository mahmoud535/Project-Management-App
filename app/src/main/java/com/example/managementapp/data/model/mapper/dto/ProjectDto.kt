package com.example.managementapp.data.model.mapper.dto

import com.example.managementapp.domain.model.Project
import com.google.gson.annotations.SerializedName

data class ProjectDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("owner") val owner: OwnerDto,
    @SerializedName("stargazers_count") val starsCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("open_issues_count") val issuesCount: Int,
) {
    fun toProject() = Project(
        id = id,
        name = name,
        description = description,
        lastUpdated = updatedAt,
        owner = owner.toOwner(),
        starsCount = starsCount,
        forksCount = forksCount,
        issuesCount = issuesCount,
    )
}