package com.example.task.task2.person

fun interface PersonRepo {
    fun getPerson(id: Int): PersonInfo
}