package com.example.codexpdfone

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.graphics.pdf.PdfRenderer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.codexpdfone.ui.PdfAdapter
import com.example.codexpdfone.ui.PdfPage
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

/**
 * Activity showing multiple PDF files in a scrollable list. Each page supports
 * pinch zoom and freehand annotations that scale along with the PDF.
 */
class PdfActivity : AppCompatActivity() {

    private val renderers = mutableListOf<PdfRenderer>()
    private val pages = mutableListOf<PdfPage>()
    private lateinit var adapter: PdfAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf)

        val pager = findViewById<ViewPager2>(R.id.pdfViewPager)

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

        adapter = PdfAdapter(pages)
        pager.adapter = adapter
        pager.orientation = ViewPager2.ORIENTATION_VERTICAL

        val addFab = findViewById<FloatingActionButton>(R.id.addImageFab)
        addFab.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_launcher_foreground
            )
            adapter.addImage(pager.currentItem, bitmap)
        }
    }

    override fun onDestroy() {
        renderers.forEach { it.close() }
        super.onDestroy()
    }
}
