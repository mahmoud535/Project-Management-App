package com.example.managementapp.presentation.screens.project

import android.content.Context
import android.content.SharedPreferences
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.usecase.GetUserProjectsUseCase
import com.example.managementapp.presentation.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.take

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectListViewModelTest {

    private lateinit var getUserProjectsUseCase: GetUserProjectsUseCase
    private lateinit var projectListViewModel: ProjectListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getUserProjectsUseCase = mockk()
        context = mockk()
        sharedPreferences = mockk()

        every { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.getString("github_token", null) } returns "sample_token"

        projectListViewModel = ProjectListViewModel(getUserProjectsUseCase, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadProjects() updates state with projects on success`() = runBlocking {
        // Arrange
        val projects = listOf(
            Project(name = "Project 1", description = "Description 1", owner = Owner("", "", ""), forksCount = 10, starsCount = 5, id = 1, issuesCount = 2, lastUpdated = "2022-09-01T00:00:00Z"),
            Project(name = "Project 2", description = "Description 2", owner = Owner("", "", ""), forksCount = 10, starsCount = 5, id = 2, issuesCount = 2, lastUpdated = "2021-09-01T00:00:00Z")
        )
        coEvery { getUserProjectsUseCase("sample_token") } returns flowOf(Resource.Success(projects))

        // Act
        projectListViewModel.loadProjects()

        // Assert
        projectListViewModel.state.take(1).collect { state ->
            assertTrue(!state.isLoading)
            assertEquals(projects, state.projects)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `loadProjects() updates state with error message on failure`() = runBlocking {
        // Arrange
        val errorMessage = "Failed to fetch projects"
        coEvery { getUserProjectsUseCase("sample_token") } returns flowOf(Resource.Error(errorMessage))

        // Act
        projectListViewModel.loadProjects()

        // Assert
        projectListViewModel.state.take(1).collect { state ->
            assertTrue(!state.isLoading)
            assertEquals(errorMessage, state.error)
            assertTrue(state.projects.isEmpty())
        }
    }

    @Test
    fun `loadProjects() updates state with error when token is not found`() = runBlocking {
        // Arrange
        every { sharedPreferences.getString("github_token", null) } returns null

        // Act
        projectListViewModel.loadProjects()

        // Assert
        projectListViewModel.state.take(1).collect { state ->
            assertTrue(!state.isLoading)
            assertEquals("Token not found", state.error)
            assertTrue(state.projects.isEmpty())
        }
    }
}