package com.example.managementapp.data.repository

import com.example.managementapp.R
import com.example.managementapp.data.local.ProjectDatabase
import com.example.managementapp.data.remote.GithubApi
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.model.User
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.data.util.NetworkManager
import com.example.managementapp.presentation.util.Resource
import com.example.managementapp.data.util.ResourceProvider
import com.example.managementapp.domain.manager.HandleApiError
import com.example.managementapp.domain.manager.RequestType
import com.example.managementapp.domain.manager.executeRequest
import com.example.managementapp.domain.mapper.toProjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val db: ProjectDatabase,
    private val handleApiError: HandleApiError,
    private val networkManager: NetworkManager,
    private val resourceProvider: ResourceProvider
) : GithubRepository {
    override suspend fun login(username: String, token: String): Flow<Resource<User>> =
        executeRequest(
            networkManager = networkManager,
            resourceProvider = resourceProvider,
            handleApiError = handleApiError,
            db = db,
            requestType = RequestType.DEFAULT,
            request = {
                val response = api.validateUser("token $token")
                val userDto = response.body()
                    ?: throw Exception(resourceProvider.getString(R.string.user_not_found))
                userDto.toUser()
            }
        )

    override suspend fun getUserProjects(token: String): Flow<Resource<List<Project>>> =
        executeRequest(
            networkManager = networkManager,
            resourceProvider = resourceProvider,
            handleApiError = handleApiError,
            db = db,
            requestType = RequestType.PROJECTS_LIST,
            request = {
                val response = api.getUserProjects("token $token")
                val projects = response.body()?.map { it.toProject() } ?: emptyList()
                db.projectDao()
                    .insertProjects(projects.map { it.toProjectEntity() })
                projects
            }
        )

    override suspend fun getProjectDetails(owner: String, repo: String): Flow<Resource<Project>> =
        executeRequest(
            networkManager = networkManager,
            resourceProvider = resourceProvider,
            handleApiError = handleApiError,
            db = db,
            requestType = RequestType.DEFAULT,
            request = {
                val response = api.getProjectDetails(owner, repo)
                val project = response.body()?.toProject()
                    ?: throw Exception(resourceProvider.getString(R.string.project_details_not_found))
                project
            }
        )
}