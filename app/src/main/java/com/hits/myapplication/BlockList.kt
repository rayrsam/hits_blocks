package com.hits.myapplication

import java.util.Collections

typealias BlockListener = (blocks: List<Block>) -> Unit

class BlockList {
    private var blocks = mutableListOf<Block>()
    private var listeners = mutableListOf<BlockListener>()
    private var index = 0

    fun getBlocks(): List<Block> {
        return blocks
    }

    fun removeBlock(block: Block) {
        val index = blocks.indexOfFirst { it.id == block.id }
        if (index != -1) {
            blocks = ArrayList(blocks)
            blocks.removeAt(index)
            notifyChanges()
        }
    }

    fun clearBlocks() {
        blocks.clear()
        notifyChanges()
    }

    fun removeTabBlock(block: Block) {
        val index = blocks.indexOfFirst { it.id == block.id }
        if (index != -1) {
            if (blocks[index].tabs > 0) blocks[index].tabs--
            else removeBlock(block)
        }
    }

    fun addTabBlock(block: Block) {
        val index = blocks.indexOfFirst { it.id == block.id }
        if (index != -1) blocks[index].tabs++
    }

    private fun addBlock(block: Block) {
        blocks.add(block)
        notifyChanges()
    }

    fun createBlock(type: Int): Block {
        var tabs = 0
        val num = blocks.size

        if (num != 0) {
            val prev = blocks[num - 1]
            tabs = prev.tabs
            if (prev.type >= 3) {
                tabs++
            }
        }

        val newBlock = when (type) {
            0 -> ListBlock(index, tabs)
            1 -> OperBlock(index, tabs)
            2 -> OutBlock(index, tabs)
            3 -> IfBlock(index, tabs)
            4 -> WhileBlock(index, tabs)
            5 -> ElseBlock(index, tabs)
            else -> ForBlock(index, tabs)
        }
        index++
        addBlock(newBlock)
        return newBlock
    }

    fun swapBlock(oldInd: Int, newInd: Int) {
        blocks = ArrayList(blocks)
        Collections.swap(blocks, oldInd, newInd)
        notifyChanges()
    }

    fun addListener(listener: BlockListener) {
        listeners.add(listener)
        notifyChanges()
    }

    fun removeListener(listener: BlockListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(blocks) }
    }

    fun readFromFile(data: String) {
        blocks.clear()
        index = 0

        val newBlocks = data.split("\n")
        newBlocks.forEach {
            val rawBlock = it.split(";")
            val block = createBlock(rawBlock[0].toInt())

            when (block.type) {
                0 -> block as ListBlock
                1 -> block as OperBlock
                2 -> block as OutBlock
                3 -> block as IfBlock
                4 -> block as WhileBlock
                5 -> block as ElseBlock
                else -> block as ForBlock
            }
            block.read(it)
        }
        notifyChanges()
    }

    fun writeToFile(): String {
        var result = ""
        blocks.forEach { result += it.write() }

        return result
    }
}