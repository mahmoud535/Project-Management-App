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

class GetProjectDetailsUseCaseTest {

    private lateinit var repository: GithubRepository
    private lateinit var getProjectDetailsUseCase: GetProjectDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        getProjectDetailsUseCase = GetProjectDetailsUseCase(repository)
    }

    @Test
    fun `invoke() with valid owner and repo returns project details`() = runBlocking {
        // Arrange
        val owner = "owner"
        val repo = "repo"
        val project = Project(name = "Project Name", description = "Project Description", owner = Owner("","",""), forksCount = 10, starsCount = 5,id = 1, issuesCount = 2, lastUpdated = "2021-09-01T00:00:00Z")
        coEvery { repository.getProjectDetails(owner, repo) } returns flowOf(Resource.Success(project))

        // Act
        val results = getProjectDetailsUseCase(owner, repo).toList()

        // Assert
        // Check if the results list has at least one element
        assertTrue(results.isNotEmpty())
        assertTrue(results[1] is Resource.Success)
        assertEquals(project, (results[1] as Resource.Success).data)
    }

    @Test
    fun `invoke() with non-existent project returns error`() = runBlocking {
        // Arrange
        val owner = "owner"
        val repo = "repo"
        coEvery { repository.getProjectDetails(owner, repo) } returns flowOf(Resource.Error("Project not found"))

        // Act
        val results = getProjectDetailsUseCase(owner, repo).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[1] is Resource.Error)
        assertEquals("Project not found", (results[1] as Resource.Error).message)
    }

    @Test
    fun `invoke() with network error returns error`() = runBlocking {
        // Arrange
        val owner = "owner"
        val repo = "repo"
        coEvery { repository.getProjectDetails(owner, repo) } returns flowOf(Resource.Error("No internet connection"))

        // Act
        val results = getProjectDetailsUseCase(owner, repo).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[1] is Resource.Error)
        assertEquals("No internet connection", (results[1] as Resource.Error).message)
    }
}