package com.hits.myapplication

open class Block (
    val id : Int,
    val type : Int = -1
)

class VarBlock (
    id : Int,
    type : Int = 0,
    var left : String? = null,
    var right : String? = null
) : Block(id, type = 0){

}

class OperBlock(
    id : Int,
    type : Int = 1,
    var left : String? = null,
    var right : String? = null
) : Block(id, type = 1){

}

class OutBlock(
    id : Int,
    type : Int = 2,
    var left : String? = null
) : Block(id, type = 2){

}


