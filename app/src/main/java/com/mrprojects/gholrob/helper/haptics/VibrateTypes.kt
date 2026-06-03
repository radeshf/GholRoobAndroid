package com.mrprojects.gholrob.helper.haptics

import android.os.Build
import androidx.annotation.RequiresApi


enum class VibrateTypes(
    val patternMs: LongArray,
    val amplitude: Int
) {
    Boss1(
        patternMs = longArrayOf(0, 40, 40, 40, 80, 120), // triple tap + tail
        amplitude = 220
    ),
    Boss2(
        patternMs = longArrayOf(0, 60, 40, 60, 40, 120),
        amplitude = 235
    ),
    Boss3(
        patternMs = longArrayOf(0, 90, 60, 90, 60, 180),
        amplitude = 255
    ),

    DefeatEmpty(
        patternMs = longArrayOf(0, 25), // small "nope"
        amplitude = 90
    ),
    DefeatEnemy(
        patternMs = longArrayOf(0, 60, 30, 140), // impact + tail
        amplitude = 230
    ),

    ClickOnHeal(
        patternMs = longArrayOf(0, 35, 40, 35, 40, 35), // soft triple
        amplitude = 120
    ),

    Lose(
        patternMs = longArrayOf(0, 180, 80, 180),
        amplitude = 255
    ),
    Win(
        patternMs = longArrayOf(0, 40, 50, 70, 50, 110),
        amplitude = 180
    ),

    Pit(
        patternMs = longArrayOf(0, 120, 60, 220), // heavier fall
        amplitude = 240
    ),

    FlagOn(
        patternMs = longArrayOf(0, 18), // tiny tick
        amplitude = 80
    ),
    FlagOff(
        patternMs = longArrayOf(0, 14, 25, 14), // double tiny
        amplitude = 70
    );

    /**
     * For API 26+: amplitudes array length MUST match timings length.
     * index 0 = initial delay -> 0 amplitude
     * odd indexes = vibration segments -> amplitude
     * even indexes (except 0) = pause segments -> 0 amplitude
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun amplitudePattern(): IntArray {
        val amp = amplitude.coerceIn(1, 255)
        val arr = IntArray(patternMs.size)

        for (i in patternMs.indices) {
            val isOn = (i % 2 == 1)
            arr[i] = if (isOn) amp else 0
        }
        return arr
    }
}
