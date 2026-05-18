package com.mrprojects.gholrob.helper

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import androidx.annotation.IntegerRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.BuyCoinDialogBinding
import com.mrprojects.gholrob.databinding.BuyHeartDialogBinding
import com.mrprojects.gholrob.databinding.BuyLifeDialogBinding
import com.mrprojects.gholrob.databinding.BuyLifeDoneDialogBinding
import com.mrprojects.gholrob.databinding.ChooseModeDialogBinding
import com.mrprojects.gholrob.databinding.CloseGameDialogBinding
import com.mrprojects.gholrob.databinding.ClosePlayDialogBinding
import com.mrprojects.gholrob.databinding.CoinNotEnoughDialogBinding
import com.mrprojects.gholrob.databinding.DialogSuccessBinding
import com.mrprojects.gholrob.databinding.DonateDoneDialogBinding
import com.mrprojects.gholrob.databinding.NoInternetDialogBinding
import com.mrprojects.gholrob.databinding.PlayHintDialogBinding
import com.mrprojects.gholrob.databinding.PleaseRateDialogBinding
import com.mrprojects.gholrob.databinding.SettingsDialogBinding
import com.mrprojects.gholrob.databinding.UseLifeDialogBinding
import com.mrprojects.gholrob.databinding.WarningDialogBinding
import com.mrprojects.gholrob.helper.payment.CoinPacks
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import com.mrprojects.gholrob.model.Puzzle
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.events.OnSoundSettingChanged
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.donate.DonateAdapter
import com.mrprojects.gholrob.view.donate.DonateItem
import com.mrprojects.gholrob.view.life.BuyLifeAdapter
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.PrefHelper
import ir.radesh.basemodule.interfaces.OnItemClickListener

fun <B : ViewBinding> BaseFragment<B>.showBuyHeartDialog(userViewModel: UserViewModel, adsHelper: TapSellHelper) {
    val binding = BuyHeartDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog.text = "قلبی برات نمونده!"
    binding.btnHeartByAds.setOnClickListener {
        adsHelper.requestAddHeartAds()
    }
    binding.btnFullHeart.setOnClickListener {
        val coin = 1000 / 100
        if (!userViewModel.checkUserCoin(coin)) return@setOnClickListener
        if (!userViewModel.checkUserHeartBeforeFill()) return@setOnClickListener
        userViewModel.spendCoins(coin)
        userViewModel.fullChargeHearts()
//        submitHint(userViewModel.userCurrentLevel(), HintType.FILL_ALL_HEARTS.key, coin, true)
        showSuccessDialog("پمپاژ خون انجام شد و همه قلب هات پر شد")
    }

    binding.btnBuyHeart.setOnClickListener {
        val coin = 3500 / 100
        if (!userViewModel.checkUserCoin(coin)) return@setOnClickListener
        if (!userViewModel.checkUserHeartBeforeBuy()) return@setOnClickListener

        userViewModel.spendCoins(coin)
        userViewModel.addExtraHeart()
//        submitHint(userViewModel.userCurrentLevel(), HintType.EXTRA_HEARTS.key, coin, true)
        showSuccessDialog("جراحی قلب با موفقیت انجام شد و تعداد کل قلب هات زیاد شد")

    }

    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}
fun <B : ViewBinding> BaseActivity<B>.showBuyCoinDialog(onCoinItemClicked: (DonateItem) -> Unit) {
    val binding = BuyCoinDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    val list = CoinPacks.entries.toTypedArray().map { it.toDonateItem() }
    binding.rvStickers.init()
    binding.rvStickers.adapter = DonateAdapter(list, object : OnItemClickListener<DonateItem> {
        override fun onItemClick(item: DonateItem) {
            onCoinItemClicked(item)
        }
    })


    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseActivity<B>.showBuyLifeDialog(onCoinItemClicked: (LifePacks) -> Unit) {
    val binding = BuyLifeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    val list = LifePacks.entries.toTypedArray().toList()

    binding.rvItems.init()
    binding.rvItems.adapter = BuyLifeAdapter(list, object : OnItemClickListener<LifePacks> {
        override fun onItemClick(item: LifePacks) {
            onCoinItemClicked(item)
        }
    })

    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.showBuyLifeDoneDialog(lifeCount: Int) {
    val binding = BuyLifeDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog.text = "نوش دارو به حساب شما اضافه شد"
    binding.tvCoin.text = "${lifeCount}"
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}
@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.showDonateDoneDialog(coinAmount: Int) {
    val binding = DonateDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog.text = "سکه ها به حساب شما اضافه شد"
    binding.tvCoin.text = "${coinAmount}+"
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showEndGameDialog() {
    val binding = PleaseRateDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog2.makeWordRed("کاراگاه شرلوک هولمز")
    binding.tvMessageDialog3.makeWordRed("ثبت نظرت")
    binding.btnRate.setOnClickListener {
        requireContext().openMarketRatePage()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showSettingsDialog() {
    val binding = SettingsDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    val soundSettings = PrefHelper(requireContext()).isSoundSettingsOn

    binding.tvSound.text = if(PrefHelper(requireContext()).isSoundSettingsOn) "(فعال)" else "(غیر فعال)"
    binding.ivSoundSetting.setImageResource(if(soundSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)

    binding.btnSoundSettings.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isSoundSettingsOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isSoundSettingsOn = newSettings
        binding.tvSound.text = if(newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivSoundSetting.setImageResource(if(newSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
        postEvent(OnSoundSettingChanged(newSettings))
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseFragment<B>.showClosePlayDialog(onCloseClicked: () -> Unit) {
    val binding = ClosePlayDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog.makeWordRed("خروج")
    binding.lnrExit.root.setOnClickListener {
        dialog.dismiss()
        onCloseClicked()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseFragment<B>.showCloseGameDialog(onCloseClicked: () -> Unit) {
    val binding = CloseGameDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvMessageDialog.makeWordRed("1 قلب")
    binding.lnrExit.root.setOnClickListener {
        dialog.dismiss()
        onCloseClicked()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseFragment<B>.showHintDialog(puzzle: Puzzle) {
    val binding = PlayHintDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseFragment<B>.showCoinNotEnoughDialog() {
    val binding = CoinNotEnoughDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.btnShop.setOnClickListener {
        postEvent(OnShowCoinShopCalled())
        dialog.dismiss()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


fun <B : ViewBinding> BaseFragment<B>.showChooseModeDialog() {
    val binding = ChooseModeDialogBinding.inflate(layoutInflater)
    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    dialog.basicConfig(cancellable = true)

    dialog.setContentView(binding.root)

    binding.btnStart.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}



@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.warningDialog(msg: String, title: String="توجه", icon: Int=R.drawable.ig_error, ) {
    layoutInflater.warningDialog(msg, title, icon)
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.warningDialog(msg: String, title: String="توجه", icon: Int=R.drawable.ig_error, ) {
    layoutInflater.warningDialog(msg, title, icon)
}


@SuppressLint("SetTextI18n")
fun LayoutInflater.warningDialog(msg: String, title: String="توجه", icon: Int=R.drawable.ig_error, ) {
    val binding = WarningDialogBinding.inflate(this)
    val dialog = Dialog(context, R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.ivIcon1.setImageResource(icon)
    binding.ivIcon2.setImageResource(icon)
    binding.tvTitleDialog.text = title
    binding.tvMessageDialog.text = msg
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.noInternetDialog(onRetryClicked: ()->Unit) {
    val binding = NoInternetDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.lnrDismiss.root.visibleByBoolean(false)
    binding.lnrRetry.btnText.text = "تلاش مجدد"
    binding.lnrRetry.root.setOnClickListener {
        onRetryClicked()
        dialog.dismiss()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {

    val binding = DialogSuccessBinding.inflate(layoutInflater)

    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)

    binding.tvMessageDialog.text = message

    binding.lnrDismiss.root.setOnClickListener {
        onOkClicked?.invoke()
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showUseLifeDialog(user: User, onUsedLifeCLicked: (dialog: Dialog) -> Unit) {
    val binding = UseLifeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig()
    dialog.setContentView(binding.root)
    binding.tvLifeCount.text = user.lives.toString()
    binding.btnUseLife.root.disableAlphaByBoolean(user.haveLife())
    binding.btnUseLife.btnText.text = "استفاده"
    binding.btnBuyLife.btnText.text = "خرید"
    binding.btnBuyLife.root.setOnClickListener {
        postEvent(OnShowLifeShopCalled())
        dialog.dismiss()
    }
    binding.btnUseLife.root.setOnClickListener {
        if (!user.haveLife()){
            warningDialog("شما هیچ نوش دارویی ندارید!")
            return@setOnClickListener
        }
        onUsedLifeCLicked(dialog)
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}

