package com.hits.myapplication

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hits.myapplication.databinding.ActionsDialogBinding
import com.hits.myapplication.databinding.BlockElseBinding
import com.hits.myapplication.databinding.BlockForBinding
import com.hits.myapplication.databinding.BlockIfBinding
import com.hits.myapplication.databinding.BlockListBinding
import com.hits.myapplication.databinding.BlockOBinding
import com.hits.myapplication.databinding.BlockOutBinding
import com.hits.myapplication.databinding.BlockWhileBinding
import com.hits.myapplication.databinding.MainActivityBinding
import com.hits.myapplication.interpretercommands.Interpreter

class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var adapter: BlockAdapter
    private var output = ""

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

            override fun onBlockUnTab(block: Block) {
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

            StartBut.setOnClickListener { start() }
            AddBut.setOnClickListener { showPopUpMenu(it) }
            OptBut.setOnClickListener { showBottomSheet() }

            ConsBut.setOnClickListener { drawer.openDrawer(GravityCompat.START) }
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

        popup.setOnMenuItemClickListener { onBlockAdd(it.itemId) }
        popup.show()
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = ActionsDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            ClearBut.setOnClickListener { blockList.clearBlocks(); dialog.dismiss() }
            SaveBut.setOnClickListener { saveLauncher.launch("new_alg.stylescript"); dialog.dismiss() }
            LoadBut.setOnClickListener { loadLauncher.launch(arrayOf("text/plain")); dialog.dismiss() }
        }
        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    private fun onBlockAdd(type: Int): Boolean {
        when (type) {
            0 -> showListBlockDialog(blockList.createBlock(type) as ListBlock)
            1 -> showOperBlockDialog(blockList.createBlock(type) as OperBlock)
            2 -> showOutBlockDialog(blockList.createBlock(type) as OutBlock)
            3 -> showIfBlockDialog(blockList.createBlock(type) as IfBlock)
            4 -> showWhileBlockDialog(blockList.createBlock(type) as WhileBlock)
            5 -> showElseBlockDialog(blockList.createBlock(type) as ElseBlock)
            else -> showForBlockDialog(blockList.createBlock(type) as ForBlock)
        }
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

    private val loadLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            try {
                uri?.let { openFile(it) }
            } catch (e: Exception) {
                showError(e)
            }
        }
    private val saveLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { result ->
            try {
                result?.let { saveFile(it) }
            } catch (e: Exception) {
                showError(e)
            }
        }

    private fun openFile(uri: Uri) {
        val data = contentResolver.openInputStream(uri)?.use {
            String(it.readBytes())
        } ?: throw IllegalStateException()
        blockList.readFromFile(data)
    }

    private fun saveFile(uri: Uri) {
        contentResolver.openOutputStream(uri)?.use {
            val bytes = blockList.writeToFile().toByteArray()
            it.write(bytes)
        } ?: throw IllegalStateException()
    }

    private fun showError(e: Exception) {
        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
    }
}

