package com.example.pratilipitask.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pratilipitask.data.dao.DataDao
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.utils.Constants
import com.example.pratilipitask.utils.UtilFunctions

@TypeConverters(UtilFunctions.BitmapConvertor::class, UtilFunctions.ImageMetaDataConvertor::class, UtilFunctions.ImageMetaDataListConvertor::class)
@Database(entities = [Data::class], version = 1, exportSchema = false )
abstract class ContentDatabase: RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var INSTANCE: ContentDatabase? = null

        fun getDatabase(context: Context): ContentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContentDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}