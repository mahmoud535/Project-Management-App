package com.example.managementapp.presentation.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.managementapp.R
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project

@Preview(showBackground = true)
@Composable
fun PreviewProjectItem() {
    val sampleProject = Project(
        name = "Sample Project",
        description = "This is a sample project description.",
        lastUpdated = "2025-01-01",
        owner = Owner(
            login = "SampleOwner",
            avatarUrl = "https://example.com/avatar.png",
            name = "Owner Name"
        ),
        starsCount = 10,
        forksCount = 5,
        issuesCount = 2,
        id = 1
    )

    ProjectItem(
        project = sampleProject,
        onClick = { }
    )
}

@Composable
fun ProjectItem(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.name, style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = project.description ?: "No description",
                style = MaterialTheme.typography.body2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "Last updated: ", style = MaterialTheme.typography.body2)
                Text(
                    text = " ${project.lastUpdated}", style = MaterialTheme.typography.body2,
                    color =
                    colorResource(id = R.color.darkBlue),
                )
            }
        }
    }
}