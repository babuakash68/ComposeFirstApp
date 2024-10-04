package com.selflearning.composefirstapp.data.remote

import com.selflearning.composefirstapp.data.remote.models.Repository
import com.selflearning.composefirstapp.data.remote.models.RepositoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Get a list of repositories for a specific user
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<Repository>>

    // Get details for a specific repository
    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Repository>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<RepositoryResponse>
}
