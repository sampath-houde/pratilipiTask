package com.example.pratilipitask.data.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pratilipitask.utils.Constants

@Entity(tableName = Constants.DATABASE_NAME)
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String?
)
