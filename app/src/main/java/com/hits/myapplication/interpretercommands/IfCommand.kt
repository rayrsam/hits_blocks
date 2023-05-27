package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.IfBlock

class IfCommand(condition: String) : ConditionCommand(condition) {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = IfCommand((block as IfBlock).cond)
    }

    override fun runCommand() {
        if (calculate(condition) == "true") queue.forEach { it.runCommand() }
    }
}