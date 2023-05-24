package com.hits.myapplication

open class Block(
    val id: Int,
    var tabs: Int,
    val type: Int = -1
)

class ListBlock(
    id: Int,
    tabs: Int,
    var name: String = "",
    var size: String = "",
    var list: String = ""
) : Block(id, tabs, type = 0)

class OperBlock(
    id: Int,
    tabs: Int,
    var left: String = "",
    var right: String = ""
) : Block(id, tabs, type = 1)

class OutBlock(
    id: Int,
    tabs: Int,
    var out: String = ""
) : Block(id, tabs, type = 2)

class IfBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 3)

class ElseBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 5)

class WhileBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 4)

class ForBlock(
    id: Int,
    tabs: Int,
    var predLeft: String = "",
    var predRight: String = "",
    var cond: String = "",
    var postLeft: String = "",
    var postRight: String = ""
) : Block(id, tabs, type = 6)
