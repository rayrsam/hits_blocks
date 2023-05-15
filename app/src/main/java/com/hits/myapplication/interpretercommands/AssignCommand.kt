//package com.hits.myapplication.interpretercommands
//
//import com.hits.myapplication.Block
//import com.hits.myapplication.VarBlock
//import com.hits.myapplication.interpretercommands.BlockCommand
//
//class AssignCommand(val type: String, val name: String, val value: String) : BlockCommand {
//    companion object Factory : BlockCommandFactory() {
//        override fun buildBlockCommand(block: Block) = AssignCommand("int", (block as VarBlock).left.toString(), block.right.toString())
            //    }
//
//    override fun runCommand() {
//        Interpreter.assignVar(type, name, value)
            //    }
//}