package com.example.task.task3.dsl

class Menu {
    private val items = mutableListOf<MenuEntry>()

    fun addEntry(entry: MenuEntry) {
        items.add(entry)
    }

    fun display() {
        for ((index, item) in items.withIndex()) {
            val name = item.name ?: "No Name"
            val type = item.type?.name ?: "No Type"
            println("${index + 1}. $name - $type")
        }
    }

    fun subMenu(name: String?, type: MenuItemType?, builder: Menu.() -> Unit) {
        val sub = Menu()
        sub.builder()
        items.add(MenuEntry(name, type, sub))
    }
}
