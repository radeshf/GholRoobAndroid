package com.mrprojects.gholrob.helper.payment

import androidx.annotation.DrawableRes
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.model.rest.BuyItemPost
import com.mrprojects.gholrob.view.profile.ProfileImage

enum class Currency(
    val title: String,
    @DrawableRes val iconRes: Int,
) {
    TOMAN(
        title = "تومان",
        iconRes = R.drawable.ig_money
    ),
    COIN(
        title = "سکه",
        iconRes = R.drawable.ig_coin
    ),
    ADS(
        title = "تبلیغ",
        iconRes = R.drawable.ig_ads
    )

}
