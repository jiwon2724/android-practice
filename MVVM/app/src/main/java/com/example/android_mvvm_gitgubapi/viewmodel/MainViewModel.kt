package com.example.android_mvvm_gitgubapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.android_mvvm_gitgubapi.model.Repositories
import com.example.android_mvvm_gitgubapi.model.UserInfo
import com.example.android_mvvm_gitgubapi.repository.MainRepository
import com.example.android_mvvm_gitgubapi.ui.RepositoriesUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _repositoriesResult = MutableLiveData<UserInfo?>()
    val repositoriesResult: LiveData<UserInfo?>
        get() = _repositoriesResult

    private val _repositoriesUiState = MutableStateFlow(RepositoriesUiState())
    val repositoriesUiState: StateFlow<RepositoriesUiState?>
        get() = _repositoriesUiState.asStateFlow()


    /** sharedFlow에 관하여
     * stateFlow보다 디테일한 설정값들을 설정 가능하다.
     * 중복값을 필터링한다. 연속된 emit이 같은값 이면 collect X
     *
     *
     * replay : 새로운 구독자들에게 이전 이벤트 방출여부 0 = 방출, 1 = 방출X
     * extraBufferCapacity : 추가 버퍼 생성 여부 1 = 생성
     * onBufferOverflow : 버퍼 초과시 처리 여부
     *
     * **/

    private val _sharedFlow = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow: SharedFlow<Int>
        get() = _sharedFlow.asSharedFlow()

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