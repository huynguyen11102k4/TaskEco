package com.example.task.task3.generics

import android.util.Log

fun main() {
    demoCovariant()
    demoContravariant()
    demoInvariant()
}

fun demoCovariant() {
    val stringList = ListCovariant(listOf("Kotlin", "Java", "C++"))
    printList(stringList)
}

fun demoContravariant() {
    val anyConsumer: ConsumerContravariant<Any> = ConsumerContravariant()
    val stringConsumer: ConsumerContravariant<String>  = anyConsumer
    stringConsumer.consume("123")
}

fun demoInvariant(){
    val stringList = MutableListInvariant(mutableListOf("List", "Set", "Map"))
    stringList.add("Array")
    Log.d("Invariant", "Size: ${stringList.size()}")
    for (i in 0 until stringList.size()) {
        println(stringList.get(i))
    }
}

fun printList(list: ListCovariant<Any>) {
    for (i in 0 until list.size()) {
        println(list.get(i))
    }
}


