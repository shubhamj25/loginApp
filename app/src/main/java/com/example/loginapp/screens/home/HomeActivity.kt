package com.example.loginapp.screens.home
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.MainActivity
import com.example.loginapp.R
import com.example.loginapp.database.LoginDatabase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*


@Suppress("DEPRECATION")
class HomeActivity: MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val application = requireNotNull(this).application
        val dataSource=LoginDatabase.getInstance(application).loginDatabaseDao
        val viewModelFactory=HomeActivityViewModelFactory(dataSource, application)
        val viewModel= ViewModelProviders.of(this, viewModelFactory).get(HomeActivityViewModel::class.java)

        viewModel.users.observe(this, { newList ->
            userList.adapter = RecyclerViewAdaptor(newList,this.application)
            userList.visibility = View.VISIBLE
            userListProgressBar.visibility = View.GONE
        })
        val layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userList.layoutManager=layoutManager
        val x:String=tv1.text.toString()
        val a=intent.extras
        val msg="$x,\n${a?.getBundle("args")?.getString("email", "User")}\nHappy Learning :)"
        tv1.text=msg
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clearDb-> uiScope.launch {
                clearDatabase()
                userList.adapter=null
            }
            R.id.logout-> {
                clearSharedPreferences(sharedPreferences)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun clearDatabase(){
        withContext(Dispatchers.IO){
            db.loginDatabaseDao.clearDatabase()
        }
    }
}