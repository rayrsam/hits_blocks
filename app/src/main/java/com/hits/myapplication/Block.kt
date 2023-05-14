package com.hits.myapplication

open class Block(
    val id: Int,
    var tabs: Int,
    val type: Int = -1
)

class VarBlock(
    id: Int,
    tabs: Int,
    type: Int = 0,
    var left: String? = null,
    var right: String? = null
) : Block(id, tabs, type = 0) {}

class OperBlock(
    id: Int,
    tabs: Int,
    type: Int = 1,
    var left: String? = null,
    var right: String? = null
) : Block(id, tabs, type = 1) {}

class OutBlock(
    id: Int,
    tabs: Int,
    type: Int = 2,
    var out: String? = null
) : Block(id, tabs, type = 2) {}

class IfBlock(
    id: Int,
    tabs: Int,
    type: Int = 3,
    var cond: String? = null
) : Block(id, tabs, type = 3) {}
