package com.sample.sololearntechnicalproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sample.sololearntechnicalproject.databinding.RepositoryDetailActivityBinding
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import com.sample.sololearntechnicalproject.viewmodel.DetailRepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity: AppCompatActivity() {

    private lateinit var repositoryDetailActivityBinding: RepositoryDetailActivityBinding
    private lateinit var viewModel: DetailRepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = intent.getParcelableExtra("REPO") as GitHubRepositoryModel?
        repositoryDetailActivityBinding = RepositoryDetailActivityBinding.inflate(layoutInflater)
        setContentView(repositoryDetailActivityBinding.root)

        setUpViewModel()

        viewModel.responseContainer.observe(this) {
            it?.let { repo ->
                repositoryDetailActivityBinding.repo = repo
                repositoryDetailActivityBinding.goToGithubPage.setOnClickListener {
                    goToGitHubPage(repo.url)
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchDetail(model)
    }


    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this)[DetailRepoViewModel::class.java]
    }

    private fun goToGitHubPage(url : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}