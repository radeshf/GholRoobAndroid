package com.mrprojects.gholrob.helper.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mrprojects.gholrob.R

class DualGameProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // پس‌زمینه شیار نوار
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.box_cleared_color)
    }

    // استروک دور کپسول
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    // پینت لایه زیرین (Secondary)
    private val secondaryProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // پینت لایه رویی (Primary)
    private val primaryProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bgRect = RectF()
    private val secondaryRect = RectF()
    private val primaryRect = RectF()

    // متغیرهای ماکزیمم مستقل
    var maxPrimary: Float = 100f
        set(value) {
            field = if (value <= 0f) 100f else value
            invalidate()
        }

    var maxSecondary: Float = 100f
        set(value) {
            field = if (value <= 0f) 100f else value
            invalidate()
        }

    // متغیرهای پیشرفت مستقل
    var progressPrimary: Float = 0f
        set(value) {
            field = value.coerceIn(0f, maxPrimary)
            invalidate()
        }

    var progressSecondary: Float = 0f
        set(value) {
            field = value.coerceIn(0f, maxSecondary)
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return

        // گرادیان لایه رویی (سبز فیروزه‌ای نئونی)
        primaryProgressPaint.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            Color.parseColor("#1E5A57"), Color.parseColor("#4CD7D0"),
            Shader.TileMode.CLAMP
        )

        secondaryProgressPaint.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            Color.parseColor("#10375C"), // سرمه‌ای سیر
            Color.parseColor("#008DDA"), // آبی نئونی براق
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = height / 2f
        bgRect.set(1f, 1f, width.toFloat() - 1f, height.toFloat() - 1f)

        // ۱. رسم پس‌زمینه کپسولی و کادر دور
        canvas.drawRoundRect(bgRect, radius, radius, bgPaint)
        canvas.drawRoundRect(bgRect, radius, radius, strokePaint)

        // ۲. رسم نوار ثانویه (زیرین) با نسبت maxSecondary خودش
        val secRatio = (progressSecondary / maxSecondary).coerceIn(0f, 1f)
        if (secRatio > 0f) {
            val secWidth = (width * secRatio).coerceAtLeast(radius * 2)
            secondaryRect.set(0f, 0f, secWidth, height.toFloat())
            canvas.drawRoundRect(secondaryRect, radius, radius, secondaryProgressPaint)
        }

        // ۳. رسم نوار اولیه (رویی) با نسبت maxPrimary خودش
        val priRatio = (progressPrimary / maxPrimary).coerceIn(0f, 1f)
        if (priRatio > 0f) {
            val priWidth = (width * priRatio).coerceAtLeast(radius * 2)
            primaryRect.set(0f, 0f, priWidth, height.toFloat())
            canvas.drawRoundRect(primaryRect, radius, radius, primaryProgressPaint)
        }
    }

    /**
     * انیمیت لایه رویی (Primary)
     */
    fun animatePrimaryProgress(target: Float, durationMs: Long = 650L) {
        ValueAnimator.ofFloat(progressPrimary, target.coerceIn(0f, maxPrimary)).apply {
            duration = durationMs
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                progressPrimary = it.animatedValue as Float
            }
            start()
        }
    }

    /**
     * انیمیت لایه زیرین (Secondary)
     */
    fun animateSecondaryProgress(target: Float, durationMs: Long = 850L) {
        ValueAnimator.ofFloat(progressSecondary, target.coerceIn(0f, maxSecondary)).apply {
            duration = durationMs
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                progressSecondary = it.animatedValue as Float
            }
            start()
        }
    }

    /**
     * انیمیت همزمان هر دو لایه با مقادیر دلخواه
     */
    fun animateBoth(targetPrimary: Float, targetSecondary: Float, durationMs: Long = 750L) {
        animatePrimaryProgress(targetPrimary, durationMs)
        animateSecondaryProgress(targetSecondary, durationMs)
    }

    /**
     * تنظیم دستی رنگ گرادیان لایه اولیه
     */
    fun setPrimaryColors(startColor: Int, endColor: Int) {
        if (width > 0) {
            primaryProgressPaint.shader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                startColor, endColor,
                Shader.TileMode.CLAMP
            )
            invalidate()
        }
    }

    /**
     * تنظیم دستی رنگ گرادیان لایه ثانویه
     */
    fun setSecondaryColors(startColor: Int, endColor: Int) {
        if (width > 0) {
            secondaryProgressPaint.shader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                startColor, endColor,
                Shader.TileMode.CLAMP
            )
            invalidate()
        }
    }
}
