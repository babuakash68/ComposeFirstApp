package com.selflearning.composefirstapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.Request

object GitHubApiClient {
    private const val BASE_URL = "https://api.github.com/"
    private const val TOKEN = "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"
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "token $TOKEN")
                .build()
            chain.proceed(requestWithToken)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
