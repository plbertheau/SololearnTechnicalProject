package com.sample.sololearntechnicalproject.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubDetailRepoModel(
    @SerializedName("name") val repositoryName: String,
    @SerializedName("description") val repositoryDescription: String,
    @SerializedName("stargazers_count") val numberOfStar: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("language") val language: String,
    @SerializedName("html_url") val url: String
) : Parcelable