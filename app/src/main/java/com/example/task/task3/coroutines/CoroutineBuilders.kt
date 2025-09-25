package com.example.task.task3.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

fun main() {
    demoCoroutineBuilders()
    demoCoroutineStarted()
    demoCoroutineScope()
    demoWithContext()
}

// Coroutine Builders
fun demoCoroutineBuilders() = runBlocking {
    // launch:  Tạo coroutine không trả về giá trị
    val job = launch {
        delay(1000)
        println("Coroutine Builders: launch completed")
    }
    job.join()

    // async:  Tạo coroutine trả về Deferred, có thể lấy kết quả bằng await()
    val deferred = async {
        delay(1000)
        println("Coroutine Builders: async completed")
        10
    }
    val res = deferred.await()
    println("Coroutine Builders: Result from async:  $res")

    // runBlocking:  Tạo coroutine chặn luồng hiện tại cho đến khi hoàn thành
    runBlocking {
        delay(1000)
        println("Coroutine Builders: runBlocking completed")
    }
}

//CoroutineStarted
fun demoCoroutineStarted() = runBlocking {
    // Default start coroutine (chỉ lập lịch cho job chưa chạy ngay)
    val jobDefault = launch {
        println("Default Start: Coroutine started")
    }
    println("Default Start: Before joining coroutine")
    jobDefault.join()
    println("Default Start: After coroutine completion")

    // Lazy start coroutine (chỉ chạy khi gọi start(), await(), join())
    val jobLazy = launch(start = CoroutineStart.LAZY) {
        println("Lazy Start: Coroutine started")
    }
    println("Lazy Start: Before starting coroutine")
    jobLazy.start() // hoặc jobLazy.join(), jobLazy.await()
    jobLazy.join()
    println("Lazy Start: After coroutine completion")

    // Atomic start coroutine (ngay lập tức chạy, không thể hủy trước khi bắt đầu)
    val jobAtomic = launch(start = CoroutineStart.ATOMIC) {
        println("Atomic Start: Coroutine started")
        try {
            delay(1000)
            println("Atomic Start: Coroutine completed")
        } catch (e: Exception) {
            println("Atomic Start: Coroutine was cancelled")
        } finally {
            println("Atomic Start: Coroutine finally block executed")
        }
    }
    jobAtomic.cancel() // Hủy ngay sau khi tạo, nhưng coroutine vẫn sẽ chạy
    println("Atomic Start: Before joining coroutine")
    jobAtomic.join()
    println("Atomic Start: After coroutine completion")

    // Undispatched start coroutine (chạy ngay trong luồng gọi, không lập lịch)
    val jobUndispatched = launch(start = CoroutineStart.UNDISPATCHED) {
        println("Undispatched Start: Coroutine started")
    }
    println("Undispatched Start: Before joining coroutine")
    jobUndispatched.join()
    println("Undispatched Start: After coroutine completion")
}

// CoroutineScope, SupervisorScope, GlobalScope
fun demoCoroutineScope() = runBlocking {
    // CoroutineScope là phạm vi coroutine thông thường
    launch {
        println("CoroutineScope: CoroutineScope started")
        launch {
            println("CoroutineScope: Child coroutine 1 started")
            delay(1000)
            println("CoroutineScope: Child coroutine 1 completed")
        }
        launch {
            println("CoroutineScope: Child coroutine 2 started")
            delay(500)
            println("CoroutineScope: Child coroutine 2 completed")
        }
        println("CoroutineScope: CoroutineScope completed")
    }.join()

    // SupervisorScope là phạm vi coroutine đặc biệt, nơi lỗi của con không ảnh hưởng đến con khác
    launch {
        println("SupervisorScope: SupervisorScope started")
        supervisorScope {
            launch {
                println("SupervisorScope: Child coroutine 2 started")
                throw Exception("Error in Child coroutine 2")
            }
            launch {
                println("SupervisorScope: Child coroutine 1 started")
                delay(1000)
                println("SupervisorScope: Child coroutine 1 completed")
            }
        }
        println("SupervisorScope: SupervisorScope completed")
    }.join()

    // Sử dụng awaitAll để chờ tất cả các coroutine hoàn thành
    val results = listOf(
        async {
            delay(1000)
            println("awaitAll: Async 1 completed")
            1
        },
        async {
            delay(500)
            println("awaitAll: Async 2 completed")
            2
        },
        async {
            delay(1500)
            println("awaitAll: Async 3 completed")
            3
        }
    ).awaitAll()
    println("awaitAll: Results:  $results")
}

//withContext dùng để chuyển đổi context của coroutine
fun demoWithContext() = runBlocking {
    println("withContext: Running on thread:  ${Thread.currentThread().name}")
    withContext(Dispatchers.IO) {
        println("withContext: Switched to IO Dispatcher on thread:  ${Thread.currentThread().name}")
        delay(1000)
    }
    println("withContext: Back to original context on thread:  ${Thread.currentThread().name}")
}







