@file:Suppress("DEPRECATION")

package com.example.aplikasigithubuser.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.fragment.FollowersFragment
import com.example.aplikasigithubuser.fragment.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}