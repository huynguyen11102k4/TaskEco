package com.example.task.task3.highFunscAndLambdas

// Class Implement Function Type
class Adder: (Int, Int) -> Int {
    override fun invoke(p1: Int, p2: Int): Int {
        return p1 + p2
    }
}