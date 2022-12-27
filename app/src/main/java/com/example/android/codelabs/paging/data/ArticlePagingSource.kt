package com.example.android.codelabs.paging.data

import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.max

private val firstArticleCreateTime = LocalDateTime.now()
private const val LOAD_DELAY_MILLIS = 3_000L
private const val STARTING_KEY = 0

class ArticlePagingSource : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Paging 라이브러리가 UI 관련 항목을 새로고침해야 할 때 호출됨. -> PagingSoruce의 데이터가 변경 됐으니까!
        // Paging Source의 기본 데이터가 변경되었으며 UI에서 업데이트해야 하는 상황을 invalidation(무효화)라고 부름
        // Paging 라이브러리가 데이터를 새로고침할 때 새로운 PagingSource를 만들고 이에 응하는
        // 새로운 PagingData를 내보내 UI에 알린다.
        // 새로운 PagingSource에서 로드할 땐 새로고침 후 리스트에서 현재 위치(position?)을 잃지 않도록
        // 해당 키를 제공하기위해 이 함수(getFreshKey)가 호출된다.

        // 무효화가 발생하는 이유는 두 가지중 하나이다.
        // 1. PagingAdapter에서 refresh() 호출 시
        // 2. PagingSouce에서 invalidate() 호출 시 -? LoadResult.Invalid?
        // 무효화 후 리스트가 이동하지 않도록 하려면 반환된 키가 화면을 채울 만큼 충분한 항목을 로드해야한다.
        // -> 스크롤 유지!

        val anchorPosition = state.anchorPosition ?: return null
        // anchorPosition : PagingData는 아이템을 read할 때 특정 색인에서 읽으려고 함
        // 데이터를 읽은 경우에 UI에 표시되는 방식이다. anchorPosition은 데이터를 성공적으로 읽었을 때
        // 마지막 색인의 인덱스이다. 새로고침 시에는 anchorPosition에 가장 가까운 key를 사용
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val start = params.key ?: STARTING_KEY // 첫번째 로드일 경우 null이니까 start는 0으로 시작
        val range = start.until(start + params.loadSize) // 요청된 항목 수 만큼 범위 지정

        if(start != STARTING_KEY) delay(LOAD_DELAY_MILLIS)

        return LoadResult.Page(
            // data
            data = range.map { number ->
                Article(
                    id = number,
                    title = "Article $number",
                    description = "This des article $number",
                    created = firstArticleCreateTime.minusDays(number.toLong())
                )
            },

            // prevKey
            prevKey = when(start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },

            // nextKey
            nextKey = range.last + 1
        )

        // 사용자가 스크롤할 떼 표시할 더 많은 데이터를 비동기식으로 가져오는 함수
        // LoadParams 객체에는 다음 항목을 불러오는 정보가 저장된다.

        // 로드할 페이지의 키 : load() 함수가 처음 호출되는 경우 params.key는 null이다.
        // params.key가 null일 경우에 초기 페이지의 키를 정의해야한다. 보통 0으로 설정하는듯.

        // Load size : 요청된 항목의 수

        // 이 함수는 LoadResult를 반환한다. LoadResult는 다음 유형 중 하나이다.
        // 1. LoadResult.Page : load를 성공한 경우
        // 2. LoadResult.Error : 오류가 발생한 경우
        // 3. LoadResult.Invalid : PagingSource가 더 이상 결과의 무결성을 보장안하므로 무효화되어야 하는 경우

        // LoadResult.Page에는 세 가지 필수 인수가(arguments) 있다.
        // 1. data : 가져온 항목의 List
        // 2. prevKey : 현재 페이지에서 이전항목을 가져와야 하는 경우에 이 함수(load()) 에서 사용되는 key
        // 3. nextKey : 현재 페이지에서 다음항목을 가져와야 하는 경우에 이 함수(load()) 에서 사용되는 key
        // 상응하는 방향으로 로드할 데이터가 더 이상 없는경우 nextKey, prevKey가 null이다.

        // 로드되는 key는 Article.id 필드이다. -> ID는 1씩 increase
    }

    //
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}

/**
 * PagingSource를 빌드하려면 다음 항목을 정의해야한다.
 *
 * 1. 페이징 키의 유형
 * 특정 아이템의 ID이다. ID가 정렬되고, 증가한다고 보장됨. DB에서 PK느낌?
 *
 * 2. 로드된 데이터의 유형
 * 해당 아이템의 리스트를 반환. (Article)
 *
 * 3. 데이터를 가져오는 위치
 * 로컬DB, 네트워크 리소스 (Data Layer)
 *
 * **/