package ir.radesh.basemodule.shapes

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable


class RoundedArcDrawable(
    private val arcColor: Int,
    private val trackColor: Int
) : Drawable() {

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 24f
        color = trackColor
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 24f
        color = arcColor
    }

    var progress: Int = 0
        set(value) {
            field = value.coerceIn(0, 100)
            invalidateSelf()
        }

    private val oval = RectF()

    override fun onBoundsChange(bounds: Rect) {
        val inset = arcPaint.strokeWidth / 2f
        oval.set(
            bounds.left + inset,
            bounds.top + inset,
            bounds.right - inset,
            bounds.bottom - inset
        )
    }

    override fun draw(canvas: Canvas) {
        // track (full circle)
        canvas.drawArc(oval, -90f, 360f, false, trackPaint)

        // progress arc
        if (progress > 0) {
            val sweep = 360f * progress / 100f
            canvas.drawArc(oval, -90f, sweep, false, arcPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        arcPaint.alpha = alpha
        trackPaint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        arcPaint.colorFilter = colorFilter
        trackPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
