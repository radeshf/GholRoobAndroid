package com.mrprojects.gholrob.helper.payment

import androidx.annotation.DrawableRes
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.model.rest.BuyItemPost
import com.mrprojects.gholrob.view.profile.ProfileImage

enum class LifePacks(
    val sku: String,
    val amount: Int,
    val title: String,
    val price: Int,
    @DrawableRes val iconRes: Int
) {
    BUY_ONE_ENERGY(
        sku = "oneEnergy",
        amount = 1,
        title = "انرژی زا",
        price = 30,
        iconRes = R.drawable.ig_energy
    ),

    BUY_FILL_ENERGY(
        sku = "fillEnergy",
        amount = 3,
        title = "اسپرسو",
        price = 19900,
        iconRes = R.drawable.ig_energy
    ),

    BUY_NEW_ENERGY(
        sku = "AddHeart",
        amount = 1,
        title = "قهوه ساز",
        price = 49900,
        iconRes = R.drawable.ig_energy
    ),

    COIN_PACK_1(
        sku = "coinPack1",
        amount = 1000,
        title = "یه مشت سکه",
        price = 19900,
        iconRes = R.drawable.ig_coin
    ),
    COIN_PACK_2(
        sku = "coinPack2",
        amount = 3000,
        title = "کیف پر سکه",
        price = 54900,
        iconRes = R.drawable.ig_coin
    ),

    COIN_PACK_3(
        sku = "coinPack3",
        amount = 7000,
        title = "صندوق گنج",
        price = 99900,
        iconRes = R.drawable.ig_coin
    ),

    LIFE_1(
        sku = "life1",
        amount = 1,
        title = "چسب زخم",
        price = 50000,
        iconRes = R.drawable.ig_heart
    ),
    LIFE_5(
        sku = "life5",
        amount = 5,
        title = "کمک های اولیه",
        price = 240000,
        iconRes = R.drawable.ig_heart
    ),

    LIFE_10(
        sku = "life10",
        amount = 10,
        title = "آمبولانس",
        price = 399000,
        iconRes = R.drawable.ig_heart
    ),

    BUY_PROFILE(
        sku = "buy_profile",
        amount = 1,
        title = "خرید پروفایل",
        price = 0,
        iconRes = R.drawable.ig_profile_unknown
    );

    fun isLife(): Boolean = this.name.startsWith("LIFE_")
    fun isCoin(): Boolean = this.name.startsWith("COIN_")

    fun convertToItemPost() : BuyItemPost{
        return BuyItemPost(this.name.lowercase())
    }

    fun convertToItemPost(profileItem: ProfileImage) : BuyItemPost{
        return BuyItemPost(this.name.lowercase(), profileItem.image)
    }

    companion object {
        fun fromSku(sku: String): LifePacks? = entries.firstOrNull { it.sku == sku }
        fun lifeList(): List<LifePacks> = entries.filter { it.isLife() }.toTypedArray().toList()
        fun coinList(): List<LifePacks> = entries.filter { it.isCoin() }.toTypedArray().toList()
    }
}
