package com.example.loginapp.screens.home
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LiveData
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.getLinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity: BaseActivity() {
    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }
    private lateinit var users: LiveData<MutableList<LoginEntity>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        users=db.loginDatabaseDao.getAllUsers()
        userList.layoutManager= getLinearLayoutManager(application)
        setObservers()
        setWelcomeString()
    }

    private fun setObservers(){
        users.observe(this, { newList ->
            userList.adapter = RecyclerViewAdaptor(newList,application)
            userList.visibility = View.VISIBLE
            userListProgressBar.visibility = View.GONE
        })
    }

    private fun setWelcomeString(){
        val welcomeMessage:String=tv1.text.toString()
        val bundle=intent.extras
        val msg="$welcomeMessage,\n${bundle?.getBundle(getString(R.string.bundleKey))?.getString(getString(R.string.bundleArgEmail), getString(R.string.defaultUserName))}\nHappy Learning :)"
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