package com.hits.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.activity.ComponentActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hits.myapplication.databinding.BlockElseBinding
import com.hits.myapplication.databinding.BlockForBinding
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockWhileBinding
import com.hits.myapplication.databinding.ConsoleDialogBinding
import com.hits.myapplication.databinding.MainActivityBinding
import com.hits.myapplication.interpretercommands.Interpreter

class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var adapter: BlockAdapter
    private var output = ""
    private var index = 0

    private val blockList: BlockList
        get() = (applicationContext as App).blockList

    private val blockListener: BlockListener = {
        adapter.blocks = it.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BlockAdapter(object : BlockActionListener {

            override fun onBlockDelete(block: Block) {
                blockList.removeBlock(block)
            }

            override fun onBlockSwap(oldInd: Int, newInd: Int) {
                blockList.swapBlock(oldInd, newInd)
            }

            override fun onBlockTab(block: Block) {
                blockList.addTabBlock(block)
            }

            override fun onBlockUntab(block: Block) {
                blockList.removeTabBlock(block)
            }

            override fun onBlockEdit(block: Block) {
                when (block.type) {
                    0 -> showListBlockDialog(block as ListBlock)
                    1 -> showOperBlockDialog(block as OperBlock)
                    2 -> showOutBlockDialog(block as OutBlock)
                    3 -> showIfBlockDialog(block as IfBlock)
                    4 -> showWhileBlockDialog(block as WhileBlock)
                    5 -> showElseBlockDialog(block as ElseBlock)
                    6 -> showForBlockDialog(block as ForBlock)
                }
            }
        })
        val layoutManager = LinearLayoutManager(this)
        val itemTouchSwap = ItemTouchHelper(DragSwap(adapter))

        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            blockList.addListener(blockListener)
            itemTouchSwap.attachToRecyclerView(recyclerView)
            console.movementMethod = ScrollingMovementMethod()

            addB1.setOnClickListener { start() }
            addB2.setOnClickListener { showPopUpMenu(it) }
            consB.setOnClickListener { drawer.openDrawer(GravityCompat.START) }
        }

    }

    private fun start() {
        Interpreter.blockList = blockList.getBlocks()
        output = ""
        for (str in Interpreter.executeCode()) output += str + "\n"
        binding.console.text = output
        binding.drawer.openDrawer(GravityCompat.START)
    }

    private fun showPopUpMenu(view: View) {
        val popup = PopupMenu(view.context, view)

        popup.menu.add(0, 1, Menu.NONE, "Значение")
        popup.menu.add(0, 0, Menu.NONE, "Массив")
        popup.menu.add(0, 3, Menu.NONE, "If - Условие")
        popup.menu.add(0, 5, Menu.NONE, "Else - Условие")
        popup.menu.add(0, 4, Menu.NONE, "Цикл While")
        popup.menu.add(0, 6, Menu.NONE, "Цикл For")
        popup.menu.add(0, 2, Menu.NONE, "Вывод")

        popup.setOnMenuItemClickListener { addBLock(it.itemId) }
        popup.show()
    }

    private fun showBottomSheet(output: String) {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = ConsoleDialogBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        //dialogBinding.console.setText(output)
        dialog.show()
    }

    private fun addBLock(type: Int): Boolean {
        var tabs = 0
        val num = blockList.getBlocks().size

        if (num != 0) {
            val prev = blockList.getBlocks()[num - 1]
            tabs = prev.tabs
            if (prev.type >= 3) {
                tabs++
            }
        }

        when (type) {
            0 -> {
                val newBlock = ListBlock(index, tabs)
                showListBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            1 -> {
                val newBlock = OperBlock(index, tabs)
                blockList.addBlock(newBlock)
                showOperBlockDialog(newBlock)
            }

            2 -> {
                val newBlock = OutBlock(index, tabs)
                showOutBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            3 -> {
                val newBlock = IfBlock(index, tabs)
                showIfBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            4 -> {
                val newBlock = WhileBlock(index, tabs)
                showWhileBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            5 -> {
                val newBlock = ElseBlock(index, tabs)
                showElseBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }

            else -> {
                val newBlock = ForBlock(index, tabs)
                showForBlockDialog(newBlock)
                blockList.addBlock(newBlock)
            }
        }
        index++
        return true
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

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
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

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
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

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
            .create()
        dialog.show()
    }

    fun showIfBlockDialog(block: IfBlock) {
        val dialogBinding = BlockIfBinding.inflate(layoutInflater)

        dialogBinding.cond.setText(block.cond)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.cond = dialogBinding.cond.text.toString()

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
            .create()
        dialog.show()
    }

    fun showElseBlockDialog(block: ElseBlock) {
        val dialogBinding = BlockElseBinding.inflate(layoutInflater)

        dialogBinding.cond.setText(block.cond)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.cond = dialogBinding.cond.text.toString()

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
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

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
            .create()
        dialog.show()
    }

    fun showForBlockDialog(block: ForBlock) {
        val dialogBinding = BlockForBinding.inflate(layoutInflater)

        dialogBinding.predLeft.setText(block.predLeft)
        dialogBinding.predRight.setText(block.predRight)
        dialogBinding.cond.setText(block.cond)
        dialogBinding.postLeft.setText(block.postLeft)
        dialogBinding.postRight.setText(block.postRight)

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->
                block.predLeft = dialogBinding.predLeft.text.toString()
                block.predRight = dialogBinding.predRight.text.toString()
                block.cond = dialogBinding.cond.text.toString()
                block.postLeft = dialogBinding.postLeft.text.toString()
                block.postRight = dialogBinding.postRight.text.toString()

                val id = blockList.getBlocks().indexOfFirst { it.id == block.id }
                adapter.notifyItemChanged(id)
            }
            .setNegativeButton("Отмена") { _, _ -> run {} }
            .setNeutralButton("Удалить") { _, _ ->
                adapter.actionListener.onBlockDelete(block)
            }
            .create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        blockList.removeListener(blockListener)
    }

}

