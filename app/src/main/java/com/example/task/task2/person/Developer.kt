package com.example.task.task2.person

import com.example.task.task2.person.Employee
import com.example.task.task2.person.PersonInfo

class Developer(name: String, age: Int, position: String) : PersonInfo, Employee(name, age, position) {
    override var id: Int = 3

    override fun getInfo(): String {
        return "Developer ID: $id, Name: $name, Age: $age, Position: $position"
    }

    override fun introduce() {
        println("Hello, I'm $name, a $position developer.")
    }

    override fun birthday() {
        println("Happy Birthday, $name! Enjoy your day as a $position developer!")
    }
}