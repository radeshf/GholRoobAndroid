package ir.radesh.basemodule.helper

import com.google.gson.JsonSyntaxException
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
Copyright 2019 Radesh Farokh Manesh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
abstract class ResponseHelper<T>() : DisposableObserver<retrofit2.Response<T>>() {
    val NO_INTERNET_ERROR_CODE = 1001
    val JSON_ERROR_CODE = 1002
    val UNKNOWN_ERROR_CODE = 9999

    override fun onComplete() {}

    override fun onStart() {
        super.onStart()
        onShowLoading()
    }

    override fun onNext(response: retrofit2.Response<T>) {
        if (response.isSuccessful && response.body()!= null){
            Timber.e("response isSuccessful")
            onResponseOk(response.body()!!)
        }else {
            val errorCode = response.code()
            if (errorCode == 401){
                onAuthFail()
            }else{
                onError("خطای سرور : ${errorCode}", errorCode)
            }
        }
    }

    override fun onError(e: Throwable) {
        onHideLoading()
        e.printStackTrace()
        when (e) {
            is retrofit2.HttpException -> {
                Timber.e("HttpException")
                onServerError(e)
            }
            is SocketTimeoutException -> {
                Timber.e("SocketTimeoutException")
                onNoInternetError()
            }
            is IOException -> {
                Timber.e("IOException")
                when (e) {
                    is UnknownHostException -> {
                        Timber.e("UnknownHostException : this means user turn off wifi and mobile data")
                        onNoInternetError()
                    }
                    is ConnectException -> {
                        Timber.e("ConnectException : when server is off e: ${e.message}")
                        onNoInternetError()
                    }
                    else -> {
//                        onError("خطا در پردازش")
                    }
                }
            }
            is JsonSyntaxException -> {
                Timber.e("JsonSyntaxException ${e.message}")
                onError("خطای پردازش", JSON_ERROR_CODE)

                //Something in json is wrong!check that
            }
            else -> {
                onError("خطای نا شناس", UNKNOWN_ERROR_CODE)
                Timber.e("Unknown Exception")
            }
        }

    }


    /**
     * this for when server error code accursed
     * e.g 500 , 401
     */
    private fun onServerError(e: retrofit2.HttpException) {
        Timber.e("onServerError code : ${e.code()}")
        onError("خطا کد : ${e.code()}", e.code())

    }

    private fun onNoInternetError() {
        Timber.e("onNoInternetError code: ${NO_INTERNET_ERROR_CODE}")
        onError("عدم دسترسی به اینترنت، لطفا از دسترسی به اینترنت اطمینان حاصل نمایید", NO_INTERNET_ERROR_CODE)

    }


    abstract fun onResponseOk(response: T)
    abstract fun onError(msg: String, code: Int)
    abstract fun onAuthFail()

    abstract fun onShowLoading()

    abstract fun onHideLoading()

    abstract fun onShowMsg(s: String)

}


