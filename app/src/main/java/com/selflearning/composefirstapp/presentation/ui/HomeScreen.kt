package com.selflearning.composefirstapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel

import androidx.compose.material3.* // Add this import for Material3 components
import androidx.compose.runtime.* // Add this import for mutableStateOf and remember
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, userName: String) {
    var searchQuery by remember { mutableStateOf("") }
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Observe loading state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Repository Search") },
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
                // Show a CircularProgressIndicator when loading
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(repositories) { repository ->
                        RepositoryCard(repository)
                    }
                }
            }
        }
    }
}

@Composable
fun RepositoryCard(repository: Repository) {
    Card(modifier = Modifier.padding(8.dp).clickable { /* Navigate to Repo Details */ }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = repository.name, fontSize = 20.sp)
            Text(text = repository.description ?: "No description available", fontSize = 14.sp)
        }
    }
}
