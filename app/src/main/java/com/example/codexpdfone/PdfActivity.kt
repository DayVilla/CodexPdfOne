package com.example.codexpdfone

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.example.codexpdfone.ui.AnnotationView

/**
 * Activity that showcases PDF viewing with a simple annotation overlay.
 * The PDF rendering uses Android's PdfRenderer with PhotoView for
 * smooth zooming and panning. AnnotationView demonstrates how
 * to capture freehand drawings that could later be converted into PDF
 * annotations using a library like PdfBox-Android.
 */
class PdfActivity : AppCompatActivity() {

    private lateinit var photoView: PhotoView
    private lateinit var annotationView: AnnotationView
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf)

        photoView = findViewById(R.id.pdfImage)
        annotationView = findViewById(R.id.annotationView)

        // Copy PDF asset to a file so PdfRenderer can read it
        val file = File(cacheDir, "sample.pdf")
        if (!file.exists()) {
            assets.open("sample.pdf").use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(descriptor)
        showPage(0)

        // Allow user to draw over the PDF for annotations.
        annotationView.drawingEnabled = true
    }

    private fun showPage(index: Int) {
        currentPage?.close()
        val renderer = pdfRenderer ?: return
        if (index < 0 || index >= renderer.pageCount) return
        val page = renderer.openPage(index)
        currentPage = page
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        photoView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        currentPage?.close()
        pdfRenderer?.close()
        super.onDestroy()
    }
}