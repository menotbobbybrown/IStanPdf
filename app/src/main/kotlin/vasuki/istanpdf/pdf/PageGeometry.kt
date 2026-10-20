package vasuki.istanpdf.pdf

import com.itextpdf.kernel.geom.Rectangle
import vasuki.istanpdf.model.NormalizeTarget

object PageGeometry {
    data class PageLayout(
        val width: Float,
        val height: Float,
        val contentRect: Rectangle
    )

    fun layout(
        target: NormalizeTarget,
        contentWidth: Float,
        contentHeight: Float,
        rotation: Int
    ): PageLayout {
        if (target == NormalizeTarget.NONE) {
            return PageLayout(contentWidth, contentHeight, Rectangle(0f, 0f, contentWidth, contentHeight))
        }
        val swapped = rotation % 180 != 0
        val displayedWidth = if (swapped) contentHeight else contentWidth
        val displayedHeight = if (swapped) contentWidth else contentHeight
        val landscape = displayedWidth > displayedHeight
        val targetDisplayedWidth = if (landscape) {
            maxOf(target.widthPt, target.heightPt)
        } else {
            minOf(target.widthPt, target.heightPt)
        }
        val targetDisplayedHeight = if (landscape) {
            minOf(target.widthPt, target.heightPt)
        } else {
            maxOf(target.widthPt, target.heightPt)
        }
        val pageWidth = if (swapped) targetDisplayedHeight else targetDisplayedWidth
        val pageHeight = if (swapped) targetDisplayedWidth else targetDisplayedHeight
        return PageLayout(
            pageWidth,
            pageHeight,
            fitInside(contentWidth, contentHeight, pageWidth, pageHeight)
        )
    }

    fun fitInside(
        contentWidth: Float,
        contentHeight: Float,
        pageWidth: Float,
        pageHeight: Float
    ): Rectangle {
        val scale = minOf(pageWidth / contentWidth, pageHeight / contentHeight)
        val width = contentWidth * scale
        val height = contentHeight * scale
        return Rectangle((pageWidth - width) / 2f, (pageHeight - height) / 2f, width, height)
    }
}
