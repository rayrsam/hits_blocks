package com.hits.myapplication

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class DragSwap(
    private val adapter: BlockAdapter
) : ItemTouchHelper.Callback() {

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
        adapter.actionListener.onBlockSwap(
            viewHolder.absoluteAdapterPosition,
            target.absoluteAdapterPosition
        )
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val ind = viewHolder.absoluteAdapterPosition
        val block = adapter.blocks[ind]

        when (direction) {
            ItemTouchHelper.LEFT -> {
                adapter.actionListener.onBlockUntab(block)
                adapter.notifyItemChanged(ind)
            }

            ItemTouchHelper.RIGHT -> {
                adapter.actionListener.onBlockTab(block)
                adapter.notifyItemChanged(ind)
            }
        }
    }
}