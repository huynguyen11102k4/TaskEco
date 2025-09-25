package com.example.task.task3.generics

// invariant - không có từ khóa, có thể truyền vào và trả về
class MutableListInvariant<T>(private val items: MutableList<T>) {
    operator fun get(index: Int): T = items[index]
    operator fun set(index: Int, item: T) {
        items[index] = item
    }
    fun size(): Int = items.size
    fun add(item: T) {
        items.add(item)
    }
    fun remove(item: T) {
        items.remove(item)
    }
}
