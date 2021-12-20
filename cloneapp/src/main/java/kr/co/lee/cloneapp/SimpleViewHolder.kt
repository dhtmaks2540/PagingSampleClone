package kr.co.lee.cloneapp

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import kr.co.lee.cloneapp.databinding.ItemCardBinding

// ViewHolder 클래스
// RecyclerView.Adapter의 메모리가 올라가 있으면 객체를 다시 초기화하지 않기 위해 사용하는 클래스(메모리 절약)
// 아이템의 갯수만큼 ViewHolder는 생성이 되고 새롭게 그려질 아이템이 있으면 이를 재사용해서 데이터만 바꿈
class SimpleViewHolder(binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
    // SimpleItem 변수
    var simpleItem: SimpleItem? = null
        private set

    // 뷰 객체 
    private val nameView = binding.nameView

    // 아이템 항목을 초기화하는 메소드
    fun bindTo(item: SimpleListItem?) {
        // Sealed 클래스 분기
        if(item is SimpleListItem.Separator) {
//            nameView.text = "${item.name} Items"
            nameView.setTypeface(null, Typeface.BOLD)
        } else {
//            nameView.text = item?.name
            nameView.setTypeface(null, Typeface.NORMAL)
        }
        // SimpleItem 형변환
        simpleItem = (item as? SimpleListItem.Item)?.simpleItem
        // 텍스트뷰에 텍스트 셋팅
        nameView.text = item?.name
    }
}