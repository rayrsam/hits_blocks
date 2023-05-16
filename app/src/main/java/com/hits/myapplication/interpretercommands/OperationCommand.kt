package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OperBlock
import java.util.Stack
import kotlin.math.pow

class OperationCommand(val name: String, val expression: String) : BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = OperationCommand((block as OperBlock).left.toString(), block.right.toString())
    }

    override fun runCommand() {
        Interpreter.assignVar(Interpreter.getType(calculate(expression)), name, calculate(expression))
    }
}