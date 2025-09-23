package com.example.task.task1

fun main() {
    //1. if-else
    val a = 5
    if (a % 2 == 0) {
        println("$a la so chan")
    } else {
        println("$a la so le")
    }
    //2. when
    var b = 2
    when (b) {
        1 -> println("Mot")
        2 -> println("Hai")
        3 -> println("Ba")
        else -> println("Khong biet")
    }
    //3. for
    var tong = 0
    for (i in 1..5 step 1) {
        tong += i
    }
    println(tong)
    for (i in 1..9) {
        print("${2 * i} ")
    }
    println()
    var gt = 1
    for (i in 1..4) {
        gt = gt * i
    }
    println(gt)
    for (i in 1 until 5) {
        print("$i ")
    }
    println()
    for (i in 5 downTo 1) {
        print("$i ")
    }
    println()
    //4. while
    var n = 5
    var i = 1
    var t = 1
    while (i <= n) {
        t = t * i
        i++
    }
    println(t)
    //5. do-while
    var m = 5
    var s = 0
    var j = 1
    do {
        s = s + j
        j++
    } while (j <= m)
}
