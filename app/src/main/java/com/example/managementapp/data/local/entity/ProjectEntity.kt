package com.example.managementapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val lastUpdated: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val starsCount: Int,
    val forksCount: Int,
    val issuesCount: Int
) {
    fun toProject() = Project(
        id = id,
        name = name,
        description = description,
        lastUpdated = lastUpdated,
        owner = Owner(ownerLogin, ownerAvatarUrl,name),
        starsCount = starsCount,
        forksCount = forksCount,
        issuesCount = issuesCount
    )
}