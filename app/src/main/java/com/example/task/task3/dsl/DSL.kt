package com.example.task.task3.dsl

fun main() {
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


fun buildMenu(builder: Menu.() -> Unit): Menu {
    val menu = Menu()
    menu.builder()
    return menu
}
