package com.example.pratilipitask.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pratilipitask.data.repo.DataRepo
import com.example.pratilipitask.ui.viewmodels.DataViewModel

class DatabaseViewModelFactory(
    private val repo: DataRepo,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {

        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(repo, application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}