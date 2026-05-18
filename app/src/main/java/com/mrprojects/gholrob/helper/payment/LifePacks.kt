package com.mrprojects.gholrob.helper.payment

import androidx.annotation.DrawableRes
import com.mrprojects.gholrob.R

enum class LifePacks(
    val sku: String,
    val lifeCount: Int,
    val title: String,
    val price: Int,
    @DrawableRes val iconRes: Int
) {
    LIFE_1(
        sku = "life1",
        lifeCount = 1,
        title = "چسب زخم",
        price = 9900,
        iconRes = R.drawable.ig_heart
    ),
    LIFE_5(
        sku = "life5",
        lifeCount = 5,
        title = "کمک های اولیه",
        price = 29900,
        iconRes = R.drawable.ig_heart
    ),

    LIFE_10(
        sku = "life10",
        lifeCount = 10,
        title = "آمبولانس",
        price = 39900,
        iconRes = R.drawable.ig_heart
    );


    companion object {
        fun fromSku(sku: String): LifePacks? = entries.firstOrNull { it.sku == sku }
    }
}
