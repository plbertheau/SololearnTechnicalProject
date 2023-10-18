package com.sample.sololearntechnicalproject.data.api

import com.sample.sololearntechnicalproject.model.GitHubDetailRepoModel
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GitHubApiService {

    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: token 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"
    )
    @GET("/repositories")
    suspend fun fetchGitHubRepositories(): Response<List<GitHubRepositoryModel>>

    @Headers("Accept: application/vnd.github+json",
        "Authorization: token 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")
    @GET("/repos/{owner}/{repo}")
    suspend fun fetchDetailRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GitHubDetailRepoModel>
}