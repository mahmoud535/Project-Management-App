package com.example.managementapp.domain.mapper

import com.example.managementapp.data.model.entity.ProjectEntity
import com.example.managementapp.domain.model.Project


fun Project.toProjectEntity(): ProjectEntity {
    return ProjectEntity(
        id = this.id ?: 0,
        name = this.name ?: "",
        description = this.description,
        lastUpdated = this.lastUpdated ?: "",
        ownerLogin = this.owner?.login ?: "" ,
        ownerAvatarUrl = this.owner?.avatarUrl ?: "",
        starsCount = this.starsCount ?: 0,
        forksCount = this.forksCount ?: 0,
        issuesCount = this.issuesCount ?: 0
    )
}