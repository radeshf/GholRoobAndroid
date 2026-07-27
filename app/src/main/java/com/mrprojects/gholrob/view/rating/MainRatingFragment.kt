package com.mrprojects.gholrob.view.rating

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.RatingMainFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import ir.radesh.basemodule.baseViews.BaseFragment

class MainRatingFragment : BaseFragment<RatingMainFragmentBinding>(RatingMainFragmentBinding::inflate) {

    data class TabItem(val title: String, @DrawableRes val icon: Int)

    private val tabs = listOf(
        TabItem("رقابت این ماه", R.drawable.ig_rank_1),
        TabItem("برندگان دور قبل", R.drawable.ig_rank_2),
        TabItem("رتبه‌بندی کل", R.drawable.ig_rank_3)
    )

    companion object {
        fun newInstance(): MainRatingFragment {
            val frag = MainRatingFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        clicks()
        initToolbar(title = "رتبه بندی")

    }

    private fun clicks() {


    }


    private fun getData() {
        val adapter = RankingPagerAdapter(this)
        binding.vpRanking.adapter = adapter

        binding.vpRanking.offscreenPageLimit = 3

        TabLayoutMediator(binding.tabRanking, binding.vpRanking) { tab, position ->
            tab.text = when (position) {
                0 -> "رقابت این ماه"
                1 -> "برندگان دور قبل"
                else -> "رتبه‌بندی کلی"
            }
        }.attach()

        TabLayoutMediator(binding.tabRanking, binding.vpRanking) { tab, position ->
            val view = layoutInflater.inflate(R.layout.rating_tab_item, null)
            view.findViewById<ImageView>(R.id.tabIcon).setImageResource(tabs[position].icon)
            view.findViewById<TextView>(R.id.tabText).text = tabs[position].title
            tab.customView = view
        }.attach()

        binding.tabRanking.post { movePillTo(0, animate = false) }

        binding.tabRanking.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                movePillTo(tab.position)
                updateTabStyles(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun updateTabStyles(selectedPos: Int) {
        for (i in tabs.indices) {
            val tab = binding.tabRanking.getTabAt(i) ?: continue
            val view = tab.customView ?: continue
            val isSelected = i == selectedPos
//            view.background = if (isSelected) ContextCompat.getDrawable(requireContext(), R.drawable.bg_ranking_tab_selected) else null
            view.findViewById<TextView>(R.id.tabText).alpha = if (isSelected) 1f else 0.5f
            view.findViewById<ImageView>(R.id.tabIcon).alpha = if (isSelected) 1f else 0.5f
        }
    }
    private fun movePillTo(position: Int, animate: Boolean = true) {
        val tabWidth = binding.tabRanking.width / tabs.size
        val targetX = (tabWidth * position).toFloat()

        if (!animate) {
            binding.tabIndicatorPill.layoutParams.width = tabWidth
            binding.tabIndicatorPill.x = targetX
            binding.tabIndicatorPill.requestLayout()
            return
        }

        val startX = binding.tabIndicatorPill.x
        ValueAnimator.ofFloat(startX, targetX).apply {
            duration = 250
            interpolator = DecelerateInterpolator()
            addUpdateListener { binding.tabIndicatorPill.x = it.animatedValue as Float }
            start()
        }
    }

}