package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ForBlock
import com.hits.myapplication.IfBlock

class ForCommand(condition: String, val name1: String, val name2: String, val exp1: String, val exp2: String): ConditionCommand(condition) {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = ForCommand((block as ForBlock).cond.toString(),
            block.predLeft.toString(), block.postLeft.toString(), block.predRight.toString(), block.postRight.toString())
    }
    override fun runCommand() {
        val operBlock1 = OperationCommand(name1, exp1)
        val operBlock2 = OperationCommand(name2, exp2)
        operBlock1.runCommand()

        while (calculate(condition) == "true") {
            queue.forEach{it.runCommand()}
            operBlock2.runCommand()
        }
    }
}