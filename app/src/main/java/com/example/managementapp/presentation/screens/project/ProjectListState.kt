package com.example.managementapp.presentation.screens.project

import com.example.managementapp.domain.model.Project

data class ProjectListState(
    val projects: List<Project> ? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)