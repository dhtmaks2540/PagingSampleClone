/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paging.android.example.com.pagingsample

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A simple [AndroidViewModel] that provides a [Flow]<[PagingData]> of delicious cheeses.
 */
/**
 * Flow<PagingData>를 제공하는 간단한 AndroidViewModel입니다.
 */

class CheeseViewModel(private val dao: CheeseDao) : ViewModel() {
    /**
     * We use the Kotlin [Flow] property available on [Pager]. Java developers should use the
     * RxJava or LiveData extension properties available in `PagingRx` and `PagingLiveData`.
     */
    // Pager 객체 생성
    // PagingConfig를 지정
    val allCheeses: Flow<PagingData<CheeseListItem>> = Pager(
        config = PagingConfig(
            /**
             * 적당한 페이지 사이즈는 적어도 사용자가 null 항목을 볼 수 없도록
             * 큰 디바이스에 가치가 있는 컨텐츠로 몇 개의 스크린을 채우는 것입니다.
             * 이 상수를 사용해 paging 동작을 관찰할 수 있습니다.
             *
             * 큰 장치에서 스크롤하는 사용자가 작은 장치보다 더 빠르게 항목을 스크롤할 것으로 예상되지 않는 한
             * (예: 큰 장치가 항목의 그리드 레이아웃을 사용하는 경우)
             * 목록 장치 크기에 따라 이를 변경할 수 있지만 필요하지 않은 경우가 많습니다.
             */

            /**
             * A good page size is a value that fills at least a few screens worth of content on a
             * large device so the User is unlikely to see a null item.
             * You can play with this constant to observe the paging behavior.
             *
             * It's possible to vary this with list device size, but often unnecessary, unless a
             * user scrolling on a large device is expected to scroll through items more quickly
             * than a small device, such as when the large device uses a grid layout of items.
             */
            pageSize = 60,

            /**
             * plageholder가 활성화된다면, PagedList는 전체 크기를 보고하지만
             * onBind 메서드에서 일부 항목이 null일 수도 있습니다(PagedListAdapter는 데이터가 로드될 때 재바인드를 트리거함)
             *
             * placeholder가 비활성화된다면, onBind는 null을 수신하지 않지만 더 많은 페이지가 로드된다면
             * 스크롤바는 안절부절하지 못하게 됩니다.
             * placeholder가 disable이라면 scrollbar를 disable 해야합니다.
             */
            enablePlaceholders = true,

            /**
             * PagedList가 메모리에 한번에 가질 수 있는 아이템의 최대 숫자입니다.
             *
             * 이 숫자는 더 많은 페이지가 로드됨에 따라 먼 페이지를 삭제하기 시작하도록 PagedList를 발생시킵니다.
             */
            maxSize = 200
        )
    ) {
        dao.allCheesesByName()
    }.flow
        .map { pagingData ->
            pagingData
                // Map cheeses to common UI model.
                .map { cheese -> CheeseListItem.Item(cheese) }
                .insertSeparators { before: CheeseListItem?, after: CheeseListItem? ->
                    if (before == null && after == null) {
                        // List is empty after fully loaded; return null to skip adding separator.
                        null
                    } else if (after == null) {
                        // Footer; return null here to skip adding a footer.
                        null
                    } else if (before == null) {
                        // Header
                        CheeseListItem.Separator(after.name.first())
                    } else if (!before.name.first().equals(after.name.first(), ignoreCase = true)){
                        // Between two items that start with different letters.
                        CheeseListItem.Separator(after.name.first())
                    } else {
                        // Between two items that start with the same letter.
                        null
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun insert(text: CharSequence) = ioThread {
        dao.insert(Cheese(id = 0, name = text.toString()))
    }

    fun remove(cheese: Cheese) = ioThread {
        dao.delete(cheese)
    }
}
