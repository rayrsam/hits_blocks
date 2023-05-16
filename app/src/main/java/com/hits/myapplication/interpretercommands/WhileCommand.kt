package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.WhileBlock

class WhileCommand(condition: String): ConditionCommand(condition) {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = WhileCommand((block as WhileBlock).cond.toString())
    }
    override fun runCommand() {
        while(calculate(condition) == "true") {
            queue.forEach{it.runCommand()}}
    }
}