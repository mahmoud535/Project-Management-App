package com.example.managementapp.presentation.screens.projectdetails

import com.example.managementapp.domain.model.Project

data class ProjectDetailsState(
    val project: Project? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)