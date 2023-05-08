package com.hits.myapplication

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.myapplication.databinding.MainActivityBinding


class MainActivity : ComponentActivity() {
    var index = 1

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
            override fun onBlockDetails(block: Block) {onDetails(block)}
            override fun onBlockMove(block: Block, moveBy: Int) {blockList.moveBLock(block, moveBy)}
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        blockList.addListener(blockListener)

        val itemTouchDelete = ItemTouchHelper(SwipeToDelete(adapter))
        itemTouchDelete.attachToRecyclerView(binding.recyclerView)

        val itemTouchUp = ItemTouchHelper(SwipeToUp(adapter))
        itemTouchUp.attachToRecyclerView(binding.recyclerView)

        val itemTouchDown = ItemTouchHelper(SwipeToDown(adapter))
        itemTouchDown.attachToRecyclerView(binding.recyclerView)


        binding.addB1.setOnClickListener() {start()}

        registerForContextMenu(binding.addB2)

    }

    fun start(){
        binding.txRes.text = blockList.getBlocks().size.toString()
    }
    fun onDetails(block: Block){
        when (block.type){
            0 -> {
                val block = block as VarBlock
                val res = block.left + " = " + block.right
                binding.txRes.text = res
            }
            1 -> {
                val block = block as OperBlock
                val res = block.left + " = " + block.right
                binding.txRes.text = res
            }
            2 ->{
                val block = block as OutBlock
                val res = block.left
                binding.txRes.text = res
            }
        }

    }

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

