package com.example.task.task2.person

/* Inheritance */
/* Không gọi open trong khai báo class và hàm thì mặc định là final (không thể kế thừa và ghi đè) bởi vì có thể gây ra lỗi runtime hoặc giá trị không mong muốn.
   Nên chỉ mở những class và hàm nào thực sự cần thiết để kế thừa và ghi đè */
open class Person(var name: String, var age: Int) {
    open val id: Int = 0
    open fun introduce() {
        println("Hi, I'm $name and I'm $age years old.")
    }

    open fun birthday() {
        age++
        println("Happy Birthday $name! You are now $age.")
    }
}