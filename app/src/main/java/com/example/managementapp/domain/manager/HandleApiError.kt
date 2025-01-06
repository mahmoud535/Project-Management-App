package com.example.managementapp.domain.manager

import com.example.managementapp.R
import com.example.managementapp.data.model.ErrorResponseModel
import com.example.managementapp.data.util.ResourceProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandleApiError @Inject constructor(private val resourceProvider: ResourceProvider) {

    fun handleApiErrors(error: Throwable): String {
        return when (error) {
            is HttpException -> handleHttpException(error)
            is IOException -> resourceProvider.getString(R.string.no_internet_connection)
            is TimeoutException -> resourceProvider.getString(R.string.request_timed_out)
            else -> resourceProvider.getString(R.string.something_went_wrong)
        }
    }

    private fun handleHttpException(error: HttpException): String {
        val code = error.code()
        return when (code) {
            400 -> handleBadRequest(error)
            401 -> getErrorMessageFromCode(code)
            403 -> resourceProvider.getString(R.string.forbidden)
            404 -> resourceProvider.getString(R.string.not_found)
            420 -> resourceProvider.getString(R.string.rate_limit_exceeded)
            500 -> resourceProvider.getString(R.string.server_error)
            else -> getErrorMessageFromCode(code)
        }
    }

    private fun handleBadRequest(error: HttpException): String {
        return error.response()?.errorBody()?.let {
            getErrorResponseMessage(error) ?: getErrorMessageFromCode(400)
        } ?: getErrorMessageFromCode(400)
    }

    private fun getErrorResponseMessage(error: HttpException): String? {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponseModel>() {}.type

        return try {
            val errorResponse: ErrorResponseModel =
                gson.fromJson(error.response()?.errorBody()?.charStream(), type)
            errorResponse.message.takeIf { !it.isNullOrEmpty() }
        } catch (exception: Exception) {
            exception.printStackTrace() // Consider using a logging framework
            null
        }
    }

    private fun getErrorMessageFromCode(code: Int): String {
        return when (code) {
            400 -> resourceProvider.getString(R.string.bad_request)
            401 -> resourceProvider.getString(R.string.unauthorized)
            403 -> resourceProvider.getString(R.string.forbidden)
            404 -> resourceProvider.getString(R.string.not_found)
            500 -> resourceProvider.getString(R.string.server_error)
            else -> resourceProvider.getString(R.string.unknown_error)
        }
    }
}