package com.example.loginapp.screens.prelogin.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.example.loginapp.screens.prelogin.ViewPagerAdapter
import com.example.loginapp.screens.prelogin.ViewPagerListener
import com.example.loginapp.screens.prelogin.fragments.ForgotPasswordFragment
import com.example.loginapp.screens.prelogin.fragments.LoginFragment
import com.example.loginapp.screens.prelogin.fragments.RegisterFragment
import kotlinx.android.synthetic.main.activity_prelogin_viewpager.*

class PreLoginViewPagerActivity:BaseActivity(), ViewPagerListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prelogin_viewpager)
        addPages(viewpager)
    }
    private fun addPages(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(this)
        val loginFragment = LoginFragment()
        val registerFragment = RegisterFragment()
        val forgotPasswordFragment = ForgotPasswordFragment()
        val arguments = Bundle()
        arguments.putBoolean(getString(R.string.withViewPager),true)
        loginFragment.arguments = arguments
        registerFragment.arguments=arguments
        forgotPasswordFragment.arguments=arguments
        adapter.addFrag(forgotPasswordFragment)
        adapter.addFrag(loginFragment)
        adapter.addFrag(registerFragment)
        viewPager.adapter = adapter
        viewPager.currentItem=1
    }
    override fun getViewPager() =viewpager!!
}