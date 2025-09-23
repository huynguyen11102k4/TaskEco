package com.example.task.task2.student

//Companion Object (giống static trong Java)
class University(val name: String) {
    companion object {
        fun create(name: String): University {
            return University(name)
        }
    }
}