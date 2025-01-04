package com.example.managementapp.domain.usecase

import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.presentation.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUserProjectsUseCaseTest {

    private lateinit var repository: GithubRepository
    private lateinit var getUserProjectsUseCase: GetUserProjectsUseCase

    @Before
    fun setup() {
        repository = mockk()
        getUserProjectsUseCase = GetUserProjectsUseCase(repository)
    }

    @Test
    fun `invoke() with valid token returns user projects`() = runBlocking {
        // Arrange
        val token = "sample_token"
        val projects = listOf(
            Project(name = "Project 1", description = "Description 1", owner = Owner("","",""), forksCount = 10, starsCount = 5,id = 1, issuesCount = 2, lastUpdated = "2022-09-01T00:00:00Z"),
            Project(name = "Project 2", description = "Description 2", owner = Owner("","",""), forksCount = 10, starsCount = 5,id = 2, issuesCount = 2, lastUpdated = "2021-09-01T00:00:00Z")
        )
        coEvery { repository.getUserProjects(token) } returns flowOf(Resource.Success(projects))

        // Act
        val results = getUserProjectsUseCase(token).toList()

        // Assert
        // Check if the results list has at least one element
        assertTrue(results.isNotEmpty())
        assertTrue(results[0] is Resource.Success)
        assertEquals(projects, (results[0] as Resource.Success).data)
    }

    @Test
    fun `invoke() with no projects found returns error`() = runBlocking {
        // Arrange
        val token = "sample_token"
        coEvery { repository.getUserProjects(token) } returns flowOf(Resource.Error("No projects found"))

        // Act
        val results = getUserProjectsUseCase(token).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[0] is Resource.Error)
        assertEquals("No projects found", (results[0] as Resource.Error).message)
    }

    @Test
    fun `invoke() with network error returns error`() = runBlocking {
        // Arrange
        val token = "sample_token"
        coEvery { repository.getUserProjects(token) } returns flowOf(Resource.Error("No internet connection"))

        // Act
        val results = getUserProjectsUseCase(token).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[0] is Resource.Error)
        assertEquals("No internet connection", (results[0] as Resource.Error).message)
    }
}