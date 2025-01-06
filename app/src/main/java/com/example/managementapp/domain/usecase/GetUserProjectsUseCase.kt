package com.example.managementapp.domain.usecase

import com.example.managementapp.R
import com.example.managementapp.data.local.PreferenceManager
import com.example.managementapp.data.util.ResourceProvider
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserProjectsUseCase @Inject constructor(
    private val repository: GithubRepository,
    private val preferenceManager: PreferenceManager,
    private val resourceProvider: ResourceProvider
) {
    suspend operator fun invoke(): Flow<Resource<List<Project>>> = flow {
        emit(Resource.Loading())
        val token = preferenceManager.getToken()
        if (token != null) {
            repository.getUserProjects(token).collect { resource ->
                emit(resource)
            }
        } else {
            emit(Resource.Error(resourceProvider.getString(R.string.token_not_found)))
        }
    }
}