package com.sample.sololearntechnicalproject.listener

import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel

interface OnRepositoryClickListener {

    fun onClickRepository(repo: GitHubRepositoryModel?)
}