package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OperBlock
import com.hits.myapplication.OutBlock
import com.hits.myapplication.VarBlock

object Interpreter {

    var blockList = emptyList<Block>()
    private var varMap = HashMap<String, Array<String>>()
    var output = mutableListOf<String>()

    fun executeCode(): MutableList<String> {
        val queue = mutableListOf<BlockCommand>()
        output = mutableListOf()
        varMap = hashMapOf()
        blockList.forEach{
            if(it is VarBlock) queue.add(AssignCommand.buildBlockCommand(it))
            if(it is OperBlock) queue.add(OperationCommand.buildBlockCommand(it))
            if(it is OutBlock) queue.add(OutputCommand.buildBlockCommand(it))
        }
        queue.forEach{
            //runCommand(it)
            it.runCommand()
        }
        return output
    }

    fun assignVar(type: String, name: String, value: String) {
        varMap[name] = arrayOf(type, value)
    }

    fun getVar(name: String): String {
        return varMap[name]?.get(1).toString()
    }

    //private fun runCommand(command: BlockCommand) {
    //    if(command is AssignCommand) {
    //        assignVar(command.type, command.name, command.value)
    //    }
    //    else if(command is OperationCommand) {
    //        assignVar(varMap[command.name]?.get(0).toString(), command.name, command.calculate().toString())
    //    }
    //}
}