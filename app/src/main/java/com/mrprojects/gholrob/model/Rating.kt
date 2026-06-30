package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.R

class Rating {
    @SerializedName("user") val user: User = User()
    @SerializedName("rank") val rank: String = ""
    @SerializedName("reward") val reward: String = ""
    @SerializedName("show_reward") val showReward: Boolean = false
    @SerializedName("show_rank") val showRank: Boolean = false
    @SerializedName("currency") val currency: String = ""
    @SerializedName("total_attempts") val totalAttempts: String = ""
    @SerializedName("total_score") val totalScore: String = ""

    fun haveRank(): Boolean{
        return rank != "0" && rank != ""
    }


    fun isInHighRank() = rank in listOf("1", "2", "3")

    fun getRankToDisplay(): String{
        if (isInHighRank())
            return ""
        return rank
    }

    fun getRankBg(): Int{
        when (rank) {
            "1" -> return R.drawable.ig_rank_1
            "2" -> return R.drawable.ig_rank_2
            "3" -> return R.drawable.ig_rank_3
            else -> return R.drawable.bg_rank
        }
    }
}

