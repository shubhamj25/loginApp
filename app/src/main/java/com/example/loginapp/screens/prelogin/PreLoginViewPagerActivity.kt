package com.example.loginapp.screens.prelogin

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import kotlinx.android.synthetic.main.activity_prelogin_viewpager.*

class PreLoginViewPagerActivity:BaseActivity(),ViewPagerListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prelogin_viewpager)
        addPages(viewpager)

    }
    private fun addPages(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val loginFragment = LoginFragment()
        val registerFragment = RegisterFragment()
        val arguments = Bundle()
        arguments.putBoolean("withViewPager",true)
        loginFragment.arguments = arguments
        registerFragment.arguments=arguments
        adapter.addFrag(loginFragment)
        adapter.addFrag(registerFragment)
        viewPager.adapter = adapter
    }

    override fun getViewPager()=viewpager!!
}