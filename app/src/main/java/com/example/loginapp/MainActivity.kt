package com.example.loginapp
import android.content.Intent
import android.os.Bundle
import com.example.loginapp.screens.prelogin.activity.PreLoginViewPagerActivity
open class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //startActivity(Intent(application,PreLoginFragmentsActivity::class.java))
        startActivity(Intent(application, PreLoginViewPagerActivity::class.java))
        this.finish()
    }
}