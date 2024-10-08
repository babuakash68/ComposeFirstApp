package com.selflearning.composefirstapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.selflearning.composefirstapp.data.local.db.AppDatabase
import com.selflearning.composefirstapp.data.remote.GitHubApiClient // Import the GitHubApiClient
import com.selflearning.composefirstapp.data.repositories.GitHubRepository
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.presentation.utils.NetworkUtils
import com.selflearning.composefirstapp.presentation.utils.WebViewScreen

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositoryDao = AppDatabase.getInstance(applicationContext).repositoryDao()
        val networkHelper = NetworkUtils(applicationContext)
        val repository = GitHubRepository(GitHubApiClient.apiService, repositoryDao, networkHelper)

        viewModel = MainViewModel(repository)

        viewModel.fetchUserRepositories()

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController, viewModel, "android",networkHelper)
                }
                composable("repoDetails/{repoName}/{owner}") { backStackEntry ->
                    val owner = backStackEntry.arguments?.getString("owner") ?: ""
                    val repoName = backStackEntry.arguments?.getString("repoName") ?: ""
                    RepoDetailsScreen(navController, repoName ,owner,viewModel)
                }
                composable("webview/{url}") { backStackEntry ->
                    val url = backStackEntry.arguments?.getString("url") ?: ""
                    WebViewScreen(url, navController)
                }
            }
        }
    }
}

@Composable
fun RepositoryListScreen(repositories: List<Repository>) {
    LazyColumn {
        items(repositories) { repository ->
            Text(
                text = repository.name,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontStyle = FontStyle.Normal,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRepositoryListScreen() {
    RepositoryListScreen(repositories = emptyList())
}