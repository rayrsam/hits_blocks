package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OperBlock
import java.util.Stack

object Interpreter {

    var blockList = emptyList<Block>()
    private var varMap = HashMap<String, Array<String>>()
    var output = mutableListOf<String>()
    private val calculator = OperationCommand.buildBlockCommand(OperBlock(-1, 0))
    var lastIf: BlockCommand? = null

    fun executeCode(): MutableList<String> {
        val queue = mutableListOf<BlockCommand>()
        output = mutableListOf()
        varMap = hashMapOf()
        val containerStack = Stack<ContainerCommand>()
        var lastBlockTabs = 0
        var line = 1
        lastIf = null

        blockList.forEach {
            val currentBlockCommand = BlockPairs.getType(it)!!.buildBlockCommand(it)

            if (it.tabs == 0) queue.add(currentBlockCommand)
            else {
                if (it.tabs < lastBlockTabs) containerStack.pop()
                containerStack.peek().queue.add(currentBlockCommand)
            }

            if (currentBlockCommand is ContainerCommand) containerStack.push(currentBlockCommand)

            if (currentBlockCommand is IfCommand || currentBlockCommand is ElseCommand)
                lastIf = currentBlockCommand

            lastBlockTabs = it.tabs
        }

        queue.forEach {
            try {
                it.runCommand()
                line += 1
            } catch (e: Exception) {
                output = mutableListOf("Runtime error!", "Line $line")
                return output
            }
        }
        return output
    }

    fun assignVar(type: String, name: String, value: String) {
        varMap[name] = arrayOf(type, value)
    }

    fun getVar(name: String): String? {
        return if (varMap.keys.contains(name)) varMap[name]!![1] else null
    }

    fun getList(variable: String): MutableList<Any>? {
        val regex = "^([a-zA-Z]*)\\[(.*)\\]$".toRegex()
        val matchResult = regex.find(variable)
        val listName = matchResult?.let { it.groupValues[1].trim() }
        var index = matchResult?.let { it.groupValues[2].trim() }
        if (index != null) index = calculator.calculate(index)
        return if (listName == null || index == null) null else mutableListOf(
            getVar(listName)!!.substring(
                1,
                getVar(listName)!!.length - 1
            ).split(", ").toMutableList(), listName, index
        )
    }

    fun getType(variable: String): String {
        return if (variable == "true" || variable == "false") "bool"
        else if (variable.toIntOrNull() != null) "int"
        else if (variable.toDoubleOrNull() != null) "double"
        else if (variable.first() == '[' && variable.last() == ']') "list"
        else "str"
    }

    fun convertVariable(variable: String): Any {
        return when (getType(variable)) {
            "int" -> variable.toInt()
            "double" -> variable.toDouble()
            "bool" -> if (variable == "true") true else "false"
            "list" -> variable.substring(1, variable.length - 1).split(", ").toMutableList()
            else -> variable
        }
    }
}