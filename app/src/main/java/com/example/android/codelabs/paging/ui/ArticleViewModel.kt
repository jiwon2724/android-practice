/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.codelabs.paging.data.Article
import com.example.android.codelabs.paging.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the [ArticleActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
private const val ITEMS_PER_PAGE = 50

class ArticleViewModel(repository: ArticleRepository, ) : ViewModel() {

    /**
     * Stream of [Article]s for the UI.
     */
    // 메모리에 로드된 모든 항목을 StateFlow에 유지한다.
    // 이는 데이터가 너무 커지면 성능에 영향을 미친다.
    // 데이터가 변경됐을 때 리스트에서 기사 하나 이상을 업데이트 하는 작업은
    // 리스트가 클수록 비용이 더 많이 든다.
    // Paging 라이브러리는 이 모든 문제를 해결하는 동시에 점진적으로
    // 데이터를 가져오는 일관된 API를 제공한다.

    // Paging 라이브러리를 사용하면 Flow<PagingData<Article>>을 대신 노출할 수 있다.
    // val items: StateFlow<List<Article>> = repository.articleStream
    // PagingData는 로드된 데이터를 래핑하고 추가 데이터를 가져올 시기를 결정하는데 도움을 준다!
    // 동일한 페이지를 두 번 요청하지 않도록 한다.

    // PagingData는 stateIn, sharedIn 연산자를 사용하면 안된다.
    // PagingData의 각 방출을 독립적으로 수행되어야 한다.
    // -> flow를 다른 flow와 혼합하면 안된다.

    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource()}
        // pagingSourceFactory 람다는 PagingSource 인스턴스ㅡㄹ 재사용할 수 없으므로 호출되는 경우 항상
        // 완전히 새로운 PagingSource를 반환해야 한다.
    ).flow
}
