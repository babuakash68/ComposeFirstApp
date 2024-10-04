package com.selflearning.composefirstapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel

import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel, userName: String) {
    var searchQuery by remember { mutableStateOf("") }
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Observe loading state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Repository") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Repositories") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = { viewModel.searchRepositories(searchQuery, 1) }) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.width(8.dp)) // Add space between buttons
                Button(
                    onClick = {
                        searchQuery = ""
                        viewModel.fetchUserRepositories(userName)
                    }
                ) {
                    Text("Show All Repositories")
                }
            }


            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn {
                    items(repositories) { repository ->
                        RepositoryCard(repository) {
                            // Navigate to RepoDetailsScreen with repository name
                            navController.navigate("repoDetails/${repository.name}")
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun RepositoryCard(repository: Repository, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // Make the whole card clickable
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface, // Background color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Add elevation for shadow
        shape = MaterialTheme.shapes.medium // Rounded corners
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Padding inside the card
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp)) // Space between name and description

            Text(
                text = repository.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp)) // Space between description and other text

            Text(
                text = "Language: ${repository.language ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}