package com.example.loginapp.screens.prelogin.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.loginapp.R
import com.example.loginapp.databinding.FragmentForgotpasswordBinding
import com.example.loginapp.screens.prelogin.PreLoginFragmentListener
import kotlinx.android.synthetic.main.fragment_forgotpassword.*

class ForgotPasswordFragment:Fragment() {
    private var withViewPager:Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.withViewPager= this.arguments?.getBoolean(getString(R.string.withViewPager)) ?: false
        val binding:FragmentForgotpasswordBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_forgotpassword,container,false)
        setListeners(binding)
        return binding.root
    }

    private fun setListeners(binding: FragmentForgotpasswordBinding){
        if(this.withViewPager) {
            binding.backToLoginFromForgotPass.visibility=View.GONE
        }
        else{
            binding.backToLoginFromForgotPass.setOnClickListener {
                (activity as PreLoginFragmentListener).navigateToLoginLayout(this)
            }
        }
    }
}