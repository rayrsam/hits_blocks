package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OutBlock

class OutputCommand(val name: String) : BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = OutputCommand((block as OutBlock).out.toString())
    }

    override fun runCommand() {
        Interpreter.output.add(Interpreter.getVar(name))
    }
}