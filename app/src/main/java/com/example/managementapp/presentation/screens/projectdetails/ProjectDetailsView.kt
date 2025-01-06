package com.example.managementapp.presentation.screens.projectdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.managementapp.R
import com.example.managementapp.domain.model.Owner
import com.example.managementapp.domain.model.Project

@Preview(showBackground = true)
@Composable
fun PreviewProjectDetailsView() {
    val sampleProject = Project(
        name = "Sample Project",
        description = "This is a sample project description.",
        lastUpdated = "2025-01-01",
        owner = Owner(login = "SampleOwner", avatarUrl = "https://example.com/avatar.png",name = "Sample Owner"),
        starsCount = 10,
        forksCount = 5,
        issuesCount = 2,
        id = 1
    )

    ProjectDetailsView(
        project = sampleProject,
        isLoading = false,
        error = null
    )
}

@Composable
fun ProjectDetailsView(
    project: Project?,
    isLoading: Boolean,
    error: String?
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally), color = colorResource(
                        id = R.color.blue
                    )
                )
            } else {
                project?.let {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberImagePainter(it.owner?.avatarUrl),
                            contentDescription = "Owner Avatar",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .border(
                                    3.dp,
                                    Color.Black,
                                    RoundedCornerShape(15.dp)
                                )
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop,

                            )

                        Text(
                            text = "Owner: ${it.owner?.login}",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    it.name?.let { it1 -> Text(text = it1, style = MaterialTheme.typography.h5) }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = it.description ?: "No description",
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Text(text = "Last updated: ", style = MaterialTheme.typography.body2)
                        Text(
                            text = " ${it.lastUpdated}", style = MaterialTheme.typography.body2,
                            color =
                            colorResource(id = R.color.darkBlue),
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Stars: ${it.starsCount}", style = MaterialTheme.typography.body2)
                    Text(text = "Forks: ${it.forksCount}", style = MaterialTheme.typography.body2)
                    Text(text = "Issues: ${it.issuesCount}", style = MaterialTheme.typography.body2)
                } ?: run {
                    Text(
                        text = stringResource(id = R.string.error_Project_details_message),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
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