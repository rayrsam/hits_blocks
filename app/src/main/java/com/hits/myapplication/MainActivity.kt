package com.hits.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.myapplication.databinding.DialogBinding
import com.hits.myapplication.databinding.MainActivityBinding


class MainActivity : ComponentActivity() {
    private lateinit var binding : MainActivityBinding
    private lateinit var adapter: BlockAdapterBinding

    private val blockList:BlockList
        get() = (applicationContext as App).blockList

    private val blockListener : BlockListener = {
        adapter.blocks = it.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BlockAdapterBinding(object : BlockActionListener{

            override fun onBlockDelete(block : Block){blockList.removeBlock(block)}
            override fun onBlockSwap(oldInd : Int, newInd : Int) {blockList.swapBlock(oldInd, newInd)}
            override fun onBlockTab(block: Block) {blockList.addTabBlock(block)}
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        blockList.addListener(blockListener)

        //val itemTouchDelete = ItemTouchHelper(SwipeToDelete(adapter))
        //itemTouchDelete.attachToRecyclerView(binding.recyclerView)

        val itemTouchSwap = ItemTouchHelper(DragSwap(adapter))
        itemTouchSwap.attachToRecyclerView(binding.recyclerView)

        binding.addB1.setOnClickListener() {start()}

        registerForContextMenu(binding.addB2)

    }



    fun start(){
        adapter.notifyDataSetChanged()
        binding.txRes.text = blockList.getBlocks().size.toString()
    }


    private var index = 0

    fun addVB(){
        val newBlock = VarBlock(index)
        blockList.addBlock(newBlock)
        index++
    }
    fun addOB(){
        val newBlock = OperBlock(index)
        blockList.addBlock(newBlock)
        index++
    }
    fun addOutB(){
        val newBlock = OutBlock(index)
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

