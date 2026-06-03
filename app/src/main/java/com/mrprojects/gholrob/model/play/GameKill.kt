package com.mrprojects.gholrob.model.play

import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.model.CellTypes

class GameKill {
    @SerializedName("id") var id: Int = 0
    @SerializedName("type") var type: String = ""
    @SerializedName("name") var name: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("damage") var damage: Int = 0
    @SerializedName("killed") var killed: Int = 0
    @SerializedName("remained") var remained: Int = 0
    @SerializedName("total") var total: Int = 0

    override fun toString(): String {
        return "GameCell(" +
                "id=$id, " +
                "name=$name, " +
                "type=$type, " +
                "description=$description, " +
                "damage=$damage, " +
                "killed=$killed, " +
                "remained=$remained, " +
                "remained=$total, " +
                ")"
    }

    fun image() = CellTypes.fromKey(this.type).image

    companion object {
        fun fromCellType(cellType: CellTypes, id: Int = 0): GameKill {
            return GameKill().apply {
                this.id = id
                this.type = cellType.key
                this.name = cellType.title.orEmpty()
                this.description = cellType.description.orEmpty()
                this.damage = cellType.damage ?: 0
                this.total = cellType.count ?: 0
            }
        }
    }
}

