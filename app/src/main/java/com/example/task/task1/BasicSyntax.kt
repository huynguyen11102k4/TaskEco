package com.example.task.task1

import android.os.Build
import androidx.annotation.RequiresApi

fun main(){
//    val set1 = setOf(1)
//    println(set1.javaClass)
//    val set2 = setOf(1,2,3)
//    println(set2.javaClass)
    demoVarAndVal()
    demoDataType()
    demoOperator()
    demoTypeCasting()
    demoString()
}

//1.Biến và hằng số
@RequiresApi(Build.VERSION_CODES.O)
fun demoVarAndVal(){
    val a : Int = 2
    var b : Int
    val c : Int = 2
    val d = 2.5
    val e : Int = d.toInt()
    println("a = $a")
    println("Kieu du lieu cua d la: " + (d::class.java.typeName))
    val ten:String? = readln()
    println("ten: " + ten)
    var tuoi: Int = 0
    val s:String? = readln()
    if(s!=null){
        tuoi = s.toInt()
        println("tuoi: " + tuoi)
    }
    val high = readln().toDouble()
    println("cao: " + high)
}

//2. Kiểu dữ liệu
fun demoDataType(){
    val a: Int = 4
    val b: Double = 4.5
    val c: Float = 2.5f
    val d: Long = 1000000000000L
    val e: Short = 10
    val f: Byte = 1
    val g: Char = 'A'
    val h: Boolean = true
    val i: String = "Hello"
    println("a = $a, b = $b, c = $c, d = $d, e = $e, f = $f, g = $g, h = $h, i = $i")
}

//3. Toán tử
fun demoOperator(){
    val a = 8
    val b = 3
    println(a + b)
    println(a - b)
    println(a * b)
    println(a / b)
    println(a % b)
    println(a == b)
    println(a != b)
    println(a > b)
    println(a < b)
    println(a >= b)
    println(a <= b)
    println(a > 5 && b < 5)
    println(a > 5 || b < 5)
    println(!(a > 5))
    var c = 9
    c += 3
    println(c)
    c -= 2
    println(c)
    c *= 3
    println(c)
    c /= 2
    println(c)
    c %= 3
    println(c)
}

//4. Ep kiểu
fun demoTypeCasting(){
    val a: Int = 2
    val b: Double = a.toDouble()
    val c: Float = a.toFloat()
    val d: Long = a.toLong()
    val e: Short = a.toShort()
    val f: Byte = a.toByte()
    println("a = $a, b = $b, c = $c, d = $d, e = $e, f = $f")
}

//5. Chuỗi
fun demoString(){
    val s1: String = "Hello"
    val s2: String = "World"
    val s3: String = s1 + " " + s2
    println(s3)
    val s4: String = "$s1 $s2"
    println(s4)
    println("Length of s4: ${s4.length}")
    println("Uppercase: ${s4.uppercase()}")
    println("Lowercase: ${s4.lowercase()}")
    println("Contains 'Hello': ${s4.contains("Hello")}")
    println("Substring (0, 5): ${s4.substring(0, 5)}")
    println("Replace 'World' with 'Kotlin': ${s4.replace("World", "Kotlin")}")
    println("Index of 'o': ${s4.indexOf('o')}")
}
