package paging.android.example.com.pagingsample

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * A [ViewModelProvider.Factory] that provides dependencies to [CheeseViewModel],
 * allowing tests to switch out [CheeseDao] implementation via constructor injection.
 */
/**
 * CheeseViewModel에게 종속성을 제공하는 ViewModelProvider.Factory는 생성자 주입을 통해 CheeseDao 구현을 전환하는 테스트를 허용합니다.
 */

// ViewModelProvider.Factory 인터페이스 구현
// Factory 인터페이스의 구현은 ViewModel은 인스턴스화하는 역할을 수행합니다.
class CheeseViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {
    // 유일한 메서드로 주어진 클래스의 새로운 인스턴스를 생성하는 메서드
    // 타입 인자를 ViewModel로 지정해 ViewModel을 상속받은 클래스만 넣을 수 있도록
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // instanceOf는 특정 Object(객체)가 어떤 클래스 / 인터페이스를 상속 / 구현했는지를 체크(코틀린에서는 is)
        // Class.isAssignableFrom은 특정 Class(클래스)가 어떤 클래스 / 인터페이스스를 상속 / 구현했는지를 체크
        if (modelClass.isAssignableFrom(CheeseViewModel::class.java)) {
            // Database 객체에서 Application 객체를 사용하여 DAO 객체 획득
            val cheeseDao = CheeseDb.get(app).cheeseDao()
            // UNCHECKED_CAST 컴파일 경고를 무시 
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            // ViewModel 클래스의 생성자에 Dao 객체를 넣고 T타입으로 형변환
            return CheeseViewModel(cheeseDao) as T
        }
        // 정한 ViewModel 클래스가 아니면 오류 던짐
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}