package com.example.managementapp.domain.usecase

import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProjectsUseCase @Inject constructor(
    private val repository: GithubRepository
) {

    suspend operator fun invoke(token: String): Flow<Resource<List<Project>>> =
        repository.getUserProjects(token)
}