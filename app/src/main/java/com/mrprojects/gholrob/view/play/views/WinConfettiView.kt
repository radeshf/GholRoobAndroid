package com.mrprojects.gholrob.view.play.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin
import kotlin.random.Random

class WinConfettiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class Particle(
        var x: Float, var y: Float,
        var vx: Float, var vy: Float,
        var size: Float, var color: Int,
        var rotation: Float, var rotationSpeed: Float,
        var alpha: Int, var shape: Int, // 0: Rect, 1: Circle, 2: Strip
        var wiggleFreq: Float, var wiggleAmp: Float, var offset: Float
    )

    private val particles = mutableListOf<Particle>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var isRunning = false
    private var startTime: Long = 0

    private val colors = listOf(
        Color.parseColor("#FF4D6D"), Color.parseColor("#FFD166"),
        Color.parseColor("#06D6A0"), Color.parseColor("#118AB2"),
        Color.parseColor("#8338EC"), Color.parseColor("#FF9F1C")
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isRunning) return

        particles.forEach { p ->
            paint.color = p.color
            paint.alpha = p.alpha

            canvas.save()
            canvas.translate(p.x, p.y)
            canvas.rotate(p.rotation)

            when (p.shape) {
                0 -> canvas.drawRect(-p.size / 2, -p.size / 2, p.size / 2, p.size / 2, paint)
                1 -> canvas.drawCircle(0f, 0f, p.size / 2, paint)
                2 -> canvas.drawRect(-p.size / 4, -p.size, p.size / 4, p.size, paint)
            }
            canvas.restore()
        }
    }

    fun startConfetti(duration: Long = 3000L, countPerSide: Int = 60) {
        if (width == 0 || height == 0) {
            post { startConfetti(duration, countPerSide) }
            return
        }

        startTime = System.currentTimeMillis()
        animator?.cancel()
        particles.clear()

        createParticles(countPerSide, true)
        createParticles(countPerSide, false)

        isRunning = true
        visibility = VISIBLE

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            addUpdateListener {
                updateParticles()
                invalidate()
            }
            start()
        }
    }

    fun stopConfetti() {
        animator?.cancel()
        particles.clear()
        isRunning = false
        invalidate()
    }

    private fun createParticles(count: Int, fromLeft: Boolean) {
        repeat(count) {
            particles.add(
                Particle(
                    x = if (fromLeft) 0f else width.toFloat(),
                    y = height * (0.1f + Random.nextFloat() * 0.4f),
                    vx = if (fromLeft) (8f + Random.nextFloat() * 15f) else -(8f + Random.nextFloat() * 15f),
                    vy = -15f + Random.nextFloat() * 10f,
                    size = dp(6f + Random.nextFloat() * 8f),
                    color = colors.random(),
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = -10f + Random.nextFloat() * 20f,
                    alpha = 255,
                    shape = Random.nextInt(0, 3),
                    wiggleFreq = 0.05f + Random.nextFloat() * 0.1f,
                    wiggleAmp = 1f + Random.nextFloat() * 3f,
                    offset = Random.nextFloat() * 6.28f // زاویه تصادفی برای شروع نوسان
                )
            )
        }
    }

    private fun updateParticles() {
        val gravity = 0.35f
        val airDrag = 0.98f
        val time = (System.currentTimeMillis() - startTime).toFloat() * 0.01f

        particles.forEach { p ->
            // محاسبه نوسان با سینوس
            val sway = sin(time * p.wiggleFreq + p.offset) * p.wiggleAmp

            p.vx += sway * 0.1f
            p.vx *= airDrag
            p.vy += gravity
            p.vy *= airDrag

            p.x += p.vx
            p.y += p.vy

            // چرخش نامنظم (متصل به sway)
            p.rotation += p.rotationSpeed + sway

            // محو شدن در انتهای صفحه
            if (p.y > height * 0.6f) {
                p.alpha = (p.alpha - 3).coerceAtLeast(0)
            }
        }

        particles.removeAll { it.alpha <= 0 || it.y > height + dp(50f) }

        if (particles.isEmpty() && isRunning) {
            stopConfetti()
        }
    }

    private fun dp(value: Float): Float = value * resources.displayMetrics.density
}
