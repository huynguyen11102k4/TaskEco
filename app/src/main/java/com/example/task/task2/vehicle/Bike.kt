package com.example.task.task2.vehicle

import android.util.Log

//Primary Constructor with Initializer Block
class Bike(val brand: String, val type: String) {
    init {
        Log.d("Bike", "Bike: $brand, Type: $type")
    }
}