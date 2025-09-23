package com.example.task.task2

import android.util.Log
import com.example.task.task2.animal.Cat
import com.example.task.task2.person.Developer
import com.example.task.task2.person.Doctor
import com.example.task.task2.person.Employee
import com.example.task.task2.person.Person
import com.example.task.task2.person.PersonInfo
import com.example.task.task2.person.PersonRepo
import com.example.task.task2.student.Room
import com.example.task.task2.student.School
import com.example.task.task2.student.Student
import com.example.task.task2.student.StudentRepo
import com.example.task.task2.student.Teacher
import com.example.task.task2.student.University
import com.example.task.task2.vehicle.Bike
import com.example.task.task2.vehicle.Car

fun main() {
    val hashSet = hashSetOf(1, 2, 3, 4, 5)
    val linkedHashSet: LinkedHashSet<Int> = linkedSetOf(1, 2, 3, 4, 5)
    val set: Set<Int> = setOf(1, 2, 3, 4, 5)
    /* Class & Object */
    // Class
    val student1 = Student("Hy", 20)
    student1.displayInfo()
    // Object Declaration - Singleton
    StudentRepo.addStudent(student1)
    Log.d("StudentRepo: ", "${StudentRepo.getAllStudents()}")
    // Object Expression - Anonymous class
    val per = object : Person("Huy", 21) { // Kế thừa Person
        fun displayInfo() {
            Log.d("Person", "Name: $name, Age: $age")
        }
    }
    val hello = object { // Không kế thừa
        val name = "Huy"
        override fun toString(): String {
            return "Hello $name"
        }
    }

    /* Constructor */
    val car = Car("Toyota", "Camry")
    car.displayInfo()
    val bike = Bike("Wave", "Alpha")
    val square = Rectangle(5.0)
    Log.d("Area of square: ", "${square.area()}")

    /* Inheritance */
    val employee = Employee("Huy", 20, "Intern Android Dev")
    employee.introduce()
    employee.birthday()
    val doctor = Doctor("An", 30, "Cardiologist")
    doctor.introduce()
    // Kế thừa đơn, đa interface không bị giới hạn
    val developer = Developer("Huy", 21, "Intern")
    developer.introduce()

    /* Interface & Abstract class */
    val teacher = Teacher("Toi", 40, "Math")
    Log.d("Teacher", "Subject: ${teacher.subject}")
    val cat = Cat()
    cat.sound()
    cat.sleep()
    //Functional interface - SAM interface (Interface chỉ có một hàm trừu tượng có thể viết gọn hơn bằng lambda)
    val repo = PersonRepo { id ->
        object : PersonInfo {
            override val id: Int = id
            override fun getInfo(): String {
                return "Person ID: $id"
            }
        }
    }
    val person = repo.getPerson(21)
    Log.d("PersonRepo", person.getInfo())

    /* Class Types */
    //Data class
    val room1 = Room(101, 2)
    val room2 = Room(101, 2)
    Log.d("Room1: ", room1.toString())
    Log.d("Equal: ", "Equal? ${room1 == room2}")
    Log.d("HashCode: ", "Room1: ${room1.hashCode()}, Room2: ${room2.hashCode()}")
    //Enum class
    println("Day: ${Day.MONDAY}")
    //Sealed class
    val ketQua1: Outcome = Outcome.Success("Thành công!")
    val ketQua2: Outcome = Outcome.Failure("Thất bại!")
    val ketQua3: Outcome = Outcome.Unknown
    ketQua1.print()
    ketQua2.print()
    ketQua3.print()
    //Nested & Inner class
    val nested = Outer.Nested()
    nested.nestedFunction()
    val inner = Outer().Inner()
    inner.innerFunction()
    val school = School()
    school.helloStudent("Huy")

    /* Singleton & Companion Object */
    Log.d("Department: ", Department.getDepartmentInfo())
    val university = University.create("HUST")
    Log.d("University: ", university.name)
}