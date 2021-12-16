package kr.co.lee.cloneapp

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import kr.co.lee.cloneapp.databinding.ItemCardBinding

class SimpleHolder(binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
    // SimpleItem 객체 생성
    var simpleItem: SimpleItem? = null
        private set

    // 뷰 객체 
    private val nameView = binding.nameView

    // Adapter에서 아이템 항목을 초기화하는 메소드
    fun bindTo(item: SimpleListItem?) {
        // Sealed 클래스 분기
        if(item is SimpleListItem.Separator) {
            nameView.text = "${item.name} Items"
            nameView.setTypeface(null, Typeface.BOLD)
        } else {
            nameView.text = item?.name
            nameView.setTypeface(null, Typeface.NORMAL)
        }
        // 형변환
        simpleItem = (item as? SimpleListItem.Item)?.simpleItem
        nameView.text = item?.name
    }
}