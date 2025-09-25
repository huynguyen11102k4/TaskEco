package com.example.task.task3.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

fun main() {
    demoCoroutineContext()
    demoJob()
    demoCoroutineDispatchers()
    demoCoroutineExceptionHandler()
}
// CoroutineContext và Elements
fun demoCoroutineContext() = runBlocking {
    // Element
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
    //demo ở MainActivity
}

// CoroutineExceptionHandler
fun demoCoroutineExceptionHandler() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("ExceptionHandler", "Caught exception: ${exception.message}")
    }

    // Coroutine với ExceptionHandler chỉ hiệu lực cho root coroutine hoặc gắn supervisorScope, parent coroutine
    supervisorScope {
        launch(handler) {
            launch {
                Log.d("ExceptionHandler","Job with handler - 1")
                throw Exception("Exception in 1")
            }
            launch {
                delay(500)
                Log.d("ExceptionHandler","Job with handler - 2")
            }
        }
    }

    // Coroutine không có ExceptionHandler
    val jobWithoutHandler = launch {
        Log.d("ExceptionHandler", "Job without handler")
        throw Exception("Exception in job without handler")
    }
    try {
        jobWithoutHandler.join()
    } catch (e: Exception) {
        Log.d("ExceptionHandler", "Caught exception from job without handler: ${e.message}")
    }
}
