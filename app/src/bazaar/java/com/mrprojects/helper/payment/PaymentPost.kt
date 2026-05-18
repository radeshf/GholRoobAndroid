package com.mrprojects.helper.payment

import com.google.gson.annotations.SerializedName

class PaymentPost() {
    @SerializedName("orderId") private var orderId: String = ""
    @SerializedName("packageName") private var packageName: String = ""
    @SerializedName("productId") var productId: String = ""

    @SerializedName("purchaseState") private var purchaseState: Int = 0
    @SerializedName("purchaseTime") private var purchaseTime: Long = 0
    @SerializedName("payload") private var payload: String = ""
    @SerializedName("purchaseToken") private var purchaseToken: String = ""


    constructor(purchaseInfo: ir.cafebazaar.poolakey.entity.PurchaseInfo) : this() {
        this.orderId = purchaseInfo.orderId
        this.packageName = purchaseInfo.packageName
        this.productId = purchaseInfo.productId
        this.purchaseTime = purchaseInfo.purchaseTime
        this.payload = purchaseInfo.payload
        this.purchaseToken = purchaseInfo.purchaseToken
    }


}

