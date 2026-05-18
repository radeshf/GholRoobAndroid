package com.mrprojects.gholrob.helper.payment

interface PaymentInterface {
    fun startConnection()
    fun purchase(sku: String, payload: String)
}