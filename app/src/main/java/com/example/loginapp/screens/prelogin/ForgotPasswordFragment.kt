package com.example.loginapp.screens.prelogin
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.loginapp.R
import com.example.loginapp.databinding.FragmentForgotpasswordBinding
import kotlinx.android.synthetic.main.fragment_forgotpassword.*

class ForgotPasswordFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FragmentForgotpasswordBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_forgotpassword,container,false)
        binding.backToLoginFromForgotPass.setOnClickListener {
            (activity as PreLoginFragmentListener).navigateToLoginLayout(this)
        }
        return binding.root
    }
}