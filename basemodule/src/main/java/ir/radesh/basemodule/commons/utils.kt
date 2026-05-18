package ir.radesh.basemodule.commons

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.location.LocationManager
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity


fun getAppVersion(context: Context): String {
    var version = "-1"
    doOnTry({
        version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    })

    return version
}

fun checkGps(ctx: Context): Boolean {
    val lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled: Boolean
    try {
        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (ex: Exception) {
        return false
    }

    return gpsEnabled
}
fun checkNetworkGps(ctx: Context): Boolean {
    val lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gps_enabled: Boolean
    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
        return false
    }

    return gps_enabled
}

fun callToNumber(c: Context, number: String?) {
    doOnTry({
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        c.startActivity(intent)
    })
}

fun convertDigitsToLatin(s: String): String {
    val sb = StringBuilder()
    for (i in s.indices) {
        when (s[i]) {
            //Persian digits
            '\u06f0' -> sb.append('0')
            '\u06f1' -> sb.append('1')
            '\u06f2' -> sb.append('2')
            '\u06f3' -> sb.append('3')
            '\u06f4' -> sb.append('4')
            '\u06f5' -> sb.append('5')
            '\u06f6' -> sb.append('6')
            '\u06f7' -> sb.append('7')
            '\u06f8' -> sb.append('8')
            '\u06f9' -> sb.append('9')
            else -> sb.append(s[i])
        }
    }
    return sb.toString()
}

/**
 * share with app sharing chooser
 */
fun share(context: Context, shareText: String){
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "معرفی")
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText)
    context.startActivity(Intent.createChooser(sharingIntent, "معرفی"))
}

fun fontChanger(view: View, typeface: Typeface) {
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            fontChanger(child, typeface)
        }
    } else if (view is TextView) {
        view.typeface = typeface
    }
}


