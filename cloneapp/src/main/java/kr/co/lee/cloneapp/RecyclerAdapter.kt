package kr.co.lee.cloneapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lee.cloneapp.databinding.ItemCardBinding

class RecyclerAdapter(val simpleList: List<SimpleItem>): RecyclerView.Adapter<SimpleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        val item = simpleList[position]
        holder.binding.textView.text = item.name
    }

    override fun getItemCount(): Int = simpleList.size
}