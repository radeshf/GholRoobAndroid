package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.model.play.GameKill


class Attempt {
    @SerializedName("id") var id: Int = 0
    @SerializedName("attempt_number") var attemptNumber: Int = 0
    @SerializedName("hearts") var hearts: Int = 0
    @SerializedName("score") var score: Int = 0
    @SerializedName("total_hearts") var totalHearts: Int = 0
    @SerializedName("is_passed") var isPassed: Boolean = false
    @SerializedName("is_show_passed_dialog") var isShowPassedDialog: Boolean = false
    @SerializedName("is_show_fail_dialog") var isShowFailDialog: Boolean = false
    @SerializedName("defeated_monsters") var defeatedMonsters: Int = 0
    @SerializedName("speed") var speed: String = ""
    @SerializedName("profile") var profile: User = User()
    @SerializedName("killed_by") var killedBy: GameCell? = null
    @SerializedName("cells") var cells: ArrayList<GameCell> = arrayListOf()
    @SerializedName("kills") var kills: ArrayList<GameKill> = arrayListOf()

}

