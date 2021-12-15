package kr.co.lee.cloneapp

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SimpleDao {
    // 리스트 아이템 삽입
    @Insert
    fun insert(items: List<SimpleItem>)

    // 아이템 하나 삽입
    @Insert
    fun insert(item: SimpleItem)

    // 아이템 삭제
    @Delete
    fun delete(item: SimpleItem)

    // Select
    @Query("SELECT * FROM simpleItem ORDER BY name COLLATE NOCASE ASC")
    fun allItemsByName(): PagingSource<Int, SimpleItem>
}