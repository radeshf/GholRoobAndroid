package com.mrprojects.gholrob.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.view.profile.AvatarMapper

@Entity(tableName = "user")
class User {
    @PrimaryKey
    @SerializedName("id") var id: Int = 0
    @SerializedName("unfinished_attempt_id") var unfinishedAttemptId: Int? = null
    @SerializedName("username") var username: String = ""
    @SerializedName("name") var name: String = ""
    @SerializedName("profile_image") var profileImage: String = ""
    @SerializedName("max_hearts") var maxHearts: Int = 3
    @SerializedName("current_hearts") var currentHearts: Int = 3
    @SerializedName("next_heart_time") var nextHeartTime: Long = 0
    @SerializedName("heart_refill_interval") var heartRefillInterval: Long = 0

    @SerializedName("lives") var lives: Int = 0
    @SerializedName("coins") var coins: Int = 0

    @Ignore
    @SerializedName("-O8w9,_s6+^i-O8w9,_s6+^i") var userHash: String = ""

    constructor(){

    }

    constructor(username: String) {
        this.id = 1
        this.username = username
        this.name = ""
        this.maxHearts = 3
        this.currentHearts = 3
        this.nextHeartTime = 0
        this.coins = 500
    }

    fun nextHeartTimeMillis() = nextHeartTime
    fun haveUnfinishedAttempt() = unfinishedAttemptId != null && unfinishedAttemptId != 0
    fun haveLife() = lives > 0

    fun getProfileResource(): Int {
        return AvatarMapper.getResourceId(this.profileImage)
    }
    override fun toString() = "User($id, $username, $name, $profileImage)"
}