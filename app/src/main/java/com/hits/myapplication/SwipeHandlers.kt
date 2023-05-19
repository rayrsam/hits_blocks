package com.hits.myapplication

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class DragSwap(
    val adapter: BlockAdapterBinding
) : ItemTouchHelper.Callback() {

    val DEFAULT_DRAG_ANIMATION_DURATION = 100
    val DEFAULT_SWIPE_ANIMATION_DURATION = 250


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

   //override fun onMoved(
   //    recyclerView: RecyclerView,
   //    viewHolder: RecyclerView.ViewHolder,
   //    fromPos: Int,
   //    target: RecyclerView.ViewHolder,
   //    toPos: Int,
   //    x: Int,
   //    y: Int
   //) {
   //    for (i in fromPos until  toPos){
   //        adapter.notifyItemChanged(i)
   //    }
   //    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
   //}

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val ind = viewHolder.absoluteAdapterPosition
        val block = adapter.blocks[ind]

        when (direction) {
            ItemTouchHelper.LEFT -> {
                adapter.actionListener.onBlockDelete(block)
                adapter.notifyItemChanged(ind)
            }

            ItemTouchHelper.RIGHT -> {
                adapter.actionListener.onBlockTab(block)
                adapter.notifyItemChanged(ind)
            }
        }
    }
}