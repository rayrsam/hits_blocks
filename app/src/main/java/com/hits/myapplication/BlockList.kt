package com.hits.myapplication

import java.util.Collections

typealias BlockListener = (blocks : List<Block>) -> Unit

class BlockList {
    private var blocks = mutableListOf<Block>()
    private var listeners = mutableListOf<BlockListener>()

    fun getBlocks() : List<Block>{
        return blocks
    }

    fun removeBlock(block: Block){
        val index = blocks.indexOfFirst{it.id == block.id}
        if (index != -1) {
            if (blocks[index].tabs > 0) blocks[index].tabs--
            else {
                blocks = ArrayList(blocks)
                blocks.removeAt(index)
            }
        }
        notifyChanges()
    }
    fun addTabBlock(block: Block){
        val index = blocks.indexOfFirst{it.id == block.id}
        if (index != -1) blocks[index].tabs++
        notifyChanges()
    }

    fun addBlock(block: Block){
        blocks.add(block)
        notifyChanges()
    }

    fun  moveBLock(block: Block, moveBy : Int){
        val oldIndex = blocks.indexOfFirst { it.id == block.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= blocks.size) return
        blocks = ArrayList(blocks)
        Collections.swap(blocks, oldIndex, newIndex)
        notifyChanges()
    }

    fun swapBlock(oldInd : Int, newInd : Int){
        blocks = ArrayList(blocks)
        Collections.swap(blocks, oldInd, newInd)
        notifyChanges()
    }

    fun addListener(listener: BlockListener){
        listeners.add(listener)
        notifyChanges()
    }
    fun removeListener(listener: BlockListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach {it.invoke(blocks)}
    }


}