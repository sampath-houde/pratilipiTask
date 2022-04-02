package com.example.pratilipitask.ui.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.data.repo.DataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataViewModel(private val repo: DataRepo, application: Application) : AndroidViewModel(application) {

    private var _readAllData : MutableStateFlow<List<Data>> = MutableStateFlow(listOf())
    var readAllData: StateFlow<List<Data>> = _readAllData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            repo.readAllData.collect {
                _readAllData.value = it
            }
        }
    }

    fun addNewData(title: String, description: String?) = viewModelScope.launch(Dispatchers.IO) {
        val data = Data(0, title, description)
        repo.addNewData(data)
    }

    fun updateData(id: Long, title: String, description: String?) = viewModelScope.launch(Dispatchers.IO) {
        val data = Data(id, title, description)
        repo.updateData(data)
    }

    fun deleteData(data: Data)  = viewModelScope.launch(Dispatchers.IO){
        repo.deleteData(data)
    }

}