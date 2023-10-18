package com.sample.sololearntechnicalproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.sololearntechnicalproject.data.api.GitHubApiService
import com.sample.sololearntechnicalproject.model.GitHubDetailRepoModel
import com.sample.sololearntechnicalproject.model.GitHubRepositoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListRepoViewModel @Inject constructor(
    @Named("Retrofit")
    private val retrofitInstance: GitHubApiService
) :
    ViewModel() {

    val responseContainer = MutableLiveData<List<GitHubRepositoryModel>>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private var job: Job? = null

    fun fetchRepoFromGitHub() {
        isLoading.value = true
        job = viewModelScope.launch {
            val response = retrofitInstance.fetchGitHubRepositories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        for ((indice, repo: GitHubRepositoryModel) in body.withIndex()) {
                            val detailResponse: Response<GitHubDetailRepoModel> =
                                retrofitInstance.fetchDetailRepository(
                                    repo.owner.ownerName,
                                    repo.repositoryName
                                )
                            withContext(Dispatchers.Main) {
                                response.body()!![indice].numberOfStar =
                                    detailResponse.body()!!.numberOfStar
                            }
                        }
                    }
                } else {
                    onError(response.code(), "Error : ${response.message()}")
                }
            }
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body()?.sortedByDescending { it.numberOfStar })
                    isLoading.value = false
                } else {
                    onError(response.code(), "Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(code: Int, message: String) {
        var errorMessage = message
        when (code) {
            403 -> errorMessage = "Forbidden"
            422 -> errorMessage = "Validation failed, or the endpoint has been spammed."
        }
        this.errorMessage.value = errorMessage
        isLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}