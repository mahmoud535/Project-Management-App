package com.example.managementapp.presentation.screens.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.managementapp.R
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project

@Preview(showBackground = true)
@Composable
fun PreviewProjectListView() {
    val sampleProjects = listOf(
        Project(
            name = "Project 1",
            description = "Description for Project 1",
            lastUpdated = "2025-01-01",
            id = 1,
            forksCount = 10,
            issuesCount = 2,
            owner = Owner("", "", ""),
            starsCount = 5,

            ),
        Project(
            name = "Project 2",
            description = "Description for Project 2",
            lastUpdated = "2025-01-02",
            id = 2,
            forksCount = 10,
            issuesCount = 2,
            owner = Owner("", "", ""),
            starsCount = 5,
        ),
        Project(
            name = "Project 3",
            description = "Description for Project 3",
            lastUpdated = "2025-01-03",
            id = 3,
            forksCount = 10,
            issuesCount = 2,
            owner = Owner("", "", ""),
            starsCount = 5,
        )
    )

    ProjectListView(
        projects = sampleProjects,
        isLoading = false,
        error = null,
        onProjectClick = { projectName -> }
    )
}

@Composable
fun ProjectListView(
    projects: List<Project>,
    isLoading: Boolean,
    error: String?,
    onProjectClick: (Project) -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.lightGray2)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                stringResource(id = R.string.project_listen_name),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally), color = colorResource(
                        id = R.color.blue
                    )
                )
            } else {
                LazyColumn (modifier = Modifier.padding(bottom = 25.dp)){
                    items(projects) { project ->
                        ProjectItem(project = project, onClick = { onProjectClick(project) })
                    }
                }
            }

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

