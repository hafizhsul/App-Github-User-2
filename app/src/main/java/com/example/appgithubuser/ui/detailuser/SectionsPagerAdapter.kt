package com.example.appgithubuser.ui.detailuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowsFragment.newInstance(position, username)
            1 -> FollowsFragment.newInstance(position + 1, username)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}