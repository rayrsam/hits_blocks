package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block

abstract class BlockCommandFactory {
    abstract fun buildBlockCommand(block: Block): BlockCommand
}