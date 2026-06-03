package com.mrprojects.gholrob.helper.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import ir.radesh.basemodule.helper.PrefHelper

class VibrationPlayer(context: Context) {

    private val appContext = context.applicationContext
    private val pref = PrefHelper(appContext)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun play(type: VibrateTypes) {
        if (!pref.isVibrationsOn) return
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        vib.cancel()

        val timings = type.patternMs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(timings, type.amplitudePattern(), -1)
            vib.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vib.vibrate(timings, -1)
        }
    }
}
