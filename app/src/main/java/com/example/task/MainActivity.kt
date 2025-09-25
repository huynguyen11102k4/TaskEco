package com.example.task

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.task.task3.coroutines.testMultiThreading
import com.example.task.task3.highFunscAndLambdas.demoHigherOrderFunctionsandLambdas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Job())

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch(Dispatchers.Default) { // Dispatchers.Default: Sử dụng cho các tác vụ CPU như tính toán nặng
            testMultiThreading()
            launch(Dispatchers.Unconfined) { // Dispatchers.Unconfined: Bắt đầu trong luồng gọi, sau đó chuyển sang luồng khác khi gặp điểm dừng
                Log.d("Dispatchers", "Running on Unconfined Dispatcher - Thread name: ${Thread.currentThread().name}")
                delay(1000)
                Log.d("Dispatchers", "Running on Unconfined Dispatcher - Thread name after delay: ${Thread.currentThread().name}")
            }.join()
        }
        val textView = findViewById<TextView>(R.id.textView)
        coroutineScope.launch { // Dispatchers.Main: Sử dụng cho các tác vụ liên quan đến UI (chỉ có trên Android)
            val data = withContext(Dispatchers.IO) { // Dispatchers.IO: Sử dụng cho các tác vụ I/O như đọc/ghi file, mạng
                fetchData()
            }
            val processData = withContext(Dispatchers.Default) {
                processData(data)
            }
            textView.text = processData
        }

        // GlobalScope: Sử dụng cho các tác vụ toàn cục, không phụ thuộc vào vòng đời của Activity/Fragment
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                println("Send log to server")
            }
        }
    }
}
suspend fun fetchData(): String{
    delay(2000)
    return "Data from server"
}

suspend fun processData(input: String): String {
    delay(1000)
    return "Processed data: $input"
}
