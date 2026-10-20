package vasuki.istanpdf.model

enum class NormalizeTarget(
    val label: String,
    val detail: String,
    val widthPt: Float,
    val heightPt: Float
) {
    NONE("Off", "Original page size", 0f, 0f),
    A4("A4", "210 × 297 mm", 595f, 842f),
    A3("A3", "297 × 420 mm", 842f, 1191f),
    LETTER("Letter", "8.5 × 11 in", 612f, 792f),
    LEGAL("Legal", "8.5 × 14 in", 612f, 1008f);

    val isEnabled: Boolean get() = this != NONE

    companion object {
        val presets: List<NormalizeTarget> = listOf(A4, A3, LETTER, LEGAL)
    }
}
