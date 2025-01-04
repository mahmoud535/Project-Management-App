package com.example.managementapp.data.repository

import com.example.managementapp.R
import com.example.managementapp.data.local.ProjectDatabase
import com.example.managementapp.data.local.entity.ProjectEntity
import com.example.managementapp.data.mapper.dto.ProjectDto
import com.example.managementapp.data.mapper.dto.UserDto
import com.example.managementapp.data.remote.GithubApi
import com.example.managementapp.data.util.NetworkManager
import com.example.managementapp.presentation.util.Resource
import com.example.managementapp.data.util.ResourceProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/*
-- login() with no network connection then login fails and returns no internet connection
-- login() with server error - 500 then login fails and returns server error message
-- login() with invalid credentials - 401 then login fails and returns invalid credentials message
-- getUserProjects() with no network connection then returns cached projects or error
-- getUserProjects() with server error - 500 then returns error message
-- getProjectDetails() with no network connection then returns error message
-- getProjectDetails() with server error - 404 then returns error message
 */

class GithubRepositoryImplTest {

    private lateinit var repository: GithubRepositoryImpl
    private lateinit var api: GithubApi
    private lateinit var db: ProjectDatabase
    private lateinit var networkManager: NetworkManager
    private lateinit var resourceProvider: ResourceProvider

    @Before
    fun setup() {
        api = mockk()
        db = mockk()
        networkManager = mockk()
        resourceProvider = mockk()

        repository = GithubRepositoryImpl(api, db, networkManager, resourceProvider)
    }

    @Test
    fun `login() with no network connection then login fails and returns no internet connection`() =
        runBlocking {
            // Arrange
            val username = "testUser"
            val token = "sample_token"
            coEvery { networkManager.isNetworkConnected() } returns false
            coEvery { resourceProvider.getString(R.string.no_internet_connection) } returns "No internet connection"

            // Act
            val results = repository.login(username, token).toList()

            // Assert
            assertTrue(results[1] is Resource.Error)
            assertEquals("No internet connection", (results[1] as Resource.Error).message)
        }


    @Test
    fun `login() with invalid credentials - 401 then login fails and returns invalid credentials message`() =
        runBlocking {
            // Arrange
            val username = "testUser"
            val token = "sample_token"
            val errorResponseBody =
                ResponseBody.create("text/plain".toMediaTypeOrNull(), "Unauthorized")
            val errorResponse = Response.error<UserDto>(401, errorResponseBody)
            coEvery { networkManager.isNetworkConnected() } returns true
            coEvery { api.validateUser("token $token") } returns errorResponse
            coEvery { resourceProvider.getString(R.string.invalid_credentials) } returns "Invalid credentials"

            // Act
            val results = repository.login(username, token).toList()

            // Assert
            assertTrue(results[1] is Resource.Error)
            assertEquals("Invalid credentials", (results[1] as Resource.Error).message)
        }

    @Test
    fun `getUserProjects() with no network connection then returns cached projects or error`() =
        runBlocking {
            // Arrange
            val token = "sample_token"

            val cachedProjects = listOf(
                ProjectEntity(
                    id = 1,
                    name = "Project 1",
                    description = "Description 1",
                    lastUpdated = "123456",
                    ownerLogin = "owner1",
                    ownerAvatarUrl = "avatar1",
                    starsCount = 10,
                    forksCount = 5,
                    issuesCount = 2
                ),
                ProjectEntity(
                    id = 2,
                    name = "Project 2",
                    description = "Description 2",
                    lastUpdated = "123456",
                    ownerLogin = "owner1",
                    ownerAvatarUrl = "avatar1",
                    starsCount = 10,
                    forksCount = 5,
                    issuesCount = 2
                ),
            )

            coEvery { networkManager.isNetworkConnected() } returns false
            coEvery { db.projectDao().getAllProjects() } returns cachedProjects

            // Act
            val results = repository.getUserProjects(token).toList()

            // Assert
            assertTrue(results[1] is Resource.Success)
            assertEquals(
                cachedProjects.map { it.toProject() },
                (results[1] as Resource.Success).data
            )
        }

    @Test
    fun `getProjectDetails() with no network connection then returns error message`() =
        runBlocking {
            // Arrange
            val owner = "owner"
            val repo = "repo"
            coEvery { networkManager.isNetworkConnected() } returns false
            coEvery { resourceProvider.getString(R.string.no_internet_connection) } returns "No internet connection"

            // Act
            val results = repository.getProjectDetails(owner, repo).toList()

            // Assert
            assertTrue(results[1] is Resource.Error)
            assertEquals("No internet connection", (results[1] as Resource.Error).message)
        }

    @Test
    fun `getProjectDetails() with server error - 404 then returns error message`() = runBlocking {
        // Arrange
        val owner = "owner"
        val repo = "repo"
        val errorResponseBody = ResponseBody.create("text/plain".toMediaTypeOrNull(), "Not Found")
        val errorResponse = Response.error<ProjectDto>(404, errorResponseBody)

        coEvery { networkManager.isNetworkConnected() } returns true
        coEvery { api.getProjectDetails(owner, repo) } returns errorResponse
        coEvery { resourceProvider.getString(R.string.failed_to_fetch_project_details) } returns "Project not found"

        // Act
        val results = repository.getProjectDetails(owner, repo).toList()

        // Assert
        assertTrue(results[1] is Resource.Error)
        assertEquals("Project not found", (results[1] as Resource.Error).message)
    }
}