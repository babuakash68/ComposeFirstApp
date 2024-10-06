package com.selflearning.composefirstapp.presentation.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.presentation.utils.NetworkUtils
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    userName: String,
    networkUtils: NetworkUtils
) {
    var searchQuery by remember { mutableStateOf("") }
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        }.collect { visibleItemsCount ->
            if (visibleItemsCount >= repositories.size && !isLoading && searchQuery.isNotEmpty()) {
                viewModel.searchRepositories(searchQuery)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Repository") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Repositories") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = {

                    if (networkUtils.isNetworkAvailable()) {
                        viewModel.searchRepositories(searchQuery)
                        viewModel.resetSearch()
                    } else {
                        networkUtils.showNetworkAlertDialog(context)
                    }
                }) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (networkUtils.isNetworkAvailable()) {
                            searchQuery = ""
                            viewModel.resetSearch()
                            viewModel.fetchUserRepositories()
                        } else {
                            networkUtils.showNetworkAlertDialog(context)
                        }
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
                            if (networkUtils.isNetworkAvailable()) {
                                navController.navigate("repoDetails/${repository.name}/${repository.owner.login}")
                            } else {
                                networkUtils.showNetworkAlertDialog(context)
                            }
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
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = repository.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Language: ${repository.language ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}
