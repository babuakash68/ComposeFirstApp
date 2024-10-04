package com.selflearning.composefirstapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.selflearning.composefirstapp.data.remote.GitHubApiClient // Import the GitHubApiClient
import com.selflearning.composefirstapp.data.repositories.GitHubRepository
import com.selflearning.composefirstapp.presentation.viewmodels.MainViewModel
import com.selflearning.composefirstapp.data.remote.models.Repository

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = GitHubRepository(GitHubApiClient.apiService)

        viewModel = MainViewModel(repository)

        // Fetch user repositories when the activity is created
        viewModel.fetchUserRepositories("android")

        setContent {
            // Observe the repository list and display it
            val repositories by viewModel.repositories.collectAsState()

            HomeScreen(viewModel,"android") // Pass the viewModel to HomeScreen
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
                fontStyle = FontStyle.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRepositoryListScreen() {
    RepositoryListScreen(repositories = listOf())
}
