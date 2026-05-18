package com.mrprojects.helper.payment

import android.app.Activity
import com.mrprojects.witk.AppConfig
import com.mrprojects.witk.helper.payment.PaymentInterface
import com.mrprojects.witk.helper.payment.PaymentOperation

import ir.myket.billingclient.IabHelper
import ir.myket.billingclient.util.*

class PaymentHelper(private val activity: Activity, private val callback: (Boolean, PaymentOperation, Purchase?) -> Unit): PaymentInterface {
    private var publicKey: String = AppConfig.PAYMENT_KEY

    private var helper: IabHelper? = null


    fun consume(purchase: Purchase) {
        helper?.consumeAsync(purchase) { p, res ->
            callback(res.isSuccess, PaymentOperation.CONSUME, p.takeIf { res.isSuccess })
        }
    }

    override fun startConnection() {
        helper = IabHelper(activity, publicKey)
        helper?.enableDebugLogging(true, "MyKet Payment")

        helper?.startSetup { result ->
            if (!result.isSuccess) {
                callback(false, PaymentOperation.SETUP, null)
                return@startSetup
            }

        }
    }

    override fun purchase(sku: String, payload: String) {
        helper?.launchPurchaseFlow(
            activity,
            sku,
            object : IabHelper.OnIabPurchaseFinishedListener {
                override fun onIabPurchaseFinished(result: IabResult?, purchase: Purchase?) {

                    if (result == null || result.isFailure) {
                        callback(false, PaymentOperation.PURCHASE, null)
                        return
                    }

                    callback(true, PaymentOperation.PURCHASE, purchase)
                    purchase?.let {
                        consume(it)
                    }
                }
            },
            payload
        )
    }

    fun findPurchasedItems() {
        val skus = listOf("premium", "gas", "infinite_gas")
        helper?.queryInventoryAsync(true, skus) { res, inv ->
            if (res.isFailure) {
                return@queryInventoryAsync
            }

            // you can read purchases here:
            val premium = inv.getPurchase("premium")
            val gas = inv.getPurchase("gas")
            val infiniteGas = inv.getPurchase("infinite_gas")

            // return whatever you need
//            callback(true, "", premium ?: gas ?: infiniteGas)
        }
    }


    fun destroy() {
        helper?.dispose()
        helper = null
    }

}
