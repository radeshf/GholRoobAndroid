package com.mrprojects.gholrob.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "puzzles")
class Puzzle {
    @PrimaryKey @SerializedName("id") var id: Int = 0
    @SerializedName("reward") var reward: Int = 0
    @SerializedName("start_at") var start_at: String = ""
    @SerializedName("end_at") var end_at: String = ""


}

