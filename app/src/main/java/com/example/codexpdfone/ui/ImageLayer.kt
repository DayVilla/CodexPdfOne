package com.example.codexpdfone.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * View that draws bitmaps over the PDF and allows moving and removing them.
 */
class ImageLayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val images = mutableListOf<ImageItem>()
    private val matrix = Matrix()
    private val inverse = Matrix()

    private var active: ImageItem? = null
    private var lastX = 0f
    private var lastY = 0f

    data class ImageItem(val bitmap: Bitmap, var x: Float, var y: Float)

    fun updateMatrix(newMatrix: Matrix) {
        matrix.set(newMatrix)
        matrix.invert(inverse)
        invalidate()
    }

    fun addImage(bitmap: Bitmap) {
        images.add(ImageItem(bitmap, 0f, 0f))
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pts = floatArrayOf(event.x, event.y)
        inverse.mapPoints(pts)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                active = images.lastOrNull { item ->
                    pts[0] >= item.x && pts[0] <= item.x + item.bitmap.width &&
                            pts[1] >= item.y && pts[1] <= item.y + item.bitmap.height
                }
                if (active != null) {
                    lastX = pts[0]
                    lastY = pts[1]
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                active?.let { item ->
                    val dx = pts[0] - lastX
                    val dy = pts[1] - lastY
                    item.x += dx
                    item.y += dy
                    lastX = pts[0]
                    lastY = pts[1]
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                active?.let { item ->
                    if (event.eventTime - event.downTime > 800) {
                        images.remove(item)
                    }
                }
                active = null
                parent.requestDisallowInterceptTouchEvent(false)
                invalidate()
                return active != null
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.concat(matrix)
        for (item in images) {
            canvas.drawBitmap(item.bitmap, item.x, item.y, null)
        }
        canvas.restore()
    }
}
