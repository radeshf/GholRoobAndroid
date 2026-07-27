package com.mrprojects.gholrob.view.rating

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RankingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListRatingFragment.newInstance("current")
            1 -> ListRatingFragment.newInstance("past")
            else -> ListRatingFragment.newInstance("total")
        }
    }
}