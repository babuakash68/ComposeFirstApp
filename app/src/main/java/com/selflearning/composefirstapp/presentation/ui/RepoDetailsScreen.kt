package com.selflearning.composefirstapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.selflearning.composefirstapp.data.remote.models.Contributor
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(navController: NavController, repoName: String,  owner: String, viewModel: MainViewModel) {
    val viewModel: MainViewModel = viewModel

    val repoDetails by viewModel.getRepositoryDetails(repoName,owner).collectAsState(initial = null)
    val contributors by viewModel.getContributors(repoName, owner).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repo Details") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Ensure content respects the padding (avoid drawing under system bars)
                    .padding(16.dp) // General padding for the content inside the screen
            ) {
                Text(text = "Repository Details", style = MaterialTheme.typography.titleLarge)

                repoDetails?.let { repo ->
                    Text(text = "Name: ${repo.name}")
                    Text(text = "Description: ${repo.description ?: "No description"}")
                    Text(
                        text = "Project Link: ${repo.html_url}",
                        modifier = Modifier.clickable {
                            // Handle project link click
                        },
                        color = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Contributors", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(contributors.size) { index ->
                        ContributorCard(contributors[index])
                    }
                }
            }
        }
    )
}

@Composable
fun ContributorCard(contributor: Contributor) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = contributor.username, style = MaterialTheme.typography.bodyLarge)
    }
}
