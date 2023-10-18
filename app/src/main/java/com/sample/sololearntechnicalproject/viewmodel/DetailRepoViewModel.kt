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
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DetailRepoViewModel @Inject constructor(
    @Named("Retrofit")
    private val retrofitInstance: GitHubApiService
) :
    ViewModel() {

    val responseContainer = MutableLiveData<GitHubDetailRepoModel>()
    val errorMessage = MutableLiveData<String>()

    private var job: Job? = null

    fun fetchDetail(model: GitHubRepositoryModel?) {
        job = viewModelScope.launch {
            val response = model?.let { retrofitInstance.fetchDetailRepository(it.owner.ownerName, it.repositoryName) }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        responseContainer.postValue(response.body())
                    } else {
                        onError(response.code(), "Error : ${response.message()}")
                    }
                }
            }
        }
    }

    private fun onError(code: Int, message: String) {
        var errorMessage = message
        when (code) {
            301 -> errorMessage = "Moved permanently"
            403 -> errorMessage = "Forbidden"
            404 -> errorMessage = "Resource not found"
        }
        this.errorMessage.value = errorMessage
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}