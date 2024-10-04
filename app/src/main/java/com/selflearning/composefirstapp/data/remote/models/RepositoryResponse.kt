package com.selflearning.composefirstapp.data.remote.models

data class RepositoryResponse(
    val items: List<Repository>,
    val total_count: Int
)