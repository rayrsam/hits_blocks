package com.hits.myapplication

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDelete(
    val adapter : BlockAdapterBinding
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented") // :)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val block = adapter.blocks[viewHolder.absoluteAdapterPosition]
        adapter.actionListener.onBlockDelete(block)
    }
}

class SwipeToUp(
    val adapter : BlockAdapterBinding
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented") // :)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val block = adapter.blocks[viewHolder.absoluteAdapterPosition]
        adapter.actionListener.onBlockMove(block, -1)
    }
}

class SwipeToDown(
    val adapter : BlockAdapterBinding
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented") // :)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val block = adapter.blocks[viewHolder.absoluteAdapterPosition]
        adapter.actionListener.onBlockMove(block, 1)
    }
}