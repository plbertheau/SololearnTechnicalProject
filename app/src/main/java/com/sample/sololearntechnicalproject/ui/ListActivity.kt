package com.sample.sololearntechnicalproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sample.sololearntechnicalproject.adapter.ListRepositoryAdapter
import com.sample.sololearntechnicalproject.databinding.RepositoryListActivityBinding
import com.sample.sololearntechnicalproject.listener.OnRepositoryClickListener
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import com.sample.sololearntechnicalproject.viewmodel.ListRepoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListActivity : AppCompatActivity(), OnRepositoryClickListener {

    private lateinit var repositoryListActivityBinding: RepositoryListActivityBinding
    private lateinit var viewModel: ListRepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryListActivityBinding = RepositoryListActivityBinding.inflate(layoutInflater)
        setContentView(repositoryListActivityBinding.root)

        setUpViewModel()

        val adapter = ListRepositoryAdapter(this@ListActivity, this)
        setUpRecyclerView(adapter)

        viewModel.responseContainer.observe(this) {
            it?.let {
                adapter.result = it
            }
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                repositoryListActivityBinding.recyclerview.visibility = View.GONE
                repositoryListActivityBinding.progress.visibility = View.VISIBLE
            } else {
                repositoryListActivityBinding.recyclerview.visibility = View.VISIBLE
                repositoryListActivityBinding.progress.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchRepoFromGitHub()
    }

    private fun setUpRecyclerView(adapter: ListRepositoryAdapter) {
        repositoryListActivityBinding.viewModel = viewModel
        repositoryListActivityBinding.recyclerview.adapter = adapter
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this)[ListRepoViewModel::class.java]
    }

    private fun goToDetail(repo: GitHubRepositoryModel?) {
        val i = Intent(this@ListActivity, DetailActivity::class.java)
        i.putExtra("REPO", repo)
        startActivity(i)
    }

    override fun onClickRepository(repo: GitHubRepositoryModel?) {
        goToDetail(repo)
    }
}