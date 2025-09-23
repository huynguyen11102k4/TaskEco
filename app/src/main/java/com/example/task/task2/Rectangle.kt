package com.example.task.task2

//Secondary Constructor
class Rectangle(val width: Double, val height: Double) {
    constructor(side: Double) : this(side, side)

    fun area(): Double {
        return width * height
    }
}