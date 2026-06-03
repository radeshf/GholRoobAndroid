package com.mrprojects.gholrob.model

import com.mrprojects.gholrob.R

enum class CellTypes(
    val id: Int,
    val key: String,
    val image: Int,
    val title: String? = null,
    val damage: Int? = null,
    val count: Int? = null,
    val description: String? = null,
    val msg: String? = null,
) {

    SMALL_BOSS(
        id = 1,
        key = "small_boss",
        image = R.drawable.ig_enemy_zahak,
        title = "ضحاک",
        damage = 6,
        count = 1,
        description = "اولین غولی که باید از پسش بر بیایی؛ پادشاهی بی\u200Cرحم با مارهایی بر دوش.",
        msg = "غول اول",
    ),

    BIG_BOSS(
        id = 2,
        key = "big_boss",
        image = R.drawable.ig_enemy_arzhang_troll,
        title = "ارژنگ دیو",
        damage = 10,
        count = 1,
        description = "دومین غولی که سر راهت می\u200Cایستد؛ دیوی از سپاه مازندران که در یکی از خوان\u200Cهای رستم کمین کرده بود.",
        msg = "غول دوم",
    ),

    FINAL_BOSS(
        id = 3,
        key = "final_boss",
        image = R.drawable.ig_enemy_white_troll,
        title = "دیو سپید",
        damage = 15,
        count = 1,
        description = "آخرین غول بازی؛ فرمانروای هولناکِ خوانِ آخر شاهنامه. شکستش یعنی پایان بازی و پیروزی تو.",
        msg = "غول سوم",
    ),

    BUG(
        id = 4,
        key = "bug",
        image = R.drawable.ig_enemy_hyena,
        title = "شغال",
        damage = 1,
        count = 5,
        description = "جانوری فرصت\u200Cطلب و مزاحم که در مسیرت پرسه می\u200Cزند؛ اما مطمئنم از پسش برمی\u200Cآیی، چون ضعیف\u200Cترین موجود بازی است.",
        msg = "",
    ),

    EYE(
        id = 5,
        key = "eye",
        image = R.drawable.ig_enemy_desert,
        title = "بیابان",
        damage = 8,
        count = 1,
        description = "بیابانی نفرین\u200Cشده که دیدگان را کور می\u200Cکند و باعث می\u200Cشود خانه\u200Cهای اطراف را نبینی.",
        msg = "عامل کوری",
    ),

    BLUE_GHOST(
        id = 6,
        key = "blue_ghost",
        image = R.drawable.ig_enemy_lion,
        title = "شیر",
        damage = 3,
        count = 4,
        description = "حریفی قوی و بی\u200Cرحم؛ اگر سر راهت باشد، مسیرت را سخت می\u200Cکند.",
        msg = "",
    ),

    PURPLE_GHOST(
        id = 7,
        key = "purple_ghost",
        image = R.drawable.ig_enemy_esfandiyar,
        title = "اسفندیار",
        damage = 7,
        count = 1,
        description = "پهلوانِ رویین\u200Cتن؛ اگر پیش از شکستِ ضحاک به سراغش بروی، باختت حتمی است.",
        msg = "",
    ),

    RED_GHOST(
        id = 8,
        key = "red_ghost",
        image = R.drawable.ig_enemy_dragon,
        title = "اژدها",
        damage = 5,
        count = 3,
        description = "اژدهایی 3 سر و وحشتناک که هر سرش به\u200Cتنهایی کافی است تا جانت را به\u200Cشدت پایین بیاورد.",
        msg = "",
    ),

    RAT(
        id = 10,
        key = "rat",
        image = R.drawable.ig_enemy_wolf,
        title = "گرگ",
        damage = 2,
        count = 5,
        description = "شکارچیِ بیابان و کمی قوی\u200Cتر از شغال؛ اگر حواست نباشد با یک حمله مسیرت را سخت می\u200Cکند.",
        msg = "",
    ),

    SKELETON(
        id = 11,
        key = "skeleton",
        image = R.drawable.ig_enemy_wizard,
        title = "جادوگر",
        damage = 4,
        count = 5,
        description = "موجودی فریب\u200Cکار که زیاد سر راهت پیدا می\u200Cشود و با جادو تلاش می\u200Cکند مسیرت را سخت کند.",
        msg = "",
    ),

    SNAKE(
        id = 12,
        key = "snake",
        image = R.drawable.ig_enemy_snake,
        title = "مار های ضحاک",
        damage = 6,
        count = 2,
        description = "دو سایهٔ خزنده بر شانه\u200Cهای پادشاهی شوم؛ هر روز تشنهٔ جان",
        msg = "",
    ),

    HEART(
        id = 13,
        key = "heart",
        image = R.drawable.ig_heart,
        title = "نوش دارو",
        damage = 0,
        count = 7,
        description = "معجونی حیات\u200Cبخش و کمیاب که می\u200Cتواند زندگی را به شما بازگرداند؛اما اگر در چاهِ شغاد بیفتید، حتی نوش\u200Cدارو هم نجاتتان نمی\u200Cدهد",
        msg = "برای پر کردن جان",
    ),

    BOTTOMLESS_PIT(
        id = 14,
        key = "bottomless_pit",
        image = R.drawable.ig_enemy_shoghad_trap,
        title = "چاه شغاد",
        damage = 100,
        count = 8,
        description = "چاهی پر از نیزه که با نیرنگ شغاد کنده شد؛ همان دامی که پایان کار رستم شد… و شاید پایان بازی تو هم باشد.",
        msg = "مرگ حتمی",
    ),

    EMPTY(
        id = 100,
        key = "empty",
        image = R.drawable.box
    );

    companion object {

        fun fromId(id: Int): CellTypes? =
            entries.find { it.id == id }

        fun fromKey(key: String): CellTypes = entries.find { it.key == key }!!
    }
}
