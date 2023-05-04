package com.hits.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


interface BlockActionListener{

    fun onBlockDelete(block : Block)

    fun onTitleChange(block : Block, newT : String)
}

class BlockAdapter(
    private val actionListener: BlockActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    var blocks = mutableListOf<Block>()
        set(newBlock) {
            field = newBlock
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            //0 -> {
            //    val view = LayoutInflater.from(parent.context).inflate(R.layout.block, parent, false)
            //    //view.findViewById<Button>(R.id.delB).setOnClickListener(this)
            //    //view.findViewById<Button>(R.id.upB).setOnClickListener(this)
            //    //view.findViewById<Button>(R.id.downB).setOnClickListener(this)
            //    return HolderV(view);
            //}
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.block_o, parent, false)
                //view.findViewById<Button>(R.id.delB).setOnClickListener(this)
                //view.findViewById<Button>(R.id.upB).setOnClickListener(this)
                //view.findViewById<Button>(R.id.downB).setOnClickListener(this)
                return HolderO(view);
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.block_v, parent, false)
                //view.findViewById<Button>(R.id.delB).setOnClickListener(this)
                //view.findViewById<Button>(R.id.upB).setOnClickListener(this)
                //view.findViewById<Button>(R.id.downB).setOnClickListener(this)
                return HolderV(view);
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]
    }


    override fun getItemViewType(position: Int) : Int{
        return blocks[position].type
    }


    override fun getItemCount(): Int = blocks.size

    override fun onClick(v: View) {
        val block = blocks[v.tag as Int]

        when(v.id){
            R.id.delB -> actionListener.onBlockDelete(block)
        }
    }
}