package com.example.loginapp.screens.prelogin
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
@Suppress("DEPRECATION")
internal class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }
    override fun getCount(): Int {
        return mFragmentList.size
    }
    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}