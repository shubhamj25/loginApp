package com.example.loginapp.screens.home
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room.databaseBuilder
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginDatabaseDao
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.getDatabaseInstance

class HomeActivityViewModel(dataSource: LoginDatabaseDao, application: Application) : ViewModel() {
    var users:LiveData<MutableList<LoginEntity>> = getDatabaseInstance(application).loginDatabaseDao.getAllUsers()
}