package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ListBlock

class ListCommand(val name: String, private val size: String, private val list: String) :
    BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = ListCommand(
            (block as ListBlock).name,
            block.size,
            block.list
        )
    }

    override fun runCommand() {
        val result = "\\s*,\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*\$)".toRegex().split(list.trim())
        val listVar = mutableListOf<String>()
        for (i in 0 until size.toInt()) {
            if (i < result.size - 1) listVar.add(calculate(result[i]))
            else if (list != "") listVar.add(calculate(result.last()))
            else listVar.add("0")
        }
        Interpreter.assignVar("list", name, listVar.toString())
    }
}