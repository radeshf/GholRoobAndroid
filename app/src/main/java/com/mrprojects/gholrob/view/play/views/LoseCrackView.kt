package com.mrprojects.gholrob.view.play.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class LoseCrackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class CrackBranch(
        val points: MutableList<PointF>,
        val maxProgress: Float,
        val thickness: Float
    )

    private val crackBranches = mutableListOf<CrackBranch>()

    private val crackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.argb(220, 255, 255, 255)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.argb(80, 180, 220, 255)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private val dimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(0, 0, 0, 0)
    }

    private var crackAnimator: ValueAnimator? = null
    private var fadeAnimator: ValueAnimator? = null

    private var drawProgress = 0f
    private var overlayAlpha = 0
    private var isRunning = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isRunning) return

        // تیره شدن خیلی کم پس‌زمینه
        dimPaint.color = Color.argb(overlayAlpha, 0, 0, 0)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)

        crackBranches.forEach { branch ->
            drawBranch(canvas, branch)
        }
    }

    private fun drawBranch(canvas: Canvas, branch: CrackBranch) {
        if (branch.points.size < 2) return

        val visibleSegments = ((branch.points.size - 1) * (drawProgress / branch.maxProgress))
            .coerceIn(0f, (branch.points.size - 1).toFloat())

        val fullSegments = visibleSegments.toInt()
        val partial = visibleSegments - fullSegments

        val path = Path()

        path.moveTo(branch.points[0].x, branch.points[0].y)

        for (i in 1..fullSegments.coerceAtMost(branch.points.lastIndex)) {
            path.lineTo(branch.points[i].x, branch.points[i].y)
        }

        if (fullSegments < branch.points.lastIndex) {
            val start = branch.points[fullSegments]
            val end = branch.points[fullSegments + 1]

            val x = start.x + (end.x - start.x) * partial
            val y = start.y + (end.y - start.y) * partial
            path.lineTo(x, y)
        }

        glowPaint.strokeWidth = branch.thickness + dp(1.5f)
        crackPaint.strokeWidth = branch.thickness

        canvas.drawPath(path, glowPaint)
        canvas.drawPath(path, crackPaint)
    }

    fun playCrack(
        centerX: Float = width / 2f,
        centerY: Float = height / 2f,
        branchCount: Int = 8,
        duration: Long = 650L,
        onEnd: (() -> Unit)? = null
    ) {
        if (width == 0 || height == 0) {
            post { playCrack(centerX, centerY, branchCount, duration, onEnd) }
            return
        }

        crackAnimator?.cancel()
        fadeAnimator?.cancel()

        generateCracks(centerX, centerY, branchCount)

        drawProgress = 0f
        overlayAlpha = 0
        isRunning = true
        visibility = VISIBLE

        crackAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()

            addUpdateListener { animator ->
                drawProgress = animator.animatedValue as Float
                overlayAlpha = (drawProgress * 90).toInt().coerceIn(0, 90)
                postInvalidateOnAnimation()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    startFadeOut(onEnd)
                }
            })

            start()
        }
    }

    private fun startFadeOut(onEnd: (() -> Unit)?) {
        fadeAnimator?.cancel()
        fadeAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 500L
            interpolator = DecelerateInterpolator()

            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                crackPaint.alpha = (220 * value).toInt().coerceIn(0, 220)
                glowPaint.alpha = (80 * value).toInt().coerceIn(0, 80)
                overlayAlpha = (90 * value).toInt().coerceIn(0, 90)
                postInvalidateOnAnimation()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    reset()
                    onEnd?.invoke()
                }
            })

            start()
        }
    }

    private fun generateCracks(centerX: Float, centerY: Float, branchCount: Int) {
        crackBranches.clear()

        val maxLen = min(width, height) * 0.35f
        val angleStep = 360f / branchCount

        repeat(branchCount) { index ->
            val baseAngle = angleStep * index + Random.nextFloat() * 18f - 9f
            val branchLength = maxLen * (0.65f + Random.nextFloat() * 0.45f)
            val segments = 4 + Random.nextInt(4)

            val points = mutableListOf<PointF>()
            points.add(PointF(centerX, centerY))

            var currentX = centerX
            var currentY = centerY
            var currentAngle = Math.toRadians(baseAngle.toDouble())

            repeat(segments) { seg ->
                val segLen = branchLength / segments * (0.8f + Random.nextFloat() * 0.5f)
                currentAngle += Math.toRadians((-18f + Random.nextFloat() * 36f).toDouble())

                currentX += (cos(currentAngle) * segLen).toFloat()
                currentY += (sin(currentAngle) * segLen).toFloat()

                points.add(
                    PointF(
                        currentX.coerceIn(0f, width.toFloat()),
                        currentY.coerceIn(0f, height.toFloat())
                    )
                )

                // شاخه فرعی
                if (seg in 1 until segments - 1 && Random.nextFloat() < 0.35f) {
                    crackBranches.add(
                        createSubBranch(
                            startX = currentX,
                            startY = currentY,
                            parentAngle = currentAngle
                        )
                    )
                }
            }

            crackBranches.add(
                CrackBranch(
                    points = points,
                    maxProgress = 0.7f + Random.nextFloat() * 0.3f,
                    thickness = dp(1.2f + Random.nextFloat() * 1.8f)
                )
            )
        }
    }

    private fun createSubBranch(
        startX: Float,
        startY: Float,
        parentAngle: Double
    ): CrackBranch {
        val points = mutableListOf<PointF>()
        points.add(PointF(startX, startY))

        var currentX = startX
        var currentY = startY
        var angle = parentAngle + Math.toRadians(if (Random.nextBoolean()) 25.0 else -25.0)
        val segments = 2 + Random.nextInt(3)
        val totalLength = min(width, height) * (0.08f + Random.nextFloat() * 0.12f)

        repeat(segments) {
            val segLen = totalLength / segments * (0.8f + Random.nextFloat() * 0.5f)
            angle += Math.toRadians((-14f + Random.nextFloat() * 28f).toDouble())

            currentX += (cos(angle) * segLen).toFloat()
            currentY += (sin(angle) * segLen).toFloat()

            points.add(
                PointF(
                    currentX.coerceIn(0f, width.toFloat()),
                    currentY.coerceIn(0f, height.toFloat())
                )
            )
        }

        return CrackBranch(
            points = points,
            maxProgress = 0.55f + Random.nextFloat() * 0.35f,
            thickness = dp(0.8f + Random.nextFloat() * 1.2f)
        )
    }

    fun reset() {
        crackAnimator?.cancel()
        fadeAnimator?.cancel()
        crackBranches.clear()
        drawProgress = 0f
        overlayAlpha = 0
        crackPaint.alpha = 220
        glowPaint.alpha = 80
        isRunning = false
        visibility = GONE
        invalidate()
    }

    private fun dp(value: Float): Float = value * resources.displayMetrics.density
}
