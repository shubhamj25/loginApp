package com.example.loginapp
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.loginapp.screens.ViewPagerAdapter
import com.example.loginapp.screens.preLogin.LoginFragment
import com.example.loginapp.screens.preLogin.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : BaseActivity() {
    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addPages(viewpager)

    }
    private fun addPages(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(LoginFragment(viewpager))
        adapter.addFrag(RegisterFragment(viewpager))
        viewPager.adapter = adapter
    }
    fun getViewPager(): ViewPager {
        return viewPager
    }
}