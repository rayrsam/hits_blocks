package com.hits.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockVBinding
import com.hits.myapplication.databinding.BlockWhileBinding
import com.hits.myapplication.databinding.MainActivityBinding
import com.hits.myapplication.interpretercommands.Interpreter


class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var adapter: BlockAdapterBinding

    private val blockList: BlockList
        get() = (applicationContext as App).blockList

    private val blockListener: BlockListener = {
        adapter.blocks = it.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BlockAdapterBinding(object : BlockActionListener {

            override fun onBlockDelete(block: Block) {
                blockList.removeBlock(block)
            }

            override fun onBlockSwap(oldInd: Int, newInd: Int) {
                blockList.swapBlock(oldInd, newInd)
            }

            override fun onBlockTab(block: Block) {
                blockList.addTabBlock(block)
            }

            override fun onBlockEdit(block: Block) {
                when (block.type) {
                    0 -> showListBlockDialog(block as ListBlock)
                    1 -> showOperBlockDialog(block as OperBlock)
                    2 -> showOutBlockDialog(block as OutBlock)
                    3 -> showIfBlockDialog(block as IfBlock)
                    3 -> showWhileBlockDialog(block as WhileBlock)
                }
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        blockList.addListener(blockListener)

        val itemTouchSwap = ItemTouchHelper(DragSwap(adapter))
        itemTouchSwap.attachToRecyclerView(binding.recyclerView)

        binding.addB1.setOnClickListener() { start() }

        registerForContextMenu(binding.addB2)

    }


    fun start() {
        adapter.notifyDataSetChanged()
        Interpreter.blockList = blockList.getBlocks()
        var output = ""
        for (str in Interpreter.executeCode()) output += str + "\n"
        binding.txRes.text = output
    }

    private var index = 0

    fun addBLock(type: Int) {
        var tabs = 0;
        val num = blockList.getBlocks().size

        if (num != 0) {
            val prev = blockList.getBlocks()[num - 1]
            tabs = prev.tabs
            if (prev.type == 3 || prev.type == 4) {
                tabs++
            }
        }

        when (type) {
            0 -> {
                val newBlock = ListBlock(index, tabs, type)
                showListBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            1 -> {
                val newBlock = OperBlock(index, tabs, type)
                showOperBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            2 -> {
                val newBlock = OutBlock(index, tabs, type)
                showOutBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            3 -> {
                val newBlock = IfBlock(index, tabs, type)
                showIfBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            else -> {
                val newBlock = WhileBlock(index, tabs, type)
                showWhileBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }
        }
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
            R.id.i0 -> addBLock(0)
            R.id.i1 -> addBLock(1)
            R.id.i2 -> addBLock(2)
            R.id.i3 -> addBLock(3)
            R.id.i4 -> addBLock(4)
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        blockList.removeListener(blockListener)
    }

    fun showOperBlockDialog(block: OperBlock) {
        val dialogBinding = BlockOBinding.inflate(layoutInflater)

        dialogBinding.left.setText(block.left)
        dialogBinding.right.setText(block.right)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.left = dialogBinding.left.text.toString()
                block.right = dialogBinding.right.text.toString()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") { _, _ -> {} }
            .create()
        dialog.show()
    }

    fun showListBlockDialog(block: ListBlock) {
        val dialogBinding = BlockListBinding.inflate(layoutInflater)

        dialogBinding.name.setText(block.name)
        dialogBinding.size.setText(block.size)
        dialogBinding.list.setText(block.list)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.name = dialogBinding.name.text.toString()
                block.size = dialogBinding.size.text.toString()
                block.list = dialogBinding.list.text.toString()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") { _, _ -> {} }
            .create()
        dialog.show()
    }

    fun showOutBlockDialog(block: OutBlock) {
        val dialogBinding = BlockOutBinding.inflate(layoutInflater)

        dialogBinding.out.setText(block.out)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.out = dialogBinding.out.text.toString()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") { _, _ -> {} }
            .create()
        dialog.show()
    }

    fun showIfBlockDialog(block: IfBlock) {
        val dialogBinding = BlockIfBinding.inflate(layoutInflater)

        dialogBinding.cond.setText(block.cond)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.cond = dialogBinding.cond.text.toString()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") { _, _ -> {} }
            .create()
        dialog.show()
    }

    fun showWhileBlockDialog(block: WhileBlock) {
        val dialogBinding = BlockWhileBinding.inflate(layoutInflater)

        dialogBinding.cond.setText(block.cond)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.cond = dialogBinding.cond.text.toString()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") { _, _ -> {} }
            .create()
        dialog.show()
    }

}

