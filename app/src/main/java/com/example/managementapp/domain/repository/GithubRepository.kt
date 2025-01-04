package com.example.managementapp.domain.repository

import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.model.User
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun login(username: String, token: String): Flow<Resource<User>>
    suspend fun getUserProjects(token: String): Flow<Resource<List<Project>>>
    suspend fun getProjectDetails(owner: String, repo: String): Flow<Resource<Project>>
}