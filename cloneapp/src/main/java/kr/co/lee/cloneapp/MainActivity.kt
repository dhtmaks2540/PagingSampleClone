package kr.co.lee.cloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
    // Android KTX에 선언된 확장 함수와 by를 사용해 viewModel의 객체 생성을 위임(지연 생성)
    // 매개변수로 함수 타입을 전달하는데 반환값이 ViewModelProvider.Factory
    private val viewModel: SimpleViewModel by viewModels { SimpleViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBinding 레이아웃 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        // RecyclerView를 위한 어댑터
        val adapter = RecyclerAdapter()
        binding.recycler.adapter = adapter


        // ViewModel을 어댑터가 subscribe하면 목록이 변경될 때 어댑터의 항목이 새로 고침
        // lifecycleScope 프로퍼티를 사용하여 CoroutineScope에 접근
        // launch 메소드를 통해 새로운 코루틴 실행
        lifecycleScope.launch {
            viewModel.allItems.collectLatest { adapter.submitData(it) }
        }

        // DataBinding 변수 셋팅
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

    // ItemTouchHelper.Callback과 RecyclerView를 ItemTouchHelper를 사용하여 연결
    private fun initSwipeToDelete() {
        // ItemTouchHelper의 생성자로 ItemTouchHelper.Callback() 추상 클래스를 구현하는 클래스 셋팅
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val simpleViewHolder = viewHolder as SimpleViewHolder
                // 아이템이 널이 아니라면
                return if(simpleViewHolder.simpleItem != null) {
                    // dragFlags는 0을 주어 드래그는 되지 않도록 설정
                    // 왼쪽이나 오른쪽으로 움직이도록
                    makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                } else {
                    makeMovementFlags(0, 0)
                }
            }

            // 항목이 움직이면 호출되는 콜백 메소드
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            // 항목이 스와이프되면 호출되는 메소드
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // SimpleViewHolder의 simpleItem이 널이 아니라면
                (viewHolder as SimpleViewHolder).simpleItem?.let {
                    // Dao의 remove 메소드
                    viewModel.remove(it)
                }
            }
        
        }).attachToRecyclerView(binding.recycler) // ItemTouchHelper와 RecyclerView를 연결
    }

    private fun initAddButtonListener() {
        // 사용자가 스크린 키보드의 "Done"버튼을 클릭한 경우 아이템 저장
        // Editor에 작업이 수행될 때 호출할 콜백에 대한 인터페이스 정의
        binding.editView.setOnEditorActionListener { _, actionId, _ -> // 메소드가 하나 존재하는 SAM이기에 람다로 변경
            // EditorInfo 클래스는 입력 메서드가 통신 중인 텍스트 편집 객체(일반적으로 EditText)의 몇 가지 특성을
            // 설명하며, 가장 중요한 것은 포함하는 텍스트 콘텐츠 유형과 현재 커서 위치를 설명합니다.
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                addItem()
                return@setOnEditorActionListener true
            }
            false // DONE을 하지 않은 액션은 무시
        }
        
        // 사용자가 버튼을 클릭하거나, 엔터를 누를 경우 아이템 저장
        // 하드웨어 키 이벤트가 이 뷰에 디스패치될 때 호출할 콜백 메소드에 대한 인터페이스 정의
        binding.editView.setOnKeyListener { _, keyCode, event -> // 메소드가 하나 존재하는 SAM이기에 람다로 변경
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addItem()
                return@setOnKeyListener true
            }
            false // DOWN이나 ENTER를 클릭하지 않은 이벤트는 무시
        }
    }
}