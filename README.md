# android-paging-main
Android Paging 기본사항 코드랩

Paging3 라이브러리를 사용하면 로컬DB(Room, SQLite)나 네트워크를 통해 대규모 데이터 페이지를 로드하고 표시…
…할 수 있다! → 시스템 리소스를 모두 더 효율적으로 사용이 가능!

### Paging3를 사용하면 얻을 수 있는 이점

- 페이징된 데이터의 메모리 내 캐싱이 가능하다. → 앱이 페이징 데이터로 작업하는 동안 시스템 리소스를 효율적으로 사용이 가능하다. → 그냥 리소스를 효율적으로 사용이 가능하다~
- 중복 제거 기능이 기본적으로 제공된다
- RecyclerView를 끝까지 스크롤할 때 어댑터가 자동으로 데이터를 요청한다.
- Coroutine, Flow, LiveData들과 호환성이 좋다!
- 새로고침 및 재시도 기능을 포함하며 오류 처리를 기본으로 지원한다.

## Paging 라이브러리의 핵심 구성요소

- `**PagingSource**` : 특정 페이지 쿼리의 Data Chunk(한번에 하나씩 데이터를 읽어 Chunk라는 덩어리를 만든후 Chunk단위로 트랜잭션. 커밋 사이에 처리되는 row의 수)를 로드하는 기본 클래스이다. 데이터 레이어(Data Layer)의 일부이고, 일반적으로 `DataSource` 클래스에서 노출되고 이후에 `ViewModel` 에서 사용하기 위해 `Repository` 에 노출된다.
- `**PagingConfig**` : 페이징 동작을 결정하는 클래스. 페이지의 크기, placeholder 등등의 사용여부 설정
- `**Pager**` : `PagingData` 스트림을 생성하는 클래스. `PagingSource` 에 따라 다르게 실행되며 `ViewModel` 에서 만들어야 한다.
- `**PagingData**` : 페이지로 나눈 데이터의 컨테이너, 데이터를 새로고침할 때마다 자체 `PagingSource` 로 상응하는 `PagingData` 내보내기가 별도로 생성된다.
- `**PagingDataAdapter**` : RecyclerView에 PagingData를 표시하는 Apdapter의 서브클래스. `**PagingDataAdapter**` 는 내부 `PagingData` 로드 이벤트를 수신 대기하고, 페이지가 로드될 때 UI를 효율적으로 업데이트한다.
