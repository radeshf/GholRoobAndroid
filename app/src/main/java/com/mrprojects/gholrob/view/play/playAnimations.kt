import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat

val ANIMATION_TIME = 1000L
val DAMAGE_ANIMATION_TIME = 500L
fun animateRiseAndSplit(root: ViewGroup, clickedView: View, drawableRes: Int) {
    val context = root.context
    val startLoc = IntArray(2)
    clickedView.getLocationOnScreen(startLoc)
    val rootLoc = IntArray(2)
    root.getLocationOnScreen(rootLoc)

    val startX = (startLoc[0] - rootLoc[0]).toFloat()
    val startY = (startLoc[1] - rootLoc[1]).toFloat()
    val size = clickedView.width.takeIf { it > 0 } ?: 120

    val image = ImageView(context).apply {
        setImageResource(drawableRes)
        layoutParams = FrameLayout.LayoutParams(size, size)
        x = startX
        y = startY
        scaleType = ImageView.ScaleType.FIT_CENTER
    }

    root.addView(image)

    // انیمیشن نرم‌تر با Interpolator
    image.animate()
        .y(startY - 200f) // کمی بالاتر برای دراماتیک شدن
        .scaleX(1.3f)     // زوم ملایم
        .scaleY(1.3f)
        .setDuration(ANIMATION_TIME)
        .setInterpolator(OvershootInterpolator(1.2f)) // حس پرش در پایان حرکت
        .withEndAction {
//            splitImage(root, image, drawableRes)
            dissolveImage(root, image, drawableRes)
        }
        .start()
}


fun splitImage(root: ViewGroup, sourceImage: ImageView, drawableRes: Int) {
    val context = root.context
    val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return
    val bitmap = drawableToBitmap(drawable)

    val halfWidth = bitmap.width / 2
    val height = bitmap.height

    // ایجاد دو تکه Bitmap
    val leftBitmap = Bitmap.createBitmap(bitmap, 0, 0, halfWidth, height)
    val rightBitmap = Bitmap.createBitmap(bitmap, halfWidth, 0, bitmap.width - halfWidth, height)

    // ساخت View ها
    val leftView = ImageView(context).apply {
        setImageBitmap(leftBitmap)
        layoutParams = FrameLayout.LayoutParams(sourceImage.width / 2, sourceImage.height)
        x = sourceImage.x
        y = sourceImage.y
        scaleType = ImageView.ScaleType.FIT_XY
    }

    val rightView = ImageView(context).apply {
        setImageBitmap(rightBitmap)
        layoutParams = FrameLayout.LayoutParams(sourceImage.width / 2, sourceImage.height)
        x = sourceImage.x + sourceImage.width / 2f
        y = sourceImage.y
        scaleType = ImageView.ScaleType.FIT_XY
    }

    root.removeView(sourceImage)
    root.addView(leftView)
    root.addView(rightView)

    // انیمیشن شکستن به سمت بالا و طرفین
    // استفاده از مقادیر منفی برای Y باعث حرکت به سمت بالا می‌شود
    leftView.animate()
        .translationX(-300f) // پرتاب قوی‌تر به سمت چپ
        .translationY(-200f) // حرکت به سمت بالا (منفی)
        .rotation(-30f)
        .alpha(0f)
        .setDuration(ANIMATION_TIME)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { root.removeView(leftView) }
        .start()

    rightView.animate()
        .translationX(300f)  // پرتاب قوی‌تر به سمت راست
        .translationY(-200f) // حرکت به سمت بالا (منفی)
        .rotation(30f)
        .alpha(0f)
        .setDuration(ANIMATION_TIME)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { root.removeView(rightView) }
        .start()
}

fun dissolveImage(root: ViewGroup, sourceImage: ImageView, drawableRes: Int) {
    val context = root.context
    val random = java.util.Random()
    val particleCount = 12 // تعداد ذرات

    // موقعیت شروع (مرکز ویو)
    val centerX = sourceImage.x + (sourceImage.width / 2f)
    val centerY = sourceImage.y + (sourceImage.height / 2f)

    root.removeView(sourceImage)

    // ساخت ذرات کوچک برای شبیه‌سازی پودر شدن
    for (i in 0 until particleCount) {
        val size = (10 + random.nextInt(20)).toFloat() // ذرات با اندازه تصادفی

        val particle = View(context).apply {
            layoutParams = FrameLayout.LayoutParams(size.toInt(), size.toInt())
            x = centerX - (size / 2)
            y = centerY - (size / 2)
            setBackgroundColor(android.graphics.Color.WHITE) // رنگ ذرات (می‌توانید از رنگ تصویر هم استفاده کنید)
            alpha = 0.8f
        }
        root.addView(particle)

        // محاسبات حرکت به بیرون
        val angle = (Math.PI * 2 * i / particleCount) // پخش دایره‌ای
        val distance = 150f + random.nextFloat() * 150f
        val targetX = (centerX + Math.cos(angle) * distance).toFloat()
        val targetY = (centerY + Math.sin(angle) * distance).toFloat()

        // انیمیشن پرتاب ذرات
        particle.animate()
            .x(targetX)
            .y(targetY)
            .scaleX(0f)
            .scaleY(0f) // ذرات کوچک می‌شوند و محو می‌شوند
            .alpha(0f)
            .setDuration(500)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .withEndAction { root.removeView(particle) }
            .start()
    }
}

fun createDamageOverlay(root: ViewGroup): View {
    val overlay = View(root.context)

    // تعریف گرادینت شعاعی: مرکز شفاف (00)، لبه‌ها قرمز (FF)
    val gradient = GradientDrawable(
        GradientDrawable.Orientation.TL_BR,
        intArrayOf(Color.parseColor("#88FF0000"), Color.TRANSPARENT)
    ).apply {
        gradientType = GradientDrawable.RADIAL_GRADIENT
        // مرکز صفحه را شفاف و لبه‌ها را قرمز می‌کنیم
        setGradientCenter(0.5f, 0.5f)
        // شعاع انحنا: هرچه عدد بزرگتر باشد، مرکز شفاف بزرگتر می‌شود
        gradientRadius = 600f
    }

    overlay.background = gradient
    overlay.alpha = 1f // در حالت عادی مخفی است

    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    root.addView(overlay, params)
    return overlay
}

fun showDamageEffect(overlay: View, damageIntensity: Float) {
    // محدود کردن شدت بین 0 و 1
    val intensity = damageIntensity.coerceIn(0.1f, 1.0f)

    // تنظیم میزان قرمزی بر اساس شدت آسیب
    overlay.alpha = intensity

    // انیمیشن محو شدن سریع بعد از ضربه
    overlay.animate()
        .alpha(0f)
        .setDuration(DAMAGE_ANIMATION_TIME) // مدت زمان محو شدن
        .setInterpolator(android.view.animation.AccelerateInterpolator())
        .start()
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) return drawable.bitmap
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
