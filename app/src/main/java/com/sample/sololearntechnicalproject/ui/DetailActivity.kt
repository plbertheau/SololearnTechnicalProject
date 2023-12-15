package com.sample.sololearntechnicalproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import com.sample.sololearntechnicalproject.viewmodel.DetailRepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    /*private lateinit var repositoryDetailActivityBinding: RepositoryDetailActivityBinding*/
    private lateinit var viewModel: DetailRepoViewModel

    /*override fun onCreate(savedInstanceState: Bundle?) {
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
    }*/


    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[DetailRepoViewModel::class.java]
    }

    private fun goToGitHubPage(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = intent.getParcelableExtra("REPO") as GitHubRepositoryModel?
        setUpViewModel()


        viewModel.fetchDetail(model)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailCard(
                        viewModel = viewModel,
                        goToGitHubPage = ::goToGitHubPage
                    )
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    modifier: Modifier = Modifier,
    viewModel: DetailRepoViewModel,
    goToGitHubPage: (String) -> Unit = {}
) {
    val itemState by viewModel.responseContainer.observeAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = modifier
                .padding(8.dp)
                .wrapContentHeight()
                .align(TopCenter)
        ) {
            var expanded by remember { mutableStateOf(false) }
            Column(
                Modifier
                    .clickable { expanded = !expanded }
                    .padding(8.dp)) {
                LineInfo(
                    cta = "Number of star: ",
                    detailText = itemState?.numberOfStar.toString()
                )
                LineInfo(cta = "Name : ", detailText = itemState?.repositoryName.toString())
                LineInfo(
                    cta = "Description : ",
                    detailText = itemState?.repositoryDescription.toString()
                )
                LineInfo(
                    cta = "Number of forks : ",
                    detailText = itemState?.forksCount.toString()
                )
                LineInfo(cta = "Language : ", detailText = itemState?.language.toString())
                AnimatedVisibility(expanded) {
                    Button(
                        onClick = { goToGitHubPage(itemState?.url.toString()) },
                        modifier.fillMaxWidth()
                    ) {
                        Text(text = "Go to github page")
                    }
                }
            }
        }
    }
}


@Composable
fun LineInfo(cta: String, detailText: String) {
    Row {
        Text(text = cta)
        Text(text = detailText)
    }
}
