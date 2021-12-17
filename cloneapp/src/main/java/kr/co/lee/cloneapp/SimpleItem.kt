package kr.co.lee.cloneapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity 어노테이션을 추가해 테이블
@Entity
data class SimpleItem(@PrimaryKey(autoGenerate = true) val id: Int, val name: String)