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
    @DrawableRes val iconRes: Int,
    val currency: Currency = Currency.TOMAN,
) {
//    ------------- ENERGY ----------------

    // DEPRECATED
    BUY_ONE_ENERGY(
        sku = "oneEnergy",
        amount = 1,
        title = "انرژی",
        price = 30,
        iconRes = R.drawable.ig_energy
    ),

    ADS_BONUS_ENERGY(
        sku = "bonus_energy",
        amount = 1,
        title = "انرژی",
        price = 30,
        iconRes = R.drawable.ig_energy
    ),

    BUY_ONE_ENERGY_V2(
        sku = "energy1",
        amount = 1,
        title = "انرژی",
        price = 700,
        iconRes = R.drawable.ig_energy,
        currency = Currency.COIN
    ),

    BUY_FILL_ENERGY(
        sku = "fillEnergy",
        amount = 3,
        title = "شارژ کامل (+3)",
        price = 19900,
        iconRes = R.drawable.ig_energy
    ),

    BUY_NEW_ENERGY(
        sku = "AddHeart",
        amount = 1,
        title = "افزایش ظرفیت",
        price = 49900,
        iconRes = R.drawable.ig_energy
    ),

    //    ------------- COIN ----------------
    ADS_BONUS_COIN(
        sku = "bonus_coin",
        amount = 50,
        title = "سکه",
        price = 30,
        iconRes = R.drawable.ig_coin,
        currency = Currency.ADS
    ),
    COIN_PACK_1(
        sku = "coinPack1",
        amount = 1000,
        title = "سکه",
        price = 19900,
        iconRes = R.drawable.ig_coin
    ),
    COIN_PACK_2(
        sku = "coinPack2",
        amount = 4000,
        title = "سکه",
        price = 69900,
        iconRes = R.drawable.ig_coin,
    ),
    COIN_PACK_3(
        sku = "coinPack3",
        amount = 6000,
        title = "سکه",
        price = 79900,
        iconRes = R.drawable.ig_coin
    ),
//    ------------- LIFE ----------------

    LIFE_1(
        sku = "life1",
        amount = 1,
        title = "نوش دارو",
        price = 25000,
        iconRes = R.drawable.ig_heart
    ),
    LIFE_2(
        sku = "life2",
        amount = 5,
        title = "نوش دارو",
        price = 109000,
        iconRes = R.drawable.ig_heart
    ),

    LIFE_3(
        sku = "life3",
        amount = 10,
        title = "نوش دارو",
        price = 149000,
        iconRes = R.drawable.ig_heart
    ),

    //    ------------- EYE ----------------
    ADS_BONUS_EYE(
        sku = "bonus_eye",
        amount = 1,
        title = "چشم عقاب",
        price = 30,
        iconRes = R.drawable.ig_eye
    ),
    EYE_1(
        sku = "eye1",
        amount = 3,
        title = "چشم عقاب",
        price = 300,
        iconRes = R.drawable.ig_eye,
        currency = Currency.COIN
    ),

    EYE_2(
        sku = "eye2",
        amount = 5,
        title = "چشم عقاب",
        price = 5900,
        iconRes = R.drawable.ig_eye,
        currency = Currency.TOMAN
    ),

    EYE_3(
        sku = "eye3",
        amount = 12,
        title = "چشم عقاب",
        price = 9900,
        iconRes = R.drawable.ig_eye,
        currency = Currency.TOMAN
    ),

//    ------------- SHIELD ----------------

    ADS_BONUS_SHIELD(
        sku = "bonus_shield",
        amount = 1,
        title = "سپر نجات",
        price = 30,
        iconRes = R.drawable.ig_shield
    ),
    SHIELD_1(
        sku = "shield1",
        amount = 3,
        title = "سپر نجات",
        price = 500,
        iconRes = R.drawable.ig_shield,
        currency = Currency.COIN
    ),

    SHIELD_2(
        sku = "shield2",
        amount = 5,
        title = "سپر نجات",
        price = 10900,
        iconRes = R.drawable.ig_shield,
        currency = Currency.TOMAN
    ),

    SHIELD_3(
        sku = "shield3",
        amount = 12,
        title = "سپر نجات",
        price = 14900,
        iconRes = R.drawable.ig_shield,
        currency = Currency.TOMAN
    ),

    //    ------------- PROFILE ----------------
    BUY_PROFILE(
        sku = "buy_profile",
        amount = 1,
        title = "خرید پروفایل",
        price = 0,
        iconRes = R.drawable.ig_profile_unknown,
        currency = Currency.COIN
    );

    fun isLife(): Boolean = this.name.startsWith("LIFE_")
    fun isCoin(): Boolean = this.name.startsWith("COIN_")

    fun convertToItemPost(): BuyItemPost {
        return BuyItemPost(this.name.lowercase())
    }

    fun convertToItemPost(profileItem: ProfileImage): BuyItemPost {
        return BuyItemPost(this.name.lowercase(), profileItem.image)
    }

    fun shopCountTitle(): String {
        if (this == BUY_FILL_ENERGY || this == BUY_NEW_ENERGY)
            return this.title
        return "${this.amount} عدد"
    }

    companion object {
        fun fromSku(sku: String): LifePacks? = entries.firstOrNull { it.sku == sku }
        fun lifeList(): List<LifePacks> = entries.filter { it.isLife() }.toTypedArray().toList()
        fun coinList(): List<LifePacks> = entries.filter { it.isCoin() }.toTypedArray().toList()
    }
}
