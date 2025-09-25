package com.example.task.task3.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

suspend fun greet(){
    Log.d("CoroutineTest","${Thread.currentThread().name}")
    delay(1000)
    for(i in 1..1000){
        Log.d("CoroutineTest","$i")
    }
}

suspend fun greet1(){
    Log.d("CoroutineTest", " ${Thread.currentThread().name}")
    delay(1000)
    for(i in 2000..3000){
        Log.d("CoroutineTest","$i")
    }
}

suspend fun testMultiThreading(){
    withContext(Dispatchers.IO) {
        launch {
            greet()
        }
        launch {
            greet1()
        }
        Log.d("CoroutineTest", "${Thread.currentThread().name}")
    }

    Log.d("CoroutineTest", "-----------------")

    withContext(Dispatchers.Main) {
        launch {
            greet()
        }
        launch {
            greet1()
        }
        Log.d("CoroutineTest", "${Thread.currentThread().name}")
    }
}
fun main() = runBlocking{
    testMultiThreading()
}
