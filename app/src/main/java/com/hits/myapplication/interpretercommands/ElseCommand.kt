package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ElseBlock


class ElseCommand(condition: String, private val ifBlock: ConditionCommand) : ConditionCommand(condition) {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = ElseCommand(
            (block as ElseBlock).cond.toString(),
            Interpreter.lastIf as ConditionCommand
        )
    }

    override fun runCommand() {
        if ((condition == "" || calculate(condition) == "true") && checkLastIf()) {
            queue.forEach { it.runCommand() }
        }
    }

    fun checkLastIf(): Boolean {
        return when (ifBlock) {
            is IfCommand -> {
                calculate(ifBlock.condition) != "true"
            }
            is ElseCommand -> {
                ifBlock.condition != "" && calculate(ifBlock.condition) != "true" && ifBlock.checkLastIf()
            }
            else -> false
        }
    }
}