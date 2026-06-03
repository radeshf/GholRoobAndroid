package com.mrprojects.gholrob.helper.sound.sfx

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mrprojects.gholrob.helper.sound.sfx.SfxTypes
import ir.radesh.basemodule.helper.PrefHelper

class SfxPlayer(context: Context) {

    private val appContext = context.applicationContext
    private val pref = PrefHelper(appContext)

    private val soundPool: SoundPool by lazy {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    private val soundIds = HashMap<Int, Int>() // resId -> soundId
    private var currentStreamId: Int? = null

    fun preload(type: SfxTypes) {
        val resId = type.resId
        if (soundIds.containsKey(resId)) return
        soundIds[resId] = soundPool.load(appContext, resId, 1)
    }

    fun preloadAll() {
        SfxTypes.entries.forEach { preload(it) }
    }

    fun play(type: SfxTypes) {
        stop()

        val resId = type.resId
        val soundId = soundIds[resId] ?: run {
            val loaded = soundPool.load(appContext, resId, 1)
            soundIds[resId] = loaded
            loaded
        }

        val volume = pref.sfxMusicVolume.coerceIn(0f, 1f)
        val streamId = soundPool.play(soundId, volume, volume, 1, 0, 1f)
        currentStreamId = streamId.takeIf { it != 0 }
    }

    fun stop() {
        currentStreamId?.let(soundPool::stop)
        currentStreamId = null
    }

    fun setVolume(volume: Float) {
        val safe = volume.coerceIn(0f, 1f)
        pref.sfxMusicVolume = safe
        currentStreamId?.let { soundPool.setVolume(it, safe, safe) }
    }

    fun release() {
        stop()
        soundIds.clear()
        soundPool.release()
    }
}