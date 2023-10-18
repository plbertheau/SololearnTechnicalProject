package com.sample.sololearntechnicalproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.sample.sololearntechnicalproject.R
import com.sample.sololearntechnicalproject.databinding.RepositoryItemBinding
import com.sample.sololearntechnicalproject.listener.OnRepositoryClickListener
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel

class ListRepositoryAdapter(
    private val context: Context, private val onRepositoryClickListener: OnRepositoryClickListener
) : RecyclerView.Adapter<ListRepositoryAdapter.ListRepositoryViewHolder>() {

    inner class ListRepositoryViewHolder(private val binding: RepositoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: GitHubRepositoryModel) {
            binding.repo = repo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRepositoryViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return getListRepoViewHolder(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: ListRepositoryViewHolder, position: Int) {
        holder.bind(result[position])
    }

    private fun getListRepoViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ListRepositoryViewHolder {
        @LayoutRes val layoutId = R.layout.repository_item
        val binding: RepositoryItemBinding =
            DataBindingUtil.inflate(inflater, layoutId, parent, false)
        binding.root.setOnClickListener { onRepositoryClickListener.onClickRepository(binding.repo) }
        return ListRepositoryViewHolder(binding)
    }

    private val differ = AsyncListDiffer(this, GitHubRepoDiffCallBack())

    var result: List<GitHubRepositoryModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount() = result.size
}