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

    fun fetchUserRepositories() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getUserRepositories()
            _isLoading.value = false

            if (response.isSuccessful) {
                _repositories.value = response.body() ?: emptyList()
            } else {
                _repositories.value = emptyList()
            }
        }
    }

    fun searchRepositories(query: String, page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.searchRepositories(query, page)
            _isLoading.value = false
            _repositories.value = result
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
}
