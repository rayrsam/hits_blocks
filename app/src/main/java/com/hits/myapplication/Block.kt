package com.hits.myapplication

abstract class Block(
    val id: Int,
    var tabs: Int,
    val type: Int = -1
) {
    abstract fun write(): String
    abstract fun read(input: String)
}

class ListBlock(
    id: Int,
    tabs: Int,
    var name: String = "",
    var size: String = "",
    var list: String = ""
) : Block(id, tabs, type = 0) {
    override fun write(): String {
        return "$type;$name;$size;$list\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.name = data[1]
        this.size = data[2]
        this.list = data[3]
    }

}

class OperBlock(
    id: Int,
    tabs: Int,
    var left: String = "",
    var right: String = ""
) : Block(id, tabs, type = 1) {
    override fun write(): String {
        return "$type;$left;$right\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.left = data[1]
        this.right = data[2]
    }
}

class OutBlock(
    id: Int,
    tabs: Int,
    var out: String = ""
) : Block(id, tabs, type = 2) {
    override fun write(): String {
        return "$type;$out\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.out = data[1]
    }
}

class IfBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 3) {
    override fun write(): String {
        return "$type;$cond\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.cond = data[1]
    }
}

class ElseBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 5) {
    override fun write(): String {
        return "$type;$cond\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.cond = data[1]
    }
}

class WhileBlock(
    id: Int,
    tabs: Int,
    var cond: String = ""
) : Block(id, tabs, type = 4) {
    override fun write(): String {
        return "$type;$cond\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.cond = data[1]
    }
}

class ForBlock(
    id: Int,
    tabs: Int,
    var predLeft: String = "",
    var predRight: String = "",
    var cond: String = "",
    var postLeft: String = "",
    var postRight: String = ""
) : Block(id, tabs, type = 6) {
    override fun write(): String {
        return "$type;$predLeft;$predRight;$cond;$postLeft;$postRight\n"
    }

    override fun read(input: String) {
        val data = input.split(";")
        this.predLeft = data[1]
        this.predRight = data[2]
        this.cond = data[3]
        this.postLeft = data[4]
        this.postRight = data[5]
    }
}
