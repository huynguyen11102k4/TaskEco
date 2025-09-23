package com.example.task.task2

// Nested & Inner Class
class Outer {
    private val outerField = "Outer"

    class Nested { // Là class tĩnh không thể truy cập thành phần của lớp ngoài
        fun nestedFunction() {
            println("Nested")
        }
    }
    inner class Inner { // Là class động có thể truy cập thành phần của lớp ngoài
        fun innerFunction() {
            println("Inner")
            println(outerField)
        }
    }
}