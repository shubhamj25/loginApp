package com.example.loginapp.screens.prelogin
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

@Suppress("DEPRECATION")
internal class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}