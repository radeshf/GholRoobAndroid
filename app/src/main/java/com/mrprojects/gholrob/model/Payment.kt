package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName

class Payment(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("status")
    val isIncome: String?="",
    @SerializedName("payment_url")
    val paymentUrl: String="",
    @SerializedName("payment_for")
    val payment_for: String? = "",
    @SerializedName("payment_type")
    val paymentType: String? = "",
    @SerializedName("msg")
    val msg: String? = "",
    @SerializedName("customer_name")
    val customerName: String? = "",
    @SerializedName("authority")
    val authority: String? = "",
    @SerializedName("created_at")
    val created_at: String = "",
    @SerializedName("updated_at")
    val updated_at: String = "",
    @SerializedName("user")
    val user: User = User()

){
    fun getDate(): String{
        return  created_at
    }
}

