package com.selflearning.composefirstapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selflearning.composefirstapp.data.remote.models.Contributor
import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.data.repositories.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GitHubRepository) : ViewModel() {

    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    private val _repositoryDetails = MutableStateFlow<Repository?>(null)
    val repositoryDetails: StateFlow<Repository?> get() = _repositoryDetails

    private val _contributors = MutableStateFlow<List<Contributor>>(emptyList())
    val contributors: StateFlow<List<Contributor>> get() = _contributors

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var currentPage = 1
    private var isLoadingNextPage = false
    private var hasMoreData = true

    fun fetchUserRepositories() {
        if (_isLoading.value || isLoadingNextPage || !hasMoreData) return

        isLoadingNextPage = true
        viewModelScope.launch {
            _isLoading.value = true
            val newRepos = repository.getUserRepositories(currentPage)
            _isLoading.value = false

            if (newRepos.isEmpty()) {
                hasMoreData = false
            } else {
                _repositories.value += newRepos
                currentPage++
            }

            isLoadingNextPage = false
        }
    }

    fun searchRepositories(query: String) {
        if (!hasMoreData || _isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.searchRepositories(query, currentPage)
            _isLoading.value = false

            if (result.isNotEmpty()) {
                _repositories.value += result
                currentPage++
            }

            if (result.size < 10) {
                hasMoreData = false
            }
        }
    }

    fun getRepositoryDetails(repoName: String, owner: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Reset previous data
            _repositoryDetails.value = null
            val response = repository.getRepositoryDetails(owner, repoName)
            if (response.isSuccessful) {
                _repositoryDetails.value = response.body()
            }
            _isLoading.value = false
        }
    }

    fun getContributors(repoName: String, owner: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Reset previous contributors
            _contributors.value = emptyList()
            val response = repository.getContributors(repoName, owner)
            if (response.isSuccessful) {
                _contributors.value = response.body() ?: emptyList()
            }
            _isLoading.value = false
        }
    }


    fun resetSearch() {
        currentPage = 1
        hasMoreData = true
        _repositories.value = emptyList()
    }
}
