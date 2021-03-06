package kr.co.lee.cloneapp

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Database Access Object
@Dao
interface SimpleDao {
    // 리스트 아이템 삽입
    @Insert
    fun insert(items: List<SimpleItem>)

    // 아이템 삽입
    @Insert
    fun insert(item: SimpleItem)

    // 아이템 삭제
    @Delete
    fun delete(item: SimpleItem)

    // Select문
    @Query("SELECT * FROM SimpleItem ORDER BY name COLLATE NOCASE ASC")
    // 반환값은 PagingSource 클래스
    fun allItemsByName(): PagingSource<Int, SimpleItem>
}