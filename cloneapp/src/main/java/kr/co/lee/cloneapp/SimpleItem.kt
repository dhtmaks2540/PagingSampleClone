package kr.co.lee.cloneapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity 어노테이션을 추가해 테이블
@Entity
// PrimaryKey에 autoGenerate를 설정하여 id 자동으로 증가하도록
data class SimpleItem(@PrimaryKey(autoGenerate = true) val id: Int, val name: String)