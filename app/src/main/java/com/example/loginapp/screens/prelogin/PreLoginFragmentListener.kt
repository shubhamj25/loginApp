package com.example.loginapp.screens.prelogin

import androidx.fragment.app.Fragment

interface PreLoginFragmentListener {
    fun navigateToRegisterLayout(frag: Fragment)
    fun navigateToLoginLayout(frag: Fragment)
    fun navigateToForgotPasswordLayout(frag: Fragment)
}