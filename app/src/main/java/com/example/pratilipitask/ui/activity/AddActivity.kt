package com.example.pratilipitask.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.toSpannable
import androidx.lifecycle.ViewModelProvider
import com.example.pratilipitask.MainActivity
import com.example.pratilipitask.R
import com.example.pratilipitask.data.database.ContentDatabase
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.data.entities.ImageMetaData
import com.example.pratilipitask.data.repo.DataRepo
import com.example.pratilipitask.databinding.ActivityAddBinding
import com.example.pratilipitask.ui.ui_utils.StyleDialog
import com.example.pratilipitask.ui.viewmodels.DataViewModel
import com.example.pratilipitask.utils.Constants
import com.example.pratilipitask.utils.DatabaseViewModelFactory
import com.example.pratilipitask.utils.UtilFunctions.fromHtml
import com.example.pratilipitask.utils.UtilFunctions.gone
import com.example.pratilipitask.utils.UtilFunctions.resize
import com.example.pratilipitask.utils.UtilFunctions.span
import com.example.pratilipitask.utils.UtilFunctions.toHtml
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class AddActivity() : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var metaDataList = mutableListOf<ImageMetaData>()
    private lateinit var job: Job
    private lateinit var binding: ActivityAddBinding
    private lateinit var viewModel: DataViewModel
    private var noteId = -1L
    private var bitmap: Bitmap? = null
    private var currentNote: Data? = null

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val stream = contentResolver.openInputStream(uri!!)
        val image = BitmapFactory.decodeStream(stream)
        setSpanImage(image.resize(250F,250F)!!)
    }

    private fun setSpanImage(image: Bitmap) {

        activeEditText { editText->
            editText?.let {
                val spannable = SpannableStringBuilder(editText.text).apply {
                    val startIndex = editText.selectionStart
                    val endIndex = editText.selectionStart+1
                    try {
                        setSpan(image.span(), startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } catch (e: Exception) {
                        setSpan(image.span(), editText.text!!.length-2,editText.text!!.length-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    }
                    ImageMetaData(
                        image = image,
                        isTitle = editText.id == R.id.data_title,
                        position = startIndex
                    ).apply { metaDataList.add(this) }
                }
                editText.text = spannable
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initClickListeners()


        noteId = intent.getLongExtra(Constants.INTENT_NOTE_ID, -1L)
        if(noteId != -1L) {
            binding.deleteBtn.visibility = View.VISIBLE
            loadNote(noteId)
        } else binding.deleteBtn.visibility = View.GONE

    }

    private fun loadNote(noteId: Long) {
        viewModel.fetchData()
        launch {
            viewModel.readAllData.collect {list->
                list.forEach{ data->
                    if(data.id == noteId) {
                        currentNote = data
                        binding.dataTitle.setText(data.title.fromHtml())
                        data.description?.fromHtml()?.apply {
                            if(isBlank()) binding.dataDesc.setHint(R.string.desc_hint)
                            else binding.dataDesc.setText(this)
                        }
                    }
                }
            }
        }

    }

    private fun initClickListeners() {

        binding.saveBtn.setOnClickListener {
            saveData()
        }

        activeEditText {
            it?.let {
                binding.galleryBtn.gone(false)
            }
            binding.galleryBtn.gone(true)
        }

        binding.galleryBtn.setOnClickListener {
            resultLauncher.launch("image/*")
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

        binding.deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(this@AddActivity)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteData(currentNote!!)
                    finish()
                }
                .setNegativeButton("No") { p0, _ -> p0?.dismiss() }
                .show()
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

    private fun saveData() {
        val title = binding.dataTitle.text
        val description = binding.dataDesc.text

        if(title.isNullOrEmpty()) binding.dataTitle.error = "Title cannot be empty"
        else {
            //addImageMetaData(title, description)
            if(noteId == -1L) viewModel.addNewData(title.toHtml()!!, description?.toHtml(), metaDataList)
            else viewModel.updateData(noteId, title.toHtml()!!, description?.toHtml(), metaDataList)
            onBackPressed()
        }
    }

    //To check if the current
    private fun addImageMetaData(title: Editable, description: Editable?) {
        metaDataList.forEach {
            if(it.isTitle) {
                val bool = title.toString().substring(it.position, it.position+1).toSpannable() is ImageSpan
                if(!bool) metaDataList.remove(it)
            } else {
                val bool = description?.toString()?.substring(it.position, it.position+1)?.toSpannable() is ImageSpan
                if(!bool) metaDataList.remove(it)
            }
        }
    }


    private fun init() {
        val dataSource = ContentDatabase.getDatabase(this).dataDao()
        val repo = DataRepo(dataSource)
        val factory = DatabaseViewModelFactory(repo, application)
        viewModel = ViewModelProvider(this, factory).get(DataViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        job = Job()
    }

    private fun activeEditText(active: (TextInputEditText?)->Unit) {
        when {
            binding.dataTitle.hasFocus() -> active(binding.dataTitle)
            binding.dataDesc.hasFocus() -> active(binding.dataDesc)
            else -> active(null)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        overridePendingTransition(R.transition.enter_anim, R.transition.exit_anim)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}