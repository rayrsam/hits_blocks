package com.hits.myapplication

typealias BlockListener = (blocks : List<Block>) -> Unit

class BlockList {
    private var blocks = mutableListOf<Block>()
    private var listeners = mutableListOf<BlockListener>()

    init {}

    fun getBlocks() : List<Block>{
        return blocks
    }

    fun removeBlock(block: Block){
        val index = blocks.indexOfFirst{it.id == block.id}
        if (index != -1) blocks.removeAt(index)
        notifyChanges()
    }

    fun addBlock(block: Block){
        blocks.add(block)
        notifyChanges()
    }

    fun addAfter (block: Block){
        TODO()
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