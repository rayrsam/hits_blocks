@file:Suppress("NAME_SHADOWING")

package com.hits.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hits.myapplication.databinding.BlockElseBinding
import com.hits.myapplication.databinding.BlockForBinding
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockWhileBinding


abstract class BlockHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun create(parent: BlockAdapter)
    abstract fun bind(block: Block, newTabs: String)
}

class BlockOHolder(
    private val binding: BlockOBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.right.setOnClickListener(parent)
        binding.left.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as OperBlock
        with(binding) {

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
}

class BlockListHolder(
    private val binding: BlockListBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.name.setOnClickListener(parent)
        binding.size.setOnClickListener(parent)
        binding.list.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as ListBlock
        with(binding) {
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
}

class BlockOutHolder(
    private val binding: BlockOutBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.out.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as OutBlock
        with(binding) {
            root.tag = block
            out.tag = block

            out.focusable = View.NOT_FOCUSABLE

            tabs.text = newTabs
            out.setText(block.out)
        }
    }
}

class BlockIfHolder(
    private val binding: BlockIfBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.cond.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as IfBlock
        with(binding) {
            root.tag = block
            cond.tag = block

            cond.focusable = View.NOT_FOCUSABLE

            tabs.text = newTabs
            cond.setText(block.cond)
        }
    }
}

class BlockWhileHolder(
    private val binding: BlockWhileBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.cond.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as WhileBlock
        with(binding) {
            root.tag = block
            cond.tag = block

            tabs.text = newTabs
            cond.focusable = View.NOT_FOCUSABLE
            cond.setText(block.cond)
        }
    }
}

class BlockElseHolder(
    private val binding: BlockElseBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.cond.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as ElseBlock
        with(binding) {
            root.tag = block
            cond.tag = block

            tabs.text = newTabs
            if (block.cond.isBlank()) {
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
}

class BlockForHolder(
    private val binding: BlockForBinding
) : BlockHolder(binding) {
    override fun create(parent: BlockAdapter) {
        binding.root.setOnClickListener(parent)
        binding.predLeft.setOnClickListener(parent)
        binding.predRight.setOnClickListener(parent)
        binding.cond.setOnClickListener(parent)
        binding.postLeft.setOnClickListener(parent)
        binding.postRight.setOnClickListener(parent)
    }

    override fun bind(block: Block, newTabs: String) {
        val block = block as ForBlock
        with(binding) {
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