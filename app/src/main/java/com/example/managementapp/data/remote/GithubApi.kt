package com.example.managementapp.data.remote

import com.example.managementapp.data.mapper.dto.ProjectDto
import com.example.managementapp.data.mapper.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubApi {
    @GET("user")
    suspend fun validateUser(@Header("Authorization") authHeader: String): Response<UserDto>

    @GET("user/repos")
    suspend fun getUserProjects(@Header("Authorization") authHeader: String): Response<List<ProjectDto>>

    @GET("repos/{owner}/{repo}")
    suspend fun getProjectDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<ProjectDto>
}