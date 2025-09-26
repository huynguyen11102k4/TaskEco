package com.example.task.task3.dsl

data class MenuEntry(
    val name: String? = null,
    val type: MenuItemType? = null,
    val subMenu: Menu? = null
)
