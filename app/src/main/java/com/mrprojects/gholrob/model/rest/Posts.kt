package com.mrprojects.gholrob.model.rest

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.AppConfig
import ir.radesh.basemodule.commons.getDeviceName


class LoginPost(
    @SerializedName("config") private val config: ConfigPost,
)

class EditProfilePost(
    @SerializedName("nick_name") private val nickName: String,
    @SerializedName("last_name") private val bio: String, //used as bio
    @SerializedName("profile_image") private val profileImage: String,
)


class PaymentMessagePost(
    @SerializedName("payment_id") private val paymentId: String,
    @SerializedName("username") private val username: String,
    @SerializedName("msg") private val msg: String,
)


class AddHintPost(
    @SerializedName("puzzle_id") private val puzzleId: Int?,
    @SerializedName("type") private val type: String,
    @SerializedName("coin") private val coin: Int,
    @SerializedName("is_coin_spend") private val isCoinSpend: String?,

)

class BuyItemPost(
    @SerializedName("type") private val type: String,
    @SerializedName("inventory_type") private val inventoryType: String?=null,
)

class BuyCoinPost(
    @SerializedName("price") private val price: Int,
    @SerializedName("coins") private val coins: Int,
)

class OtpPost(
    @SerializedName("mobile") private val mobile: String,
    @SerializedName("type") private val type: String,
    @SerializedName("code") private val code: String?=null,
)

class BuyEnergyPost(
    @SerializedName("buy_type") private val buyType: String

)

class ConfigPost(
    @SerializedName("unique_id") private val uniqueId: String,
    @SerializedName("device") private val device: String,
    @SerializedName("version_name") private val versionName: String,
    @SerializedName("version_code") private val versionCode: Int,
    @SerializedName("android") private val android: Int
){
    companion object {
        fun newInstance(context: Context): ConfigPost{
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return ConfigPost(androidId, context.getDeviceName(), AppConfig.VERSION_NAME, AppConfig.VERSION_CODE, Build.VERSION.SDK_INT)
        }
    }


}
class UpdatePost(
    @SerializedName("last_played_level") private val lastPlayedLevel: Int,

){
    companion object {
        fun newInstance(context: Context): ConfigPost{
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return ConfigPost(androidId, context.getDeviceName(), AppConfig.VERSION_NAME, AppConfig.VERSION_CODE, Build.VERSION.SDK_INT)
        }
    }


}