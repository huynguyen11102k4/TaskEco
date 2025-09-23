package com.example.task.task2.student

//Object
object StudentRepo {
    private val students = mutableListOf<Student>()
    fun addStudent(student: Student) {
        students.add(student)
    }

    fun getAllStudents(): List<Student> {
        return students
    }
}