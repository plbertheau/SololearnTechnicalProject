package com.sample.sololearntechnicalproject.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class GitHubRepositoryModel(
    @SerializedName("name") val repositoryName: String,
    @SerializedName("description") val repositoryDescription: String,
    @SerializedName("stargazers_count") var numberOfStar: Int,
    @SerializedName("owner") val owner: Owner
) : Parcelable

@Parcelize
data class Owner(
    @SerializedName("login") val ownerName: String
) : Parcelable