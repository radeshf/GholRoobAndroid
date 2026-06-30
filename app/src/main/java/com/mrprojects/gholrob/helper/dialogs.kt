package com.mrprojects.gholrob.helper

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.BuyCoinDialogBinding
import com.mrprojects.gholrob.databinding.BuyCoinDoneDialogBinding
import com.mrprojects.gholrob.databinding.BuyHeartDialogBinding
import com.mrprojects.gholrob.databinding.BuyLifeDialogBinding
import com.mrprojects.gholrob.databinding.BuyLifeDoneDialogBinding
import com.mrprojects.gholrob.databinding.ChooseModeDialogBinding
import com.mrprojects.gholrob.databinding.CloseGameDialogBinding
import com.mrprojects.gholrob.databinding.ClosePlayDialogBinding
import com.mrprojects.gholrob.databinding.CoinNotEnoughDialogBinding
import com.mrprojects.gholrob.databinding.DialogSuccessBinding
import com.mrprojects.gholrob.databinding.DonateDoneDialogBinding
import com.mrprojects.gholrob.databinding.EnemyInfoDialogBinding
import com.mrprojects.gholrob.databinding.NoInternetDialogBinding
import com.mrprojects.gholrob.databinding.PlayHintDialogBinding
import com.mrprojects.gholrob.databinding.PleaseRateDialogBinding
import com.mrprojects.gholrob.databinding.SettingsDialogBinding
import com.mrprojects.gholrob.databinding.TutorialDoneDialogBinding
import com.mrprojects.gholrob.databinding.UseLifeDialogBinding
import com.mrprojects.gholrob.databinding.WarningDialogBinding
import com.mrprojects.gholrob.helper.haptics.OnVibrationsSettingsChanged
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.model.Puzzle
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.helper.sound.bg.OnBgMusicSettingsChanged
import com.mrprojects.gholrob.helper.sound.sfx.OnSfxMusicSettingsChanged
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.play.GameKill
import com.mrprojects.gholrob.view.donate.DonateAdapter
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

fun <B : ViewBinding> BaseFragment<B>.showBuyEnergyDialog(
    userViewModel: UserViewModel,
    onBuyOneClicked: ()->Unit,
    onBuyAllClicked: ()->Unit,
    onBuyNewClicked: ()->Unit
) {
    val binding = BuyHeartDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvPriceRefill.text = userViewModel.user.value?.fillAllEnergyPrice.toString()
    binding.tvPriceNew.text = userViewModel.user.value?.buyNewEnergyPrice.toString()
    binding.tvMessageDialog.text = "میتونی انرژی ات رو پر کنی با آیتم های زیر"
    binding.btnHeartByAds.setOnClickListener {
        onBuyOneClicked()
    }
    binding.btnFullHeart.setOnClickListener {
        onBuyAllClicked()
    }

    binding.btnBuyNew.setOnClickListener {
        onBuyNewClicked()
    }

    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseActivity<B>.showBuyCoinDialog(onCoinItemClicked: (LifePacks) -> Unit) {
    val binding = BuyCoinDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    val list = LifePacks.coinList().toList()

    binding.rvStickers.init()
    binding.rvStickers.adapter = DonateAdapter(list, object : OnItemClickListener<LifePacks> {
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

fun <B : ViewBinding> BaseActivity<B>.showBuyLifeDialog(
    userLives: String,
    onCoinItemClicked: (LifePacks) -> Unit) {
    val binding = BuyLifeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    val list = LifePacks.lifeList().toList()

    binding.tvMessageDialog.text = "شما ${userLives} نوش دارو دارید"
    binding.tvMessageDialog.makeWordRed(userLives)
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
    dialog.basicConfig(binding.root)
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
fun <B : ViewBinding> BaseActivity<B>.showBuyCoinDoneDialog(count: Int) {
    val binding = BuyCoinDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvMessageDialog.text = "سکه به حساب شما اضافه شد"
    binding.tvCoin.text = "${count}"
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
    dialog.basicConfig(binding.root)
    val isBgMusicOn = PrefHelper(requireContext()).isBgMusicOn
    val isSfxMusicOn = PrefHelper(requireContext()).isSfxMusicOn
    val isVibrationsOn = PrefHelper(requireContext()).isVibrationsOn

    binding.tvBg.text = if (isBgMusicOn) "(فعال)" else "(غیر فعال)"
    binding.ivBg.setImageResource(if (isBgMusicOn) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
    binding.sbBg.progress = (PrefHelper(requireContext()).bgMusicVolume * 100).toInt()
    binding.sbSfx.progress = (PrefHelper(requireContext()).sfxMusicVolume * 100).toInt()

    binding.tvSfx.text = if (isSfxMusicOn) "(فعال)" else "(غیر فعال)"
    binding.ivSfx.setImageResource(if (isSfxMusicOn) R.drawable.ig_sound_on else R.drawable.ig_sound_off)

    binding.tvVibrate.text = if (isVibrationsOn) "(فعال)" else "(غیر فعال)"
    binding.ivVibrate.setImageResource(if (isVibrationsOn) R.drawable.ig_vibrate_on else R.drawable.ig_vibrate_off)

    binding.ivBg.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isBgMusicOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isBgMusicOn = newSettings
        binding.tvBg.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivBg.setImageResource(if (newSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
        postEvent(OnBgMusicSettingsChanged(isVolumeChanged = false, isStatusChanged = true))
    }

    binding.ivSfx.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isSfxMusicOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isSfxMusicOn = newSettings
        binding.tvSfx.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivSfx.setImageResource(if (newSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
        postEvent(OnSfxMusicSettingsChanged(isVolumeChanged = false, isStatusChanged = true))

    }


    binding.ivVibrate.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isVibrationsOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isVibrationsOn = newSettings
        binding.tvVibrate.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivVibrate.setImageResource(if (newSettings) R.drawable.ig_vibrate_on else R.drawable.ig_vibrate_off)
        postEvent(OnVibrationsSettingsChanged(isStatusChanged = true))

    }

    binding.sbBg.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                postEvent(OnBgMusicSettingsChanged(isVolumeChanged = true, isStatusChanged = false, volume=progress / 100f))
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })

    binding.sbSfx.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                postEvent(OnSfxMusicSettingsChanged(isVolumeChanged = true, isStatusChanged = false, volume=progress / 100f))

            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })

    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseActivity<B>.showClosePlayDialog(onCloseClicked: () -> Unit) {
    val binding = ClosePlayDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
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

fun <B : ViewBinding> BaseActivity<B>.showCloseGameDialog(onCloseClicked: () -> Unit) {
    val binding = CloseGameDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
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


fun <B : ViewBinding> BaseActivity<B>.showCoinNotEnoughDialog() {
    val binding = CoinNotEnoughDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
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


@SuppressLint("SetTextI18n")
fun LayoutInflater.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    val binding = WarningDialogBinding.inflate(this)
    val dialog = Dialog(context, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
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
fun <B : ViewBinding> BaseFragment<B>.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    layoutInflater.warningDialog(msg, title, icon)
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    layoutInflater.warningDialog(msg, title, icon)
}



@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.noInternetDialog(title: String?=null, msg: String?=null, onRetryClicked: () -> Unit) {
    val binding = NoInternetDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.lnrDismiss.root.visibleByBoolean(false)
    if (!title.isNullOrEmpty()){
        binding.tvTitleDialog.text = title
    }
    if (!msg.isNullOrEmpty()){
        binding.tvMessageDialog.text = msg
    }
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
fun LayoutInflater.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {

    val binding = DialogSuccessBinding.inflate(this)

    val dialog = Dialog(context, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)

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
fun <B : ViewBinding> BaseFragment<B>.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {
    layoutInflater.showSuccessDialog(message, onOkClicked)
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {
    layoutInflater.showSuccessDialog(message, onOkClicked)
}



@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showUseLifeDialog(user: User, onUsedLifeCLicked: (dialog: Dialog) -> Unit) {
    val binding = UseLifeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvLifeCount.text = user.lives.toString()
    binding.btnUseLife.root.disableAlphaByBoolean(user.haveLife())
    binding.btnUseLife.btnText.text = "استفاده"
    binding.btnBuyLife.btnText.text = "خرید"
    binding.btnBuyLife.root.setOnClickListener {
        postEvent(OnShowLifeShopCalled())
        dialog.dismiss()
    }
    binding.btnUseLife.root.setOnClickListener {
        if (!user.haveLife()) {
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


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showTutorialDoneDialog(title: String, msg: String, btn: String="آموزش بعدی", onOkCLicked: (dialog: Dialog) -> Unit) {
    val binding = TutorialDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvTitleDialog.text = title
    binding.tvMessageDialog.text = msg
    binding.btnContinue.btnText.text = btn

    binding.winConfettiView.startConfetti()
    binding.btnContinue.root.setOnClickListener {
        onOkCLicked(dialog)
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showEnemyInfoDialog(item: GameKill, isInPlay: Boolean = false) {
    val binding = EnemyInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivEnemy.setImageResource(item.image())
    binding.tvTitleDialog.text = item.name
    binding.tvMessageDialog.text = item.description
    binding.tvDamage.text = item.damage.toString()
    binding.tvTotal.text = item.total.toString()
    binding.lnrRemain.visibleByBoolean(isInPlay)
    binding.tvRemain.text = item.remained.toString()

    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


