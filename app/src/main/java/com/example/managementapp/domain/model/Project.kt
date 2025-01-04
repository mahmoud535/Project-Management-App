package com.example.managementapp.domain.model

data class Project(
    val id: Long,
    val name: String,
    val description: String?,
    val lastUpdated: String,
    val owner: Owner,
    val starsCount: Int,
    val forksCount: Int,
    val issuesCount: Int,
)
