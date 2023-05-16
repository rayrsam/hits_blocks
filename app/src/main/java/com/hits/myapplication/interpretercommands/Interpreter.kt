package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import java.util.Stack

object Interpreter {

    var blockList = emptyList<Block>()
    private var varMap = HashMap<String, Array<String>>()
    var output = mutableListOf<String>()

    fun executeCode(): MutableList<String> {
        val queue = mutableListOf<BlockCommand>()
        output = mutableListOf()
        varMap = hashMapOf()
        val containterStack = Stack<ContainerCommand>()
        var lastBlockTabs = 0
        blockList.forEach{
            val currentBlockCommand = BlockPairs.getType(it)!!.buildBlockCommand(it)
            if(it.tabs == 0) queue.add(currentBlockCommand)
            else containterStack.peek().queue.add(currentBlockCommand)
            if(it.tabs < lastBlockTabs) containterStack.pop()
            if(currentBlockCommand is ContainerCommand) containterStack.push(currentBlockCommand)
            lastBlockTabs = it.tabs
        }
        queue.forEach{it.runCommand()}
        return output
    }

    fun assignVar(type: String, name: String, value: String) {
        varMap[name] = arrayOf(type, value)
    }

    fun getVar(name: String): String? {
        return if(varMap.keys.contains(name)) varMap[name]!![1] else null
    }

    fun getType(variable: String): String {
        return if(variable == "true" || variable == "false") "bool"
        else if(variable.toIntOrNull() != null) "int"
        else if(variable.toDoubleOrNull() != null) "double"
        else if(variable.first() == '[' && variable.last() == ']') "list"
        else "str"
    }

    fun convertVariable(variable: String): Any {
        return when (getType(variable)) {
            "int" -> variable.toInt()
            "double" -> variable.toDouble()
            "bool" -> if (variable == "true") true else "false"
            "list" -> variable.substring(1, variable.length - 1).split(',').toMutableList()
            else -> variable
        }
    }
}