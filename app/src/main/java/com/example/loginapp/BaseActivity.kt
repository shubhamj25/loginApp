package com.example.loginapp
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.database.LoginDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseActivity:AppCompatActivity() {
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    lateinit var db:LoginDatabase
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = getDatabaseInstance(this.application)
        sharedPreferences=getSharedPreferenceInstance(application)
    }
}