package com.example.loginapp.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginapp.database.LoginDatabaseDao

class HomeActivityViewModelFactory(private val dataSource: LoginDatabaseDao, private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeActivityViewModel::class.java)){
            return HomeActivityViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}