package com.example.task.task2.student

// Local Class
class School {
    fun helloStudent(name: String) {
        class Student(val name: String) {
            fun display() {
                println("Hello Student: $name")
            }
        }
        val student = Student(name)
        student.display()
    }
}