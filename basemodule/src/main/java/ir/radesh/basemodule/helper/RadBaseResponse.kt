package ir.radesh.basemodule.helper

import com.google.gson.annotations.SerializedName

abstract class RadBaseResponse{
    @SerializedName("success")
    private val success: Boolean = false
    @SerializedName("code")
    private val code: Int = 0
    @SerializedName("msg")
    private val msg: String = ""

    fun isOk(): Boolean {
        return success
    }

    fun getMessage(): String{
        return msg
    }

    fun getCode(): Int{
        return code
    }

}