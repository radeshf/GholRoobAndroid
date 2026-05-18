package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName


class AttemptHistory {
    @SerializedName("id") var id: Int = 0
    @SerializedName("score") var score: Int = 0
    @SerializedName("is_passed") var isPassed: Boolean = false
    @SerializedName("defeated_monsters") var defeatedMonsters: Int = 0
    @SerializedName("speed") var speed: String = ""
    @SerializedName("title") var title: String = ""
    @SerializedName("game_status_title") var gameStatusTitle: String = ""
    @SerializedName("finished_at") var finishedAt: String = ""
    @SerializedName("killed_by") var killedBy: GameCell? = null

}

