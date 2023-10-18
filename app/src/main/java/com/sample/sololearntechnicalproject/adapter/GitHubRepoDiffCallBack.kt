package com.sample.sololearntechnicalproject.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel

class GitHubRepoDiffCallBack : DiffUtil.ItemCallback<GitHubRepositoryModel>() {
    override fun areItemsTheSame(
        oldItem: GitHubRepositoryModel,
        newItem: GitHubRepositoryModel
    ): Boolean {
        return oldItem.repositoryName == newItem.repositoryName
    }

    override fun areContentsTheSame(
        oldItem: GitHubRepositoryModel,
        newItem: GitHubRepositoryModel
    ): Boolean {
        return oldItem == newItem
    }
}