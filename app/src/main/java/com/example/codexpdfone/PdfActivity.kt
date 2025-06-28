package com.example.codexpdfone

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.example.codexpdfone.ui.AnnotationView

/**
 * Activity that showcases PDF viewing with a simple annotation overlay.
 * The PDF rendering is provided by AndroidPdfViewer (Pdfium based) which
 * supports smooth zooming and panning. AnnotationView demonstrates how
 * to capture freehand drawings that could later be converted into PDF
 * annotations using a library like PdfBox-Android.
 */
class PdfActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private lateinit var annotationView: AnnotationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf)

        pdfView = findViewById(R.id.pdfView)
        annotationView = findViewById(R.id.annotationView)

        // Load a sample PDF from assets. In a real app this could come from
        // external storage or be created dynamically.
        val uri: Uri = Uri.parse("file:///android_asset/sample.pdf")
        pdfView.fromUri(uri)
            .enableAnnotationRendering(true)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()

        // Allow user to draw over the PDF for annotations.
        annotationView.drawingEnabled = true
    }
}
