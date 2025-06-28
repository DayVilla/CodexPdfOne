package com.example.codexpdfone

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.graphics.pdf.PdfRenderer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codexpdfone.ui.PdfAdapter
import com.example.codexpdfone.ui.PdfPage
import java.io.File
import java.io.FileOutputStream

/**
 * Activity showing multiple PDF files in a scrollable list. Each page supports
 * pinch zoom and freehand annotations that scale along with the PDF.
 */
class PdfActivity : AppCompatActivity() {

    private val renderers = mutableListOf<PdfRenderer>()
    private val pages = mutableListOf<PdfPage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf)

        val recycler = findViewById<RecyclerView>(R.id.pdfRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)

        // Load all PDF assets and collect their pages
        val assetPdfs = assets.list("")?.filter { it.endsWith(".pdf") } ?: emptyList()
        for (name in assetPdfs) {
            val file = File(cacheDir, name)
            if (!file.exists()) {
                assets.open(name).use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(descriptor)
            renderers.add(renderer)
            for (i in 0 until renderer.pageCount) {
                pages.add(PdfPage(renderer, i))
            }
        }

        recycler.adapter = PdfAdapter(pages)
    }

    override fun onDestroy() {
        renderers.forEach { it.close() }
        super.onDestroy()
    }
}
