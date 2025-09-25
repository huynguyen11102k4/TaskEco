package com.example.task.task3.generics

// in - contravariant chỉ được truyền vào, không được trả về (subtype nếu A <: B thì ConsumerContravariant<B> <: ConsumerContravariant<A>)
class ConsumerContravariant<in T> {
    fun consume(item: T) {
        println("Consuming: $item")
    }
//    fun produce(): T {
//        // Không thể thực hiện việc này vì T là in
//        throw Exception("Cannot produce item from contravariant consumer")
//    }
}