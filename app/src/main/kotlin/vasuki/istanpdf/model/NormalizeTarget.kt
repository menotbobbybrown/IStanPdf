package vasuki.istanpdf.model

enum class NormalizeTarget(
    val label: String,
    val widthPt: Float,
    val heightPt: Float
) {
    NONE("Off", 0f, 0f),
    A4("A4", 595f, 842f),
    A3("A3", 842f, 1191f),
    LETTER("Letter", 612f, 792f),
    LEGAL("Legal", 612f, 1008f);

    val isEnabled: Boolean get() = this != NONE

    companion object {
        val presets: List<NormalizeTarget> = listOf(A4, A3, LETTER, LEGAL)
    }
}
