package com.hits.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding
import org.w3c.dom.Text

interface BlockActionListener{
    fun onBlockDelete(block : Block)
    fun onBlockMove(block: Block, moveBy: Int)
    fun onBlockDetails(block: Block)
}

class blockDiffCallback(
    private val oldlist : List<Block>,
    private val newlist : List<Block>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldlist.size

    override fun getNewListSize(): Int = newlist.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition].id == newlist[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldlist[oldItemPosition]
        val new = newlist[newItemPosition]
        val res : Boolean = old.id == new.id && old.type == new.type
        return res
    }

}

class BlockOHolder(
    val binding : BlockOBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockVHolder(
    val binding : BlockVBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockOutHolder(
    val binding : BlockOutBinding
) : RecyclerView.ViewHolder(binding.root)


class BlockAdapterBinding (
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

        when(viewType){
            0 -> {
                val binding = BlockVBinding.inflate(inflater, parent, false)
                binding.delB.setOnClickListener(this)
                binding.upB.setOnClickListener(this)
                binding.downB.setOnClickListener(this)
                return BlockVHolder(binding);
            }
            1 -> {
                val binding = BlockOBinding.inflate(inflater, parent, false)
                binding.delB.setOnClickListener(this)
                binding.upB.setOnClickListener(this)
                binding.downB.setOnClickListener(this)
                return BlockOHolder(binding);
            }
            else -> {
                val binding = BlockOutBinding.inflate(inflater, parent, false)
                binding.delB.setOnClickListener(this)
                binding.upB.setOnClickListener(this)
                binding.downB.setOnClickListener(this)
                return BlockOutHolder(binding);
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var block = blocks[position]

        when (block.type){
            0 -> {
                val block = block as VarBlock
                with((holder as BlockVHolder).binding) {
                    delB.tag = block
                    upB.tag = block
                    downB.tag = block

                    block.left = left.text.toString()
                    block.right = right.text.toString()
                }
            }
            1 -> {
                val block = block as OperBlock
                with((holder as BlockOHolder).binding) {
                    delB.tag = block
                    upB.tag = block
                    downB.tag = block

                    block.left = left.text.toString()
                    block.right = right.text.toString()
                }
            }
            else -> {
                val block = block as OutBlock
                with((holder as BlockOutHolder).binding) {
                    delB.tag = block
                    upB.tag = block
                    downB.tag = block

                    block.left = outTx.text.toString()

                }
            }
        }

    }

    override fun getItemViewType(position: Int) : Int{
        return blocks[position].type
    }


    override fun getItemCount(): Int = blocks.size

    override fun onClick(v: View) {
        val block = v.tag as Block

        when(v.id){
            R.id.delB -> {
                notifyDataSetChanged()
                actionListener.onBlockDetails(block)
            }
            R.id.upB -> actionListener.onBlockMove(block, -1)
            R.id.downB -> actionListener.onBlockMove(block, 1)
        }
    }
}

