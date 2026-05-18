package com.mrprojects.gholrob.view.main

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.helper.payment.PaymentHelper
import com.mrprojects.helper.payment.PaymentPost
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.base.BaseAppActivity
import com.mrprojects.gholrob.databinding.ActivityMainBinding
import com.mrprojects.gholrob.helper.isAbove
import com.mrprojects.gholrob.helper.payment.CoinPacks
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.payment.PaymentInterface
import com.mrprojects.gholrob.helper.payment.PaymentOperation
import com.mrprojects.gholrob.helper.showBuyCoinDialog
import com.mrprojects.gholrob.helper.showBuyLifeDialog
import com.mrprojects.gholrob.helper.showBuyLifeDoneDialog
import com.mrprojects.gholrob.helper.showDonateDoneDialog
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.events.OnSoundSettingChanged
import com.mrprojects.gholrob.model.rest.BuyLifePost
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.commons.changeTo
import ir.radesh.basemodule.commons.setEventBus
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.helper.PlaySoundHelper
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseAppActivity<ActivityMainBinding>(ActivityMainBinding::inflate), PlaySoundHelper.Listener{

    private lateinit var userViewModel: UserViewModel
//    var musicPlayer: PlaySoundHelper? = null
    lateinit var paymentInterface: PaymentInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.changeTo(R.id.mainContainer, MainFragment.newInstance(),false, Gravity.CENTER,false)

        configPayment()
        userConfig()
        hideSystemUI()


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

                    buyLife(pack)

                } else {
                    warningDialog("پرداخت شما انجام نشد. اگر مبلغی کسر شده باشد، به\u200Cزودی به حسابتان بازگردانده می\u200Cشود", title="پرداخت ناموفق")
                    if(AppConfig.IS_TEST){
                        buyLife(LifePacks.LIFE_1)

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

    fun buyLife(pack: LifePacks) {
        val post = BuyLifePost(pack.price, pack.lifeCount)
        Provider.provideApiHelper(this).buyLife(post) {
            userViewModel.addLife(pack.lifeCount)
            showBuyLifeDoneDialog(pack.lifeCount)
        }
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
//        musicPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
//        musicPlayer?.resume()
    }
    private fun playMusic(){
//        musicPlayer?.playVoice(R.raw.bg_music)

    }
    private fun stopMusic(){
//        musicPlayer?.stopVoice()

    }

    override fun onPlaySoundCompleted() {
        playMusic()
    }

    @Subscribe
    fun checkAppOpenEvent(event: OnSoundSettingChanged) {
        if (event.isSoundOn){
            playMusic()
        }else{
            stopMusic()
        }
    }

    @Subscribe
    fun openBuyCoinDialog(event: OnShowCoinShopCalled) {
        showBuyCoinDialog {
            paymentInterface.purchase(it.id, it.name)
        }
    }

    @Subscribe
    fun openBuyLifeDialog(event: OnShowLifeShopCalled) {
        showBuyLifeDialog {
            paymentInterface.purchase(it.sku, it.name)
        }
    }
}
