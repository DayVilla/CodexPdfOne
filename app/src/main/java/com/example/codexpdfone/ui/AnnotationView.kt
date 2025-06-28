package com.example.codexpdfone.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Simple view used to draw freehand annotations over the PDF.
 * This can be extended to support shapes and other annotation types.
 */
class AnnotationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val path = Path()
    var drawingEnabled: Boolean = false
    private val matrix = Matrix()
    private val inverse = Matrix()

    fun updateMatrix(newMatrix: Matrix) {
        matrix.set(newMatrix)
        matrix.invert(inverse)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!drawingEnabled) return false
        val pts = floatArrayOf(event.x, event.y)
        inverse.mapPoints(pts)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(pts[0], pts[1])
            MotionEvent.ACTION_MOVE -> path.lineTo(pts[0], pts[1])
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.concat(matrix)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    /**
     * Clears current annotations. In a full implementation the path data should
     * be converted into PDF annotations using a library like PdfBox-Android.
     */
    fun clearAnnotations() {
        path.reset()
        invalidate()
    }
}
