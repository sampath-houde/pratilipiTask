package com.example.pratilipitask.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
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

    fun View.toast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }


}


