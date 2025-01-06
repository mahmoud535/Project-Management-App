package com.example.managementapp.domain.model

data class Project(
    val id: Long ?=null,
    val name: String?=null,
    val description: String?=null,
    val lastUpdated: String?=null,
    val owner: Owner?=null,
    val starsCount: Int?=null,
    val forksCount: Int?=null,
    val issuesCount: Int?=null,
)
