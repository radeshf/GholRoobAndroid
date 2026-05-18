package com.mrprojects.helper.payment


import androidx.fragment.app.FragmentActivity
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.helper.payment.PaymentInterface
import com.mrprojects.gholrob.helper.payment.PaymentOperation
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment

import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.entity.PurchaseInfo
import ir.cafebazaar.poolakey.request.PurchaseRequest
import ir.cafebazaar.poolakey.config.SecurityCheck
import timber.log.Timber


class PaymentHelper(
    private val activity: FragmentActivity,
    private val callback: (Boolean, PaymentOperation, PurchaseInfo?) -> Unit
) : PaymentInterface {

    private val rsaKey = AppConfig.PAYMENT_KEY

    private var payment: Payment? = null
    private var connection: Connection? = null

    override fun startConnection() {
        val security = SecurityCheck.Enable(rsaPublicKey = rsaKey)
        val config = PaymentConfiguration(localSecurityCheck = security)
        payment = Payment(context = activity, config = config)

        connection = payment?.connect {
            connectionSucceed {
                Timber.tag("BazaarBilling").d("Connection succeeded")
                // ready to purchase
            }

            connectionFailed { throwable ->
                Timber.tag("BazaarBilling").e(throwable, "Connection failed")
                callback(false, PaymentOperation.SETUP, null)
            }

            disconnected {
                Timber.tag("BazaarBilling").e("Disconnected")
            }
        }
    }

    override fun purchase(sku: String, payload: String) {
        val request = PurchaseRequest(
            productId = sku,
            payload = payload
        )

        payment?.purchaseProduct(
            registry = activity.activityResultRegistry,
            request = request
        ) {
            purchaseFlowBegan {
                Timber.tag("BazaarBilling").d("purchaseFlowBegan")
            }

            failedToBeginFlow { throwable ->
                throwable.printStackTrace()
                Timber.tag("BazaarBilling").e(throwable, "Failed to begin purchase")
                callback(false, PaymentOperation.PURCHASE, null)
            }

            purchaseSucceed { purchaseInfo ->
                Timber.tag("BazaarBilling").i("Purchase success: ${purchaseInfo.purchaseToken}")

                callback(true, PaymentOperation.PURCHASE, purchaseInfo)
                consume(purchaseInfo.purchaseToken)
            }

            purchaseCanceled {
                Timber.tag("BazaarBilling").d("purchaseCanceled")
                callback(false, PaymentOperation.PURCHASE, null)
            }

            purchaseFailed { throwable ->
                throwable.printStackTrace()
                callback(false, PaymentOperation.PURCHASE, null)
            }
        }
    }

    private fun consume(token: String) {
        payment?.consumeProduct(token) {
            consumeSucceed {
                Timber.tag("BazaarBilling").i("Consume success")
                callback(true, PaymentOperation.CONSUME, null)
            }

            consumeFailed { throwable ->
                throwable.printStackTrace()
                callback(false, PaymentOperation.CONSUME, null)
            }
        }
    }

    fun destroy() {
        connection?.disconnect()
        connection = null
    }
}
