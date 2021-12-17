package kr.co.lee.cloneapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kr.co.lee.cloneapp.databinding.ItemCardBinding

class RecyclerAdapter: PagingDataAdapter<SimpleListItem, SimpleViewHolder>(diffCallback) {
    // 아이템 항목 
    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
        viewHolder.bindTo(getItem(position))
    }

    // Layout 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleViewHolder(view)
    }

    companion object {
        // DiffCallback 선언
        // diff callback은 새로운 리스트가 도착했을 때 리스트 차이점들을 어떻게 계산하는지 PagedListAdapter에게 알립니다.

        // Add Button을 눌러 새로운 아이템을 추가했을 때, PagedListAdapter는 diffCallback을 사용하여
        // 이전과 단일 항목 차이만 감지하므로 단일 뷰에 애니메이션과 리바인드만 하면 됩니다.

        var diffCallback = object : DiffUtil.ItemCallback<SimpleListItem>() {
            // 두 객체가 같은 아이템을 가리키고 있는지 검사하는 메소드
            override fun areItemsTheSame(
                oldItem: SimpleListItem,
                newItem: SimpleListItem
            ): Boolean {
                return if (oldItem is SimpleListItem.Item && newItem is SimpleListItem.Item) {
                    // id를 비교
                    oldItem.simpleItem.id == newItem.simpleItem.id
                } else if (oldItem is SimpleListItem.Separator && newItem is SimpleListItem.Separator) {
                    // name을 비교
                    oldItem.name == newItem.name
                } else {
                    oldItem == newItem
                }
            }

            // 두 개의 아이템이 같은 데이터를 가지고 있는지 검사하는 메소드
            override fun areContentsTheSame(
                oldItem: SimpleListItem,
                newItem: SimpleListItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}