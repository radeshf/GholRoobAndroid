package com.mrprojects.gholrob.view

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mrprojects.gholrob.base.BaseAppActivity
import com.mrprojects.gholrob.databinding.ActivitySplashBinding
import com.mrprojects.gholrob.view.main.MainActivity
import ir.radesh.basemodule.commons.delay
import ir.radesh.basemodule.commons.goTo
import ir.radesh.basemodule.commons.scaleAnimation

class SplashActivity : BaseAppActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate){


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding.mainContainer.scaleAnimation(2500)
        delay(750) {
            goTo(MainActivity::class.java)
        }
    }


}