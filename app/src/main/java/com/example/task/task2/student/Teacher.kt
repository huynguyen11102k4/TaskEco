package com.example.task.task2.student

import com.example.task.task2.person.PersonInfo

class Teacher(var name: String, var age: Int, var subject: String) : PersonInfo {
    override val id: Int = 2
    override fun getInfo(): String {
        return "Teacher ID: $id, Name: $name, Age: $age, Subject: $subject"
    }
}