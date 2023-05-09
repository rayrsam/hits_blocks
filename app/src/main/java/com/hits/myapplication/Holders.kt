package com.hits.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding

class HolderV (view : View) : RecyclerView.ViewHolder(view) {

}
class HolderO (view : View) : RecyclerView.ViewHolder(view) {

}
class HolderOut (view : View) : RecyclerView.ViewHolder(view) {

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