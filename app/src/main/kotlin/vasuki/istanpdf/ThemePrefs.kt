package vasuki.istanpdf

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color

object ThemePrefs {
    const val PREFS_NAME = "istan_theme"
    private const val KEY_AMOLED = "amoled"
    private const val KEY_THEME_MODE = "theme_mode"
    private const val LEGACY_THEME_AMOLED = 3

    const val THEME_AUTO = 0
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2

    enum class TypeScale(val sp: Float, val isBold: Boolean, val letterSpacing: Float = 0f, val isAllCaps: Boolean = false) {
        HEADLINE(32f, true, -0.02f, false),
        TITLE(22f, true, 0f, false),
        BODY(16f, false, 0f, false),
        LABEL(14f, true, 0f, false),
        CAPTION(12f, false, 0.04f, true)
    }

    fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun themeMode(context: Context): Int {
        val preferences = prefs(context)
        if (preferences.contains(KEY_THEME_MODE)) {
            val mode = preferences.getInt(KEY_THEME_MODE, THEME_AUTO)
            if (mode == LEGACY_THEME_AMOLED) return THEME_DARK
            if (mode in THEME_AUTO..THEME_DARK) return mode
        }
        if (preferences.contains(KEY_AMOLED)) {
            return if (preferences.getBoolean(KEY_AMOLED, false)) THEME_DARK else THEME_LIGHT
        }
        return THEME_AUTO
    }

    fun setThemeMode(context: Context, mode: Int) {
        if (mode < THEME_AUTO || mode > THEME_DARK) return
        prefs(context).edit().putInt(KEY_THEME_MODE, mode).remove(KEY_AMOLED).apply()
    }

    fun isDark(context: Context): Boolean {
        val mode = themeMode(context)
        if (mode == THEME_LIGHT) return false
        if (mode == THEME_DARK) return true
        val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun token(context: Context): String =
        "${themeMode(context)}:${isDark(context)}"

    fun resolveColor(context: Context, colorRes: Int): Int {
        val dark = isDark(context)
        return when (colorRes) {
            R.color.istan_olive -> if (dark) Color.WHITE else Color.parseColor("#09090B")
            R.color.istan_olive_dark -> if (dark) Color.parseColor("#E4E4E7") else Color.parseColor("#27272A")
            R.color.istan_background -> if (dark) Color.BLACK else Color.parseColor("#FAFAFA")
            R.color.istan_surface -> if (dark) Color.parseColor("#0C0C0E") else Color.WHITE
            R.color.istan_surface_high -> if (dark) Color.parseColor("#1C1C1F") else Color.parseColor("#F4F4F5")
            R.color.istan_sheet -> if (dark) Color.parseColor("#0C0C0E") else Color.WHITE
            R.color.istan_outline -> if (dark) Color.parseColor("#27272A") else Color.parseColor("#E4E4E7")
            R.color.istan_text -> if (dark) Color.parseColor("#FAFAFA") else Color.parseColor("#09090B")
            R.color.istan_text_muted -> if (dark) Color.parseColor("#A1A1AA") else Color.parseColor("#52525B")
            else -> context.getColor(colorRes)
        }
    }
}
