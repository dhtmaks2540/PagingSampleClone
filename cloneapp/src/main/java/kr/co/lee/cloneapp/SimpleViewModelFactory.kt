package kr.co.lee.cloneapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// ViewModelProvider.Factory 인터페이스 구현
class SimpleViewModelFactory(
    private val app: Application
): ViewModelProvider.Factory {
    // ViewModelProvider.Factory에 선언된 유일한 메소드
    // ViewModel을 생성하는 역할을 수행
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SimpleViewModel::class.java)) {
            // Database 객체에서 dao 객체를 획득
            val simpleDao = SimpleDatabase.get(app).simpleDao()
            @Suppress("UNCHECKED_CAST")
            // ViewModel 반환
            return SimpleViewModel(simpleDao) as T
        }
        // 예외 처리
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}