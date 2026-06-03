package ir.radesh.basemodule.commons

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.radesh.basemodule.R
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ir.radesh.basemodule.adapter.CustomSpinnerAdapter
import ir.radesh.basemodule.helper.DialogLoadingHelper
import ir.radesh.basemodule.helper.LoadingHelperV2
import ir.radesh.basemodule.helper.viewpager.adapter.BottomBarAdapter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


const val default_font = "yekan_bakh_reg"
const val animationDurationShort = 1000L

//#1
fun <T> Activity.goTo(cls: Class<T>, finish: Boolean = true, extras: HashMap<String, Any>? = null) {
    val intent = Intent(this, cls)
    intent.put(extras)
    startActivity(intent)
    if (finish) finish()
}

fun <T> Fragment.goTo(
    cls: Class<T>,
    finish: Boolean = true,
    stringExtras: HashMap<String, String>? = null,
    intExtras: HashMap<String, Int>? = null,
    booleanExtra: HashMap<String, Boolean>? = null
) {
    val intent = Intent(requireActivity(), cls)
    stringExtras?.forEachR { key, Value -> intent.putExtra(key, Value!!) }
    intExtras?.forEachR { key, Value -> intent.putExtra(key, Value!!) }
    booleanExtra?.forEachR { key, Value -> intent.putExtra(key, Value!!) }

    startActivity(intent)
    if (finish) requireActivity().finish()
}


/**
 * only support Int, String, Boolean
 */
fun Intent.put(hashMap: HashMap<String, Any>?) {
    hashMap?.forEachR { key, Value ->
        if (Value is Int) putExtra(key, Value)
        if (Value is String) putExtra(key, Value)
        if (Value is Boolean) putExtra(key, Value)
    }
}

//#2
fun Activity.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    showToastBase(this, msg, length)
}

fun Fragment.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    showToastBase(requireContext(), msg, length)
}

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    showToastBase(this, msg, length)
}

private fun showToastBase(context: Context, msg: String, length: Int) {
    if (msg.isEmpty() || msg == "null") return
    Toast.makeText(context, msg, length).show()
}

fun Fragment.showCustomToast(msg: String) {
    val toast = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
    if (isBelow(Build.VERSION_CODES.R)) {
        val toastView = toast.view
        val toastMessage = toastView?.findViewById(android.R.id.message) as TextView
        toastMessage.changeFont()
        toastMessage.setShadowLayer(0f, 0f, 0f, 0)
        toastMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.textPrimary))
        toastView.setBackgroundResource(R.drawable.toast_bg)
    }
    toast.show()
}





//
fun Activity.setEventBus(register: Boolean) {
    if (register) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    } else {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}

fun Fragment.setEventBus(register: Boolean) {
    if (register) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    } else {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun RecyclerView.init(lm: RecyclerView.LayoutManager) {
    setHasFixedSize(true)
    layoutManager = lm
}


fun RecyclerView.init(isVertical: Boolean = true, withDivider: Boolean = false) {
    setHasFixedSize(true)
    vertical(isVertical)
    if (withDivider) setDivider(R.drawable.recycler_view_divider)
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    val drawable = ContextCompat.getDrawable(this.context, drawableRes)
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}


fun RecyclerView.initReverseHorizontal() {
    setHasFixedSize(true)
    val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
    linearLayoutManager.stackFromEnd = true
    layoutManager = linearLayoutManager
}

fun RecyclerView.initGrid(rowCount: Int = 4, reverseLayout: Boolean = false, canScroll: Boolean = true) {
    setHasFixedSize(true)
    layoutManager = object : GridLayoutManager(context, rowCount, RecyclerView.VERTICAL, reverseLayout) {
        override fun isLayoutRTL(): Boolean {
            return true
        }

        override fun canScrollVertically(): Boolean {
            return canScroll
        }

        override fun canScrollHorizontally(): Boolean {
            return canScroll
        }
    }
}

fun RecyclerView.initFlexGrid(rowCount: Int = 4, reverseLayout: Boolean = false, canScroll: Boolean = true) {
//    val flexboxLayoutManager = FlexboxLayoutManager(context).apply {
//        flexDirection = FlexDirection.ROW
//        flexWrap = FlexWrap.WRAP
//        justifyContent = JustifyContent.CENTER
//    }
//    layoutManager = flexboxLayoutManager
}

fun RecyclerView.vertical(isVertical: Boolean) {
    layoutManager = when {
        isVertical -> LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }
}

fun <T> RecyclerView.getAdp(): T {
    return adapter as T
}


fun FragmentManager.changeTo(
    container: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    animationGravity: Int = Gravity.END,
    addToContainer: Boolean = false,
    sharedElements: HashMap<View, String>? = null
) {
    val transactions = beginTransaction()
    if (addToBackStack) {
        transactions.addToBackStack(null)
    }
    when (animationGravity) {
        Gravity.END -> transactions.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
        Gravity.BOTTOM -> transactions.setCustomAnimations(R.anim.slide_in_top, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_top)
        Gravity.CENTER -> transactions.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        Gravity.START -> transactions.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
        else -> {

        }
    }
    if (sharedElements != null && isAbove(Build.VERSION_CODES.LOLLIPOP)) {
        sharedElements.forEachR { key, Value ->
            transactions.addSharedElement(key, Value!!)
        }

    }
    if (addToContainer) {
        transactions.add(container, fragment)
    } else {
        transactions.replace(container, fragment)
    }

    transactions.commit()
}


fun <T, V> HashMap<T, V>.forEachR(doOnEach: (key: T, value: V?) -> Unit) {
    for (key in keys) doOnEach(key, get(key))
}

fun <T, V> HashMap<T, V>.forEachRIndexed(doOnEach: (key: T, value: V?, index: Int) -> Unit) {
    for ((index, key) in keys.withIndex()) {
        doOnEach(key, get(key), index)
    }
}

fun TextView.changeFont() {
    typeface = context.getFont()
}

fun Context.getFont(): Typeface {
    return Typeface.createFromAsset(assets, "font/$default_font.ttf")
}

fun TextView.setTextColorWithAnimation(@ColorInt toColor: Int, duration: Long = 300) {
    val colorAnim = ObjectAnimator.ofInt(this, "textColor", this.currentTextColor, toColor)
    colorAnim.setEvaluator(ArgbEvaluator())
    colorAnim.duration = duration
    colorAnim.start()
}

fun ImageView.setTintColorWithAnimation(fromColor: Int, toColor: Int, duration: Long = 300) {
    val colorAnim = ObjectAnimator.ofFloat(0f, 1f)
    colorAnim.addUpdateListener { animation ->
        val mul = animation.animatedValue as Float
        val argb = ArgbEvaluator().evaluate(
            mul,
            ContextCompat.getColor(context, fromColor),
            ContextCompat.getColor(context, toColor)
        )
        setColorFilter(argb as Int, PorterDuff.Mode.SRC_IN)
    }

    colorAnim.duration = duration
    colorAnim.start()
}

fun ImageView.setTintColor(@ColorRes color: Int) {
    setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
}

fun ImageView.setTint(color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

/**
 * change visibility with boolean
 * very simple and useful Nah !?
 */
fun ImageView.setVisibilityR(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}


fun doOnTry(func: () -> Unit, onCatch: (() -> Unit)? = null) {
    try {
        func()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        onCatch?.invoke()
    }
}

/**
 * e.g isAbove(Build.VERSION_CODES.LOLLIPOP)
 */
fun isAbove(SDK: Int): Boolean {
    return Build.VERSION.SDK_INT >= SDK
}

/**
 * e.g isBelow(Build.VERSION_CODES.LOLLIPOP)
 */
fun isBelow(SDK: Int): Boolean {
    return Build.VERSION.SDK_INT < SDK
}

fun CardView.init(animator: Int = R.animator.card_smooth_shadow) {
    if (isAbove(Build.VERSION_CODES.LOLLIPOP)) {
        stateListAnimator = AnimatorInflater.loadStateListAnimator(context, animator)
    }
}


fun TextView.setTextWithAnimation(text: String, duration: Long = 100L) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        this@setTextWithAnimation.text = text
        animate().setDuration(duration).alpha(1f)
    }
}


fun View.clickAnimation(duration: Long = 200) {
    animate().setDuration(duration).alpha(0f).withEndAction { animate().setDuration(duration).alpha(1f) }
}

fun View.scaleAnimation(duration: Long = 200, onEnded: (() -> Unit)? = null) {
    animate().setDuration(duration).scaleX(1.5f).scaleY(1.5f).withEndAction {
        onEnded?.invoke()
        animate().setDuration(duration).scaleX(1f).scaleY(1f)
    }
}

fun View.clickOnTileAnimation(duration: Long = 600, onEnded: (() -> Unit)? = null) {
    visibility = View.VISIBLE
    animate().setDuration(duration).scaleX(2f).scaleY(2f).alpha(0.3f).withEndAction {
        visibility = View.GONE
        alpha = 1f
        scaleX = 1f
        scaleY = 1f
    }
}

fun ImageView.setImageWithAnimation(resId: Int, duration: Long = 100L) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        this@setImageWithAnimation.setImageResource(resId)
        animate().setDuration(duration).alpha(1f)
    }
}

fun getDimenForView(context: Context, id: Int): Int = (context.resources.getDimension(id) / context.resources.displayMetrics.density).toInt()


fun ImageView.dropDownAnimation(dropDown: Boolean) {
    val arrowAnimation: RotateAnimation = if (dropDown) {
        RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    } else {
        RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    }
    arrowAnimation.fillAfter = true
    arrowAnimation.duration = 300
    this.startAnimation(arrowAnimation)
}


fun TextView.setTextCounterAnimation(text: String) {
    val duration = 100L
    animate().setDuration(duration).translationY(-100f).alpha(0f).withEndAction {
        this@setTextCounterAnimation.text = text
        animate().setDuration(duration).translationY(100f).translationY(0f).alpha(1f)
    }
}

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

/**
 * @param resId : its show this resource after done animation
 *          so you can use it for like and dislike
 */
fun ImageView.likeAnimation(resId: Int, duration: Long = 100) {
    animate().scaleX(1.5f).scaleY(1.5f).alpha(0f).setDuration(duration)
        .withEndAction {
            setImageResource(resId)
            animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(duration)
        }
}


fun Fragment.showLoadingInView(view: View, doOnRetry: () -> Unit) {
    DialogLoadingHelper.showIn(this, view, view.toString(), getString(R.string.please_wait), doOnRetry)
}

fun Fragment.hideLoadingInView() {
    DialogLoadingHelper.hideLoading()
}

fun Fragment.showNoDataInView() {
    DialogLoadingHelper.showNoData()
}

fun Fragment.showLoading() {
    LoadingHelperV2.show(requireActivity())
}

fun Fragment.hideLoading() {
    LoadingHelperV2.hide(requireActivity())
}

fun Activity.showLoading() {
    LoadingHelperV2.show(this)
}

fun Activity.hideLoading() {
    LoadingHelperV2.hide(this)
}

fun <T> Observable<T>.networkSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

fun <T> Observable<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
    Observable.zip(this, Observable.interval(interval, timeUnit), BiFunction { t1, _ -> t1 })

fun Fragment.addExtra(
    stringExtras: HashMap<String, String>? = null,
    intExtras: HashMap<String, Int>? = null,
    booleanExtra: HashMap<String, Boolean>? = null
): Fragment {
    val bundle = Bundle()

    stringExtras?.forEachR { key, Value -> bundle.putString(key, Value!!) }
    intExtras?.forEachR { key, Value -> bundle.putInt(key, Value!!) }
    booleanExtra?.forEachR { key, Value -> bundle.putBoolean(key, Value!!) }
    arguments = bundle
    return this
}

fun Dialog.basicConfig(contentView: View, cancellable: Boolean = false) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setCanceledOnTouchOutside(cancellable)
    setCancelable(cancellable)
    setContentView(contentView)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}


fun Dialog.back(btnBack: View) {
    btnBack.setOnClickListener { dismiss() }
}


fun Context.rcheckPermission(perm: String): Boolean {
    val result = checkCallingOrSelfPermission(perm)
    return result == PackageManager.PERMISSION_GRANTED
}

fun String.addZero(): String {
    return when (this.length) {
        1 -> "0${this}"
        2 -> this
        else -> ""
    }
}

fun String.addYear(): String {
    return when (this.length) {
        2 -> "13${this}"
        4 -> this
        else -> ""
    }
}

fun EditText.isMobileCorrect(): Boolean {
    return text.toString().trim { it <= ' ' }.length == 11 && text.toString().trim { it <= ' ' }[0] == '0'
            || text.toString().trim { it <= ' ' }.length == 10 && text.toString().trim { it <= ' ' }[0] == '9'
}

fun TextInputEditText.isMobileCorrect(): Boolean {
    return text.toString().trim { it <= ' ' }.length == 11 && text.toString().trim { it <= ' ' }[0] == '0'
            || text.toString().trim { it <= ' ' }.length == 10 && text.toString().trim { it <= ' ' }[0] == '9'
}

fun TextInputEditText.getStringText(): String {
    return text.toString().trim()
}

fun Context.openLink(url: String) {
    doOnTry({
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    })
}


fun Context.openWhatsappChat(number: String) {
    openLink("https://api.whatsapp.com/send?phone=$number")
}

fun Context.openTelegramChat(telegramId: String) {
    openLink("https://t.me/$telegramId")
}

fun Context.callToNumber(number: String?) {
    doOnTry({
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }, {
        showToast("دستگاه شما از تماس تلفنی پشتیبانی نمیکند")
    })
}

fun Context.sendEmail(recipient: String, subject: String = "", body: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    startActivity(Intent.createChooser(intent, "Choose an Email app"))

}

fun Context.delay(duration: Long, func: () -> Unit) {
    Handler(Looper.getMainLooper())
        .postDelayed({
            func()
        }, duration)
}

fun View.startLeftAnimation(duration: Long = animationDurationShort) {
    startAnim(duration, R.anim.left_to_center_anim)
}

fun View.startRightAnimation(duration: Long = animationDurationShort) {
    startAnim(duration, R.anim.right_to_center_anim)
}

fun View.startTopAnimation(duration: Long = animationDurationShort) {
    startAnim(duration, R.anim.top_to_center_anim)
}

fun View.startBottomAnimation(duration: Long = animationDurationShort) {
    startAnim(duration, R.anim.bottom_to_center_anim)
}

fun View.startFadeInAnimation(duration: Long = animationDurationShort) {
    startAnim(duration, R.anim.fade_in)
}

fun View.startMakeBiggerAnimation(duration: Long? = null) {
    startAnim(duration, R.anim.make_bigger_anim)
}

fun View.startAnim(duration: Long?, animRes: Int) {
    val anim = AnimationUtils.loadAnimation(this.context, animRes)
    anim.duration = duration ?: 1500
    startAnimation(anim)
}


private fun getDate(source: String): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(source)!!
}


fun String.getPersianTime(): String {
    val date = getDate(this)
    val cal = Calendar.getInstance()
    cal.time = date
    return "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
}


fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Long.toMoneyString(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

fun Float.toMoneyString(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

fun Int.toMoneyString(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

fun Int.toAwesomeMoneyString(): String {
    return ""
}

fun String.convertDigitsToLatin(): String {
    val sb = StringBuilder()
    for (i in indices) {
        when (get(i)) {
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
            else -> sb.append(get(i))
        }
    }
    return sb.toString()
}


fun ImageView.setImageFromPath(path: String) {
    doOnTry({
        val imgFile = File(path)
        if (imgFile.exists()) {
            this.setImageBitmap(BitmapFactory.decodeFile(imgFile.absolutePath))
        } else {
            Timber.e("${imgFile.absolutePath} Not Exist")
        }
    })

}

fun Context.openInBrowser(url: String) {
    doOnTry({
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    })
}


fun Context.getCollor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun TextView.setTextCollor(color: Int) {
    setTextColor(this.context.getCollor(color))
}

fun Context.getDDrawable(drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}


fun View.disableAlphaByBoolean(visible: Boolean){
    this.alpha = if(visible) 1.0f else 0.4f
}

fun View.visibleByBoolean(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.inVisibleByBoolean(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}


fun EditText.searchListener(): Observable<String> {
    val subject = PublishSubject.create<String>()
    doOnTextChanged { text, _, _, _ ->
        text?.let {
            subject.onNext(text.toString())
        }
    }
    return subject
}


fun Intent.printExtras() {
    val bundle = extras
    if (bundle != null) {
        for (key in bundle.keySet()) {
            Timber.e("$key + : + ${if (bundle[key] != null) bundle[key] else "NULL"}")
        }
    }
}

fun TextView.loadHtml(msg: String) {
    setLinkTextColor(Color.parseColor("#FFDE59"))
    movementMethod = LinkMovementMethod.getInstance()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        text = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY)
    } else {
        text = Html.fromHtml(msg)
    }
}

fun String.loadHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun Context.getFilesDirFixed(applicationId: String): File {
    for (a in 0..9) {
        val path = applicationContext.filesDir
        if (path != null) {
            return path
        }
    }
    try {
        val info: ApplicationInfo = applicationContext.applicationInfo
        val path = File(info.dataDir, "files")
        path.mkdirs()
        return path
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return File("/data/data/${applicationId}/files")
}

private fun ImageView.loadWebp(assetsFilePath: String) {
    val stream = context.assets.open(assetsFilePath)
    setImageDrawable(Drawable.createFromStream(stream, null))
}

fun Fragment.showOnMap(lat: Double, lng: Double) {
    requireContext().showOnMap(lat, lng)
}

fun Context.showOnMap(lat: Double, lng: Double) {
    doOnTry({
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=$lat,$lng"))
        startActivity(Intent.createChooser(intent, "Choose"))
    })
}

fun ViewPager.setOnPageChanged(onChanged: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            onChanged(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    })
}

fun BottomNavigationView.setOnPageChanged(onChanged: (id: Int) -> Unit) {
    setOnNavigationItemSelectedListener {
        onChanged(it.itemId)
        return@setOnNavigationItemSelectedListener true
    }
}

fun BottomNavigationView.changeFont() {
    fontChanger(this, context.getFont())
}


fun Spinner.config(
    list: List<String>,
    selectedView: Int = R.layout.spinner_item,
    listItemView: Int = R.layout.spinner_listitem,
    doOnItemSelected: (position: Int, item: String) -> Unit
) {
    val adp = CustomSpinnerAdapter(context, selectedView, list)
    adp.setDropDownViewResource(listItemView)
    adapter = adp
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            doOnItemSelected(position, list[position])
        }

    }
}

fun Context.copyTextToClipboard(textToCopy: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", textToCopy)
    clipboardManager.setPrimaryClip(clipData)
    showToast(getString(R.string.copied))
}

fun File.getRequestPartBody(fieldName: String): MultipartBody.Part {
    val requestBody = RequestBody.create(MediaType.parse("*multipart/form-data*"), this)
    return MultipartBody.Part.createFormData(fieldName, name, requestBody)
}

fun Uri.getRequestPartBody(context: Context, fieldName: String, fileName: String): MultipartBody.Part {
    val requestBody = RequestBody.create(MediaType.parse("*multipart/form-data*"), context.contentResolver.openInputStream(this)?.readBytes()!!)
    return MultipartBody.Part.createFormData(fieldName, fileName, requestBody)
}

fun String.getRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), this)
}

fun SwipeRefreshLayout.hide() {
    isRefreshing = false
}

fun SwipeRefreshLayout.show() {
    isRefreshing = true
}

fun ViewPager.setupViewPage(fm: FragmentManager, bottomNavigation: BottomNavigationView, pages: List<Fragment>, homeIndex: Int = 0) {
    val pagerAdapter = BottomBarAdapter(fm)
    pages.forEachIndexed { index, fragment ->
        Timber.e("fragments $fragment, index, $index")
        pagerAdapter.addFragments(fragment)
    }

    offscreenPageLimit = pages.size + 1
    adapter = pagerAdapter
    currentItem = homeIndex
    bottomNavigation.menu.forEachIndexed { menu_index, menu ->
        if (homeIndex == menu_index) bottomNavigation.selectedItemId = menu.itemId
    }

    bottomNavigation.changeFont()
    setOnPageChanged {
        Timber.e("page change to $it")
        bottomNavigation.menu.forEachIndexed { menuIndex, menu ->
            if (it == menuIndex) bottomNavigation.selectedItemId = menu.itemId
        }
    }

    bottomNavigation.setOnPageChanged {
        bottomNavigation.menu.forEachIndexed { menu_index, menu ->
            if (menu.itemId == it) currentItem = menu_index
        }
    }
}

fun ImageView.loadPicasso(url: String?) {
    if (url.isNullOrEmpty()) {
        Timber.e("Picasso => url is null or empty")
        return
    }
    Picasso.get()
        .load(url)
        .into(this)

}

fun Context.getPrice(price: String): Int {
    if (price.isEmpty() || price.toInt() < 1000) {
        showToast(getString(R.string.invalid_price))
        return -1
    }
    return price.toInt()
}

fun hideKeyboard(ctx: Context) {
    doOnTry({
        val inputManager = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val v = (ctx as Activity).currentFocus ?: return@doOnTry
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    })
}

fun ImageView.rotateAnimation() {
    val animation = RotateAnimation(
        360f, 0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = 1000
    startAnimation(animation)
    setAnimation(animation)
}

fun String.capitalize(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val first = this[0]
    return if (Character.isUpperCase(first)) {
        this
    } else {
        Character.toUpperCase(first).toString() + this.substring(1)
    }
}

fun Context.getDeviceName(): String {
    return try {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val brand = Build.BRAND
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            model.capitalize() + " " + brand
        } else {
            manufacturer.capitalize() + " " + model + " " + brand
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Context.getAppVersion(): String {
    var version = ""
    doOnTry({
        version = packageManager.getPackageInfo(packageName, 0).versionName
    })

    return version
}

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()


fun Context.shareText(text: String) {
    doOnTry({
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Share"))
    })

}


fun Context.openPlayStoreRate() {
    val uri: Uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
    }
}

fun TextView.makeWordRed(
    wordToStyle: String,
    color: Int = Color.RED,
    sizeMultiplier: Float = 1.3f   // 1.0 = same size, 1.3 = 30% bigger
) {
    val fullText = text
    val spannable = fullText as? SpannableString ?: SpannableString(fullText)

    var start = spannable.toString().indexOf(wordToStyle)

    while (start != -1) {
        val end = start + wordToStyle.length

        // Color
        spannable.setSpan(
            ForegroundColorSpan(color),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Bigger size
        spannable.setSpan(
            RelativeSizeSpan(sizeMultiplier),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        start = spannable.toString().indexOf(wordToStyle, start + 1)
    }

    text = spannable
}