package com.mrprojects.gholrob.view.main

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.base.BaseAppActivity
import com.mrprojects.gholrob.databinding.ActivityMainBinding
import com.mrprojects.gholrob.helper.haptics.OnVibrate
import com.mrprojects.gholrob.helper.haptics.OnVibrationsSettingsChanged
import com.mrprojects.gholrob.helper.haptics.VibrateTypes
import com.mrprojects.gholrob.helper.haptics.VibrationPlayer
import com.mrprojects.gholrob.helper.isAbove
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.payment.PaymentInterface
import com.mrprojects.gholrob.helper.payment.PaymentOperation
import com.mrprojects.gholrob.helper.showBuyCoinDialog
import com.mrprojects.gholrob.helper.showBuyCoinDoneDialog
import com.mrprojects.gholrob.helper.showBuyLifeDialog
import com.mrprojects.gholrob.helper.showBuyLifeDoneDialog
import com.mrprojects.gholrob.helper.showCloseGameDialog
import com.mrprojects.gholrob.helper.showClosePlayDialog
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.helper.sound.bg.OnBgMusicSettingsChanged
import com.mrprojects.gholrob.model.events.OnBuyNewLifeCalled
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.rest.BuyItemPost
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.play.PlayFragment
import com.mrprojects.gholrob.viewmodel.UserViewModel
import com.mrprojects.helper.payment.PaymentHelper
import com.mrprojects.helper.payment.PaymentPost
import ir.radesh.basemodule.commons.changeTo
import ir.radesh.basemodule.commons.setEventBus
import ir.radesh.basemodule.helper.PrefHelper
import com.mrprojects.gholrob.helper.sound.bg.BgMusicPlayer
import com.mrprojects.gholrob.helper.sound.sfx.SfxPlayer
import com.mrprojects.gholrob.helper.sound.sfx.SfxTypes
import com.mrprojects.gholrob.helper.sound.sfx.OnPlaySfx
import com.mrprojects.gholrob.helper.sound.sfx.OnSfxMusicSettingsChanged
import com.mrprojects.gholrob.model.events.OnBuyRefillEnergyCalled
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseAppActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    private lateinit var userViewModel: UserViewModel
    private var bgMusic: BgMusicPlayer ?= null
    private var sfx: SfxPlayer  ?= null
    private var vibrationPlayer: VibrationPlayer?= null
    lateinit var paymentInterface: PaymentInterface
    private var skipBackDialog = false
    private var testPackToBuy: LifePacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.changeTo(R.id.mainContainer, MainFragment.newInstance(),false, Gravity.CENTER,false)

        configPayment()
        userConfig()
        hideSystemUI()
        onBackHandle()
        configMusic()
    }

    fun buyPack(pack: LifePacks){
        val post = BuyItemPost(pack.name.lowercase())
        Provider.provideApiHelper(this).buyItem(post) {
            userViewModel.storeUser(it.data)
            if(pack.isLife()){
                showBuyLifeDoneDialog(pack.amount)
            }else if (pack.isCoin()){
                showBuyCoinDoneDialog(pack.amount)
            }else{
                showSuccessDialog(it.getMessage())
            }
        }
    }

    private fun configPayment() {
        paymentInterface = PaymentHelper(this) { success, type, purchase ->
            if (type == PaymentOperation.PURCHASE) {
                if (success) {
                    val payment = PaymentPost(purchase!!)
                    val pack = LifePacks.fromSku(payment.productId)
                    if (pack == null){
                        warningDialog("بسته مورد نظر پیدا نشد${payment.productId}", title="پرداخت نا موفق")
                        return@PaymentHelper
                    }
                    buyPack(pack)

                } else {
                    warningDialog("پرداخت شما انجام نشد. اگر مبلغی کسر شده باشد، به\u200Cزودی به حسابتان بازگردانده می\u200Cشود", title="پرداخت ناموفق")
                    if(AppConfig.IS_TEST){
                        buyPack(testPackToBuy!!)
                    }
                }
            } else if (type == PaymentOperation.SETUP) {
                if (!success) {
//                    showToast("نرم افزار %s جهت پرداخت یافت نشد! ".format(AppConfig.marketName()))
                }
            } else if (type == PaymentOperation.CONSUME) {
                if (!success) {
                    warningDialog("فرایند خرید ناقص ماند لطفا با پشتیبانی تماس بگیرید", title="پرداخت نا موفق")
                }
            }

        }
        paymentInterface.startConnection()


    }

    private fun configMusic(){
        bgMusic = BgMusicPlayer(this)
        if (PrefHelper(this).isBgMusicOn){
            playMusic()
        }
        sfx = SfxPlayer(this)
        sfx?.preloadAll()
        vibrationPlayer = VibrationPlayer(this)
    }

    private fun hideSystemUI() {
        if (isAbove(Build.VERSION_CODES.R)) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }


    fun userConfig(){
        userViewModel = Provider.provideUserViewModel(this)

    }


    public override fun onStart() {
        super.onStart()
        setEventBus(true)
    }

    public override fun onStop() {
        super.onStop()
        setEventBus(false)
    }

    override fun onPause() {
        super.onPause()
        bgMusic?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (PrefHelper(this).isBgMusicOn){
            bgMusic?.resume()
        }
    }
    private fun playMusic(){
        bgMusic?.play(R.raw.bg_music)
    }
    private fun stopMusic(){
        bgMusic?.pause()
    }

    fun onBackHandle(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
                if (skipBackDialog) {
                    skipBackDialog = false
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                    return
                }
                if (currentFragment is MainFragment) {
                    showCloseGameDialog {
                        isEnabled = false
                        onBackClicked()
                    }
                } else if (currentFragment is PlayFragment){
                    showClosePlayDialog {
                        isEnabled = false
                        onBackClicked()
                    }
                }
                else {
                    // بک عادی
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }

    fun closeWithoutDialog() {
        skipBackDialog = true
        onBackPressedDispatcher.onBackPressed()
    }

    @Subscribe
    fun onBgMusicSettingsChanged(event: OnBgMusicSettingsChanged) {
        if(event.isStatusChanged){
            if (PrefHelper(this).isBgMusicOn){
                playMusic()
            }else{
                stopMusic()
            }
        }

        if (event.isVolumeChanged){
            bgMusic?.setVolume(event.volume)
        }

    }

    @Subscribe
    fun onSfxSettingsChanged(event: OnSfxMusicSettingsChanged) {
        if(event.isStatusChanged){
            if (PrefHelper(this).isSfxMusicOn){
                sfx?.play(SfxTypes.DefeatEmpty)
            }
        }

        if (event.isVolumeChanged){
            sfx?.setVolume(event.volume)
        }

    }



    @Subscribe
    fun onPlaySfx(event: OnPlaySfx) {
        if (PrefHelper(this).isSfxMusicOn){
            sfx?.play(event.type)
        }

    }

    @Subscribe
    fun onVibrate(event: OnVibrate) {
        if (PrefHelper(this).isVibrationsOn){
            vibrationPlayer?.play(event.type)
        }

    }

    @Subscribe
    fun onVibrationsSettingsChanged(event: OnVibrationsSettingsChanged) {
        if(event.isStatusChanged){
            if (PrefHelper(this).isVibrationsOn){
                vibrationPlayer?.play(VibrateTypes.ClickOnHeal)
            }
        }



    }

    @Subscribe
    fun openBuyCoinDialog(event: OnShowCoinShopCalled) {
        showBuyCoinDialog {
            testPackToBuy = LifePacks.COIN_PACK_1
            paymentInterface.purchase(it.sku, it.name)
        }
    }

    @Subscribe
    fun openBuyLifeDialog(event: OnShowLifeShopCalled) {
        showBuyLifeDialog(userViewModel.user.value?.lives.toString()) {
            testPackToBuy = LifePacks.LIFE_1
            paymentInterface.purchase(it.sku, it.name)
        }
    }

    @Subscribe
    fun onBuyNewEnergy(event: OnBuyNewLifeCalled) {
        val pack = LifePacks.BUY_FILL_ENERGY
        testPackToBuy = pack
        paymentInterface.purchase(pack.sku, pack.title)

    }
    @Subscribe
    fun onBuyFillEnergy(event: OnBuyRefillEnergyCalled) {
        val pack = LifePacks.BUY_FILL_ENERGY
        testPackToBuy = pack
        paymentInterface.purchase(pack.sku, pack.title)

    }


    override fun onDestroy() {
        super.onDestroy()
        bgMusic?.release()
        sfx?.release()
    }
}
