package com.example.task.task2.student

//Class
class Student(val name: String, var age: Int) {

    /* Backing field
    Thuộc tính với getter và setter, field đại diện cho giá trị hiện tại của thuộc tính
    Nếu không có field thì sẽ chỉ tinh toán giá trị mỗi khi gọi getter
    */
    var id: Int = 1
        get() = field
        set(value) {
            field = value
        }

    /* Backing property
    Thuộc tính với getter và setter, _id đại diện cho giá trị hiện tại của thuộc tính
     */
    private var _id: Int = 1
    var studentId: Int
        get() = _id
        set(value) {
            _id = value
        }
    fun displayInfo() {
        println("Name: $name, Age: $age")
    }
}