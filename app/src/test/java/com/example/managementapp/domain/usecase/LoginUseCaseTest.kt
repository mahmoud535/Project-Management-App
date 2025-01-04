package com.example.managementapp.domain.usecase

import com.example.managementapp.domain.model.User
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


class LoginUseCaseTest {

    private lateinit var repository: GithubRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        repository = mockk()
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `invoke() with valid credentials returns success`() = runBlocking {
        // Arrange
        val username = "testUser"
        val token = "sample_token"
        val user = User(username = username, token = token)
        coEvery { repository.login(username, token) } returns flowOf(Resource.Success(user))

        // Act
        val results = loginUseCase(username, token).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[0] is Resource.Success)
        assertEquals(user, (results[0] as Resource.Success).data)
    }

    @Test
    fun `invoke() with invalid credentials returns error`() = runBlocking {
        // Arrange
        val username = "testUser"
        val token = "sample_token"
        coEvery { repository.login(username, token) } returns flowOf(Resource.Error("Invalid credentials"))

        // Act
        val results = loginUseCase(username, token).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        org.junit.Assert.assertTrue(results[0] is Resource.Error)
        assertEquals("Invalid credentials", (results[0] as Resource.Error).message)
    }

    @Test
    fun `invoke() with network error returns error`() = runBlocking {
        // Arrange
        val username = "testUser"
        val token = "sample_token"
        coEvery { repository.login(username, token) } returns flowOf(Resource.Error("No internet connection"))

        // Act
        val results = loginUseCase(username, token).toList()

        // Assert
        assertTrue(results.isNotEmpty())
        assertTrue(results[0] is Resource.Error)
        assertEquals("No internet connection", (results[0] as Resource.Error).message)
    }
}