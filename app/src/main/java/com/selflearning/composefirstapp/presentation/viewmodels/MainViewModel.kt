package com.selflearning.composefirstapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selflearning.composefirstapp.data.remote.models.Contributor
import com.selflearning.composefirstapp.data.repositories.GitHubRepository
import com.selflearning.composefirstapp.data.remote.models.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GitHubRepository) : ViewModel() {

    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private var currentPage = 1
    private var isLoadingNextPage = false
    private var hasMoreData = true

    fun fetchUserRepositories() {
        if (_isLoading.value || isLoadingNextPage || !hasMoreData) return // Avoid fetching if already loading or no more data

        isLoadingNextPage = true
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getUserRepositories(currentPage) // Fetch repositories based on the current page
            _isLoading.value = false

            if (response.isSuccessful) {
                val newRepos = response.body() ?: emptyList()
                if (newRepos.isEmpty()) {
                    hasMoreData = false // No more data to load
                } else {
                    _repositories.value += newRepos // Append new repositories to the existing list
                    currentPage++ // Increment the page number
                }
            } else {
                _repositories.value = emptyList()
            }

            isLoadingNextPage = false
        }
    }

    fun searchRepositories(query: String) {
        if (!hasMoreData || _isLoading.value) return // Prevent additional API calls if no more data

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.searchRepositories(query, currentPage)
            _isLoading.value = false

            if (result.isNotEmpty()) {
                _repositories.value = _repositories.value + result // Append new results to the existing list
                currentPage++ // Increment page for the next fetch
            }

            if (result.size < 10) {
                hasMoreData = false // If fewer than 10 items are returned, stop pagination
            }
        }
    }

    fun getRepositoryDetails(repoName: String, owner: String): StateFlow<Repository?> {
        val repositoryDetails = MutableStateFlow<Repository?>(null)
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getRepositoryDetails(owner,repoName)
            if (response.isSuccessful) {
                repositoryDetails.value = response.body()
                _isLoading.value = false
            }
        }
        return repositoryDetails
    }

    fun getContributors(repoName: String, owner: String): StateFlow<List<Contributor>> {
        val contributors = MutableStateFlow<List<Contributor>>(emptyList())
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getContributors(repoName,owner)
            if (response.isSuccessful) {
                contributors.value = response.body() ?: emptyList()
                _isLoading.value = false
            }
        }
        return contributors
    }
    fun resetSearch() {
        currentPage = 1
        hasMoreData = true
        _repositories.value = emptyList() // Clear previous results
    }
}
