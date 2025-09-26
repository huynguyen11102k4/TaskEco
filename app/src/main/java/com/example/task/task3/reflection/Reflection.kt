package com.example.task.task3.reflection

import com.example.task.task2.student.Student
import kotlin.reflect.KClass

fun main() {
    val stu = Student("Huy", 21)
    println("Class Info: ${stu::class}")
    println("Class Name: ${stu::class.simpleName}")
    println("Properties: ${stu::class.members}")

    // KClass là đại diện cho một lớp trong Kotlin, tương tự như Class trong Java
    val x = 10
    val k: KClass<out Int> = x::class
    println("KClass Info: ${k.simpleName}")

    val funcRef = ::sum
    println("Function Reference: ${funcRef(3, 5)}")

    val list = listOf(1, 2, 3, 4, 5)
    val evenNumbers = list.filter(::isEven)
    println("Even Numbers: $evenNumbers")
}

// Function references
fun sum(a: Int, b: Int): Int {
    return a + b
}

fun isEven(n: Int): Boolean {
    return n % 2 == 0
}


