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
            0 -> { //List
                val binding = BlockListBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.name.setOnClickListener(this)
                binding.size.setOnClickListener(this)
                binding.list.setOnClickListener(this)
                return BlockListHolder(binding);
            }

            1 -> { //Operation
                val binding = BlockOBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.right.setOnClickListener(this)
                binding.left.setOnClickListener(this)
                return BlockOHolder(binding);
            }

            2 -> { //Output
                val binding = BlockOutBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.out.setOnClickListener(this)
                return BlockOutHolder(binding);
            }

            3 -> { //if
                val binding = BlockIfBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.cond.setOnClickListener(this)
                return BlockIfHolder(binding);
            }

            4 -> { //while
                val binding = BlockWhileBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.cond.setOnClickListener(this)
                return BlockWhileHolder(binding);
            }

            5 -> { //else
                val binding = BlockElseBinding.inflate(inflater, parent, false)
                //binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.cond.setOnClickListener(this)
                return BlockElseHolder(binding);
            }

            else -> { //for
                val binding = BlockForBinding.inflate(inflater, parent, false)
                binding.downB.setOnClickListener(this)
                binding.root.setOnClickListener(this)
                binding.predLeft.setOnClickListener(this)
                binding.predRight.setOnClickListener(this)
                binding.cond.setOnClickListener(this)
                binding.postLeft.setOnClickListener(this)
                binding.postRight.setOnClickListener(this)
                return BlockForHolder(binding);
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]

        var newTabs = "";
        for (i in 0 until block.tabs) {
            newTabs += "●"
        }

        when (block.type) {
            0 -> { //List
                val block = block as ListBlock
                with((holder as BlockListHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    name.tag = block
                    size.tag = block
                    list.tag = block

                    name.focusable = View.NOT_FOCUSABLE
                    size.focusable = View.NOT_FOCUSABLE
                    list.focusable = View.NOT_FOCUSABLE

                    tabs.text = newTabs
                    name.setText(block.name)
                    size.setText(block.size)
                    list.setText(block.list)

                }
            }

            1 -> { //Operation
                val block = block as OperBlock
                with((holder as BlockOHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    left.tag = block
                    right.tag = block

                    left.focusable = View.NOT_FOCUSABLE
                    right.focusable = View.NOT_FOCUSABLE

                    tabs.text = newTabs
                    left.setText(block.left)
                    right.setText(block.right)
                }
            }

            2 -> { //Output
                val block = block as OutBlock
                with((holder as BlockOutHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    out.tag = block

                    out.focusable = View.NOT_FOCUSABLE

                    tabs.text = newTabs
                    out.setText(block.out)
                }
            }

            3 -> { //if
                val block = block as IfBlock
                with((holder as BlockIfHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    cond.tag = block
                    
                    cond.focusable = View.NOT_FOCUSABLE

                    tabs.text = newTabs
                    cond.setText(block.cond)
                }
            }

            4 -> { //While
                val block = block as WhileBlock
                with((holder as BlockWhileHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    cond.tag = block
                    
                    tabs.text = newTabs
                    cond.focusable = View.NOT_FOCUSABLE
                    cond.setText(block.cond)
                }
            }

            5 -> { // Else
                val block = block as ElseBlock
                with((holder as BlockElseHolder).binding) {
                    //downB.tag = block
                    root.tag = block
                    cond.tag = block

                    tabs.text = newTabs
                    if (block.cond?.isBlank() == true) {
                        cond.visibility = View.INVISIBLE
                        textIf.visibility = View.INVISIBLE
                    } else {
                        cond.visibility = View.VISIBLE
                        textIf.visibility = View.VISIBLE
                        cond.focusable = View.NOT_FOCUSABLE
                        cond.setText(block.cond)
                    }
                }
            }

            else -> { //for
                val block = block as ForBlock
                with((holder as BlockForHolder).binding) {
                    downB.tag = block
                    root.tag = block
                    predLeft.tag = block
                    predRight.tag = block
                    cond.tag = block
                    postLeft.tag = block
                    postRight.tag = block

                    predLeft.focusable = View.NOT_FOCUSABLE
                    predRight.focusable = View.NOT_FOCUSABLE
                    cond.focusable = View.NOT_FOCUSABLE
                    postLeft.focusable = View.NOT_FOCUSABLE
                    postRight.focusable = View.NOT_FOCUSABLE

                    tabs.text = newTabs
                    predLeft.setText(block.predLeft)
                    predRight.setText(block.predRight)
                    cond.setText(block.cond)
                    postLeft.setText(block.postLeft)
                    postRight.setText(block.postRight)
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

