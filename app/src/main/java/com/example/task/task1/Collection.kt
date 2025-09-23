package com.example.task.task1

import kotlin.collections.fill
import kotlin.collections.iterator
import kotlin.collections.reverse
import kotlin.collections.shuffle
import kotlin.collections.sort
import kotlin.collections.sortDescending

fun main() {
    listExample()
    setExample()
    mapExample()
}
fun listExample() {
    val items = listOf("apple", "banana", "mango")
    for (item in items) {
        print("$item ")
    }
    //Truy cập theo chỉ số
    println(items[0]) // trả về phần tử đầu tiên
    println(items.get(1)) // trả về phần tử thứ hai
    println(items.indexOf("mango")) // trả về chỉ số của phần tử "mango"
    println(items.lastIndexOf("banana")) // trả về chỉ số cuối cùng của phần tử "banana"
    val numbers = mutableListOf(1, 2, 3, 4)
    println(numbers.indexOfFirst { it > 2}) // trả về chỉ số của phần tử đầu tiên lớn hơn 2
    println(numbers.indexOfLast { it % 2 == 1}) // trả về chỉ số của phần tử cuối cùng là số lẻ

    //Sublist
    println(items.subList(0, 2)) // trả về view của list gốc khi thay đổi phần tử của sublist thì list gốc cũng thay đổi
    println()

    items.forEach { item -> print("$item ")}
    println()
    items.forEach { print("$it ") }
    println()
    val fruits = listOf("banana", "avocado", "apple", "mango")
    fruits
        .filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.uppercase() }
        .forEach { print("$it ") }
    println()
    fruits.reduce { acc, s -> "$acc, $s" }
    val num = listOf(1, 2, 3, 4, 5)
    println(num.size)
    println(num.all { it > 0 })
    println(num.any { it > 3 })
    println(num.count { it > 3 })
    println(num.find { it > 3 })
    println(num.findLast { it > 3 })
    println(num.maxOrNull())
    println(num.minOrNull())
    println(num.sum())
    println(num.toMutableList())
    println(num.toSet())
    println(num.distinct())
    println(num.fold(10) { acc, i -> acc + i })
    println(num.reduce { acc, i -> acc + i })
    println(num.joinToString())
    println(num.joinToString(separator = " - ", prefix = "(", postfix = ")"))
    println(num.slice(1..3)) // [2, 3, 4]
    println(num.slice(listOf(0, 2, 4))) // [1, 3, 5]
    println(num.subList(1, 4))
    println(num.take(3))
    println(num.drop(2))
    println(num.chunked(2)) // [[1, 2], [3, 4], [5]]
    println(num.windowed(3, 1)) // [[1, 2, 3], [2, 3, 4], [3, 4, 5]] cua so truot
    println(num.sorted())
    println(num.sortedDescending())
    println(num.reversed())
    println(num.distinct())
    println(num.first())
    println(num.last())
    println(num.elementAt(2))
    println(num.elementAtOrNull(10))
    println(num.getOrNull(10))
    println(num.indexOf(3))
    println(num.indexOfFirst { it > 3 })
    println(num.indexOfLast { it > 3 })
    println(num.contains(3))
    println(num.containsAll(listOf(3, 4, 5)))
    println(num.isEmpty())
    println(num.isNotEmpty())
    println(num.asReversed()) // dao nguoc list goc
    println(num.asSequence().map { it * 2 }.toList()) //Chỉ thực hiện khi cần thiết(lazy)
    println(num.random())
    println(num.filter { it > 3 })
    println(num.map { it * 2 })
    println(num.flatMap { listOf(it, it * 10) }) // [1, 10, 2, 20, 3, 30, 4, 40, 5, 50]
    println(num.groupBy { it % 2 }) // {1=[1, 3, 5], 0=[2, 4]}
    println(num.groupingBy { it % 2 }.eachCount()) // {1=3, 0=2}

    val mnum = mutableListOf(1, 2, 3)
    mnum.add(4)
    mnum.addAll(listOf(5, 6))
    println(mnum)
    mnum.remove(3)
    mnum.removeAt(0)
    println(mnum)
    mnum[0] = 10
    println(mnum)
    mnum.clear()
    println(mnum)
    println(mnum.isEmpty())
    println(mnum.isNotEmpty())
    println(mnum.size)
    mnum += 1 // mnum.add(1)
    mnum += listOf(2, 3) // mnum.addAll(listOf(2, 3))
    println(mnum)
    mnum -= 2
    mnum -= listOf(1, 3)
    println(mnum)
    mnum.sort()
    println(mnum)
    mnum.sortDescending()
    println(mnum)
    mnum.shuffle()
    println(mnum)
    mnum.reverse()
    println(mnum)
    mnum.fill(0)
    println(mnum)
    mnum.replaceAll { it * 2 }
    println(mnum)
    mnum.removeIf { it > 0 }
    println(mnum)
}

fun setExample() {
    // Read-only set
    val num = setOf(1, 2, 3, 4, 5, 5, 4)
    println(num::class)
    for (n in num) {
        print("$n ")
    }
    num.forEach { print("$it ") }
    println(num)
    val fruits = listOf("banana", "avocado", "apple", "mango", "apple")
    val fruitSet = fruits.toSet()
    println(fruitSet)
    val fruitSet2 = fruits.toSortedSet()
    println(fruitSet2)
    val num2 = setOf(3, 4, 5, 6, 7)
    println(num.intersect(num2))
    println(num.union(num2))
    println(num.subtract(num2))
    println(num2.subtract(num))
    println(num.all { it > 3 })
    println(num.any { it > 3 })
    println(num.count { it > 3 })
    println(num.find { it > 3 })
    println(num.findLast { it > 3 })
    println(num.maxOrNull())
    println(num.minOrNull())
    println(num.sum())
    println(num.contains(3))
    println(num.containsAll(listOf(3, 4, 5)))
    println(num.isEmpty())
    println(num.isNotEmpty())
    println(num.size)
    println(num.first())
    println(num.last())
    println(num.elementAt(2))
    println(num.elementAtOrNull(10))
    println(num.random())
    println(num.filter { it > 3 })
    println(num.map { it * 2 })
    println(num.count { it > 3 })
    println(num.sorted())
    println(num.sortedDescending())
    println(num.reversed())
    println(num.take(3))
    println(num.drop(2))
    println(num.chunked(2))
    println(num.windowed(3, 1))
    println(num.reduce { acc, i -> acc + i })
    println(num.fold(10) { acc, i -> acc + i })
    println(num.joinToString())
    println(num.joinToString(separator = " - ", prefix = "(", postfix = ")"))
    println(num.groupBy { it % 2 })
    println(num.groupingBy { it % 2 }.eachCount())

    val mnum = mutableSetOf(1, 2, 3)
    mnum.add(4)
    mnum.add(4)
    mnum.addAll(listOf(5, 6, 6))
    println(mnum)
    mnum.remove(3)
    println(mnum)
    mnum.clear()
    println(mnum)
    println(mnum.isEmpty())
    println(mnum.isNotEmpty())
    println(mnum.size)
    mnum += 1
    mnum += listOf(2, 3, 3)
    println(mnum)
    mnum -= 2
    mnum -= listOf(1, 3)
    println(mnum)
    mnum.removeIf { it > 0 }
    println(mnum)

    val hashSet = hashSetOf(1, 2, 3, 4, 5) // Không đảm bảo thứ tự, không trùng lặp, hiệu suất cao vì sử dụng bảng băm O(1)
    println(hashSet)
    val linkedSet = linkedSetOf(1, 2, 3, 4, 5) // Duy trì thứ tự chèn, không trùng lặp
    println(linkedSet)
    val sortedSet = sortedSetOf(5, 3, 1, 4, 2) // Tự động sắp xếp, không trùng lặp
    println(sortedSet)

    //test hieu nang
    val size = 5_000_000
    val largeHashSet = hashSetOf<Int>()
    val largelinkedSet = mutableSetOf<Int>()
    repeat(size) {
        largeHashSet.add(it)
        largelinkedSet.add(it)
    }
    var start = System.nanoTime()
    largeHashSet.contains(size - 1)
    println("HashSet: ${(System.nanoTime() - start) / 1_000_000.0} ms")
    start = System.nanoTime()
    largelinkedSet.contains(size - 1)
    println("LinkedHashSet: ${(System.nanoTime() - start) / 1_000_000.0} ms")

}

fun mapExample() {
    //read-only map
    val num = mapOf("one" to 1, "two" to 2, "three" to 3)
    val map: Map<String, Int> = mapOf("one" to 1, "two" to 2, "three" to 3)
    //Muttable map
    val mnum: MutableMap<String, Int> = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    val mnum2 = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    for ((key, value) in num) {
        println("$key = $value")
    }
    num.forEach { (key, value) -> println("$key = $value") }
    num.forEach { println("${it.key} = ${it.value}") }
    val fruits = listOf("banana", "avocado", "apple", "mango", "apple")
    val fruitCount = fruits.groupingBy { it }.eachCount()
    println(fruitCount)
    val fruitCount2 = fruits.groupBy { it.startsWith("a") }
    println(fruitCount2)
    println(fruitCount2[true])
    println(fruitCount2[false])
    println(num.keys)
    println(num.values)
    println(num.entries)
    println(num.size)
    println(num.isEmpty())
    println(num.isNotEmpty())
    println(num.containsKey("two")) // true
    println(num.containsValue(3))
    println(num["two"])
    println(num.get("two"))
    println(num.getOrDefault("four", 4))
    println(num.getOrElse("four") { 4 } )
    println(num.filter { (key, value) -> key.startsWith("t") && value > 2 })
    println(num.mapValues { (key, value) -> value * 2 })
    println(num.mapKeys { (key, value) -> key.uppercase() } )
    println(num.entries)
    println(num.entries.map { "${it.key} = ${it.value}" })
    println(num.entries.filter { it.value > 1 } )
    println(num.entries.associate { it.value to it.key } )
    println(num.maxBy { it.value })

    val hashMap = hashMapOf("one" to 1, "two" to 2, "three" to 3)  // Không đảm bảo thứ tự, hiệu suất cao vì sử dụng bảng băm O(1)
    println(hashMap)
    val linkedMap = linkedMapOf("one" to 1, "two" to 2, "three" to 3) // Duy trì thứ tự chèn
    println(linkedMap)
    val treeMap = sortedMapOf("three" to 3, "one" to 1, "two" to 2) // Tự động sắp xếp theo key
    println(treeMap)
}
