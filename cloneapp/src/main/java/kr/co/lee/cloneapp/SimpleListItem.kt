package kr.co.lee.cloneapp

// Sealed 클래스
// Simple Item data 클래스와 구분자 사이에 UI model 입니다.
sealed class SimpleListItem(val name: String) {
    data class Item(val simpleItem: SimpleItem): SimpleListItem(simpleItem.name)
    data class Separator(private val letter: Char): SimpleListItem(letter.uppercase())
}