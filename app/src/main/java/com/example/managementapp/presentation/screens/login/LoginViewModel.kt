package com.example.managementapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementapp.domain.usecase.LoginUseCase
import com.example.managementapp.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _state.value = _state.value.copy(username = event.username)
            }

            is LoginEvent.TokenChanged -> {
                _state.value = _state.value.copy(token = event.token)
            }

            LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            loginUseCase(_state.value.username, _state.value.token).collect { resource ->
                _state.value = when (resource) {
                    is Resource.Loading -> _state.value.copy(isLoading = true)
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null
                    )

                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }
    }
}