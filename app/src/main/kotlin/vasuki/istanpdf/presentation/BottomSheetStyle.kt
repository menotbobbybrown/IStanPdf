package vasuki.istanpdf.presentation

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import vasuki.istanpdf.R
import vasuki.istanpdf.ThemePrefs

object BottomSheetStyle {
    fun apply(activity: Activity, dialog: BottomSheetDialog, sheet: View) {
        val sheetColor = ThemePrefs.resolveColor(activity, R.color.istan_sheet)
        sheet.backgroundTintList = ColorStateList.valueOf(sheetColor)
        val window = dialog.window ?: return
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = sheetColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        val controller = WindowCompat.getInsetsController(window, sheet)
        controller.isAppearanceLightNavigationBars = !ThemePrefs.isAmoled(activity)
    }
}
