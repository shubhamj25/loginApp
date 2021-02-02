package com.example.loginapp.screens.home
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room.databaseBuilder
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginDatabaseDao
import com.example.loginapp.database.LoginEntity

class HomeActivityViewModel(dataSource: LoginDatabaseDao, application: Application) : ViewModel() {
    private val db= databaseBuilder(application, LoginDatabase::class.java, "login_app_database").build()
    var users:LiveData<List<LoginEntity>> = db.loginDatabaseDao.getAllUsers()
}