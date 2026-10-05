package com.mrprojects.gholrob


object AppConfig {
    const val VERSION_NAME = BuildConfig.VERSION_NAME
    const val VERSION_CODE = BuildConfig.VERSION_CODE
    const val ID = BuildConfig.APPLICATION_ID
    const val SOURCE = BuildConfig.MARKET

    const val USERNAME_PREFIX = BuildConfig.USERNAME_PREFIX
//    const val PAYMENT_KEY = ""
    const val PAYMENT_KEY = BuildConfig.PAYMENT_KEY

    const val BASE_URL = "http://gholrob.radeshf.ir"
//    const val BASE_URL = "http://192.168.1.101:18001"
//    const val BASE_URL = "http://10.86.178.72:18001"
    const val TUTORIAL_URL = "$BASE_URL/api/v1/gholrob/games/tutorial"

    const val DATABASE_NAME = "app_db"
    const val DATABASE_VERSION = 23


    const val BOLD_FONT = "YekanBakhFaNum-Bold"
    const val DEFAULT_FONT = "Vazirmatn-FD-SemiBold"

    const val SHOW_ADS = true

    const val IS_TEST = false
    const val IS_ADMIN = false
    const val HIDDEN_KEY = -9999
    const val REWARD_COIN = 100


    fun isBazaar(): Boolean{
        return SOURCE == "BAZAAR"
    }

    fun marketName(): String{
        return if (this.isBazaar()) return "\"کافه بازار\"" else "\"مایکت\""
    }

}


