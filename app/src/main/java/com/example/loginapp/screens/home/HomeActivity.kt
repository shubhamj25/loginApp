package com.example.loginapp.screens.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import com.example.loginapp.*
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.screens.usage.UsageActivity
import com.example.loginapp.screens.usage.fragments.CurrentUsageBarFragment
import com.example.loginapp.screens.usage.fragments.StackBarFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : BaseActivity() {
    private lateinit var users: LiveData<MutableList<LoginEntity>>
    private lateinit var manager: LocationManager
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_500)))
        supportActionBar?.elevation=0f

        val userEmail = bundle?.getBundle(getString(R.string.bundleKey))
            ?.getString(getString(R.string.bundleArgEmail), getString(R.string.defaultUserName))
        users = db.loginDatabaseDao.getAllUsersExcept(userEmail!!)
        userList.layoutManager = getLinearLayoutManager(application)
        setObservers()
        setListeners()
        setWelcomeString()
        setChartFragment()
    }

    private fun setChartFragment() {
        supportFragmentManager.commit {
            add(R.id.currentUsageChart, CurrentUsageBarFragment())
        }
    }

    private fun setListeners() {
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locateMeFab.setColorFilter(Color.parseColor(getString(R.string.black)))
        locateMeFab.setOnClickListener {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(Intent(applicationContext, LocationActivity::class.java))
            } else {
                showDialog(
                    { _, _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) },
                    { dialog, _ -> dialog.cancel() },
                    R.string.locationReq,
                    R.string.locationReqMessage,
                    R.string.dialogPositive,
                    R.string.dialogNegative
                )?.show()
            }
        }
    }

    private fun setObservers() {
        users.observe(this, { newList ->
            userList.adapter = RecyclerViewAdaptor(newList, application, this)
            userList.visibility = View.VISIBLE
            userList.isNestedScrollingEnabled=false
            userListProgressBar.visibility = View.GONE
        })
    }

    private fun setWelcomeString() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val bundle = intent.extras
        val userEmail = bundle?.getBundle(getString(R.string.bundleKey))
            ?.getString(getString(R.string.bundleArgEmail), getString(R.string.defaultUserName))
        val message = getString(
            R.string.welcomeMessage,
            userEmail,
            getString(R.string.welcomeMessageSubtitle)
        )
        tv1.text = message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearDb -> uiScope.launch {
                clearDatabase()
                userList.adapter = null
            }
            R.id.logout -> {
                getSharedPreferenceInstance(application).edit().remove((getString(R.string.sh_email))).apply()
                this.finish()
            }
            R.id.viewUsage -> {
                startActivity(
                    Intent(
                        this,
                        UsageActivity::class.java
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            db.loginDatabaseDao.clearDatabase()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }
}