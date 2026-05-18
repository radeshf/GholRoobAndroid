package ir.radesh.basemodule.helper

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import ir.radesh.basemodule.commons.doOnTry
import timber.log.Timber

class PlaySoundHelper(var seekBar: SeekBar?, val listener: Listener) {
    private var mediaPlayer: MediaPlayer? = null
    private val delay = 200
    private var isInPlay = false
    private var startTime = 0.0
    private var finalTime = 0.0

    private val mHandler = Handler(Looper.getMainLooper())
    //Make sure driverUser update Seekbar on UI thread
    private val updateSongTime = object : Runnable {
        override fun run() {
            startTime = mediaPlayer?.currentPosition?.toDouble()!!
            seekBar?.progress = startTime.toInt()
            if (startTime > finalTime) {
                mHandler.removeCallbacks(this)
            } else {
                mHandler.postDelayed(this, delay.toLong())
            }
        }
    }

    fun addSeekBar(seekBar: SeekBar){
        this.seekBar = seekBar
    }

    fun playVoice(path: String?) {
        if (path == null){
            Timber.e("path can not be null")
            return
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        doOnTry({
            if (isInPlay) {
                mHandler.removeCallbacks(updateSongTime)
                mediaPlayer?.reset()
            }
            mediaPlayer?.setDataSource(path)

            mediaPlayer?.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
                finalTime = mediaPlayer.duration.toDouble()
                startTime = mediaPlayer.currentPosition.toDouble()
                seekBar?.max = finalTime.toInt()
                seekBar?.progress = startTime.toInt()
                mHandler.postDelayed(updateSongTime, delay.toLong())
                isInPlay = true
            }
            mediaPlayer?.prepareAsync()
            seekBarConfig()
            mediaPlayer?.setOnCompletionListener { _ -> stopVoice() }
        })
    }

    fun playVoice(rawId: Int) {
        if (isInPlay) {
            mHandler.removeCallbacks(updateSongTime)
            mediaPlayer?.reset()
            mediaPlayer = null
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(seekBar?.context, rawId)
            mediaPlayer?.let {
                it.start()
                finalTime = it.duration.toDouble()
                startTime = it.currentPosition.toDouble()

            }
            seekBar?.max = finalTime.toInt()
            seekBar?.progress = startTime.toInt()
            mHandler.postDelayed(updateSongTime, delay.toLong())
            isInPlay = true
            seekBarConfig()
            mediaPlayer?.setOnCompletionListener { _ ->
                listener.onPlaySoundCompleted()
            }
        }
    }

    fun stopVoice() {
        doOnTry({
            mHandler.removeCallbacks(updateSongTime)
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            isInPlay = false
        })
    }

    fun toggle(){
        Timber.e( "isInPlay $isInPlay $mediaPlayer")
        isInPlay = if (isInPlay){
            mediaPlayer?.pause()
            false
        } else{
            mediaPlayer?.start()
            true
        }
    }
    fun pause(){
        mediaPlayer?.pause()
    }

    fun resume(){
        mediaPlayer?.start()
    }

    fun isPlaying(): Boolean {
        return isInPlay
    }

    private fun seekBarConfig(){
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    fun destroy(){
        mHandler.removeCallbacks(updateSongTime)
    }

    interface Listener{
        fun onPlaySoundCompleted()
    }
}