package kr.co.lee.cloneapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SimpleViewModel(private val dao: SimpleDao): ViewModel() {
    /**
     * Pager에 사용가능 한 코틀린의 Flow 프로퍼티를 사용
     * Rxjava, RxKotlin, LiveData등 사용 가능
     */

    // Pager 객체 생성
    // Paging으로 들어가는 기본 진입점
    // PagingData의 반응형 스트림을 위한 생성자
    val allItems: Flow<PagingData<SimpleListItem>> = Pager(
        // PagingConfig 객체 생성
        // PagingSource에서 데이터를 로드할 때, Pager 내에서 로드 동작을 구성하는데 사용되는 객체
        config = PagingConfig(
            // PagingSource로부터 한번에 로드되는 아이템의 갯수를 정의
            pageSize = 60,
            // PagingSource에서 Null울 제공하는 경우, PagingData가 Null을 표시할 수 있는지 여부
            enablePlaceholders = true,
            // 페이지를 삭제하기 전에 PagingData에 로드할 수 있는 최대 항목 수를 지정
            maxSize = 200
        )
    ) {
        // PagingSourceFactory에 해당
        // PagingSource를 반환하는 메소드
        dao.allItemsByName()
    }.flow // Flow 프로퍼티
        .map { pagingData ->
            pagingData
                // SimpleItem을 공통 UI 모델에 매핑
                .map { simpleItem -> SimpleListItem.Item(simpleItem) }
                .insertSeparators { before: SimpleListItem?, after: SimpleListItem? ->
                    if (before == null && after == null) {
                        // List is empty after fully loaded; return null to skip adding separator.
                        null
                    } else if (after == null) {
                        // Footer; return null here to skip adding a footer.
                        //바닥글; 바닥글을 추가하는 것을 스킵하려면 null
                        null
                    } else if (before == null) {
                        // Header
                        // 머리글에 해당
                        SimpleListItem.Separator(after.name.first())
                    } else if (!before.name.first().equals(after.name.first(), ignoreCase = true)){
                        // Between two items that start with different letters.
                        // 다른 글자로 시작하는 두 개의 아이템 사이
                        SimpleListItem.Separator(after.name.first())
                    } else {
                        // Between two items that start with the same letter.
                        // 같은 언어로 시작하는 두 개의 아이템 사이
                        null
                    }
                }
        }
        // 이 흐름의 downStream 컬렉션이 동일한 페이징 데이터를 공유하도록
        // 페에징 데이터를 캐시
        .cachedIn(viewModelScope)

    // 액티비티에서 호출하는 메소드(데이터베이스에 삽입)
    fun insert(text: CharSequence) = ioThread {
        dao.insert(SimpleItem(id = 0, name = text.toString()))
    }

    // 액티비티에서 호출하는 메소드(데이터베이스에서 삭제)
    fun remove(simpleItem: SimpleItem) = ioThread {
        dao.delete(simpleItem)
    }
}
