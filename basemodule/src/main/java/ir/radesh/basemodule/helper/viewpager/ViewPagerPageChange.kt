package ir.radesh.basemodule.helper.viewpager

import androidx.viewpager.widget.ViewPager

interface OnViewPagerChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        onPageChanged(position)
    }

    fun onPageChanged(position: Int)
}