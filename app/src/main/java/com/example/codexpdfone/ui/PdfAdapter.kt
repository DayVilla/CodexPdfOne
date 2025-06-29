package com.example.codexpdfone.ui

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

private const val SCALE = 2f

/**
 * RecyclerView adapter that renders PDF pages into [PdfPageView].
 */
class PdfAdapter(private val pages: List<PdfPage>): RecyclerView.Adapter<PdfAdapter.PageHolder>() {

    class PageHolder(val pageView: PdfPageView): RecyclerView.ViewHolder(pageView) {
        var index: Int = -1
    }

    private val holders = mutableMapOf<Int, PageHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        val view = PdfPageView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.enableDrawing(true)
        return PageHolder(view)
    }

    override fun getItemCount() = pages.size

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        val desc = pages[position]
        holder.index = position
        val page = desc.renderer.openPage(desc.index)
        val width = (page.width * SCALE).toInt()
        val height = (page.height * SCALE).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rect = Rect(0, 0, width, height)
        page.render(bitmap, rect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        holder.pageView.setBitmap(bitmap)
        holders[position] = holder
    }

    override fun onViewRecycled(holder: PageHolder) {
        holders.entries.removeIf { it.value == holder }
        super.onViewRecycled(holder)
    }

    fun addImage(position: Int, bitmap: Bitmap) {
        holders[position]?.pageView?.addImage(bitmap)
    }
}

/**
 * Simple descriptor for a PDF page belonging to a [PdfRenderer].
 */
data class PdfPage(val renderer: PdfRenderer, val index: Int)
