package com.hits.myapplication

import java.util.Collections

typealias BlockListener = (blocks: List<Block>) -> Unit

class BlockList {
    private var blocks = mutableListOf<Block>()
    private var listeners = mutableListOf<BlockListener>()

    fun getBlocks(): List<Block> {
        return blocks
    }

    fun removeBlock(block: Block) {
        val index = blocks.indexOfFirst { it.id == block.id }
        if (index != -1) {
            if (blocks[index].tabs > 0) blocks[index].tabs--
            else {
                blocks = ArrayList(blocks)
                blocks.removeAt(index)
                notifyChanges()
            }
        }
    }

    fun addTabBlock(block: Block) {
        val index = blocks.indexOfFirst { it.id == block.id }
        if (index != -1) blocks[index].tabs++
    }

    fun addBlock(block: Block) {
        blocks.add(block)
        blocks = ArrayList(blocks)
        notifyChanges()
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
}