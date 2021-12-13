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

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

/**
 * A simple PagedListAdapter that binds Cheese items into CardViews.
 *
 * PagedListAdapter is a RecyclerView.Adapter base class which can present the content of PagedLists
 * in a RecyclerView. It requests new pages as the user scrolls, and handles new PagedLists by
 * computing list differences on a background thread, and dispatching minimal, efficient updates to
 * the RecyclerView to ensure minimal UI thread work.
 *
 * If you want to use your own Adapter base class, try using a PagedListAdapterHelper inside your
 * adapter instead.
 *
 * @see androidx.paging.PagedListAdapter
 * @see androidx.paging.AsyncPagedListDiffer
 */
// PagingAdapter를 사용
// RecyclerView.Adapter와의 차이점은 DiffUtil을 사용한다는 점
class CheeseAdapter : PagingDataAdapter<CheeseListItem, CheeseViewHolder>(diffCallback) {
    // 아이템 하나를 초기화하는 코드
    // ViewHolder에 bindTo 메서드로 아이템을 전달
    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    // Layout 초기화(ViewHolder에게 parent를 전달)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        return CheeseViewHolder(parent)
    }

    companion object {
        /**
         * diff callback은 PagedListAdapter에게 알립니다. 새로운 PagedList가 도착했을 때 리스트의 차이점을 어떻게 계산하는지
         * 
         * Add button을 클릭해 치즈를 추가했을 때, PageListAdapter는 diffCallback을 사용합니다.
         * 발견하기 위해서 이전으로부터 하나의 아이템 차이점이 있다는 것을
         * 그래서 이것은 애니메이션이 필요하고 하나의 뷰를 리바인드합니다.
         */
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         *
         * When you add a Cheese with the 'Add' button, the PagedListAdapter uses diffCallback to
         * detect there's only a single item difference from before, so it only needs to animate and
         * rebind a single view.
         *
         * @see DiffUtil
         */
        // object 키워드를 사용하여 객체 생성 및 초기화
        // DiffUtil의 ItemCallback이라는 추상 클래스 구현
        // 타입 파라미터로 CheeseListItem(Sealed Class) 전달
        val diffCallback = object : DiffUtil.ItemCallback<CheeseListItem>() {
            // 두 개의 아이템이 같은 아이템인지 검사할 때 호출됩니다
            // oldItem과 newItem이 같으면 true호출, 다르면 false 호출
            override fun areItemsTheSame(oldItem: CheeseListItem, newItem: CheeseListItem): Boolean {
                // oldItem과 newItem이 일단 CheeseListItem.Item 클래스인지
                return if (oldItem is CheeseListItem.Item && newItem is CheeseListItem.Item) {
                    // id를 비교
                    oldItem.cheese.id == newItem.cheese.id
                // oldItem과 newItem이 CheeseListItem.Separaor 클래스인지
                } else if (oldItem is CheeseListItem.Separator && newItem is CheeseListItem.Separator) {
                    // name을 비교
                    oldItem.name == newItem.name
                // 그 외의 경우에는 
                } else {
                    // 객체를 비교
                    oldItem == newItem
                }
            }

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            // 코틀린의 데이터 클래스에서는 ==은 객체의 프로퍼티를 비교하기에 ==을 사용
            // 두 개의 아이템이 같은 data를 가지고 있는지 검사하기 위해 호출
            // 이 정보는 아이템의 컨텐츠가 바뀐 것을 발견하기 위해 사용됨
            override fun areContentsTheSame(oldItem: CheeseListItem, newItem: CheeseListItem): Boolean {
                // 아이템의 컨텐츠가 같다면 true 반환, 아니면 false를 반환
                return oldItem == newItem
            }
        }
    }
}

