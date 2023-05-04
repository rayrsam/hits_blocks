package com.hits.myapplication

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.myapplication.databinding.MainActivityBinding


class MainActivity : ComponentActivity() {
    var index = 1

    private lateinit var binding : MainActivityBinding
    private lateinit var adapter: BlockAdapter

    private val blockList:BlockList
        get() = (applicationContext as App).blockList

    private val blockListener : BlockListener = {
        adapter.blocks = it.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BlockAdapter(object : BlockActionListener{

            override fun onBlockDelete(block : Block){blockList.removeBlock(block)}

            override fun onTitleChange(block : Block, newT : String){}
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        blockList.addListener(blockListener)

        binding.addB1.setOnClickListener(){
            val newBlock = OutBlock(index, "$index", 0)
            blockList.addBlock(newBlock)
            index++
        }
        registerForContextMenu(binding.addB2)
    }
    fun addVB(){
        val newBlock = VarBlock(index, "$index", R.drawable.ussr)
        blockList.addBlock(newBlock)
        index++
    }
    fun addOB(){
        val newBlock = OperBlock(index, "$index", R.drawable.rus)
        blockList.addBlock(newBlock)
        index++
    }
    fun addOutB(){
        val newBlock = OutBlock(index, "$index", R.drawable.emp)
        blockList.addBlock(newBlock)
        index++
    }


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_item, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.i0 -> addVB()
            R.id.i1 -> addOB()
            R.id.i2 -> addOutB()
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        blockList.removeListener(blockListener)
    }
}

