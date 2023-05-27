package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ForBlock


class ForCommand(
    condition: String,
    private val name1: String,
    private val name2: String,
    private val exp1: String,
    private val exp2: String
) : ConditionCommand(condition) {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = ForCommand(
            (block as ForBlock).cond,
            block.predLeft,
            block.postLeft,
            block.predRight,
            block.postRight
        )
    }

    override fun runCommand() {
        val operBlock1 = OperationCommand(name1, exp1)
        val operBlock2 = OperationCommand(name2, exp2)
        operBlock1.runCommand()

        while (calculate(condition) == "true") {
            queue.forEach { it.runCommand() }
            operBlock2.runCommand()
        }
    }
}