package com.example.android_mvvm_gitgubapi.viewmodel

import androidx.lifecycle.*
import com.example.android_mvvm_gitgubapi.model.Repositories
import com.example.android_mvvm_gitgubapi.model.UserInfo
import com.example.android_mvvm_gitgubapi.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _repositoriesResult = MutableLiveData<UserInfo?>()

    val repositoriesResult: LiveData<UserInfo?>
        get() = _repositoriesResult

    fun getRepositories(username: String) = viewModelScope.launch {
        val response = mainRepository.getUserRepositories(username)
        _repositoriesResult.value = response
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