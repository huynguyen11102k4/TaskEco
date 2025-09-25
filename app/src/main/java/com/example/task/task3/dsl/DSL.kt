package com.example.task.task3.dsl

fun main(){
    val menu = buildMenu {
        addEntry(MenuEntry("Start Game", MenuItemType.START))
        addEntry(MenuEntry("Settings", MenuItemType.SETTINGS))
        subMenu("Help", MenuItemType.HELP) {
            addEntry(MenuEntry("How to Play", MenuItemType.HELP))
            addEntry(MenuEntry("About", MenuItemType.HELP))
        }
        addEntry(MenuEntry("Exit", MenuItemType.EXIT))
    }
    menu.display()
}

class Menu{
    private val items = mutableListOf<MenuEntry>()

    fun addEntry(entry: MenuEntry){
        items.add(entry)
    }
    fun display(){
        for((index, item) in items.withIndex()){
            val name = item.name ?: "No Name"
            val type = item.type?.name ?: "No Type"
            println("${index + 1}. $name - $type")
        }
    }
    fun subMenu(name: String?, type: MenuItemType?, builder: Menu.() -> Unit){
        val sub = Menu()
        sub.builder()
        items.add(MenuEntry(name, type, sub))
    }
}

data class MenuEntry(val name: String? = null, val type: MenuItemType? = null, val subMenu: Menu? = null)

fun buildMenu(builder: Menu.() -> Unit): Menu {
    val menu = Menu()
    menu.builder()
    return menu
}
