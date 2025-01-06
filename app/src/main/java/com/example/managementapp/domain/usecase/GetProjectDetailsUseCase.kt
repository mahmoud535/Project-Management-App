package com.example.managementapp.domain.usecase

import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectDetailsUseCase @Inject constructor(
    private val repository: GithubRepository,
) {
    suspend operator fun invoke(owner: String, repo: String): Flow<Resource<Project>> = flow {
        emit(Resource.Loading())
        repository.getProjectDetails(owner, repo).collect { resource ->
            emit(resource)
        }
    }
}