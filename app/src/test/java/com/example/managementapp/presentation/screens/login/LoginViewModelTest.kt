package com.example.managementapp.presentation.screens.login

import com.example.managementapp.domain.usecase.LoginUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        loginUseCase = mockk()
        loginViewModel = LoginViewModel(loginUseCase)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `onEvent() with UsernameChanged updates username in state`() {
        // Arrange
        val username = "testUser"

        // Act
        loginViewModel.onEvent(LoginEvent.UsernameChanged(username))

        // Assert
        assertEquals(username, loginViewModel.state.value.username)
    }

    @Test
    fun `onEvent() with TokenChanged updates token in state`() {
        // Arrange
        val token = "sample_token"

        // Act
        loginViewModel.onEvent(LoginEvent.TokenChanged(token))

        // Assert
        assertEquals(token, loginViewModel.state.value.token)
    }
}