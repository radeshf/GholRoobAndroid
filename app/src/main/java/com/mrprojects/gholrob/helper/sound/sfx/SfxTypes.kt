package com.mrprojects.gholrob.helper.sound.sfx

import androidx.annotation.RawRes
import com.mrprojects.gholrob.R

enum class SfxTypes(@RawRes val resId: Int) {
    Boss1(R.raw.sfx_boss1),
    Boss2(R.raw.sfx_boss2),
    Boss3(R.raw.sfx_boss3),
    DefeatEmpty(R.raw.sfx_defeat_empty),
    DefeatEnemy(R.raw.sfx_defeat_enemy),
    ClickOnHeal(R.raw.sfx_click_on_heal),
    Lose(R.raw.sfx_lose),
    Win(R.raw.sfx_win),
    Pit(R.raw.sfx_pit),
    FlagOn(R.raw.sfx_click_on_flag),
    FlagOff(R.raw.sfx_click_off_flag),
}