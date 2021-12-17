package kr.co.lee.cloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.lee.cloneapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
        private set
    // ViewModel 생성
    private val viewModel by viewModels<SimpleViewModel> { SimpleViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        // RecyclerView를 위한 어댑터
        val adapter = RecyclerAdapter()
        binding.recycler.adapter = adapter

        // Subscribe the adapter to the ViewModel, so the items in the adapter are refreshed
        // when the list changes
        lifecycleScope.launch {
            viewModel.allItems.collectLatest { adapter.submitData(it) }
        }

        // DataBinding Variable
        binding.main = this

        initAddButtonListener()
        initSwipeToDelete()
    }

    // 새로운 아이템 추가 메소드
    fun addItem() {
        // 공백 제거
        val newItem = binding.editView.text.trim()
        if (newItem.isNotEmpty()) {
            viewModel.insert(newItem)
            binding.editView.setText("")
        }
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            // item이 왼쪽이나 오른쪽으로 스와이프 되도록 설정
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val simpleViewHolder = viewHolder as SimpleViewHolder
                // 아이템이 널이 아니라면
                return if(simpleViewHolder.simpleItem != null) {
                    // 왼쪽이나 오른쪽으로 움직이도록
                    makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                } else {
                    makeMovementFlags(0, 0)
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                (viewHolder as SimpleViewHolder).simpleItem?.let {
//                    viewModel.remove(it)
//                }
            }

        }).attachToRecyclerView(binding.recycler)
    }

    private fun initAddButtonListener() {
        // 사용자가 스크린 키보드의 "Done"버튼을 클릭한 경우 아이템 저장
        binding.editView.setOnEditorActionListener { _, actionId, _ ->
            // EditorInfo 클래스는 입력 메서드가 통신 중인 텍스트 편집 객체(일반적으로 EditText)의 몇 가지 특성을
            // 설명하며, 가장 중요한 것은 포함하는 텍스트 콘텐츠 유형과 현재 커서 위치를 설명합니다.
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                addItem()
                return@setOnEditorActionListener true
            }
            false // DONE을 하지 않은 액션은 무시
        }
        
        // 사용자가 버튼을 클릭하거나, 엔터를 누를 경우 아이템 저장
        binding.editView.setOnKeyListener { _, keyCode, event -> 
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addItem()
                return@setOnKeyListener true
            }
            false // DOWN이나 ENTER를 클릭하지 않은 이벤트는 무시
        }
    }
}