package com.sample.sololearntechnicalproject

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.sololearntechnicalproject.data.api.GitHubApiService
import com.sample.sololearntechnicalproject.model.GitHubDetailRepoModel
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import com.sample.sololearntechnicalproject.viewmodel.DetailRepoViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)

class DetailRepoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailRepoViewModel
    private lateinit var item: GitHubDetailRepoModel
    private lateinit var errorMessage: String
    private val mockGitHubRepositoryModel = mockk<GitHubRepositoryModel>()
    private val mockGitHubApiService = mockk<GitHubApiService>()
    private val mockGitHubDetailRepoModel = mockk<GitHubDetailRepoModel>()

    @Before
    fun setup() {
        mockkStatic(Looper::class)
        val looper = mockk<Looper> {
            every { thread } returns Thread.currentThread()
        }
        every { Looper.getMainLooper() } returns looper
    }

    @Test
    fun fetch_response_is_successful() {
        viewModel = createViewModel()

        viewModel.responseContainer.observeForever {
            item = it
        }
        val success = Response.success(mockGitHubDetailRepoModel)

        coEvery {
            mockGitHubApiService.fetchDetailRepository(any(), any())
        } returns success

        viewModel.fetchDetail(mockGitHubRepositoryModel)
        viewModel.responseContainer.postValue(success.body())
        Assert.assertEquals(mockGitHubDetailRepoModel, viewModel.responseContainer.value)
    }

    @Test
    fun fetch_response_is_error() {
        viewModel = createViewModel()
        errorMessage = "empty"
        viewModel.errorMessage.observeForever {
            errorMessage = it
        }
        val message = "Error from api"
        val exception = IllegalAccessException(message)
        coEvery {
            mockGitHubApiService.fetchDetailRepository(any(), any())
        } throws exception

        viewModel.fetchDetail(mockGitHubRepositoryModel)
        viewModel.errorMessage.postValue(exception.message)
        Assert.assertEquals(exception.message, viewModel.errorMessage.value)

    }


    private fun createViewModel(): DetailRepoViewModel = DetailRepoViewModel(mockGitHubApiService)

}