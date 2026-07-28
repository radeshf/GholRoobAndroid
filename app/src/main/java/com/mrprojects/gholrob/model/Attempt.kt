package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.model.play.GameKill


class Attempt {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("attempt_number")
    var attemptNumber: Int = 0
    @SerializedName("hearts")
    var hearts: Int = 0
    @SerializedName("score")
    var score: Int = 0
    @SerializedName("reward_coin")
    var rewardCoin: Int = 0
    @SerializedName("total_hearts")
    var totalHearts: Int = 0
    @SerializedName("is_passed")
    var isPassed: Boolean = false
    @SerializedName("is_show_passed_dialog")
    var isShowPassedDialog: Boolean = false
    @SerializedName("is_show_fail_dialog")
    var isShowFailDialog: Boolean = false
    @SerializedName("defeated_monsters")
    var defeatedMonsters: Int = 0
    @SerializedName("speed")
    var speed: String = ""
    @SerializedName("profile")
    var profile: User = User()
    @SerializedName("killed_by")
    var killedBy: GameCell? = null
    @SerializedName("cells")
    var cells: ArrayList<GameCell> = arrayListOf()
    @SerializedName("kills")
    var kills: ArrayList<GameKill> = arrayListOf()

    constructor() {
    }

    constructor(hearts: Int, totalHearts: Int, cells: ArrayList<GameCell>) {
        this.hearts = hearts
        this.totalHearts = totalHearts
        this.cells = cells
    }

    companion object {
        fun tutorialBottomlessPit(): Attempt {
            val cells = arrayListOf<GameCell>()

            return Attempt(
                hearts = 6,
                totalHearts = 6,
                cells = cells,
            )

        }
        fun tutorialZero(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.BOTTOMLESS_PIT, true, false))
            cells.add(GameCell(5, 4, CellTypes.SMALL_BOSS, true, false))
            cells.add(GameCell(5, 5, CellTypes.BOTTOMLESS_PIT, true, false))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY))
            cells.add(GameCell(6, 3, CellTypes.HEART,true, false))
            cells.add(GameCell(5, 4, CellTypes.BIG_BOSS, true, false))
            cells.add(GameCell(6, 5, CellTypes.HEART,true, false))
            cells.add(GameCell(6, 6, CellTypes.EMPTY))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.BOTTOMLESS_PIT,true, false))
            cells.add(GameCell(7, 4, CellTypes.FINAL_BOSS,true, false))
            cells.add(GameCell(7, 5, CellTypes.BOTTOMLESS_PIT,true, false))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.EMPTY))

            return Attempt(
                hearts = 6,
                totalHearts = 6,
                cells = cells,
            )

        }
        fun tutorialKill(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 4, CellTypes.BUG, true))
            cells.add(GameCell(5, 5, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY))
            cells.add(GameCell(6, 3, CellTypes.BLUE_GHOST,true, false))
            cells.add(GameCell(6, 4, CellTypes.EMPTY,true, true))
            cells.add(GameCell(6, 5, CellTypes.BLUE_GHOST,true, true))
            cells.add(GameCell(6, 6, CellTypes.EMPTY))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.EMPTY,true, true))
            cells.add(GameCell(7, 4, CellTypes.RAT,true))
            cells.add(GameCell(7, 5, CellTypes.EMPTY,true, true))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.EMPTY))

            return Attempt(
                hearts = 6,
                totalHearts = 6,
                cells = cells,
            )

        }
        fun tutorialHeart(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 4, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 5, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY))
            cells.add(GameCell(6, 3, CellTypes.SNAKE,true, false))
            cells.add(GameCell(6, 4, CellTypes.SMALL_BOSS,true, false))
            cells.add(GameCell(6, 5, CellTypes.SNAKE,true, false))
            cells.add(GameCell(6, 6, CellTypes.EMPTY))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.HEART,true, false))
            cells.add(GameCell(7, 4, CellTypes.HEART,true))
            cells.add(GameCell(7, 5, CellTypes.HEART,true, false))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.EMPTY))

            return Attempt(
                hearts = 0,
                totalHearts = 6,
                cells = cells,
            )

        }
        fun tutorialFlag(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.EMPTY, true, true ))
            cells.add(GameCell(5, 4, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 5, CellTypes.EMPTY, true, true ))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY, true, true))
            cells.add(GameCell(6, 3, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 4, CellTypes.BOTTOMLESS_PIT,true, false))
            cells.add(GameCell(6, 5, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 6, CellTypes.EMPTY, true, true))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(7, 4, CellTypes.EMPTY,true, true))
            cells.add(GameCell(7, 5, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.BOTTOMLESS_PIT))

            return Attempt(
                hearts = 10,
                totalHearts = 10,
                cells = cells,
            )

        }
        fun tutorialEye(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.HEART, true, false))
            cells.add(GameCell(5, 4, CellTypes.EMPTY, true, true))
            cells.add(GameCell(5, 5, CellTypes.EMPTY, true, true ))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY))
            cells.add(GameCell(6, 3, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 4, CellTypes.EYE,true, false))
            cells.add(GameCell(6, 5, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 6, CellTypes.EMPTY))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(7, 4, CellTypes.EMPTY,true, true))
            cells.add(GameCell(7, 5, CellTypes.PURPLE_GHOST,true, false ))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.EMPTY))

            return Attempt(
                hearts = 10,
                totalHearts = 10,
                cells = cells,
            )

        }
        fun tutorialFinal(): Attempt {
            val cells = arrayListOf<GameCell>()
            cells.add(GameCell(5, 1, CellTypes.EMPTY))
            cells.add(GameCell(5, 2, CellTypes.EMPTY))
            cells.add(GameCell(5, 3, CellTypes.HEART, true, false))
            cells.add(GameCell(5, 4, CellTypes.BIG_BOSS, true, false))
            cells.add(GameCell(5, 5, CellTypes.EMPTY, true, true ))
            cells.add(GameCell(5, 6, CellTypes.EMPTY))
            cells.add(GameCell(5, 7, CellTypes.EMPTY))

            cells.add(GameCell(6, 1, CellTypes.EMPTY))
            cells.add(GameCell(6, 2, CellTypes.EMPTY))
            cells.add(GameCell(6, 3, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 4, CellTypes.BOTTOMLESS_PIT,true, false))
            cells.add(GameCell(6, 5, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(6, 6, CellTypes.EMPTY))
            cells.add(GameCell(6, 7, CellTypes.EMPTY))

            cells.add(GameCell(7, 1, CellTypes.EMPTY))
            cells.add(GameCell(7, 2, CellTypes.EMPTY))
            cells.add(GameCell(7, 3, CellTypes.HEART,true, false ))
            cells.add(GameCell(7, 4, CellTypes.FINAL_BOSS,true, false))
            cells.add(GameCell(7, 5, CellTypes.EMPTY,true, true ))
            cells.add(GameCell(7, 6, CellTypes.EMPTY))
            cells.add(GameCell(7, 7, CellTypes.EMPTY))

            return Attempt(
                hearts = 3,
                totalHearts = 10,
                cells = cells,
            )

        }
    }
}

