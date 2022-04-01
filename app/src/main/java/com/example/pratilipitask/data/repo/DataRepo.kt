package com.example.pratilipitask.data.repo

import androidx.lifecycle.LiveData
import com.example.pratilipitask.data.dao.DataDao
import com.example.pratilipitask.data.entities.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataRepo(private val dao: DataDao) {

    val readAllData: Flow<List<Data>>
        get() = flow {
            emit(dao.readAllData())
        }.flowOn(Dispatchers.IO)


    suspend fun addNewData(data: Data) {
        dao.addData(data)
    }

    suspend fun updateData(data: Data) {
        dao.updateData(data)
    }

    suspend fun deleteData(data: Data) {
        dao.deleteData(data)
    }
}