package com.example.managementapp.presentation.screens.project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementapp.domain.usecase.GetUserProjectsUseCase
import com.example.managementapp.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val getUserProjectsUseCase: GetUserProjectsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectListState())
    val state = _state.asStateFlow()

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            getUserProjectsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Success -> _state.value = _state.value.copy(
                        projects = resource.data,
                        isLoading = false
                    )

                    is Resource.Error -> _state.value = _state.value.copy(
                        error = resource.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}