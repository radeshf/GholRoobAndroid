package com.mrprojects.gholrob.repository

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mrprojects.gholrob.App
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.base.BaseAppActivity
import com.mrprojects.gholrob.helper.getDeviceUsername
import com.mrprojects.gholrob.repository.api.Api
import com.mrprojects.gholrob.repository.api.ApiHelper
import com.mrprojects.gholrob.repository.api.ApiRepo
import com.mrprojects.gholrob.repository.db.AppDatabase
import com.mrprojects.gholrob.repository.db.DatabaseHelper
import com.mrprojects.gholrob.repository.db.DatabaseProvider
import com.mrprojects.gholrob.viewmodel.UserViewModel
import com.mrprojects.gholrob.viewmodel.UserViewModelFactory
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getDeviceName
import ir.radesh.basemodule.commons.showLoading
import ir.radesh.basemodule.helper.PrefHelper
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object Provider {

    fun <B : ViewBinding> provideApiHelper(activity: BaseAppActivity<B>): ApiHelper {
        return ApiHelper(activity, activity, provideApiRepo(), provideDatabase(activity))
    }


    fun <B : ViewBinding> provideApiHelper(fragment: BaseFragment<B>, showLoading: Boolean=true): ApiHelper {
        val context = fragment.requireContext()
        return ApiHelper(context, fragment, provideApiRepo(), provideDatabase(context))
    }

    fun <B : ViewBinding> provideDatabaseHelper(fragment: BaseFragment<B>): DatabaseHelper {
        val context = fragment.requireContext()
        return DatabaseHelper(context, fragment, provideDatabase(context))
    }
    fun <B : ViewBinding> provideDatabaseHelper(activity: BaseAppActivity<B>): DatabaseHelper {
        return DatabaseHelper(activity, activity, provideDatabase(activity))
    }

    fun provideUserViewModel(fragment: Fragment): UserViewModel {
        val dao = provideDatabase(fragment.requireContext()).userDao()
        return ViewModelProvider(fragment.requireActivity(), UserViewModelFactory(dao)).get(UserViewModel::class.java)
    }


    fun <B : ViewBinding> provideUserViewModel(activity: BaseAppActivity<B>): UserViewModel {
        val dao = provideDatabase(activity).userDao()
        return ViewModelProvider(activity, UserViewModelFactory(dao)).get(UserViewModel::class.java)
    }

    fun provideDatabase(context: Context): AppDatabase {
        return DatabaseProvider.get(context)
    }

    fun provideApiRepo(): ApiRepo {
        return ApiRepo(provideApi())
    }

    private fun provideApi(): Api {
        return provideRetrofit()
            .create(Api::class.java)
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }


    private fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    private fun provideOkHttpClient(): OkHttpClient {

        return unSafeOkHttpClient()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer ${PrefHelper(App.instance.applicationContext).token}")
                    .header("X_DEVICE_ID", getDeviceUsername())
                    .header("X_DEVICE_NAME", App.instance.applicationContext.getDeviceName())
                    .header("X_VERSION_NAME", AppConfig.VERSION_NAME)
                    .header("X_VERSION_CODE", AppConfig.VERSION_CODE.toString())
                    .header("X_APPLICATION", AppConfig.ID)
                    .header("X_APPLICATION_SOURCE", AppConfig.SOURCE)
                    .header("X_ANDROID_SDK", Build.VERSION.SDK_INT.toString())
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .build()
    }

    private fun safeOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    private fun unSafeOkHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
                okHttpClient.hostnameVerifier(object : HostnameVerifier {
                    override fun verify(p0: String?, p1: SSLSession?): Boolean {
                        return true
                    }

                })
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }

}