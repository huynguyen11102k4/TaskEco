package com.example.task.task2.person

import android.util.Log
import com.example.task.task2.person.Person

open class Employee(name: String, age: Int, var position: String) : Person(name, age) {
    override var id: Int = 1

    //Ghi đè một phần
    override fun introduce() {
        super.introduce()
        Log.d("Employee", "I work as a $position.")
    }

    //Ghi đè hoàn toàn
    override fun birthday() {
        Log.d("Employee", "Congratulations on your new role as $position!")
    }

    inner class department(var deptName: String) {
        fun showDepartment() {
            //Gọi hàm introduce() của lớp cha Person
            Log.d("Employee", "${super@Employee.introduce()} works in the $deptName department.")
        }
    }
}