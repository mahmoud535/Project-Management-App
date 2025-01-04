package com.example.managementapp.presentation.screens.projectdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managementapp.domain.usecase.GetProjectDetailsUseCase
import com.example.managementapp.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectDetailsUseCase: GetProjectDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectDetailsState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("owner")?.let { owner ->
            savedStateHandle.get<String>("repo")?.let { repo ->
                loadProjectDetails(owner, repo)
            }
        }
    }

    fun loadProjectDetails(owner: String, repo: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            getProjectDetailsUseCase(owner, repo).collect { resource ->
                when (resource) {
                    is Resource.Loading -> { }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            project = resource.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = resource.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}