package com.example.managementapp.domain.usecase

import com.example.managementapp.domain.model.User
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    suspend operator fun invoke(username: String, token: String): Flow<Resource<User>> =
        repository.login(username, token)
}