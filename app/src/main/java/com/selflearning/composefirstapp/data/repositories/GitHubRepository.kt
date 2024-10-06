package com.selflearning.composefirstapp.data.repositories

import com.selflearning.composefirstapp.data.local.dao.RepositoryDao
import com.selflearning.composefirstapp.data.local.entities.RepositoryEntity
import com.selflearning.composefirstapp.data.remote.ApiService
import com.selflearning.composefirstapp.data.remote.models.Owner
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.presentation.utils.NetworkUtils
import retrofit2.Response

class GitHubRepository(
    private val apiService: ApiService,
    private val repositoryDao: RepositoryDao,
    private val networkUtils: NetworkUtils
) {

    suspend fun getUserRepositories(page: Int, perPage: Int = 10): List<Repository> {
        return if (networkUtils.isNetworkAvailable()) {
            val response = apiService.fetchAllRepositories(page, perPage)
            if (response.isSuccessful) {
                response.body()?.let { repositories ->
                    // Save to Room DB
                    repositoryDao.insertRepositories(repositories.map { it.toEntity() })
                    return repositories
                }
            }
            emptyList()
        } else {
            // Get data from Room when no network
            repositoryDao.getAllRepositories().map { it.toDomain() }
        }
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
        apiService.getContributors(owner, repoName)

    // Mapping functions (from domain to entity and vice-versa)
    private fun Repository.toEntity(): RepositoryEntity {
        return RepositoryEntity(
            id = this.id,
            name = this.name,
            fullName = this.full_name,
            htmlUrl = this.html_url,
            description = this.description,
            stargazersCount = this.stargazers_count,
            forksCount = this.forks_count,
            language = this.language,
            ownerName = this.owner.login // Extracting owner name from Owner object
        )
    }

    private fun RepositoryEntity.toDomain(): Repository {
        return Repository(
            id = this.id,
            name = this.name,
            full_name = this.fullName,
            html_url = this.htmlUrl,
            description = this.description,
            stargazers_count = this.stargazersCount,
            forks_count = this.forksCount,
            language = this.language,
            owner = Owner(
                login = this.ownerName,
                id = 0L,
                avatar_url = ""
            )
        )
    }
}
