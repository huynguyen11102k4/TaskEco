package com.example.task.task3.generics

fun main() {
    val list: MutableListInvariant<Int> = MutableListInvariant(mutableListOf(1, 2, 3, 4, 5))
    println("Swap index 1 and 3")
    list.swap(1, 3)
    for (i in 0 until list.size()) {
        println(list[i])
    }

    val findList: ListCovariant<Int> = ListCovariant(listOf(1, 2, 3, 4, 5))
    val index = findList.findIndex(3)
    println("Index of 3 is $index")
}

fun <T> MutableListInvariant<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

// <T : Comparable<T>> là chỉ chấp nhận các kiểu T mà có thể so sánh được với nhau
fun <T : Comparable<T>> ListCovariant<T>.findIndex(value: T): Int {
    val n = this.size()
    var left = 0
    var right = n - 1
    while(left <= right){
        val mid = (left + right) / 2
        val midValue = this.get(mid)
        when {
            midValue == value -> return mid
            midValue < value -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return -1
}

//Star projection (đại diện sao) - khi không biết kiểu cụ thể, sử dụng * để đại diện cho bất kỳ kiểu nào
// * = out Any? (covariant) hoặc in Nothing (contravariant)
fun printList(list: ListCovariant<*>) {
    for (i in 0 until list.size()) {
        println(list.get(i))
    }
}

fun copy(from: MutableListInvariant<*>, to: MutableListInvariant<Any?>) {
    for (i in 0 until from.size()) {
        to.add(from[i])
    }
}