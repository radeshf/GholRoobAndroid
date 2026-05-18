package com.mrprojects.gholrob.model

import CellTypes
import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.AppConfig

class GameCell {
    @SerializedName("id") var id: Int = 0
    @SerializedName("type") var type: String = ""
    @SerializedName("index") var index: Int = 0
    @SerializedName("row") var row: Int = 0
    @SerializedName("col") var col: Int = 0
    @SerializedName("is_revealed") var isRevealed: Boolean = false
    @SerializedName("is_defeated") var isDefeated: Boolean = false
    @SerializedName("is_flagged") var isFlagged: Boolean = false
    @SerializedName("revealed_at") var revealedAt: String = ""
    @SerializedName("damage") var damage: Int = 0
    @SerializedName("name") var name: String = ""

    var isLoading = false

    override fun toString(): String {
        return "GameCell(" +
                "id=$id, " +
                "type=$type, " +
                "isLoading=$isLoading, " +
                "index=$index, " +
                "row=$row, " +
                "col=$col, " +
                "isRevealed=$isRevealed, " +
                "revealedAt='$revealedAt', " +
                "damage=$damage, " +
                "name='$name'" +
                ")"
    }

    fun isDessert() = CellTypes.fromKey(this.type) == CellTypes.EYE
    fun isEmpty() = CellTypes.fromKey(this.type) == CellTypes.EMPTY
    fun isHeart() = CellTypes.fromKey(this.type) == CellTypes.HEART
    fun isBottomLessPit() = CellTypes.fromKey(this.type) == CellTypes.BOTTOMLESS_PIT
    fun image() = CellTypes.fromKey(this.type).image

    fun getTotalDamage(cells: List<GameCell>): Int{
        val currentRow = this.row
        val currentCol = this.col
        val aroundCells = cells.filter { cell ->
            val isInRowRange = cell.row in (currentRow - 1)..(currentRow + 1)
            val isInColRange = cell.col in (currentCol - 1)..(currentCol + 1)
            val isNotCurrent = cell.id != this.id

            isInRowRange && isInColRange && isNotCurrent
        }
        var totalDamage = 0
        for (cell in aroundCells){
            if (!cell.isDefeated){
                totalDamage += cell.damage
            }
            if (cell.isDessert() && !cell.isDefeated)
                return AppConfig.HIDDEN_KEY
        }
        return totalDamage

    }
}

