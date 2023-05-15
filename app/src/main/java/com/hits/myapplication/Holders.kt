package com.hits.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding
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