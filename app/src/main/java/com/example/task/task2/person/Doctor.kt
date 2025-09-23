package com.example.task.task2.person

import android.util.Log
import com.example.task.task2.person.Person

class Doctor : Person {
    var specialization: String

    //Nếu không có primary constructor thì mọi constructor phụ phải gọi super()
    constructor(name: String, age: Int, specialization: String) : super(name, age) {
        this.specialization = specialization
    }

    override fun introduce() {
        super.introduce()
        Log.d("Doctor", "I am a doctor specializing in $specialization.")
    }
}