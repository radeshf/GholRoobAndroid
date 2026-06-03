package com.mrprojects.gholrob.helper.tapsell

import android.app.Activity
import android.app.Application
import android.widget.RelativeLayout
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.helper.tapsell.HeartAdsKey
import com.mrprojects.helper.tapsell.HintAdsKey
import com.mrprojects.helper.tapsell.TapSellKey
import com.mrprojects.helper.tapsell.bannerKey
import com.mrprojects.helper.tapsell.doubleCoinAdsKey
import ir.tapsell.plus.*

import ir.tapsell.plus.model.*
import timber.log.Timber
import java.lang.ref.WeakReference


class TapSellHelper(
    val activity: Activity,
    val onAddHeartRewarded: (() -> Unit)? = null,
    val onHintRewarded: (() -> Unit)? = null,
    val onDoubleCoinRewarded: (() -> Unit)? = null
) {
    private val activityRef = WeakReference(activity)
    private var responseId: String? = null
    private var rewarded: Boolean = false
    private var bannerView: RelativeLayout? = null

    private fun activity(): Activity? = activityRef.get()

    private fun requestVideoAds(key: String){
        activity()?.let { TapsellPlus.requestRewardedVideoAd(it, key, advListener()) }

    }
    fun requestAddHeartAds() {
        requestVideoAds(HeartAdsKey)
    }

    fun requestHintAds() {
        requestVideoAds(HintAdsKey)
    }

    fun requestDoubleCoinAds() {
        requestVideoAds(doubleCoinAdsKey)
    }

    fun requestBannerAds(view: RelativeLayout) {
        bannerView = view
        activity()?.let { TapsellPlus.requestStandardBannerAd(it, bannerKey, TapsellPlusBannerType.BANNER_320x50, advListener()) }

    }

    private fun showAd(res: TapsellPlusAdModel) {
        rewarded = false
        if (activity() == null) return
        when (res.zoneId) {
            in arrayOf(HeartAdsKey, HintAdsKey, doubleCoinAdsKey) -> {
                TapsellPlus.showRewardedVideoAd(activity(), res.responseId, advShowListener())
            }
            bannerKey -> {
                TapsellPlus.showStandardBannerAd(activity(), res.responseId, bannerView, advShowListener())
            }
            else -> {
                Timber.e("Wrong zoneId: ${res.zoneId}")
            }
        }

    }

    fun destroyAd() {
        if (bannerView != null && activity() != null){
            TapsellPlus.destroyStandardBanner(activity(), responseId, bannerView)
        }
    }

    private fun advListener(): AdRequestCallback {
        return object : AdRequestCallback() {
            override fun response(s: TapsellPlusAdModel) {
                super.response(s)

                Timber.e("advListener ${s.responseId}")
                val act = activity() ?: return
                if (activity.isDestroyed || activity.isFinishing) return

                responseId = s.responseId
                showAd(s)
            }

            override fun error(message: String) {
                Timber.e("error $message")
                val act = activity() ?: return
                if (activity.isDestroyed || activity.isFinishing) return

            }
        }
    }

    private fun advShowListener(): AdShowListener {
        return object : AdShowListener() {
            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                super.onOpened(tapsellPlusAdModel)
                Timber.e("AdShowListener onOpened")
            }

            override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                super.onClosed(tapsellPlusAdModel)
                Timber.e("AdShowListener onClosed")
                val zoneId = tapsellPlusAdModel.zoneId
                if (zoneId == doubleCoinAdsKey){
                    if (rewarded) {
                        onDoubleCoinRewarded?.invoke()
                    } else {
                        activity()!!.layoutInflater.warningDialog("برای دریافت جایزه باید ویدئو را تا پایان تماشا نمایید", "قطع تبلیغ")

                    }
                }
                else if (zoneId == HintAdsKey){
                    if (rewarded) {
                        onHintRewarded?.invoke()
                    } else {
                        activity()!!.layoutInflater.warningDialog("برای دریافت جایزه باید ویدئو را تا پایان تماشا نمایید", "قطع تبلیغ")

                    }
                }
                else if (zoneId == HeartAdsKey){
                    if (rewarded) {
                        onAddHeartRewarded?.invoke()
                    } else {
                        activity()!!.layoutInflater.warningDialog("برای دریافت جایزه باید ویدئو را تا پایان تماشا نمایید", "قطع تبلیغ")
                    }
                }
                rewarded = false
            }

            override fun onRewarded(tapsellPlusAdModel: TapsellPlusAdModel) {
                super.onRewarded(tapsellPlusAdModel)
                Timber.e("AdShowListener onRewarded")
                rewarded = true
            }

            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                super.onError(tapsellPlusErrorModel)
                Timber.e("AdShowListener onError $tapsellPlusErrorModel")
            }
        }
    }

    companion object {
        fun init(context: Application) {
            TapsellPlus.initialize(
                context, TapSellKey,
                object : TapsellPlusInitListener {
                    override fun onInitializeSuccess(adNetworks: AdNetworks) {
                        Timber.e("Tapsell onInitializeSuccess ${adNetworks.name}")
                    }

                    override fun onInitializeFailed(adNetworks: AdNetworks, adNetworkError: AdNetworkError) {
                        Timber.e("Tapsell onInitializeFailed ${adNetworks.name}, e: ${adNetworkError.errorMessage}")
                    }
                })
        }
    }
}