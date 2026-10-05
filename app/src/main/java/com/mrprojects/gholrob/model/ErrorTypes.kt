package com.mrprojects.gholrob.model

enum class ErrorTypes(val key: String) {
    COIN_NOT_ENOUGH("متاسفانه سکه هات کافی نیست"),
    HEARTS_ARE_FULL("انرژیت پره پره!"),
    MAX_HEART_REACHED("حداکثر تعداد انرژی روزانه 5 عدد می\u200Cباشد"),

    MAX_BONUS_ENERGY_REACHED("برای دریافت انرژی رایگان تا پایان زمان صبر نمایید"),

    MAX_BONUS_COIN_REACHED("برای دریافت سکه رایگان تا پایان زمان صبر نمایید"),
    MAX_BONUS_EYE_REACHED("برای دریافت چشم عقاب رایگان تا پایان زمان صبر نمایید"),
    MAX_BONUS_SHIELD_REACHED("برای دریافت سپر رایگان تا پایان زمان صبر نمایید"),
}