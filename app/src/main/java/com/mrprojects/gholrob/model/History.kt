package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName

data class History(

    @SerializedName("id")
    val id: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("is_won")
    val isWinner: Boolean,

    @SerializedName("score")
    val score: Int,

    @SerializedName("speed")
    val speed: String,

    @SerializedName("enemy_kills")
    val enemyKills: Int,

)