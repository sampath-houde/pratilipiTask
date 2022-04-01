package com.example.pratilipitask.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.pratilipitask.data.entities.Data
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(data: Data)

    @Query("SELECT * FROM data_table ORDER BY id DESC")
    fun readAllData(): List<Data>

    @Update
    suspend fun updateData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)
}