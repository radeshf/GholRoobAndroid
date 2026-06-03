package com.mrprojects.gholrob.view.rating

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.RatingFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.showBuyCoinDialog
import com.mrprojects.gholrob.model.Rating
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
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

class RatingFragment : BaseFragment<RatingFragmentBinding>(RatingFragmentBinding::inflate), OnItemClickListener<Rating> {



    companion object {
        fun newInstance(): RatingFragment {
            val frag = RatingFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRatings.init()
        binding.rvRatings.adapter = RatingsAdapter(this)
        getData()
        clicks()
        initToolbar(title = "رتبه بندی")

    }

    private fun clicks() {

        binding.userRatingLayout.root.setOnClickListener {
            openFragment(ProfileFragment.newInstance())
        }
    }


    private fun getData() {
        Provider.provideApiHelper(this).getRatings() {
            binding.userRatingLayout.tvUserXp.text = it.data.me.totalScore
            binding.userRatingLayout.tvUserName.text = it.data.me.user.name
            binding.userRatingLayout.ivUserProfile.setImageResource(it.data.me.user.getProfileResource())
            binding.userRatingLayout.lnrEditProfile.visibleByBoolean(true)
            binding.userRatingLayout.tvYou.visibleByBoolean(true)
            binding.userRatingLayout.lnrRank.inVisibleByBoolean(it.data.me.haveRank())
            binding.userRatingLayout.tvRank.text = it.data.me.getRankToDisplay()
            binding.userRatingLayout.lnrRank.setBackgroundResource(it.data.me.getRankBg())
            binding.rvRatings.getAdp<RatingsAdapter>().setData(it.data.ratings)

            binding.lnrReward.visibleByBoolean(it.data.seasonReward.isActive)
            if (it.data.seasonReward.isActive){
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