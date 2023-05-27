package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OutBlock

class OutputCommand(private val output: String) : BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) =
            OutputCommand((block as OutBlock).out)
    }

    override fun runCommand() {
        Interpreter.output.add(calculate(output))
    }
}