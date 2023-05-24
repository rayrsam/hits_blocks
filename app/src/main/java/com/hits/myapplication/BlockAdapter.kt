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
    fun onBlockUntab(block: Block)
    fun onBlockEdit(block: Block)
}

class BlockDiffCallback(
    private val oldlist: List<Block>,
    private val newlist: List<Block>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldlist.size

    override fun getNewListSize(): Int = newlist.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition].id == newlist[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldlist[oldItemPosition]
        val new = newlist[newItemPosition]
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
        lateinit var block: Block
        val newTabs = getNewTabs(position)

        when (blocks[position].type) {
            0 -> { //List
                block = blocks[position] as ListBlock
                (holder as BlockListHolder).bind(block, newTabs)
            }

            1 -> { //Operation
                block = blocks[position] as OperBlock
                (holder as BlockOHolder).bind(block, newTabs)
            }

            2 -> { //Output
                block = blocks[position] as OutBlock
                (holder as BlockOutHolder).bind(block, newTabs)
            }

            3 -> { //if
                block = blocks[position] as IfBlock
                (holder as BlockIfHolder).bind(block, newTabs)
            }

            4 -> { //While
                block = blocks[position] as WhileBlock
                (holder as BlockWhileHolder).bind(block, newTabs)
            }

            5 -> { // Else
                block = blocks[position] as ElseBlock
                (holder as BlockElseHolder).bind(block, newTabs)
            }

            else -> { //for
                block = blocks[position] as ForBlock
                (holder as BlockForHolder).bind(block, newTabs)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return blocks[position].type
    }

    override fun getItemCount(): Int = blocks.size

    override fun onClick(v: View) {
        val block = v.tag as Block
        actionListener.onBlockEdit(block)
    }

    private fun getNewTabs(position: Int): String {
        var newTabs = ""
        for (i in 0 until blocks[position].tabs) {
            newTabs += "●"
        }
        return newTabs
    }
}

