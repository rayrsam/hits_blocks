package com.hits.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding

interface BlockActionListener {
    fun onBlockDelete(block: Block)
    fun onBlockSwap(oldInd: Int, newInd: Int)
    fun onBlockTab(block: Block)
    fun onBlockEdit(block: Block)
}

class blockDiffCallback(
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
        val res: Boolean = old.id == new.id && old.type == new.type && old.tabs == new.tabs
        return res
    }

}

class BlockAdapterBinding(
    val actionListener: BlockActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    var blocks = mutableListOf<Block>()
        set(newBlocks) {
            val diffCallback = blockDiffCallback(field, newBlocks)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newBlocks
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            0 -> { //Variable
                val binding = BlockVBinding.inflate(inflater, parent, false)
                binding.downB.setOnClickListener(this)
                return BlockVHolder(binding);
            }

            1 -> { //Operation
                val binding = BlockOBinding.inflate(inflater, parent, false)
                binding.downB.setOnClickListener(this)
                return BlockOHolder(binding);
            }

            2 -> { //Output
                val binding = BlockOutBinding.inflate(inflater, parent, false)
                binding.downB.setOnClickListener(this)
                return BlockOutHolder(binding);
            }

            else -> { //if
                val binding = BlockIfBinding.inflate(inflater, parent, false)
                binding.downB.setOnClickListener(this)
                return BlockIfHolder(binding);
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]

        var newTabs = "";
        for (i in 0 until block.tabs) {
            newTabs += " ● "
        }

        when (block.type) {
            0 -> { //Variable
                val block = block as VarBlock
                with((holder as BlockVHolder).binding) {
                    left.setEnabled(false);
                    right.setEnabled(false);

                    downB.tag = block

                    tabs.setText(newTabs)
                    left.setText(block.left)
                    right.setText(block.right)
                }
            }

            1 -> { //Operation
                val block = block as OperBlock
                with((holder as BlockOHolder).binding) {
                    left.setEnabled(false);
                    right.setEnabled(false);

                    downB.tag = block

                    tabs.setText(newTabs)
                    left.setText(block.left)
                    right.setText(block.right)
                }
            }

            2 -> { //Output
                val block = block as OutBlock
                with((holder as BlockOutHolder).binding) {
                    out.setEnabled(false);

                    downB.tag = block

                    tabs.setText(newTabs)
                    out.setText(block.out)
                }
            }

            else -> { //if
                val block = block as IfBlock
                with((holder as BlockIfHolder).binding) {
                    cond.setEnabled(false);

                    downB.tag = block

                    tabs.setText(newTabs)
                    cond.setText(block.cond)
                }
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


}

