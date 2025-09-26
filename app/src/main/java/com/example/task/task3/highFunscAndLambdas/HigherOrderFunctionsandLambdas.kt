package com.example.task.task3.highFunscAndLambdas

import android.util.Log
import kotlinx.coroutines.delay


fun demoHigherOrderFunctionsandLambdas() {
    //higher-order function example
    val numbers = listOf(1, 2, 3, 4, 5)
    val squaredNumbers = numbers.customMap { it * it }
    Log.d("HighOrderFunc", "Squared Numbers: $squaredNumbers")

    /* Function Types mô tả kiểu của các hàm, bao gồm kiểu của các tham số và kiểu trả về */

    /* 1. Lambda expression: Biểu thức lambda là một hàm vô danh có thể được sử dụng như một giá trị */
    // Receiver Type: Kiểu của đối tượng mà hàm được gọi trên đó (nếu có)
    val stringLength: String.() -> Int = { this.length }
    Log.d("Lambda expression - Receiver Type", "Length of 'Hello': ${"Hello".stringLength()}")
    Log.d("Lambda expression - Receiver Type", "Length of 'World': ${stringLength("World")}")
    // Parameter Type: Kiểu của các tham số mà hàm nhận vào
    val add = { a: Int, b: Int -> a + b }
    val sum = add(2, 5)
    Log.d("Lambda expression - Parameter Type", "Sum of 2 and 5: $sum")
    // Suspend Function Type: Kiểu của các hàm tạm dừng (suspend functions) trong lập trình bất đồng bộ
    val delayed: suspend (Long) -> String = { delay ->
        delay(delay)
        "Finished after $delay ms"
    }
    // Nullable Function Type: Kiểu của các hàm có thể trả về null
    val nullableFunc: ((Int, Int) -> Int)? = null
    val result = nullableFunc?.invoke(3, 4) ?: "Null"
    Log.d("Lambda expression - Nullable Function Type", result.toString())
    // Lồng nhau
    val multiply: (Int) -> (Int) -> Int = { a ->
        { b -> a * b }
    }
    val resultMultiply = multiply(3)(4)
    Log.d("Lambda expression - Nested Function Type", "$resultMultiply")

    /* 2. Anonymous Function: Hàm vô danh là một hàm không có tên, thường được sử dụng khi bạn cần một hàm tạm thời */
    // Receiver Type
    val stringLengthAnon = fun String.(): Int {
        return this.length
    }
    Log.d("Anonymous Function - Receiver Type", "Length of 'Hello': ${"Kotlin".stringLengthAnon()}")
    Log.d("Anonymous Function - Receiver Type", "Length of 'World': ${stringLengthAnon("Programming")}")
    // Parameter Type
    val addAnon = fun(a: Int, b: Int): Int {
        return a + b
    }
    val sumAnon = addAnon(10, 15)
    Log.d("Anonymous Function - Parameter Type", "Sum of 10 and 15: $sumAnon")
    // Nullable Function Type
    val nullableFuncAnon =  fun(a: Int, b: Int): Int? {
        return a * b
    }
    val resultAnon = nullableFuncAnon?.invoke(3, 4) ?: "Null"
    Log.d("Anonymous Function - Nullable Function Type", resultAnon.toString())
    // Lồng nhau
    val multiplyAnon = fun(a: Int): (Int) -> Int {
        return fun(b: Int): Int {
            return a * b
        }
    }
    val resultMultiplyAnon = multiplyAnon(5)(6)
    Log.d("Anonymous Function - Nested Function Type", "$resultMultiplyAnon")

    /* Callable Reference: Tham chiếu hàm là một cách để tham chiếu đến một hàm mà không cần gọi nó */
    // Tham chiếu hàm top-level
    val topLevelRef: (Int, Int) -> Int = ::topLevelFunction
    Log.d("Callable Reference - Top-level Function", "${topLevelRef(7, 8)}")
    // Tham chiếu hàm thành viên
    val multiplier = Multiplier()
    val memberRef: (Int, Int) -> Int = multiplier::multi
    Log.d("Callable Reference - Member Function", "${memberRef(3, 4)}")
    // Tham chiếu tới property
    val stringLengthRef: (String) -> Int = String::length
    Log.d("Callable Reference - Property Reference", "Length of 'Kotlin': ${stringLengthRef("Kotlin")}")
    // class implement function type
    val adder = Adder()
    Log.d("Callable Reference - Class Implement Function Type", "${adder(10, 20)}")

    // Invoking a function type instance
    Log.d("Invoking Function Type Instance", "${adder.invoke(20, 30)}")
    Log.d("Invoking Function Type Instance", "${adder(20, 30)}")

    /* Inline Function: Hàm nội tuyến là một hàm mà trình biên dịch sẽ chèn mã của nó trực tiếp vào vị trí gọi hàm, thay vì tạo một cuộc gọi hàm thông thường */
    val resultSubtract = subtract(10, 5)
    Log.d("Inline Function", "$resultSubtract")
}

// Higher-order function là một hàm nhận một hoặc nhiều hàm khác làm đối số hoặc trả về một hàm
fun <T, R> List<T>.customMap(t: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(t(item))
    }
    return result
}

// Tham chiếu hàm top-level
fun topLevelFunction(a: Int, b: Int): Int {
    return a + b
}

// Inline Function
inline fun subtract(a: Int, b: Int): Int {
    return a - b
}