package com.example.task.task3.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main(): Unit = runBlocking{
    demoCoroutineBuilders()
    demoCoroutineStarted()
    demoCoroutineScope()
}

// Coroutine Builders
fun demoCoroutineBuilders() = runBlocking {
    // launch: Tạo coroutine không trả về giá trị
    val job = launch {
        delay(1000)
        Log.d("Coroutine Builders", "launch completed")
    }
    job.join()

    // async: Tạo coroutine trả về Deferred, có thể lấy kết quả bằng await()
    val deferred = async {
        delay(1000)
        Log.d("Coroutine Builders", "async completed")
        10
    }
    val res = deferred.await()
    Log.d("Coroutine Builders", "Result from async: $res")

    // runBlocking: Tạo coroutine chặn luồng hiện tại cho đến khi hoàn thành
    runBlocking {
        delay(1000)
        Log.d("Coroutine Builders", "runBlocking completed")
    }
}

//CoroutineStarted
fun demoCoroutineStarted() = runBlocking {
    // Default start coroutine (chỉ lập lịch cho job chưa chạy ngay)
    val jobDefault = launch {
        Log.d("Default Start", "Coroutine started")
    }
    Log.d("Default Start", "Before joining coroutine")
    jobDefault.join()
    Log.d("Default Start", "After coroutine completion")

    // Lazy start coroutine (chỉ chạy khi gọi start(), await(), join())
    val jobLazy = launch(start = CoroutineStart.LAZY) {
        Log.d("Lazy Start", "Coroutine started")
    }
    Log.d("Lazy Start", "Before starting coroutine")
    jobLazy.start() // hoặc jobLazy.join(), jobLazy.await()
    jobLazy.join()
    Log.d("Lazy Start", "After coroutine completion")

    // Atomic start coroutine (ngay lập tức chạy, không thể hủy trước khi bắt đầu)
    val jobAtomic = launch(start = CoroutineStart.ATOMIC) {
        Log.d("Atomic Start", "Coroutine started")
        try {
            delay(1000)
            Log.d("Atomic Start", "Coroutine completed")
        } catch (e: Exception) {
            Log.d("Atomic Start", "Coroutine was cancelled")
        } finally {
            Log.d("Atomic Start", "Coroutine finally block executed")
        }
    }
    jobAtomic.cancel() // Hủy ngay sau khi tạo, nhưng coroutine vẫn sẽ chạy
    Log.d("Atomic Start", "Before joining coroutine")
    jobAtomic.join()
    Log.d("Atomic Start", "After coroutine completion")

    // Undispatched start coroutine (chạy ngay trong luồng gọi, không lập lịch)
    val jobUndispatched = launch(start = CoroutineStart.UNDISPATCHED) {
        Log.d("Undispatched Start", "Coroutine started")
    }
    Log.d("Undispatched Start", "Before joining coroutine")
    jobUndispatched.join()
    Log.d("Undispatched Start", "After coroutine completion")
}

// CoroutineScope và SupervisorScope
fun demoCoroutineScope() = runBlocking {
    // CoroutineScope là phạm vi coroutine thông thường
    launch {
        Log.d("CoroutineScope", "CoroutineScope started")
        launch {
            Log.d("CoroutineScope", "Child coroutine 1 started")
            delay(1000)
            Log.d("CoroutineScope", "Child coroutine 1 completed")
        }
        launch {
            Log.d("CoroutineScope", "Child coroutine 2 started")
            delay(500)
            Log.d("CoroutineScope", "Child coroutine 2 completed")
        }
        Log.d("CoroutineScope", "CoroutineScope completed")
    }.join()

    // SupervisorScope là phạm vi coroutine đặc biệt, nơi lỗi của con không ảnh hưởng đến con khác
    launch {
        Log.d("SupervisorScope", "SupervisorScope started")
        supervisorScope {
            launch {
                Log.d("SupervisorScope", "Child coroutine 2 started")
                throw Exception("Error in Child coroutine 2")
            }
            launch {
                Log.d("SupervisorScope", "Child coroutine 1 started")
                delay(1000)
                Log.d("SupervisorScope", "Child coroutine 1 completed")
            }
        }
        Log.d("SupervisorScope", "SupervisorScope completed")
    }.join()

    // Sử dụng awaitAll để chờ tất cả các coroutine hoàn thành
    val results = listOf(
        async {
            delay(1000)
            Log.d("awaitAll", "Async 1 completed")
            1
        },
        async {
            delay(500)
            Log.d("awaitAll", "Async 2 completed")
            2
        },
        async {
            delay(1500)
            Log.d("awaitAll", "Async 3 completed")
            3
        }
    ).awaitAll()
    Log.d("awaitAll", "Results: $results")
}



