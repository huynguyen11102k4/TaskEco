package com.example.task.task2.animal

import android.util.Log

class Cat : Animal() {
    override fun sound() {
        Log.d("Cat", "Meow")
    }
}