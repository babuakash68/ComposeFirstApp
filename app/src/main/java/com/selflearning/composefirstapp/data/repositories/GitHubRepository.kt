package com.selflearning.composefirstapp.data.repositories

import com.selflearning.composefirstapp.data.remote.ApiService
import com.selflearning.composefirstapp.data.remote.models.Repository
import retrofit2.Response

class GitHubRepository(private val apiService: ApiService) {

    suspend fun getUserRepositories(username: String): Response<List<Repository>> {
        return apiService.getUserRepositories(username)
    }

    // Fetch repository details from GitHub API
    suspend fun getRepositoryDetails(owner: String, repo: String): Response<Repository> {
        return apiService.getRepositoryDetails(owner, repo)
    }

    suspend fun searchRepositories(query: String, page: Int): List<Repository> {
        val response = apiService.searchRepositories(query, page)
        if (response.isSuccessful) {
            response.body()?.items?.let { repositories ->
//                dao.insertAll(repositories.take(15)) // Save first 15 items offline
                return repositories
            }
        }
        return emptyList()
    }

    suspend fun getContributors(repoName: String, owner: String) =
        apiService.getContributors(owner,repoName)
}
