package com.mrprojects.gholrob

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        TapSellHelper.init(this)
        Fresco.initialize(this)
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("font/${AppConfig.DEFAULT_FONT}.ttf")
                            .build()
                    )
                )
                .build()
        )
//        FirebaseAnalytics.getInstance(this).appOpened()

        Timber.plant(Timber.DebugTree())
    }
}