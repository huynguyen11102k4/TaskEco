package com.example.task.task2.student

// Data Class (lưu trữ dữ liệu)
data class Room(val roomNumber: Int, val capacity: Int) {
    //Tự động override toString(), equals(), hashCode() hoặc có thể tự override lại
    override fun toString(): String {
        return "Room(roomNumber=$roomNumber, capacity=$capacity)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Room) return false
        return roomNumber == other.roomNumber && capacity == other.capacity
    }

    override fun hashCode(): Int {
        var result = roomNumber
        result = 31 * result + capacity
        return result
    }
}