package com.hits.myapplication

import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockElseBinding
import com.hits.myapplication.databinding.BlockForBinding
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockWhileBinding

class BlockOHolder(
    val binding: BlockOBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockListHolder(
    val binding: BlockListBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockOutHolder(
    val binding: BlockOutBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockIfHolder(
    val binding: BlockIfBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockWhileHolder(
    val binding: BlockWhileBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockElseHolder(
    val binding: BlockElseBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockForHolder(
    val binding: BlockForBinding
) : RecyclerView.ViewHolder(binding.root)