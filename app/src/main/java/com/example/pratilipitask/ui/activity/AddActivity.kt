package com.example.pratilipitask.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pratilipitask.MainActivity
import com.example.pratilipitask.data.database.ContentDatabase
import com.example.pratilipitask.data.repo.DataRepo
import com.example.pratilipitask.databinding.ActivityAddBinding
import com.example.pratilipitask.ui.ui_utils.StyleDialog
import com.example.pratilipitask.ui.viewmodels.DataViewModel
import com.example.pratilipitask.utils.Constants
import com.example.pratilipitask.utils.DatabaseViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var viewModel: DataViewModel
    private var noteId = -1L
    var isBold = false
    var oldString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        oldString = binding.dataTitle.text.toString()

        initData()
        initClickListeners()




        noteId = intent.getLongExtra(Constants.INTENT_NOTE_ID, -1L)
        if(noteId != -1L) {
            binding.deleteBtn.visibility = View.VISIBLE
            loadNote(noteId)
        } else binding.deleteBtn.visibility = View.GONE

    }

    private fun loadNote(noteId: Long) {
        viewModel.fetchData()
        viewModel.readAllData.observe(this) {list->
            list.forEach{data->
                if(data.id == noteId) {
                    binding.dataTitle.setText(data.title)
                    binding.dataDesc.setText(data.description)

                    binding.deleteBtn.setOnClickListener {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Delete")
                            .setMessage("Do you want to delete?")
                            .setPositiveButton("Yes"
                            ) { _, _ ->
                                viewModel.deleteData(data)
                                finish()
                            }
                            .setNegativeButton("No") { p0, _ -> p0?.dismiss() }
                            .show()
                    }
                }
            }
        }
    }

    private fun initClickListeners() {



        binding.saveBtn.setOnClickListener {

            val title = binding.dataTitle.text.toString().trim()
            val description = binding.dataDesc.text.toString().trim()


            if(title.isNullOrEmpty()) binding.dataTitle.error = "Title cannot be empty"
            else {
                if(noteId == -1L) viewModel.addNewData(title, description)
                else viewModel.updateData(noteId, title, description)
                onBackPressed()
            }
        }


        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                this.putExtra(
                    Intent.EXTRA_TEXT,
                    "Title: ${binding.dataTitle.text.toString()}"
                )
                this.type = "text/plain"
            }
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.dataDesc.let { editText->
            editText.setOnLongClickListener {
                StyleDialog(this, editText).apply {
                    showDialog()
                }
                true
            }
        }

        binding.dataTitle.let { editText->
            editText.setOnLongClickListener {
                StyleDialog(this, editText).apply {
                    showDialog()
                }
                true
            }
        }



    }


    private fun initData() {
        val dataSource = ContentDatabase.getDatabase(this).dataDao()
        val repo = DataRepo(dataSource)
        val factory = DatabaseViewModelFactory(repo, application)
        viewModel = ViewModelProvider(this, factory).get(DataViewModel::class.java)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }

}