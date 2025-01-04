package com.example.managementapp.presentation.screens.projectdetails

import androidx.lifecycle.SavedStateHandle
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project
import com.example.managementapp.domain.usecase.GetProjectDetailsUseCase
import com.example.managementapp.presentation.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
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

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectDetailsViewModelTest {

    private lateinit var getProjectDetailsUseCase: GetProjectDetailsUseCase
    private lateinit var projectDetailsViewModel: ProjectDetailsViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Initialize mocks
        getProjectDetailsUseCase = mockk()
        savedStateHandle = SavedStateHandle()

        // Mock saved state handle to provide initial values
        savedStateHandle.set("owner", "testOwner")
        savedStateHandle.set("repo", "testRepo")

        // Initialize the ViewModel
        projectDetailsViewModel = ProjectDetailsViewModel(getProjectDetailsUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadProjectDetails() updates state with project details on success`() = runBlocking {
        // Arrange
        val project = Project(
            name = "Project 1",
            description = "Description 1",
            owner = Owner("", "", ""),
            forksCount = 10,
            starsCount = 5,
            id = 1,
            issuesCount = 2,
            lastUpdated = "2022-09-01T00:00:00Z"
        )
        coEvery { getProjectDetailsUseCase("testOwner", "testRepo") } returns flowOf(Resource.Success(project))

        // Act
        projectDetailsViewModel.loadProjectDetails("testOwner", "testRepo")

        // Assert
        projectDetailsViewModel.state.take(1).collect { state -> // Use take(1) to collect only the first emission
            assertTrue(!state.isLoading) // Assert loading state is false
            assertEquals(project, state.project) // Assert project details are as expected
            assertEquals(null, state.error) // Assert no error
        }
    }

    @Test
    fun `loadProjectDetails() updates state with error message on failure`() = runBlocking {
        // Arrange
        val errorMessage = "Failed to fetch project details"
        coEvery { getProjectDetailsUseCase("testOwner", "testRepo") } returns flowOf(Resource.Error(errorMessage))

        // Act
        projectDetailsViewModel.loadProjectDetails("testOwner", "testRepo")

        // Assert
        projectDetailsViewModel.state.take(1).collect { state -> // Use take(1) to collect only the first emission
            assertTrue(!state.isLoading) // Assert loading state is false
            assertEquals(errorMessage, state.error) // Assert error message is as expected
            assertEquals(null, state.project) // Assert no project details
        }
    }

    @Test
    fun `loadProjectDetails() sets loading state correctly`() = runBlocking {
        // Arrange
        coEvery { getProjectDetailsUseCase("testOwner", "testRepo") } returns flowOf(Resource.Loading())

        // Act
        projectDetailsViewModel.loadProjectDetails("testOwner", "testRepo")

        // Assert
        projectDetailsViewModel.state.take(1).collect { state -> // Use take(1) to collect only the first emission
            assertTrue(state.isLoading) // Assert loading state is true
        }
    }
}