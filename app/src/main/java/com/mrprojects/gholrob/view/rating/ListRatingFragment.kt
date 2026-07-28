package com.mrprojects.gholrob.view.rating

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.RatingListFragmentBinding
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.model.Rating
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.profile.ProfileFragment
import com.mrprojects.gholrob.view.profile.showProfileInfoDialog
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.inVisibleByBoolean
import ir.radesh.basemodule.commons.init
import ir.radesh.basemodule.commons.loadHtml
import ir.radesh.basemodule.commons.setEventBus
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.interfaces.OnItemClickListener
import org.greenrobot.eventbus.Subscribe

class ListRatingFragment : BaseFragment<RatingListFragmentBinding>(RatingListFragmentBinding::inflate), OnItemClickListener<Rating> {

    lateinit var type: String

    companion object {
        fun newInstance(type: String): ListRatingFragment {
            val frag = ListRatingFragment()
            val b = Bundle()
            b.putString("type", type)
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments?.getString("type", "")!!
        binding.rvRatings.init()
        binding.rvRatings.adapter = RatingsAdapter(this)
        getData()
        clicks()

        binding.tvPageTitle.text = when(type){
            "current" -> "لیست بهترین بازیکنان این دوره"
            "past" -> "لیست بهترین بازیکنان دوره قبل"
            "total" -> "لیست بهترین بازیکنان کل"
            else -> ""
        }

    }

    private fun clicks() {

        binding.userRatingLayout.root.setOnClickListener {
            openFragment(ProfileFragment.newInstance())
        }
    }


    private fun getData() {
        Provider.provideApiHelper(this).getRatings(type) {
            binding.userRatingLayout.tvUserXp.text = it.data.me.totalScore
            binding.userRatingLayout.tvUserGames.text = it.data.me.totalAttempts
            binding.userRatingLayout.tvUserName.text = it.data.me.user.name
            binding.userRatingLayout.ivUserProfile.setImageResource(it.data.me.user.getProfileResource())
            binding.userRatingLayout.lnrEditProfile.visibleByBoolean(true)
            binding.userRatingLayout.tvYou.visibleByBoolean(true)
            binding.userRatingLayout.lnrRank.inVisibleByBoolean(it.data.me.showRank)
            binding.userRatingLayout.tvRank.text = it.data.me.getRankToDisplay()
            binding.userRatingLayout.lnrRank.setBackgroundResource(it.data.me.getRankBg())
            binding.userRatingLayout.ivRank.setImageResource(it.data.me.getRankImage())
            binding.userRatingLayout.ivRank.visibleByBoolean(it.data.me.isInHighRank())
            binding.userRatingLayout.tvRank.visibleByBoolean(!it.data.me.isInHighRank())

            binding.userRatingLayout.lnrMain.setBackgroundResource(R.drawable.box_btn_me)
            binding.rvRatings.getAdp<RatingsAdapter>().setData(it.data.ratings)

            binding.lnrReward.visibleByBoolean(it.data.seasonReward.isActive)
            if (it.data.seasonReward.isActive){
                binding.tvSeasonTitle.text = it.data.seasonReward.title
                binding.tvReward.text = it.data.seasonReward.pricePool
                binding.tvSeasonEndTime.text = it.data.seasonReward.seasonEnd
                binding.tvRewardDescription.loadHtml(it.data.seasonReward.description.toString())

            }
        }
    }

    override fun onItemClick(item: Rating) {
        showProfileInfoDialog(item.user)
    }

    public override fun onStart() {
        super.onStart()
        setEventBus(true)
    }

    public override fun onStop() {
        super.onStop()
        setEventBus(false)
    }

    @Subscribe
    fun onProfileChanged(event: OnProfileChanged) {
        getData()
    }

}