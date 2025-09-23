package com.example.task.task2

import android.util.Log

// Sealed Class (không thể có lớp con bên ngoài file khai báo)
sealed class Outcome {
    data class Success(val value: String) : Outcome()
    data class Failure(val message: String) : Outcome()
    object Unknown : Outcome()
}
fun Outcome.print() {
    when (this) {
        is Outcome.Success -> Log.d("Outcome", value)
        is Outcome.Failure -> Log.d("Outcome", message)
        Outcome.Unknown -> Log.d("Outcome", "Kết quả không xác định")
    }
}