package kr.co.lee.cloneapp

import java.util.concurrent.Executors

// 쓰레드 1개인 ExecutorService를 리턴합니다.
// 싱글 쓰레드에서 동작해야 하는 작업을 처리할 때 사용합니다.
private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

// background thread의 블록 코드를 작동시키는 Utility 메소드로 io/database를 위해 사용됨
// 함수를 인자로 받는 고차 함수
// 백그라운드에서 작동하는 하나의 스레드이기에 코루틴을 사용해도 무방하다고 생각
fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}
