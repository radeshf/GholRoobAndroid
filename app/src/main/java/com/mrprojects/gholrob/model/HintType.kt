package com.mrprojects.gholrob.model

enum class HintType(val key: String) {
    HINT_AFTER_FAILED("hint_after_failed"),
    ADD_TIME("add_time"),

    ADD_HEART("add_heart"),
    ADD_HEART_AFTER_FAIL("add_heart_after_fail"),
    ADD_HEART_IN_MAIN("add_heart_in_main"),
    DOUBLE_COIN("double_coin"),
    FILL_ALL_HEARTS("fill_all_hearts"),
    EXTRA_HEARTS("extra_hearts"),

    WIN_REWARD("win_reward"),

    BUY_COIN_1("buy_coin_1"),
    BUY_COIN_2("buy_coin_2"),
    BUY_COIN_3("buy_coin_3"),
}