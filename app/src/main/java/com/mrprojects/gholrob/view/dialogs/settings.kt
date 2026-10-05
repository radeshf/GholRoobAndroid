package com.mrprojects.gholrob.view.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.SeekBar
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.SettingsDialogBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.haptics.OnVibrationsSettingsChanged
import com.mrprojects.gholrob.helper.sound.bg.OnBgMusicSettingsChanged
import com.mrprojects.gholrob.helper.sound.sfx.OnSfxMusicSettingsChanged
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.helper.PrefHelper


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showSettingsDialog() {
    val binding = SettingsDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    val isBgMusicOn = PrefHelper(requireContext()).isBgMusicOn
    val isSfxMusicOn = PrefHelper(requireContext()).isSfxMusicOn
    val isVibrationsOn = PrefHelper(requireContext()).isVibrationsOn

    binding.tvBg.text = if (isBgMusicOn) "(فعال)" else "(غیر فعال)"
    binding.ivBg.setImageResource(if (isBgMusicOn) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
    binding.sbBg.progress = (PrefHelper(requireContext()).bgMusicVolume * 100).toInt()
    binding.sbSfx.progress = (PrefHelper(requireContext()).sfxMusicVolume * 100).toInt()

    binding.tvSfx.text = if (isSfxMusicOn) "(فعال)" else "(غیر فعال)"
    binding.ivSfx.setImageResource(if (isSfxMusicOn) R.drawable.ig_sound_on else R.drawable.ig_sound_off)

    binding.tvVibrate.text = if (isVibrationsOn) "(فعال)" else "(غیر فعال)"
    binding.ivVibrate.setImageResource(if (isVibrationsOn) R.drawable.ig_vibrate_on else R.drawable.ig_vibrate_off)

    binding.ivBg.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isBgMusicOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isBgMusicOn = newSettings
        binding.tvBg.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivBg.setImageResource(if (newSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
        postEvent(OnBgMusicSettingsChanged(isVolumeChanged = false, isStatusChanged = true))
    }

    binding.ivSfx.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isSfxMusicOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isSfxMusicOn = newSettings
        binding.tvSfx.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivSfx.setImageResource(if (newSettings) R.drawable.ig_sound_on else R.drawable.ig_sound_off)
        postEvent(OnSfxMusicSettingsChanged(isVolumeChanged = false, isStatusChanged = true))

    }


    binding.ivVibrate.setOnClickListener {
        val currentSettings = PrefHelper(requireContext()).isVibrationsOn
        val newSettings = !currentSettings
        PrefHelper(requireContext()).isVibrationsOn = newSettings
        binding.tvVibrate.text = if (newSettings) "(فعال)" else "(غیر فعال)"
        binding.ivVibrate.setImageResource(if (newSettings) R.drawable.ig_vibrate_on else R.drawable.ig_vibrate_off)
        postEvent(OnVibrationsSettingsChanged(isStatusChanged = true))

    }

    binding.sbBg.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                postEvent(OnBgMusicSettingsChanged(isVolumeChanged = true, isStatusChanged = false, volume=progress / 100f))
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })

    binding.sbSfx.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                postEvent(OnSfxMusicSettingsChanged(isVolumeChanged = true, isStatusChanged = false, volume=progress / 100f))

            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })

    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}
