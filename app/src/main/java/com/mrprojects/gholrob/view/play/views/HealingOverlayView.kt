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
import kotlin.math.hypot

class HealingOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var currentAlphaValue: Int = 0
    private var healingAnimator: ValueAnimator? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (width == 0 || height == 0 || currentAlphaValue <= 0) return

        val cx = width / 2f
        val cy = height / 2f
        val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // رنگ سبز به جای قرمز
        val shader = RadialGradient(
            cx, cy, radius,
            intArrayOf(
                Color.argb(0, 0, 255, 0),
                Color.argb((currentAlphaValue * 0.10f).toInt(), 0, 255, 0),
                Color.argb((currentAlphaValue * 0.25f).toInt(), 0, 255, 0),
                Color.argb((currentAlphaValue * 0.55f).toInt(), 0, 255, 0),
                Color.argb(currentAlphaValue, 0, 255, 0)
            ),
            floatArrayOf(0f, 0.45f, 0.70f, 0.88f, 1f),
            Shader.TileMode.CLAMP
        )

        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun showHeal(intensity: Float, duration: Long = 2000L) {
        val safeIntensity = intensity.coerceIn(0f, 1f)
        val maxAlpha = (safeIntensity * 220).toInt().coerceIn(0, 220)

        healingAnimator?.cancel()

        healingAnimator = ValueAnimator.ofInt(maxAlpha, 0).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                currentAlphaValue = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }
}