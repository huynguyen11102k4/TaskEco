package com.example.task.task3.generics

// out - covariant chỉ được trả về, không được truyền vào (subtype nếu A <: B thì ListCovariant<A> <: ListCovariant<B>)
class ListCovariant<out T>(private val items: List<T>) {
    fun get(index: Int): T = items[index]
    fun size(): Int = items.size
//    fun set(index: Int, item:  T) {
//        // Không thể thực hiện việc này vì T là out
//        // items[index] = item
//        throw Exception("Cannot add item to covariant list")
//    }
}