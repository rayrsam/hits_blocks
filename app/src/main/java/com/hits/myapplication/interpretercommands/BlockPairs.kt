package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.ElseBlock
import com.hits.myapplication.ForBlock
import com.hits.myapplication.IfBlock
import com.hits.myapplication.ListBlock
import com.hits.myapplication.OperBlock
import com.hits.myapplication.OutBlock
import com.hits.myapplication.WhileBlock

object BlockPairs {
    fun getType(block: Block): BlockCommandFactory? {
        return when (block) {
            is OutBlock -> OutputCommand
            is OperBlock -> OperationCommand
            is IfBlock -> IfCommand
            is WhileBlock -> WhileCommand
            is ListBlock -> ListCommand
            is ElseBlock -> ElseCommand
            is ForBlock -> ForCommand
            else -> null
        }
    }
}