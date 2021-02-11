package com.example.loginapp.screens.prelogin
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.loginapp.BaseActivity
import com.example.loginapp.R

class PreLoginFragmentsActivity:BaseActivity(),PreLoginFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prelogin_fragments)
    }

    override fun navigateToRegisterLayout(frag: Fragment) {
        val fragment = RegisterFragment()
        val arguments = Bundle()
        arguments.putBoolean("withViewPager",false)
        fragment.arguments = arguments
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.preLoginFragConstraintLayout,fragment)
        transaction.hide(frag)
        transaction.commit()
    }

    override fun navigateToLoginLayout(frag:Fragment) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.preLoginFragConstraintLayout,LoginFragment())
        transaction.hide(frag)
        transaction.commit()
    }

    override fun navigateToForgotPasswordLayout(frag: Fragment) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.preLoginFragConstraintLayout,ForgotPasswordFragment())
        transaction.hide(frag)
        transaction.commit()
    }
}