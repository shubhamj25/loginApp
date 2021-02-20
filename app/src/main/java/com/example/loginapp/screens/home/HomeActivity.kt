package com.example.loginapp.screens.home
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.getLinearLayoutManager
import com.example.loginapp.showDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity: BaseActivity() {
    private lateinit var users: LiveData<MutableList<LoginEntity>>
    private lateinit var  manager:LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        users=db.loginDatabaseDao.getAllUsers()
        userList.layoutManager= getLinearLayoutManager(application)
        setObservers()
        setListeners()
        setWelcomeString()
    }
    private fun setListeners(){
        manager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locateMeFab.setOnClickListener{
            if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
                startActivity(Intent(applicationContext, LocationActivity::class.java))
            }
            else{
                showDialog({ _, _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) },
                        { dialog, _ -> dialog.cancel() },
                        R.string.locationReq,
                        R.string.locationReqMessage,
                        R.string.dialogPositive,
                        R.string.dialogNegative)?.show()
            }
        }
    }
    private fun setObservers(){
        users.observe(this, { newList ->
            userList.adapter = RecyclerViewAdaptor(newList, application, this)
            userList.visibility = View.VISIBLE
            userListProgressBar.visibility = View.GONE
        })
    }

    private fun setWelcomeString(){
        val bundle=intent.extras
        val userEmail=bundle?.getBundle(getString(R.string.bundleKey))?.getString(getString(R.string.bundleArgEmail), getString(R.string.defaultUserName))
        val message=getString(R.string.welcomeMessage, userEmail, getString(R.string.welcomeMessageSubtitle))
        tv1.text=message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clearDb -> uiScope.launch {
                clearDatabase()
                userList.adapter = null
            }
            R.id.logout -> {
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

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }
}