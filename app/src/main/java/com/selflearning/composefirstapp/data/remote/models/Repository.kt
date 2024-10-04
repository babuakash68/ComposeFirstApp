package com.selflearning.composefirstapp.data.remote.models

data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: Owner,
    val html_url: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val language: String?
)

