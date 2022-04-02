package com.example.pratilipitask.ui.ui_utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.example.pratilipitask.R
import com.example.pratilipitask.databinding.EditTextDialogBinding
import com.example.pratilipitask.ui.activity.AddActivity
import com.example.pratilipitask.utils.UtilFunctions.toast
import com.google.android.material.textfield.TextInputEditText

class StyleDialog(val context: Context, val editText: TextInputEditText) {

    private var dialogBinding: EditTextDialogBinding
    private var builder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    private var inputText = editText.text.toString()

    object Style {
        val BOLD = StyleSpan(Typeface.BOLD)
        val ITALIC = StyleSpan(Typeface.ITALIC)
        val UNDERLINE = UnderlineSpan()
        val STRIKETHROUGH = StrikethroughSpan()
    }


    init {
        val inflater = (context as AddActivity).layoutInflater
        dialogBinding = EditTextDialogBinding.inflate(inflater)

        if(builder == null) {
            builder = AlertDialog.Builder(context).apply {
                this.setView(dialogBinding.root)
                dialog = create()
                setCancelable(false)
            }
        }

        dialogBinding.noteText.text = inputText
        dialogBinding.dropDown.setOnClickListener {
            showMenu(it, R.menu.styles)
        }
        dialogBinding.negativeBtn.setOnClickListener { dialog?.dismiss() }
        dialogBinding.positiveBtn.setOnClickListener {
            if(checkConditions()) {
                styleText()
                dialog?.dismiss()
            }
        }

    }

    private fun styleText() {
        try {
            val startIndex = dialogBinding.startIndex.text.toString().toInt()-1
            val endIndex = dialogBinding.endIndex.text.toString().toInt()-1

            val spannable = SpannableStringBuilder(inputText)
            var style: Any? = null
            when(dialogBinding.dropDown.text.toString()) {
                "Bold" -> style = Style.BOLD
                "Italic" -> style = Style.ITALIC
                "Underline" -> style = Style.UNDERLINE
                "Strikethrough" -> style = Style.STRIKETHROUGH
            }
            spannable.setSpan(style, startIndex,endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            editText.text = spannable
        } catch (e: Exception) {
            dialogBinding.root.toast("Enter correct values")
        }
    }

    fun showDialog() = dialog?.show()


    private fun checkConditions(): Boolean {

        if(dialogBinding.startIndex.text.isNullOrEmpty()) {
            dialogBinding.startIndex.error = "Cannot be empty"
        }

        if(dialogBinding.endIndex.text.isNullOrEmpty()) {
            dialogBinding.endIndex.error = "Cannot be empty"
        }

        if(dialogBinding.dropDown.text.equals("Style")) {
            dialogBinding.dropDown.error = "Select a Style"
        }

        return (!dialogBinding.startIndex.text.isNullOrEmpty() &&
                !dialogBinding.endIndex.text.isNullOrEmpty() && !dialogBinding.dropDown.text.equals("Style"))
    }

    private fun showMenu(view: View, @MenuRes styles: Int) {
        PopupMenu(context, view).apply {
            menuInflater.inflate(styles, this.menu)
            setOnMenuItemClickListener { menuItem->
                (view as AutoCompleteTextView).setText(menuItem.title)
                true
            }
            show()
        }
    }

}