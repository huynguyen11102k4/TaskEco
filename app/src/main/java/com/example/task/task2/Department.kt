package com.example.task.task2

//Singleton (chỉ có một instance duy nhất)
object Department {
    val name = "Computer Science"
    fun getDepartmentInfo(): String {
        return "Department: $name"
    }
}