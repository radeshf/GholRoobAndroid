package com.mrprojects.gholrob.view.dialogs

import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.BuyDoneDialogBinding
import com.mrprojects.gholrob.databinding.BuyOptionEyeDialogBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.payment.Currency
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.setTime
import com.mrprojects.gholrob.model.events.shop.OnBuyWithAds
import com.mrprojects.gholrob.model.events.shop.OnBuyWithCoin
import com.mrprojects.gholrob.model.events.shop.OnBuyWithPayment
import com.mrprojects.gholrob.viewmodel.UserViewModel
import com.mrprojects.gholrob.viewmodel.user.TimerState
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.toMoneyString
import ir.radesh.basemodule.commons.visibleByBoolean


fun <B : ViewBinding> BaseActivity<B>.showBuyEnergyDialog(
    userViewModel: UserViewModel,
) {
    baseShopDialog(
        userViewModel,
        R.drawable.ig_energy,
        "فروشگاه انرژی",
        "با انرژی می\u200Cتونی به بازی ادامه بدی و جایگاهت رو در رتبه\u200Cبندی بهتر کنی.",
        LifePacks.BUY_ONE_ENERGY_V2,
        LifePacks.BUY_FILL_ENERGY,
        LifePacks.BUY_NEW_ENERGY,
        LifePacks.ADS_BONUS_ENERGY,
        userViewModel.bonusEnergyTimer.timer
    )
}

fun <B : ViewBinding> BaseActivity<B>.showBuyCoinDialog(
    userViewModel: UserViewModel,
) {
    baseShopDialog(
        userViewModel,
        R.drawable.ig_coin_chest,
        "فروشگاه سکه",
        "با سکه می‌تونی آیتم های مختلف برای طول بازی بخری و پروفایلت رو جذاب کنی",
        LifePacks.COIN_PACK_1,
        LifePacks.COIN_PACK_2,
        LifePacks.COIN_PACK_3,
        LifePacks.ADS_BONUS_COIN,
        userViewModel.bonusCoinTimer.timer
    )
}

fun <B : ViewBinding> BaseActivity<B>.showBuyLifeDialog(
    userViewModel: UserViewModel,
) {
    baseShopDialog(
        userViewModel,
        R.drawable.ig_heart,
        "قدرت نوش دارو",
        "با نوش دارو می‌تونی از باخت فرار کنی، حتی پس از باخت!",
        LifePacks.LIFE_1,
        LifePacks.LIFE_2,
        LifePacks.LIFE_3
    )
}

fun <B : ViewBinding> BaseActivity<B>.showBuyEyeDialog(
    userViewModel: UserViewModel,
) {
    baseShopDialog(
        userViewModel,
        R.drawable.ig_eye2,
        "قدرت چشم عقاب",
        "با چشم عقاب می‌تونی زیر یه کاشی رو بدون آسیب دیدن ببینی",
        LifePacks.EYE_1,
        LifePacks.EYE_2,
        LifePacks.EYE_3,
        LifePacks.ADS_BONUS_EYE,
        userViewModel.bonusEyeTimer.timer

    )
}


fun <B : ViewBinding> BaseActivity<B>.showBuyShieldDialog(
    userViewModel: UserViewModel,
) {
    baseShopDialog(
        userViewModel,
        R.drawable.ig_shield2,
        "قدرت سپر نگهبان",
        "با سپر نگهبان می‌تونی بدون آسیب دیدن، یه کاشی رو از بین ببری",
        LifePacks.SHIELD_1,
        LifePacks.SHIELD_2,
        LifePacks.SHIELD_3,
        LifePacks.ADS_BONUS_SHIELD,
        userViewModel.bonusShieldTimer.timer

    )
}


fun <B : ViewBinding> BaseActivity<B>.baseShopDialog(
    userViewModel: UserViewModel,
    headerImage: Int,
    title: String,
    msg: String,
    smallItem: LifePacks,
    targetItem: LifePacks,
    largeItem: LifePacks,
    adsItem: LifePacks? = null,
    timer: LiveData<TimerState>? = null,
    onBuyWithAdsClicked: ((item: LifePacks) -> Unit)? = null,
    onBuySmallClicked: ((item: LifePacks) -> Unit)? = null,
    onBuyMediumClicked: ((item: LifePacks) -> Unit)? = null,
    onBuyLargeClicked: ((item: LifePacks) -> Unit)? = null
) {
    val binding = BuyOptionEyeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.Widget_Game_Dialog)
    dialog.basicConfig(binding.root)
    binding.btnDismiss.tvDismiss.text = "فعلاً نه، بعداً"
    binding.ivHeader.setImageResource(headerImage)
    binding.tvTitleDialog.text = title
    binding.tvMessageDialog.text = msg

    binding.tvSmallCount.text = smallItem.shopCountTitle()
    binding.ivSmall.setImageResource(smallItem.iconRes)
    binding.btnBuySmall.tvPrice.text = smallItem.price.toMoneyString()
    binding.btnBuySmall.tvCurrency.text = smallItem.currency.title
    binding.btnBuySmall.ivCurrency.setImageResource(smallItem.currency.iconRes)

    binding.tvMediumCount.text = targetItem.shopCountTitle()
    binding.ivMedium.setImageResource(targetItem.iconRes)
    binding.btnBuyTarget.tvPrice.text = targetItem.price.toMoneyString()
    binding.btnBuyTarget.tvCurrency.text = targetItem.currency.title
    binding.btnBuyTarget.ivCurrency.setImageResource(targetItem.currency.iconRes)

    binding.tvLargeCount.text = largeItem.shopCountTitle()
    binding.ivLarge.setImageResource(largeItem.iconRes)
    binding.btnBuyLarge.tvPrice.text = largeItem.price.toMoneyString()
    binding.btnBuyLarge.tvCurrency.text = largeItem.currency.title
    binding.btnBuyLarge.ivCurrency.setImageResource(largeItem.currency.iconRes)
    val showAds = adsItem != null
    binding.btnAdAction.visibleByBoolean(showAds)
    binding.tvAdsTitle.text = "مشاهده تبلیغ (${adsItem?.amount} ${adsItem?.title} رایگان)"

//    val drawable = RoundedArcDrawable(
//        arcColor = ContextCompat.getColor(requireContext(), R.color.colorAccent),
//        trackColor = ContextCompat.getColor(requireContext(), R.color.darkInputBackground)
//    )
//    binding.lnrTimer.timerProgress.progressDrawable = drawable


    userViewModel.user.observe(this) { user ->

    }

    timer?.observe(this) { it ->

        if (it.remainingMs > 0) {
            binding.lnrTimer.setTime(it.remainingMs)
            binding.btnByAds.disableAlphaByBoolean(false)
            binding.lnrTimer.root.visibleByBoolean(true)
            binding.lnrDot.visibleByBoolean(false)
            binding.lnrTimer.timerProgress.progress = it.percent
        } else {
            binding.btnByAds.disableAlphaByBoolean(true)
            binding.lnrTimer.root.visibleByBoolean(false)
            binding.lnrDot.visibleByBoolean(true)
            binding.lnrTimer.timerProgress.progress = 0
            binding.lnrTimer.setTime(0)


        }
    }

    binding.btnAdAction.setOnClickListener {
        postEvent(OnBuyWithAds(adsItem!!))
    }
    fun onClick(item: LifePacks) {
        if (item.currency == Currency.COIN) {
            postEvent(OnBuyWithCoin(item))
        } else {
            postEvent(OnBuyWithPayment(item))
        }
    }

    binding.btnBuySmall.root.setOnClickListener {
        onClick(smallItem)
    }

    binding.btnBuyTarget.root.setOnClickListener {
        onClick(targetItem)

    }

    binding.btnBuyLarge.root.setOnClickListener {
        onClick(largeItem)
    }

    binding.cardTierSmall.setOnClickListener {
        onClick(smallItem)
    }

    binding.cardTierMedium.setOnClickListener {
        onClick(targetItem)
    }

    binding.cardTierLarge.setOnClickListener {
        onClick(largeItem)
    }

    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


fun <B : ViewBinding> BaseActivity<B>.showShopDoneDialog(purchasedItem: LifePacks) {
    val binding = BuyDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.Widget_Game_Dialog)
    dialog.basicConfig(binding.root)
    binding.btnDismiss.tvDismiss.text = "حله"
    binding.ivHeader.setImageResource(R.drawable.ig_ok)
    binding.tvTitleDialog.text = "خرید موفق"
    binding.tvMessageDialog.text = "${purchasedItem.title} به حساب شما اضافه شد"
    binding.tvAmount.text = "+${purchasedItem.amount}"
    binding.ivPurchasedItem.setImageResource(purchasedItem.iconRes)

    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}
