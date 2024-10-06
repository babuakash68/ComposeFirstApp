package com.selflearning.composefirstapp.data.repositories

import com.selflearning.composefirstapp.data.remote.ApiService
import com.selflearning.composefirstapp.data.remote.models.Repository
import retrofit2.Response

class GitHubRepository(private val apiService: ApiService) {

    suspend fun getUserRepositories(page: Int, perPage: Int = 10): Response<List<Repository>> {
        return apiService.fetchAllRepositories(page, perPage)
    }

    // Fetch repository details from GitHub API
    suspend fun getRepositoryDetails(owner: String, repo: String): Response<Repository> {
        return apiService.getRepositoryDetails(owner, repo)
    }

    suspend fun searchRepositories(query: String, page: Int, perPage: Int = 10): List<Repository> {
        val response = apiService.searchRepositories(query, page, perPage)
        if (response.isSuccessful) {
            response.body()?.items?.let { repositories ->
                return repositories // Return the list of repositories
            }
        }
        return emptyList() // Return empty list if response fails
    }

    suspend fun getContributors(repoName: String, owner: String) =
        apiService.getContributors(owner,repoName)
}
