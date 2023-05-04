package com.hits.myapplication

open class Block (
    val id : Int,
    val title: String,
    val imageId: Int,
    val type : Int = -1
)

class VarBlock (
    id : Int,
    title: String,
    imageId: Int = R.drawable.ussr,
    type : Int = 0
) : Block(id, title, imageId, type = 0)

class OperBlock(
    id : Int,
    title: String,
    imageId: Int = R.drawable.rus,
    type : Int = 1
) : Block(id, title, imageId, type = 1)

class OutBlock(
    id : Int,
    title: String,
    imageId: Int = R.drawable.emp,
    type : Int = 2
) : Block(id, title, imageId, type = 2)


