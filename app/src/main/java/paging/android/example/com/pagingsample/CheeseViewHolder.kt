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

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple ViewHolder that can bind a Cheese or Separator item. It also accepts null items since
 * the data may not have been fetched before it is bound.
 */
// RecyclerView.ViewHolder 클래스
// 생성자 안에서 LayoutInflater를 사용하여 레이아웃 초기화
class CheeseViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)
) {
    // cheese 객체 생성
    // set은 private으로 설정
    var cheese: Cheese? = null
        private set
    // 텍스트뷰 가져오기
    private val nameView = itemView.findViewById<TextView>(R.id.name)

    /**
     * 그들이 아직 페이지되지 않았다면 아이템들은 널일 것이다.
     * PageListAdapter는 아이템이 로드되었을 때 ViewHolder를 리바인드 할 것이다.
     */

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    // Adapter에서 아이템 항목을 초기화 하는 메서드
    fun bindTo(item: CheeseListItem?) {
        // Sealed 클래스 아이템 분기시키기
        if (item is CheeseListItem.Separator) { // Separator라면
            nameView.text = "${item.name} Cheeses"
            nameView.setTypeface(null, Typeface.BOLD)
        } else {
            nameView.text = item?.name
            nameView.setTypeface(null, Typeface.NORMAL)
        }
        // item을 CheeseListItem.Item으로 형변환 후 cheese 프로퍼티 가져오기
        // holder 클래스에서 cheese set하기
        cheese = (item as? CheeseListItem.Item)?.cheese
        println("cheese: $cheese")
        nameView.text = item?.name
    }
}

