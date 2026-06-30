package com.mrprojects.gholrob.view.play.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import kotlin.math.hypot

class DamageOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var currentAlphaValue: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (width == 0 || height == 0 || currentAlphaValue <= 0) return

        val cx = width / 2f
        val cy = height / 2f

        val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val shader = RadialGradient(
            cx,
            cy,
            radius,
            intArrayOf(
                Color.argb(0, 255, 0, 0),                              // مرکز کاملاً شفاف
                Color.argb((currentAlphaValue * 0.10f).toInt(), 255, 0, 0),
                Color.argb((currentAlphaValue * 0.25f).toInt(), 255, 0, 0),
                Color.argb((currentAlphaValue * 0.55f).toInt(), 255, 0, 0),
                Color.argb(currentAlphaValue, 255, 0, 0)               // لبه‌ها قرمزتر
            ),
            floatArrayOf(0f, 0.45f, 0.70f, 0.88f, 1f),
            Shader.TileMode.CLAMP
        )

        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun showDamage(damage: Int, duration: Long = 2000L) {
        val normalized = (damage / 10f).coerceIn(0f, 1f)
        val intensity = (0.3f + normalized * 0.7f).coerceIn(0f, 1f)
        val safeIntensity = intensity.coerceIn(0f, 1f)

        // شدت نهایی بر اساس دمیج
        val maxAlpha = (safeIntensity * 220).toInt().coerceIn(0, 220)


        ValueAnimator.ofInt(maxAlpha, 0).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()

            addUpdateListener {
                currentAlphaValue = it.animatedValue as Int
                invalidate()
            }

            doOnEnd {
                currentAlphaValue = 0
                invalidate()
            }

            start()
        }
    }
}