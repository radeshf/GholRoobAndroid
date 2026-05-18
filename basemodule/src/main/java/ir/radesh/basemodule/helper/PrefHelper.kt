package ir.radesh.basemodule.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * created by radesh farokh manesh
 */
class PrefHelper(context: Context) {
    private var shp: SharedPreferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE)

    var userId: Int
        get() = shp.getInt("userId",-1)
        set(value) = shp.edit().putInt("userId",value).apply()

    var username: String
        get() = shp.getString("username","")!!
        set(value) = shp.edit().putString("username",value).apply()

    var userHash: String
        get() = shp.getString("-O8w9,_s6+^i-O8w9,_s6+^i","")!!
        set(value) = shp.edit().putString("-O8w9,_s6+^i-O8w9,_s6+^i",value).apply()


    var token: String
        get() = shp.getString("token","")!!
        set(value) = shp.edit().putString("token",value).apply()


    var isSoundSettingsOn: Boolean
        get() = shp.getBoolean("isSoundSettingsOn",true)
        set(value) = shp.edit().putBoolean("isSoundSettingsOn",value).apply()


}