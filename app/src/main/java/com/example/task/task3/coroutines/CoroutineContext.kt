package com.example.task.task3.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

fun main() = runBlocking {
    demoCoroutineContext()
    demoJob()
    demoCoroutineDispatchers()
}
// CoroutineContext và Elements
fun demoCoroutineContext() = runBlocking {
    // Elements
    val element1: CoroutineContext = Dispatchers.IO
    val element2: CoroutineContext = CoroutineName("MyCoroutine")
    // CoroutineContext là tập hợp các Elements
    val combinedContext: CoroutineContext = element1 + element2
    // Tạo coroutine với context kết hợp
    val job = launch(combinedContext) {
        val dispatcher = coroutineContext[ContinuationInterceptor]
        Log.d("CoroutineContext", "Running in context: $coroutineContext")
        Log.d("CoroutineContext", "Dispatcher: $dispatcher")
        Log.d("CoroutineContext", "Coroutine Name: ${coroutineContext[CoroutineName]}")
        Log.d("CoroutineContext", "Job: ${coroutineContext[Job]}")
    }
    job.join()
}

// Job và các trạng thái của Job
fun demoJob() = runBlocking {
    // Nhánh complete
    val parentJob = launch {
        Log.d("Job", "Parent Job started")
        val childJob = launch {
            Log.d("Job", "Child Job 1 started")
            delay(1000)
            Log.d("Job", "Child Job 1 completed")
        }
        launch {
            Log.d("Job", "Child Job 2 started")
            delay(500)
            Log.d("Job", "Child Job 2 completed")
        }
        Log.d("Job", "Parent Job completed")
    }

    Log.d("Job", "Waiting for parent job to complete...")

    Log.d("Job", "Job active: ${parentJob.isActive}")
    delay(2000)
    Log.d("Job", "Job active after delay: ${parentJob.isActive}")

    Log.d("Job", "Canceling parent job...")
    parentJob.cancel()
    Log.d("Job", "Job canceled: ${parentJob.isCancelled}")
    Log.d("Job", "Job completed: ${parentJob.isCompleted}")
    parentJob.join()
    Log.d("Job", "All jobs completed")


    // Nhánh canceled
    val jobToCancel = launch {
        Log.d("Job", "Job to be canceled started")
        try {
            delay(500)
        } catch (e: Exception) {
            Log.d("Job", "Job was canceled: ${e.message}")
        } finally {
            Log.d("Job", "Job finally block")
        }
    }
    delay(1000)
    Log.d("Job", "Canceling the job")
    jobToCancel.cancel()
    jobToCancel.join()
    Log.d("Job", "Job canceled: ${jobToCancel.isCancelled}")
    Log.d("Job", "Job completed: ${jobToCancel.isCompleted}")

    // Nhánh failed
    val jobToFail = launch {
        Log.d("Job", "Job to fail started")
        try {
            delay(500)
            throw Exception("Simulated failure")
        } catch (e: Exception) {
            Log.d("Job", "Job failed with exception: ${e.message}")
            throw e
        } finally {
            Log.d("Job", "Job finally block")
        }
    }
    try {
        jobToFail.join()
    } catch (e: Exception) {
        Log.d("Job", "Caught exception: ${e.message}")
    }
    Log.d("Job", "Job failed: ${jobToFail.isCancelled}")
    Log.d("Job", "Job completed: ${jobToFail.isCompleted}")
}


// Coroutine Dispatchers
fun demoCoroutineDispatchers() = runBlocking {
    // Dispatchers.Default: Sử dụng cho các tác vụ CPU như tính toán nặng
    launch(Dispatchers.Default) {
        Log.d("Dispatchers", "Running on Default Dispatcher")
    }.join()

    // Dispatchers.IO: Sử dụng cho các tác vụ I/O như đọc/ghi file, mạng
    launch(Dispatchers.IO) {
        Log.d("Dispatchers", "Running on IO Dispatcher")
    }.join()

    // Dispatchers.Main: Sử dụng cho các tác vụ liên quan đến UI (chỉ có trên Android)
    launch(Dispatchers.Main) {
        Log.d("Dispatchers", "Running on Main Dispatcher")
    }.join()

    // Dispatchers.Unconfined: Bắt đầu trong luồng gọi, sau đó chuyển sang luồng khác khi gặp điểm dừng
    launch(Dispatchers.Unconfined) {
        Log.d("Dispatchers", "Running on Unconfined Dispatcher - Thread name: ${Thread.currentThread().name}")
        delay(1000)
        Log.d("Dispatchers", "Running on Unconfined Dispatcher - Thread name after delay: ${Thread.currentThread().name}")
    }.join()
}