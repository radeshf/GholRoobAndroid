package ir.radesh.basemodule.helper.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import java.util.ArrayList

class BottomBarAdapter(fragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(fragmentManager) {
    private val fragments = ArrayList<Fragment>()

    fun addFragments(fragment: Fragment) {
        fragments.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
