package com.hits.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockElseBinding
import com.hits.myapplication.databinding.BlockForBinding
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockWhileBinding

interface BlockActionListener {
    fun onBlockDelete(block: Block)
    fun onBlockSwap(oldInd: Int, newInd: Int)
    fun onBlockTab(block: Block)
    fun onBlockUnTab(block: Block)
    fun onBlockEdit(block: Block)
}

class BlockDiffCallback(
    private val oldList: List<Block>,
    private val newList: List<Block>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id && old.type == new.type && old.tabs == new.tabs
    }

}

class BlockAdapter(
    val actionListener: BlockActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    var blocks = mutableListOf<Block>()
        set(newBlocks) {
            val diffCallback = BlockDiffCallback(field, newBlocks)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newBlocks
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val holder = when (viewType) {
            0 -> BlockListHolder(BlockListBinding.inflate(inflater, parent, false))
            1 -> BlockOHolder(BlockOBinding.inflate(inflater, parent, false))
            2 -> BlockOutHolder(BlockOutBinding.inflate(inflater, parent, false))
            3 -> BlockIfHolder(BlockIfBinding.inflate(inflater, parent, false))
            4 -> BlockWhileHolder(BlockWhileBinding.inflate(inflater, parent, false))
            5 -> BlockElseHolder(BlockElseBinding.inflate(inflater, parent, false))
            else -> BlockForHolder(BlockForBinding.inflate(inflater, parent, false))
        }
        holder.create(this)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]
        val newTabs = getNewTabs(block.tabs)

        @Suppress("NAME_SHADOWING")
        val holder = when (block.type) {
            0 -> holder as BlockListHolder
            1 -> holder as BlockOHolder
            2 -> holder as BlockOutHolder
            3 -> holder as BlockIfHolder
            4 -> holder as BlockWhileHolder
            5 -> holder as BlockElseHolder
            else -> holder as BlockForHolder
        }
        holder.bind(block, newTabs)
    }


    override fun getItemViewType(position: Int): Int {
        return blocks[position].type
    }

    override fun getItemCount(): Int = blocks.size

    override fun onClick(v: View) {
        val block = v.tag as Block
        actionListener.onBlockEdit(block)
    }

    private fun getNewTabs(numTabs: Int): String {
        var newTabs = ""
        for (i in 0 until numTabs) {
            newTabs += "●"
        }
        return newTabs
    }
}

