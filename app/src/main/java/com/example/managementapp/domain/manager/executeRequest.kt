package com.example.managementapp.domain.manager

import com.example.managementapp.R
import com.example.managementapp.data.local.ProjectDatabase
import com.example.managementapp.data.util.NetworkManager
import com.example.managementapp.data.util.ResourceProvider
import com.example.managementapp.presentation.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

enum class RequestType {
    PROJECTS_LIST,
    DEFAULT,
}

suspend fun <T> executeRequest(
    networkManager: NetworkManager,
    resourceProvider: ResourceProvider,
    handleApiError: HandleApiError,
    db: ProjectDatabase,
    requestType: RequestType,
    request: suspend () -> T,
): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading())

        // Check if the network is connected
        if (networkManager.isNetworkConnected()) {
            try {
                val response = request()
                emit(Resource.Success(data = response))
            } catch (exception: HttpException) {
                val errorMessage = handleApiError.handleApiErrors(exception)
                emit(Resource.Error(errorMessage))
            } catch (exception: Exception) {
                emit(
                    Resource.Error(
                        exception.localizedMessage
                            ?: resourceProvider.getString(R.string.unknown_error)
                    )
                )
            }
        } else {
            // If no network, check for cached data based on request type
            when (requestType) {
                RequestType.PROJECTS_LIST -> {
                    val cachedProjects = db.projectDao().getAllProjects().map { it.toProject() }
                    if (cachedProjects.isNotEmpty()) {
                        emit(Resource.Success(cachedProjects as T))
                    } else {
                        emit(Resource.Error(resourceProvider.getString(R.string.no_internet_connection)))
                    }
                }

                RequestType.DEFAULT -> {
                    emit(Resource.Error(resourceProvider.getString(R.string.no_internet_connection)))
                }
            }
        }
    }
}