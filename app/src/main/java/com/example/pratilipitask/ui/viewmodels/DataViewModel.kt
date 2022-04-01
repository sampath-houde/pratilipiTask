package com.example.pratilipitask.ui.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.data.repo.DataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataViewModel(private val repo: DataRepo, application: Application) : AndroidViewModel(application) {

    private var _readAllData : MutableLiveData<List<Data>> = MutableLiveData()
    var readAllData: LiveData<List<Data>> = _readAllData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            repo.readAllData.collect {
                _readAllData.postValue(it)
            }
        }
    }

    fun addNewData(title: String, description: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = Data(0, title, description)
        repo.addNewData(data)
    }

    fun updateData(id: Long, title: String, description: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = Data(id, title, description)
        repo.updateData(data)
    }

    fun deleteData(data: Data)  = viewModelScope.launch(Dispatchers.IO){
        repo.deleteData(data)
    }

}