package com.example.android_mvvm_gitgubapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.android_mvvm_gitgubapi.model.Repositories
import com.example.android_mvvm_gitgubapi.model.UserInfo
import com.example.android_mvvm_gitgubapi.repository.MainRepository
import com.example.android_mvvm_gitgubapi.ui.RepositoriesUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _repositoriesResult = MutableLiveData<UserInfo?>()
    val repositoriesResult: LiveData<UserInfo?>
        get() = _repositoriesResult

    private val _repositoriesUiState = MutableStateFlow(RepositoriesUiState())
    val repositoriesUiState: StateFlow<RepositoriesUiState?>
        get() = _repositoriesUiState.asStateFlow()

    /** asStateFLow()에 관하여..
     *
     * 시퀀스를 동일한 방식으로 치환하는 건 성능을 위해서
     * state flow를 치환하는 건 안정성을 위해서이다. -> 더 확실하게 안전성을 보장한다?
     * Represents this mutable state flow as a read-only state flow.
     * kotlin에서 read-only는 안정성과 동치니까요
     *
     * **/


    fun getRepositories(username: String) = viewModelScope.launch {
        val response = mainRepository.getUserRepositories(username)
        _repositoriesResult.value = response
    }

    fun getRepositoriesFlow(username: String) = viewModelScope.launch {
        mainRepository.getUserRepositoriesFlow(username).collect {
            _repositoriesUiState.value = it
        }
    }
}

class MainViewModelFactory(private val param: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(param) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}