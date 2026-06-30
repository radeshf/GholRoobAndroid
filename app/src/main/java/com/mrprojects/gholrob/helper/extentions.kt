@file:JvmName("ExtensionsUtils")

package com.mrprojects.gholrob.helper

import android.animation.Animator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.RelativeSizeSpan
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.mrprojects.gholrob.App
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.BuildConfig
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.HeartsLayoutBinding
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.rest.AddHintPost
import com.mrprojects.gholrob.view.play.PlayFragment
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.changeTo
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.dpToPx
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.DialogHelper
import timber.log.Timber

import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

fun Fragment.openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    parentFragmentManager.changeTo(R.id.mainContainer, fragment, addToBackStack = addToBackStack, animationGravity = Gravity.CENTER)
}


fun AppCompatActivity.openFragment(fragment: Fragment) {
    supportFragmentManager.changeTo(R.id.mainContainer, fragment, true, Gravity.CENTER, true)
}

fun RecyclerView.init(lm: RecyclerView.LayoutManager) {
    setHasFixedSize(true)
    layoutManager = lm
}


fun RecyclerView.init(isVertical: Boolean = true) {
    setHasFixedSize(true)
    vertical(isVertical)
}


fun RecyclerView.vertical(isVertical: Boolean) {
    layoutManager = when {
        isVertical -> LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        else -> LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
    }
}


fun doOnTry(func: () -> Unit, onCatch: (() -> Unit)? = null) {
    try {
        func()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        onCatch?.invoke()
    }
}

fun isAbove(SDK: Int): Boolean {
    return Build.VERSION.SDK_INT >= SDK
}


fun TextView.setTextWithAnimation(text: String, duration: Long = 60L) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        this@setTextWithAnimation.text = text
        animate().setDuration(duration).alpha(1f)
    }
}


fun getDimenForView(context: Context, id: Int): Int = (context.resources.getDimension(id) / context.resources.displayMetrics.density).toInt()


fun View.changeVisibility(visibility: Int, direction: Int = 0) {
    val duration = 300

    if (visibility == View.GONE) {
        animate()
            .translationY(
                when (direction) {
                    Gravity.TOP -> -height.toFloat()
                    Gravity.BOTTOM -> height.toFloat()
                    else -> 0f
                }
            )
            .translationX(
                when (direction) {
                    Gravity.START -> -width.toFloat()
                    Gravity.END -> width.toFloat()
                    else -> 0f
                }
            )
            .alpha(0.0f)
            .setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    this@changeVisibility.visibility = visibility
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
    } else if (visibility == View.VISIBLE) {
        alpha = 0f
        animate()
            .translationY(0f)
            .translationX(0f)
            .alpha(1.0f)
            .setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    this@changeVisibility.visibility = visibility
                }

                override fun onAnimationEnd(animator: Animator) {

                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
    }


}


fun SimpleDraweeView.loadWebpIcon(name: String) {
    loadWebp("icons", name, animate = true)
}


fun SimpleDraweeView.loadWebp(folder: String, name: String, animate: Boolean = false) {
    val uri = context.getAssetsFile(folder, name).toUri()

    val imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
//    if (animate) imageRequestBuilder.resizeOptions = ResizeOptions(50, 50)

    this.controller = Fresco.newDraweeControllerBuilder()
        .setOldController(this.controller)
        .setAutoPlayAnimations(animate)
        .setImageRequest(imageRequestBuilder.build())
        .setControllerListener(object : ControllerListener<ImageInfo> {
            override fun onFailure(id: String?, throwable: Throwable?) {

            }

            override fun onRelease(id: String?) {

            }

            override fun onSubmit(id: String?, callerContext: Any?) {
            }

            override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
            }

            override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
            }

            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {

            }

        })
        .build()
}


private fun getStickerAssetFilePath(folder: String, name: String): String {
    return "StickerWhatsapp/${folder}/${name}"
}

private fun getIconAssetFilePath(name: String): String {
    return "icons/${name}"
}


@Throws(IOException::class)
fun Context.getAssetsFile(folder: String, name: String): File {
    val uniqueName = "${folder}_$name"
    val f = File(cacheDir, uniqueName)
    var filePath = getStickerAssetFilePath(folder, name)
    if (folder == "icons") {
        filePath = getIconAssetFilePath(name)
    }
    f.outputStream().use { cache -> assets.open(filePath).use { it.copyTo(cache) } }
    return f
}

fun <B : ViewBinding> BaseFragment<B>.initToolbar(
    title: String = "",
    showBack: Boolean = true,
    showToolbar: Boolean = true,
    showAction: Boolean = false,
    onActionClicked: (() -> Unit)? = null
) {
    val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
    val toolbarTitle = binding.root.findViewById<AppCompatTextView>(R.id.tvTitle)
    val backButton = binding.root.findViewById<AppCompatImageButton>(R.id.btnBack)
    val actionButton = binding.root.findViewById<Button>(R.id.btnAction)

    toolbar.visibleByBoolean(showToolbar)
    actionButton.visibleByBoolean(showAction)
    backButton.visibleByBoolean(showBack)

    toolbarTitle.text = title
    backButton.setOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
    actionButton.setOnClickListener {
        onActionClicked?.invoke()
    }

}

fun Context.openMarketRatePage() {
    val packageName = BuildConfig.APPLICATION_ID
    when (BuildConfig.MARKET.lowercase()) {
        "bazaar" -> {
            val uri = "bazaar://details?id=$packageName".toUri()
            doOnTry({
                startActivity(Intent(Intent.ACTION_EDIT, uri).apply { setPackage("com.farsitel.bazaar") })
            }, {

                showToast("اپلیکیشن کافه بازار یافت نشد")
            })

        }

        "myket" -> {
            val uri = "myket://comment?id=$packageName".toUri()
            doOnTry({
                startActivity(Intent(Intent.ACTION_VIEW, uri).apply { setPackage("ir.mservices.market") })
            }, {
                showToast("اپلیکیشن مایکت یافت نشد")
            })
        }

    }

}

fun getDeviceUsername(): String {
    val deviceId = Settings.Secure.getString(App.instance.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
    return "${AppConfig.USERNAME_PREFIX}-$deviceId"
}

fun HeartsLayoutBinding.updateHearts(user: User, showAdd: Boolean = false, showTimer: Boolean = false) {
    val userCurrentHeart = user.currentHearts
    val userMaxHearts = user.maxHearts
    tvHeartCount.text = userCurrentHeart.toString()
    ivHeartAdd.visibleByBoolean(showAdd)
    tvHeartTimer.visibleByBoolean(showTimer && userCurrentHeart != userMaxHearts)

    val hearts = arrayListOf(ivHeart1, ivHeart2, ivHeart3)
    ivHeart4.visibleByBoolean(userMaxHearts >= 4)
    ivHeart5.visibleByBoolean(userMaxHearts >= 5)
    if (userMaxHearts >= 4) hearts.add(ivHeart4)
    if (userMaxHearts >= 5) hearts.add(ivHeart5)
    hearts.forEachIndexed { index, view ->
        view.disableAlphaByBoolean(index < userCurrentHeart)
    }


}


fun <B : ViewBinding> BaseFragment<B>.submitHint(puzzleId: Int?, hintType: String, coin: Int=0, isCoinSpend: Boolean? = null) {
    val spendStatus = isCoinSpend?.toString() ?: "null"
    val body = AddHintPost(puzzleId, hintType, coin, spendStatus)

//    Provider.provideApiHelper(this).addHint(body) {
//        Timber.e("hint $hintType added to server")
//    }
}

fun <B : ViewBinding> BaseFragment<B>.openNextLevel(gameId: Int, isMain: Boolean): Boolean {

    val fragment = PlayFragment.newInstance(gameId)
    val addToBackStack = if(isMain) true else false
    parentFragmentManager.changeTo(R.id.mainContainer, fragment, addToBackStack = addToBackStack, animationGravity = Gravity.CENTER, addToContainer = false)
    return true
}


fun TextView.setTutorialText(bigImage: Boolean=false) {
    val inputText = context.getString(R.string.tutorial_game_guide)
    val spannable = SpannableStringBuilder(inputText)
    data class StyleConfig(@ColorRes val colorRes: Int, val size: Float)

    val styles = mapOf(
        "ایرانی" to StyleConfig(R.color.green, 1.3f),
        "ضحاک" to StyleConfig(R.color.enemy_color, 1.3f),
        "ارژنگ دیو" to StyleConfig(R.color.enemy_color, 1.3f),
        "دیو سپید" to StyleConfig(R.color.enemy_color, 1.3f),
        "چاه‌های شغاد" to StyleConfig(R.color.redPrimary, 1.3f)
    )

    styles.forEach { (word, config) ->
        var startIndex = inputText.indexOf(word)
        while (startIndex != -1) {
            val endIndex = startIndex + word.length
            val color = ContextCompat.getColor(context, config.colorRes)
            spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(config.size), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            startIndex = inputText.indexOf(word, endIndex)
        }
    }

    val imageMap = mapOf(
        "[MINESWEEPER]" to R.drawable.ig_minesweeper,
        "[ZAHAK]" to R.drawable.ig_enemy_zahak,
        "[ARZHANG]" to R.drawable.ig_enemy_arzhang_troll,
        "[DIV_SEPID]" to R.drawable.ig_enemy_white_troll,
        "[MINE]" to R.drawable.ig_enemy_shoghad_trap
    )

    imageMap.forEach { (key, drawableRes) ->
        var startIndex = inputText.indexOf(key)
        while (startIndex != -1) {
            val endIndex = startIndex + key.length
            val drawable = ContextCompat.getDrawable(context, drawableRes)
            val sizeInPx = dpToPx(if (bigImage) 35f else 25f)
            drawable?.setBounds(0, 0, sizeInPx, sizeInPx)
            val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_CENTER)

            spannable.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            startIndex = inputText.indexOf(key, endIndex)
        }
    }

    this.text = spannable
}

fun TextView.setTutorial1Text(inputText: String, bigImage: Boolean = false) {
    val spannable = SpannableStringBuilder(inputText)

    val imageMap = CellTypes.values().associate {
        "[${it.key.uppercase()}]" to it.image
    }.toMutableMap()

    imageMap.forEach { (key, drawableRes) ->
        var startIndex = inputText.indexOf(key)
        while (startIndex != -1) {
            val endIndex = startIndex + key.length
            val drawable = ContextCompat.getDrawable(context, drawableRes)
            val sizeInPx = dpToPx(if (bigImage) 35f else 25f)
            drawable?.setBounds(0, 0, sizeInPx, sizeInPx)

            val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_CENTER)
            spannable.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            startIndex = inputText.indexOf(key, endIndex)
        }
    }

    CellTypes.values().forEach { cell ->
        val word = cell.title ?: ""
        if (word.isNotEmpty()) {
            var startIndex = inputText.indexOf(word)
            while (startIndex != -1) {
                val endIndex = startIndex + word.length
                val color = ContextCompat.getColor(context, R.color.enemy_color)
                spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                startIndex = inputText.indexOf(word, endIndex)
            }
        }
    }

    this.text = spannable
}