package com.selflearning.composefirstapp.presentation.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.selflearning.composefirstapp.R
import com.selflearning.composefirstapp.data.remote.models.Contributor
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    navController: NavController,
    repoName: String,
    owner: String,
    viewModel: MainViewModel
) {
    val viewModel: MainViewModel = viewModel

    val repoDetails by viewModel.getRepositoryDetails(repoName, owner)
        .collectAsState(initial = null)
    val contributors by viewModel.getContributors(repoName, owner)
        .collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        repoDetails?.let { repo ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = rememberImagePainter(repo.owner.avatar_url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp) // Adjust the size as needed
                                        .padding(end = 15.dp)
                                        .align(Alignment.CenterVertically),
                                    contentScale = ContentScale.Crop
                                )
                                Column {
                                    Text(
                                        text = repo.name,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = repo.description ?: "No description",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Project Link: ",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f) // Weight to push link to the right
                                        )
                                        Text(
                                            text = repo.html_url,
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("webview/${Uri.encode(repo.html_url)}")
                                                },
                                            color = Color.Blue,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (contributors.isEmpty()) {
                            Text(
                                text = "No contributors available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            Text(
                                text = "Contributors List",
                                style = MaterialTheme.typography.titleLarge
                            )
                            LazyColumn {
                                items(contributors.size) { index ->
                                    ContributorCard(contributors[index])
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun ContributorCard(contributor: Contributor) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_blue),
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp)
    ) {
        Text(
            text = contributor.username,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.dark_blue)
        )
    }
}


