package com.example.managementapp.domain.mapper

import com.example.managementapp.data.local.entity.ProjectEntity
import com.example.managementapp.domain.model.Project


fun Project.toProjectEntity(): ProjectEntity {
    return ProjectEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        lastUpdated = this.lastUpdated,
        ownerLogin = this.owner.login,
        ownerAvatarUrl = this.owner.avatarUrl,
        starsCount = this.starsCount,
        forksCount = this.forksCount,
        issuesCount = this.issuesCount
    )
}