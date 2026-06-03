package com.mrprojects.gholrob.view.profile

import com.mrprojects.gholrob.R

object AvatarMapper {
    private val map = mapOf(
        "ig_profile_anoshirvan" to R.drawable.ig_profile_anoshirvan,
        "ig_profile_arezo" to R.drawable.ig_profile_arezo,
        "ig_profile_bizhan" to R.drawable.ig_profile_bizhan,
        "ig_profile_bozorgmehr" to R.drawable.ig_profile_bozorgmehr,
        "ig_profile_derafsh" to R.drawable.ig_profile_derafsh,
        "ig_profile_farangis" to R.drawable.ig_profile_farangis,
        "ig_profile_fereydon" to R.drawable.ig_profile_fereydon,
        "ig_profile_garshasp" to R.drawable.ig_profile_garshasp,
        "ig_profile_giv" to R.drawable.ig_profile_giv,
        "ig_profile_godarz" to R.drawable.ig_profile_godarz,
        "ig_profile_golnar" to R.drawable.ig_profile_golnar,
        "ig_profile_gordafarid" to R.drawable.ig_profile_gordafarid,
        "ig_profile_gordie" to R.drawable.ig_profile_gordie,
        "ig_profile_homay" to R.drawable.ig_profile_homay,
        "ig_profile_jarire" to R.drawable.ig_profile_jarire,
        "ig_profile_kave" to R.drawable.ig_profile_kave,
        "ig_profile_keikhosro" to R.drawable.ig_profile_keikhosro,
        "ig_profile_manizhe" to R.drawable.ig_profile_manizhe,
        "ig_profile_rodabeh" to R.drawable.ig_profile_rodabeh,
        "ig_profile_rostam" to R.drawable.ig_profile_rostam,
        "ig_profile_sepinode" to R.drawable.ig_profile_sepinode,
        "ig_profile_siavash" to R.drawable.ig_profile_siavash,
        "ig_profile_simorgh" to R.drawable.ig_profile_simorgh,
        "ig_profile_sindokht" to R.drawable.ig_profile_sindokht,
        "ig_profile_sohrab" to R.drawable.ig_profile_sohrab,
        "ig_profile_tahmine" to R.drawable.ig_profile_tahmine,
        "ig_profile_unkown" to R.drawable.ig_profile_unkown,
        "ig_profile_zal" to R.drawable.ig_profile_zal,
    )

    fun getResourceId(name: String?): Int {
        return map[name] ?: R.drawable.ig_profile_unkown
    }
}