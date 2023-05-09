package com.hits.myapplication

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class DragSwap(
    val adapter : BlockAdapterBinding
) : ItemTouchHelper.Callback(){

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swapFlag = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(dragFlag, swapFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.actionListener.onBlockSwap(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                val block = adapter.blocks[viewHolder.absoluteAdapterPosition]
                adapter.actionListener.onBlockDelete(block)
                adapter.notifyDataSetChanged()
            }
            ItemTouchHelper.RIGHT -> {
                val block = adapter.blocks[viewHolder.absoluteAdapterPosition]
                adapter.actionListener.onBlockTab(block)
                adapter.notifyDataSetChanged()
            }
        }
    }
}