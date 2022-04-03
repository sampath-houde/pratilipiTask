package com.example.pratilipitask.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

object UtilFunctions {

    public class BitmapConvertor {

        @TypeConverter
        fun fromBitmap(bitmap: Bitmap?) : ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            return outputStream.toByteArray()
        }

        @TypeConverter
        fun toBitmap(byteArray: ByteArray) : Bitmap? {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    fun View.gone(boolean: Boolean) {
        if(boolean) View.GONE
        else View.VISIBLE
    }

    fun Editable.toHtml(): String? {
        return Html.toHtml(this)
    }

    fun String.fromHtml(): Spanned {
        return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    fun View.toast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    fun Bitmap.span(): ImageSpan {
        return ImageSpan(this, ImageSpan.ALIGN_BASELINE)
    }

    fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Bitmap.resize(newWidth: Float, newHeight: Float): Bitmap? {
        val width: Int = this.width
        val height: Int = this.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(
            this, 0, 0, width, height, matrix, false)
        this.recycle()
        return resizedBitmap
    }


}


