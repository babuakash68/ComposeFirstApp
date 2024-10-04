package com.selflearning.composefirstapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun fetchUserRepositories(username: String) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            val response = repository.getUserRepositories(username)
            _isLoading.value = false // Set loading state to false

            if (response.isSuccessful) {
                _repositories.value = response.body() ?: emptyList()
            } else {
                // Handle the error, e.g., set an error state or log the error
                _repositories.value = emptyList()
            }
        }
    }

    fun searchRepositories(query: String, page: Int) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            val result = repository.searchRepositories(query, page)
            _isLoading.value = false // Set loading state to false
            _repositories.value = result
        }
    }
}
