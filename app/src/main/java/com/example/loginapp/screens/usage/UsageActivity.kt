package com.example.loginapp.screens.usage

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.example.loginapp.screens.usage.fragments.*

class UsageActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        title = "Usage"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_500)))
        supportActionBar?.elevation=0f
        supportFragmentManager.commit {
            add(R.id.chartONE, CurrentUsageBarFragment())
            add(R.id.chartTWO,CoLineNbarChartFragment())
            add(R.id.chartTHREE, LineChartFragment())
            add(R.id.chartFOUR, StackBarFragment())
            add(R.id.chartFIVE, GroupedBarChartFragment())
            //CombinedChartFragment() is for reference purpose
        }
    }
}