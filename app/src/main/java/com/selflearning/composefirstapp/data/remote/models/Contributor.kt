package com.selflearning.composefirstapp.data.remote.models

import com.google.gson.annotations.SerializedName

data class Contributor(
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val profileUrl: String,
    @SerializedName("contributions") val contributions: Int
)