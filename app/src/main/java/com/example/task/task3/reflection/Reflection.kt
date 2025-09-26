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


    // Function references
    val funcRef = ::sum
    println("Function Reference: ${funcRef(3, 5)}")

    val list = listOf(1, 2, 3, 4, 5)
    val evenNumbers = list.filter(::isEven)
    println("Even Numbers: $evenNumbers")

    // Property references
    val studentName = Student::name
    val student = Student("Huy", 22)
    println("Student Name via Property Reference: ${studentName.get(student)}")

    val listName = listOf("Huy", "Tuan", "Dat")
    println("List of Names: ${listName.map(String::length)}")

    // Overloaded function references
    val printIntVal: (Int) -> Unit = ::printVal
    printVal(10)
    val printStringVal: (String) -> Unit = ::printVal
    printVal("Hello")

    // Extension function reference
    val isLongerThan: String.(Int) -> Boolean = String::isLongerThan
    println("'Kotlin' is longer than 3: ${"Kotlin".isLongerThan(3)}")

    // Contructor references
    val createStudent: (String, Int) -> Student = ::Student
    val newStudent = createStudent("Huy", 23)
    newStudent.displayInfo()

    // Bound property reference
    val str = "Huy"::length
    println("Length of 'Huy': ${str()}")

    // Bound function reference
    val greet: () -> Unit = student::displayInfo
    greet()
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun isEven(n: Int): Boolean {
    return n % 2 == 0
}

fun printVal(value: Int) {
    println("Integer value: $value")
}

fun printVal(value: String) {
    println("String value: $value")
}

//Extension function reference
fun String.isLongerThan(length: Int): Boolean {
    return this.length > length
}



