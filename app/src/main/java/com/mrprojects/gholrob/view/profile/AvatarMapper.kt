package com.mrprojects.gholrob.view.profile

import com.mrprojects.gholrob.R

object AvatarMapper {
    private val map = mapOf(
        "profile_1" to R.drawable.ig_enemy_wizard,
        "profile_2" to R.drawable.ig_enemy_esfandiyar,
        "profile_3" to R.drawable.ig_enemy_wolf,
        "profile_4" to R.drawable.ig_enemy_dragon,
        "profile_5" to R.drawable.ig_enemy_lion,
        "profile_6" to R.drawable.ig_enemy_arzhang_troll,
        "profile_7" to R.drawable.ig_enemy_white_troll,
        "profile_8" to R.drawable.ig_enemy_zahak,
        "profile_9" to R.drawable.ig_enemy_hyena,
    )

    fun getResourceId(name: String?): Int {
        return map[name] ?: R.drawable.ig_enemy_desert
    }
}