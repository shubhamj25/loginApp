package com.example.loginapp

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment(),ViewTypeInterface{
    val v=0
    override fun viewType(): Int {
        return v
    }
}