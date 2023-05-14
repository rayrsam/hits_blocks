package com.hits.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding

class BlockOHolder(
    val binding: BlockOBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockVHolder(
    val binding: BlockVBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockOutHolder(
    val binding: BlockOutBinding
) : RecyclerView.ViewHolder(binding.root)

class BlockIfHolder(
    val binding: BlockIfBinding
) : RecyclerView.ViewHolder(binding.root)