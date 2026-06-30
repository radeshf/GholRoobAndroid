package com.mrprojects.gholrob.view.profile

import com.mrprojects.gholrob.AppConfig.REWARD_COIN
import com.mrprojects.gholrob.R

enum class Profiles(
    val drawableName: String,
    val drawableRes: Int,
    val titleFa: String,
    val descriptionFa: String,
    val price: Int,
    val isComingSoon: Boolean = false,
) {

    SOHRAB(
        "ig_profile_sohrab", R.drawable.ig_profile_sohrab,
        "سهراب", "پهلوان جوان و فرزند رستم با سرنوشتی تراژیک.", 0, false
    ),

    GORDAFARID(
        "ig_profile_gordafarid", R.drawable.ig_profile_gordafarid,
        "گردآفرید", "دختر جنگجوی ایرانی و نماد شجاعت زنان.", 0, false
    ),

    DERAFSH(
        "ig_profile_derafsh", R.drawable.ig_profile_derafsh,
        "درفش کاویانی", "پرچم اسطوره‌ای ایران و نماد قیام و آزادی.", 0, false
    ),

    GIV(
        "ig_profile_giv", R.drawable.ig_profile_giv,
        "گیو", "پهلوان وفادار ایرانی و یاور کیخسرو.", REWARD_COIN
    ),

    KAVE(
        "ig_profile_kave", R.drawable.ig_profile_kave,
        "کاوه", "آهنگر آزاده که علیه ضحاک قیام کرد.", REWARD_COIN
    ),

    HOMAY(
        "ig_profile_homay", R.drawable.ig_profile_homay,
        "همای", "پادشاهی خردمند از دودمان کیانی.", REWARD_COIN
    ),

    GODARZ(
        "ig_profile_godarz", R.drawable.ig_profile_godarz,
        "گودرز", "پهلوان با تجربه و مشاور خردمند.", REWARD_COIN * 2
    ),


    GORDIE(
        "ig_profile_gordie", R.drawable.ig_profile_gordie,
        "گردیه", "خواهر بهرام چوبین و بانویی جنگاور.", REWARD_COIN * 2
    ),

    SIAVASH(
        "ig_profile_siavash", R.drawable.ig_profile_siavash,
        "سیاوش", "شاهزاده پاک‌دامن و نماد مظلومیت.", REWARD_COIN * 2
    ),

    KEIKHOSRO(
        "ig_profile_keikhosro", R.drawable.ig_profile_keikhosro,
        "کیخسرو", "شاه دادگر و وارث خون سیاوش.", REWARD_COIN * 5
    ),

    AREZO(
        "ig_profile_arezo", R.drawable.ig_profile_arezo,
        "آرزو", "شخصیتی نمادین با مفهوم امید و خواستن.", REWARD_COIN * 5
    ),

    ROSTAM(
        "ig_profile_rostam", R.drawable.ig_profile_rostam,
        "رستم", "بزرگ‌ترین پهلوان شاهنامه و نماد قدرت و وفاداری.", REWARD_COIN * 5
    ),

    SIMORGH(
        "ig_profile_simorgh", R.drawable.ig_profile_simorgh,
        "سیمرغ", "مرغ اسطوره‌ای دانا و یاری‌رسان قهرمانان.", REWARD_COIN * 10
    ),


    FARANGIS(
        "ig_profile_farangis", R.drawable.ig_profile_farangis,
        "فرنگیس", "همسر سیاوش و مادر کیخسرو.", REWARD_COIN * 10
    ),

    TAHMINE(
        "ig_profile_tahmine", R.drawable.ig_profile_tahmine,
        "تهمینه", "مادر سهراب و بانویی خردمند.", REWARD_COIN * 10
    ),

    BIZHAN(
        "ig_profile_bizhan", R.drawable.ig_profile_bizhan,
        "بیژن", "پهلوان ایرانی و قهرمان داستان بیژن و منیژه.", REWARD_COIN * 20
    ),

    MANIZHE(
        "ig_profile_manizhe", R.drawable.ig_profile_manizhe,
        "منیژه", "دختر افراسیاب و عاشق بیژن.", REWARD_COIN * 20
    ),

    RODABEH(
        "ig_profile_rodabeh", R.drawable.ig_profile_rodabeh,
        "رودابه", "مادر رستم و بانویی دلیر و باوقار.", REWARD_COIN * 20
    ),

    BOZORGMEHR(
        "ig_profile_bozorgmehr", R.drawable.ig_profile_bozorgmehr,
        "بزرگمهر", "وزیر دانا و نماد خرد در ایران باستان.", REWARD_COIN * 30
    ),

    GARSHASP(
        "ig_profile_garshasp", R.drawable.ig_profile_garshasp,
        "گرشاسپ", "قهرمان اسطوره‌ای و اژدهاکش ایرانی.", REWARD_COIN * 30
    ),

    SEPINODE(
        "ig_profile_sepinode", R.drawable.ig_profile_sepinode,
        "سپینود", "خواهر رستم و بانویی نجیب.", REWARD_COIN * 30
    ),

    SINDOKHT(
        "ig_profile_sindokht", R.drawable.ig_profile_sindokht,
        "سیندخت", "مادر رودابه و بانویی سیاستمدار.", REWARD_COIN * 50
    ),

    GOLNAR(
        "ig_profile_golnar", R.drawable.ig_profile_golnar,
        "گل‌نار", "شخصیتی لطیف در روایت‌های حماسی.", REWARD_COIN * 50
    ),

    JARIRE(
        "ig_profile_jarire", R.drawable.ig_profile_jarire,
        "جریره", "همسر سیاوش در توران.", REWARD_COIN * 50
    ),


    ANOSHERVAN(
        "ig_profile_anoshirvan", R.drawable.ig_profile_anoshirvan,
        "انوشیروان", "پادشاه ساسانی مشهور به عدالت.", REWARD_COIN * 100
    ),


    ZAL(
        "ig_profile_zal", R.drawable.ig_profile_zal,
        "زال", "پدر رستم که با پرورش سیمرغ بزرگ شد.", REWARD_COIN * 100
    ),


    FEREYDON(
        "ig_profile_fereydon", R.drawable.ig_profile_fereydon,
        "فریدون", "پادشاه دادگر که ضحاک را شکست داد.", REWARD_COIN * 100
    ),


    UNKNOWN(
        "ig_profile_unkown", R.drawable.ig_profile_unknown,
        "نامشخص", "پروفایل پیش‌فرض.", 0
    );

    fun toProfileImage(): ProfileImage {
        return ProfileImage(
            image = this.drawableName,
            drawableRes = this.drawableRes,
            name = this.titleFa,
            description = this.descriptionFa,
            price = this.price,
            isComingSoon = this.isComingSoon,
        )
    }
    companion object {
        fun fromServerName(name: String?): Profiles {
            return entries.firstOrNull { it.drawableName == name } ?: UNKNOWN
        }
        fun getAllAsProfileImages(): List<ProfileImage> {
            return entries.filter { it != UNKNOWN }.map { it.toProfileImage() }
        }
    }
}
