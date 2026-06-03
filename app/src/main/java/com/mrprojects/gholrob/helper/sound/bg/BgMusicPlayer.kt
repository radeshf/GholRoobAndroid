package com.mrprojects.gholrob.helper.sound.bg

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes
import ir.radesh.basemodule.helper.PrefHelper

class BgMusicPlayer(
    context: Context
) {
    private val appContext = context.applicationContext
    private val pref = PrefHelper(appContext)
    private var mediaPlayer: MediaPlayer? = null


    fun play(@RawRes rawId: Int, loop: Boolean = true) {
        stop()

        mediaPlayer = MediaPlayer.create(appContext, rawId)?.apply {
            isLooping = loop

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            val volume = pref.bgMusicVolume
            setVolume(volume, volume)

            setOnErrorListener { _, _, _ ->
                stop()
                true
            }

            start()
        }
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.apply {
            try {
                if (isPlaying) stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            release()
        }
        mediaPlayer = null
    }

    fun release() {
        stop()
    }

    fun setVolume(volume: Float) {
        val safeVolume = volume.coerceIn(0f, 1f)
        pref.bgMusicVolume = safeVolume
        mediaPlayer?.setVolume(safeVolume, safeVolume)
    }


    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}