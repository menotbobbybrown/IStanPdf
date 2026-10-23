package vasuki.istanpdf.domain

import android.net.Uri
import vasuki.istanpdf.data.PdfEngine
import vasuki.istanpdf.model.NormalizeTarget
import vasuki.istanpdf.model.PageItem
import vasuki.istanpdf.model.PageOrientation

class ReorderPdf(private val pdfEngine: PdfEngine) {
    @Throws(Exception::class)
    fun execute(
        source: Uri,
        pages: List<PageItem>,
        destination: Uri,
        normalize: NormalizeTarget = NormalizeTarget.NONE,
        orientation: PageOrientation = PageOrientation.AUTO
    ): Int {
        return pdfEngine.reorder(source, pages, destination, normalize, orientation)
    }
}
