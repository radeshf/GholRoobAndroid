package com.mrprojects.gholrob.view.rating

import android.annotation.SuppressLint

import com.mrprojects.gholrob.databinding.OptionItemBinding
import com.mrprojects.gholrob.databinding.RatingItemBinding
import com.mrprojects.gholrob.model.Rating
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener


class RatingsAdapter(listener: OnItemClickListener<Rating>?) : RvAdapter<Rating, RatingItemBinding>(RatingItemBinding::inflate, listener = listener) {


    @SuppressLint("SetTextI18n")
    override fun onBindView(view: RatingItemBinding, item: Rating, position: Int, listener: OnItemClickListener<Rating>?) {
        view.tvUserXp.text = item.totalScore
        view.tvUserName.text = item.user.name
        view.ivUserProfile.setImageResource(item.user.getProfileResource())
        view.lnrRank.visibleByBoolean(item.haveRank())
        view.lnrReward.visibleByBoolean(item.haveReward())
        if (item.haveRank()){
            view.tvRank.text = item.getRankToDisplay()
            view.lnrRank.setBackgroundResource(item.getRankBg())
        }
        if (item.haveReward()){
            view.tvReward.text = item.reward
        }

        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }

    }

}
