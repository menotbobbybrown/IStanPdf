package vasuki.istanpdf.pdf

import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import vasuki.istanpdf.model.NormalizeTarget

object PdfPageNormalizer {
    fun normalize(document: PdfDocument, target: NormalizeTarget) {
        if (target == NormalizeTarget.NONE) return
        for (index in 1..document.numberOfPages) {
            if (Thread.currentThread().isInterrupted) throw InterruptedException()
            normalizePage(document, document.getPage(index), target)
        }
    }

    private fun normalizePage(document: PdfDocument, page: PdfPage, target: NormalizeTarget) {
        val visible = Rectangle.getCommonRectangle(page.mediaBox, page.cropBox)
        if (visible.width <= 0f || visible.height <= 0f) return

        val layout = PageGeometry.layout(target, visible.width, visible.height, page.rotation)
        val contentRect = layout.contentRect
        val scale = contentRect.width / visible.width
        val offsetX = contentRect.x - visible.left * scale
        val offsetY = contentRect.y - visible.bottom * scale

        val before = PdfCanvas(page.newContentStreamBefore(), page.resources, document)
        before.saveState()
        before.concatMatrix(
            scale.toDouble(),
            0.0,
            0.0,
            scale.toDouble(),
            offsetX.toDouble(),
            offsetY.toDouble()
        )
        before.release()

        val after = PdfCanvas(page.newContentStreamAfter(), page.resources, document)
        after.restoreState()
        after.release()

        val box = Rectangle(0f, 0f, layout.width, layout.height)
        page.setMediaBox(box)
        page.setCropBox(box)
        page.setTrimBox(box)
        page.setBleedBox(box)
        page.setArtBox(box)
    }
}
