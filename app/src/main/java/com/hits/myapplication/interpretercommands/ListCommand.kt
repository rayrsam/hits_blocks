package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ListBlock

class ListCommand(val name: String, val size: String, val list: String): BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = ListCommand((block as ListBlock).name.toString(), block.size.toString(), block.list.toString())
    }
    override fun runCommand() {
        val result = "\\s*,\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*\$)".toRegex().split(list.trim())
        val listVar = mutableListOf<String>()
        for(i in 0..size.toInt() - 1) {
            if(i < result.size - 1) listVar.add(calculate(result[i]))
            else if(list != "") listVar.add(calculate(result.last()))
            else listVar.add("0")
        }
        Interpreter.assignVar("list", name, listVar.toString())
    }
}