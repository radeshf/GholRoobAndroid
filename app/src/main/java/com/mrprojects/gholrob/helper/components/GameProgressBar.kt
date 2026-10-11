package com.mrprojects.gholrob.helper.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.helper.getFontPath

class GameProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.box_cleared_color)
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // تنظیمات تکست ویوی درون‌سازمانی با سایه گیمینگ
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.createFromAsset(context.assets, context.getFontPath())
        setShadowLayer(4f, 0f, 2f, Color.BLACK)
    }

    private val rect = RectF()
    private val progressRect = RectF()
    private val textBounds = Rect()

    var showText: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var customText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var max: Float = 100f
        set(value) {
            field = if (value <= 0f) 100f else value
            invalidate()
        }

    var progress: Float = 0f
        set(value) {
            field = value.coerceIn(0f, max)
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // گرادیان نوار
        progressPaint.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            Color.parseColor("#1E5A57"), Color.parseColor("#4CD7D0"),
            Shader.TileMode.CLAMP
        )
        // سایز فونت متناسب با ارتفاع نوار (تقریباً ۵۰ درصد ارتفاع)
        textPaint.textSize = h * 0.52f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = height / 2f
        rect.set(1f, 1f, width.toFloat() - 1f, height.toFloat() - 1f)

        // ۱. رسم پس‌زمینه کپسولی
        canvas.drawRoundRect(rect, radius, radius, bgPaint)
        canvas.drawRoundRect(rect, radius, radius, strokePaint)

        // ۲. رسم نوار پر شونده با لبه‌های کاملاً نرم
        val ratio = (progress / max).coerceIn(0f, 1f)
        if (ratio > 0f) {
            val progressWidth = (width * ratio).coerceAtLeast(radius * 2)
            progressRect.set(0f, 0f, progressWidth, height.toFloat())
            canvas.drawRoundRect(progressRect, radius, radius, progressPaint)
        }

        // ۳. رسم متن دقیقاً در مرکز عمودی و افقی نوار
        if (showText) {
            val displayText = customText ?: "${(ratio * 100).toInt()}%"
            textPaint.getTextBounds(displayText, 0, displayText.length, textBounds)
            val textY = (height / 2f) + (textBounds.height() / 2f) - textBounds.bottom
            canvas.drawText(displayText, width / 2f, textY, textPaint)
        }
    }

    fun animateProgress(target: Float, durationMs: Long = 800L) {
        ValueAnimator.ofFloat(progress, target.coerceIn(0f, max)).apply {
            duration = durationMs
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Float
            }
            start()
        }
    }
}
