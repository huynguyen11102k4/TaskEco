package com.example.task.task2.vehicle

import android.util.Log

/* Constructor */
//Primary Constructor with Default Parameter
class Car(val make: String, val model: String) {
    fun displayInfo() {
        Log.d("Car", "Car: $make, Model: $model")
    }
}