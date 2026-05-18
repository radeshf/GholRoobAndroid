package com.mrprojects.gholrob.helper.payment

import androidx.annotation.DrawableRes
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.view.donate.DonateItem

enum class CoinPacks(
    val sku: String,
    val coinAmount: Int,
    val title: String,
    val price: Int,
    @DrawableRes val iconRes: Int
) {
    COIN_PACK_1(
        sku = "coinPack1",
        coinAmount = 1000,
        title = "یه مشت سکه",
        price = 9900,
        iconRes = R.drawable.ig_coin
    ),
    COIN_PACK_2(
        sku = "coinPack2",
        coinAmount = 3000,
        title = "کیف پر سکه",
        price = 29900,
        iconRes = R.drawable.ig_coin
    ),

    COIN_PACK_3(
        sku = "coinPack3",
        coinAmount = 7000,
        title = "صندوق گنج",
        price = 39900,
        iconRes = R.drawable.ig_coin
    );

    fun toDonateItem(): DonateItem {
        return DonateItem(sku, coinAmount, title, price, iconRes)
    }

    companion object {
        fun fromSku(sku: String): CoinPacks? = entries.firstOrNull { it.sku == sku }
    }
}
