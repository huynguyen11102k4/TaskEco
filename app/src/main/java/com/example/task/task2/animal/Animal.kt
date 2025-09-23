package com.example.task.task2.animal

import android.util.Log

//Abstract class
abstract class Animal {
    abstract fun sound()
    fun sleep() {
        Log.d("Animal", "Zzz...")
    }
}