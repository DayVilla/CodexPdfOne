package com.example.codexpdfone.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.chrisbanes.photoview.PhotoView

/**
 * View combining a PhotoView for PDF page rendering and an AnnotationView
 * that is scaled together with the PhotoView matrix.
 */
class PdfPageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val photoView = PhotoView(context)
    private val annotationView = AnnotationView(context)

    init {
        addView(photoView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        addView(annotationView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        photoView.setOnMatrixChangeListener {
            annotationView.updateMatrix(photoView.imageMatrix)
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
        annotationView.updateMatrix(photoView.imageMatrix)
    }

    fun enableDrawing(enabled: Boolean) {
        annotationView.drawingEnabled = enabled
    }

    fun clearAnnotations() {
        annotationView.clearAnnotations()
    }
}
