package com.example.managementapp.data.repository

import android.content.Context
import com.example.managementapp.R
import com.example.managementapp.data.local.ProjectDatabase
import com.example.managementapp.data.remote.GithubApi
import com.example.managementapp.domain.mapper.toProjectEntity
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.model.User
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.data.util.NetworkManager
import com.example.managementapp.presentation.util.Resource
import com.example.managementapp.data.util.ResourceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val db: ProjectDatabase,
    private val networkManager: NetworkManager,
    private val resourceProvider: ResourceProvider
) : GithubRepository {

    override suspend fun login(username: String, token: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        if (!networkManager.isNetworkConnected()) {
            emit(Resource.Error(resourceProvider.getString(R.string.no_internet_connection)))
            return@flow
        }

        try {
            val response = api.validateUser("token $token")
            if (response.isSuccessful) {
                val userDto = response.body()
                    ?: throw Exception(resourceProvider.getString(R.string.user_not_found))
                saveToken(token)
                emit(Resource.Success(userDto.toUser()))
            } else {
                emit(Resource.Error(resourceProvider.getString(R.string.invalid_credentials)))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_fetch_projects)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: resourceProvider.getString(R.string.unknown_error)))
        }
    }

    override suspend fun getUserProjects(token: String): Flow<Resource<List<Project>>> = flow {
        emit(Resource.Loading())

        if (!networkManager.isNetworkConnected()) {
            val cachedProjects = db.projectDao().getAllProjects()
            if (cachedProjects.isNotEmpty()) {
                emit(Resource.Success(cachedProjects.map { it.toProject() }))
            } else {
                emit(Resource.Error(resourceProvider.getString(R.string.no_cached_data)))
            }
            return@flow
        }

        try {
            val response = api.getUserProjects("token $token")
            if (response.isSuccessful) {
                val projects = response.body()?.map { it.toProject() } ?: emptyList()
                db.projectDao().insertProjects(projects.map { it.toProjectEntity() })
                emit(Resource.Success(projects))
            } else {
                emit(
                    Resource.Error(
                        resourceProvider.getString(
                            R.string.failed_to_fetch_projects,
                            response.message()
                        )
                    )
                )
            }
        } catch (e: HttpException) {
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_fetch_projects)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: resourceProvider.getString(R.string.unknown_error)))
        }
    }

    override suspend fun getProjectDetails(owner: String, repo: String): Flow<Resource<Project>> =
        flow {
            emit(Resource.Loading())

            if (!networkManager.isNetworkConnected()) {
                emit(Resource.Error(resourceProvider.getString(R.string.no_internet_connection)))
                return@flow
            }

            try {
                val response = api.getProjectDetails(owner, repo)
                if (response.isSuccessful) {
                    val project = response.body()?.toProject()
                        ?: throw Exception(resourceProvider.getString(R.string.project_details_not_found))
                    emit(Resource.Success(project))
                } else {
                    emit(Resource.Error(resourceProvider.getString(R.string.failed_to_fetch_project_details)))
                }
            } catch (e: HttpException) {
                emit(Resource.Error(resourceProvider.getString(R.string.failed_to_fetch_projects)))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message ?: resourceProvider.getString(R.string.unknown_error)
                    )
                )
            }
        }

    private fun saveToken(token: String) {
        val sharedPreferences =
            resourceProvider.getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("github_token", token).apply()
    }

}