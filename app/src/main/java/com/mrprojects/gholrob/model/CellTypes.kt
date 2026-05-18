import com.mrprojects.gholrob.R

enum class CellTypes(
    val id: Int,
    val key: String,
    val image: Int,
    val title: String? = null,
    val damage: Int? = null,
    val count: Int? = null,
) {

    SMALL_BOSS(
        id = 1,
        key = "small_boss",
        image = R.drawable.ig_enemy_zahak,
        title = "ضحاک",
        damage = 6,
        count = 1
    ),

    BIG_BOSS(
        id = 2,
        key = "big_boss",
        image = R.drawable.ig_enemy_arzhang_troll,
        title = "ارژنگ دیو",
        damage = 10,
        count = 1
    ),

    FINAL_BOSS(
        id = 3,
        key = "final_boss",
        image = R.drawable.ig_enemy_white_troll,
        title = "دیو سپید",
        damage = 15,
        count = 1
    ),

    BUG(
        id = 4,
        key = "bug",
        image = R.drawable.ig_enemy_hyena,
        title = "شغال",
        damage = 1,
        count = 5
    ),

    EYE(
        id = 5,
        key = "eye",
        image = R.drawable.ig_enemy_desert,
        title = "بیابان",
        damage = 8,
        count = 1
    ),

    BLUE_GHOST(
        id = 6,
        key = "blue_ghost",
        image = R.drawable.ig_enemy_lion,
        title = "شیر",
        damage = 3,
        count = 4
    ),

    PURPLE_GHOST(
        id = 7,
        key = "purple_ghost",
        image = R.drawable.ig_enemy_esfandiyar,
        title = "اسفندیار",
        damage = 7,
        count = 1
    ),

    RED_GHOST(
        id = 8,
        key = "red_ghost",
        image = R.drawable.ig_enemy_dragon,
        title = "اژدها",
        damage = 5,
        count = 3
    ),

    RAT(
        id = 10,
        key = "rat",
        image = R.drawable.ig_enemy_wolf,
        title = "گرگ",
        damage = 2,
        count = 5
    ),

    SKELETON(
        id = 11,
        key = "skeleton",
        image = R.drawable.ig_enemy_wizard,
        title = "جادوگر",
        damage = 4,
        count = 5
    ),

    SNAKE(
        id = 12,
        key = "snake",
        image = R.drawable.ig_enemy_snake,
        title = "مار های ضحاک",
        damage = 6,
        count = 2
    ),

    HEART(
        id = 13,
        key = "heart",
        image = R.drawable.ig_heart,
        title = "نوش دارو",
        damage = 0,
        count = 7
    ),

    BOTTOMLESS_PIT(
        id = 14,
        key = "bottomless_pit",
        image = R.drawable.ig_enemy_shoghad_trap,
        title = "چاه شغاد",
        damage = 100,
        count = 8
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
