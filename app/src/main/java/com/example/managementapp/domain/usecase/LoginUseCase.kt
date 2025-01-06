package com.example.managementapp.domain.usecase

import com.example.managementapp.R
import com.example.managementapp.data.local.PreferenceManager
import com.example.managementapp.data.util.ResourceProvider
import com.example.managementapp.domain.model.User
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: GithubRepository,
    private val resourceProvider: ResourceProvider,
    private val preferenceManager: PreferenceManager
) {
    suspend operator fun invoke(username: String, token: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        repository.login(username, token).collect { resource ->
            when (resource) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {
                    val userDto = resource.data
                        ?: throw Exception(resourceProvider.getString(R.string.user_not_found))
                    preferenceManager.saveToken(token)
                    emit(Resource.Success(userDto))
                }

                is Resource.Error -> {
                    emit(
                        Resource.Error(
                            resource.message
                                ?: resourceProvider.getString(R.string.invalid_credentials)
                        )
                    )
                }
            }
        }
    }
}