package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OperBlock

class OperationCommand(val name: String, private val expression: String) : BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) =
            OperationCommand((block as OperBlock).left, block.right)
    }

    override fun runCommand() {
        val list = Interpreter.getList(name)
        if (list != null) {
            (list[0] as MutableList<String>)[(list[2] as String).toInt()] = calculate(expression)
            Interpreter.assignVar("list", list[1] as String, list[0].toString())
        } else Interpreter.assignVar(
            Interpreter.getType(calculate(expression)),
            name,
            calculate(expression)
        )
    }
}