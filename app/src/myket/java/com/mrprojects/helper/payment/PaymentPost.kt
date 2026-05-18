package com.mrprojects.helper.payment

import com.google.gson.annotations.SerializedName
import ir.myket.billingclient.util.Purchase

class PaymentPost() {
    @SerializedName("orderId") private var orderId: String = ""
    @SerializedName("packageName") private var packageName: String = ""
    @SerializedName("productId") var productId: String = ""

    @SerializedName("purchaseState") private var purchaseState: Int = 0
    @SerializedName("purchaseTime") private var purchaseTime: Long = 0
    @SerializedName("payload") private var payload: String = ""
    @SerializedName("purchaseToken") private var purchaseToken: String = ""


    constructor(purchase: Purchase) : this() {
        this.orderId = purchase.orderId
        this.packageName = purchase.packageName
        this.productId = purchase.sku
        this.purchaseState = purchase.purchaseState
        this.purchaseTime = purchase.purchaseTime
        this.payload = purchase.developerPayload
        this.purchaseToken = purchase.token

    }
}
