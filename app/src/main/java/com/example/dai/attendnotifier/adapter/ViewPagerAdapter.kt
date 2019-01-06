package com.example.dai.attendnotifier.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.view.DailyClassworkFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, context: Context) :
    FragmentPagerAdapter(fragmentManager) {

    private val tabTitle = context.resources.getStringArray(R.array.tab_title)

    override fun getItem(position: Int): Fragment {
        return DailyClassworkFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return tabTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitle[position]
    }

}