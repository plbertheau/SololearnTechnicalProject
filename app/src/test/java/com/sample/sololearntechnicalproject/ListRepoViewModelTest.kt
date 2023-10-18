package com.sample.sololearntechnicalproject

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.sololearntechnicalproject.data.api.GitHubApiService
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import com.sample.sololearntechnicalproject.viewmodel.ListRepoViewModel
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
class ListRepoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListRepoViewModel
    private lateinit var viewList: List<GitHubRepositoryModel>
    private lateinit var errorMessage: String

    private val mockGitHubApiService = mockk<GitHubApiService>()

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
        viewList = mutableListOf()
        viewModel.responseContainer.observeForever {
            viewList = it
        }
        val success = Response.success(viewList)

        coEvery {
            mockGitHubApiService.fetchGitHubRepositories()
        } returns success

        viewModel.fetchRepoFromGitHub()
        viewModel.responseContainer.postValue(success.body())
        Assert.assertEquals(viewList, viewModel.responseContainer.value)
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
            mockGitHubApiService.fetchGitHubRepositories()
        } throws exception

        viewModel.fetchRepoFromGitHub()
        viewModel.errorMessage.postValue(exception.message)
        Assert.assertEquals(exception.message, viewModel.errorMessage.value)

    }


    private fun createViewModel(): ListRepoViewModel = ListRepoViewModel(mockGitHubApiService)

}