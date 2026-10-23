package vasuki.istanpdf.presentation

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.TextView
import vasuki.istanpdf.AppFont
import vasuki.istanpdf.R
import vasuki.istanpdf.ThemePrefs

class RoundedDropdown(
    private val activity: Activity,
    private val anchor: View,
    private val options: List<String>,
    private val selectedIndex: Int,
    private val onSelect: (Int) -> Unit
) {
    private var popup: PopupWindow? = null

    fun show() {
        dismiss()
        val content = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(4), 0, dp(4))
        }
        for (index in options.indices) {
            content.addView(
                optionRow(options[index], index == selectedIndex) {
                    onSelect(index)
                    dismiss()
                },
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(48))
            )
        }

        val scroll = ScrollView(activity).apply {
            isFillViewport = true
            isVerticalScrollBarEnabled = false
            overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            background = GradientDrawable().apply {
                cornerRadius = dp(16).toFloat()
                setColor(color(R.color.istan_surface_high))
                setStroke(dp(1), color(R.color.istan_outline))
            }
            clipToOutline = true
        }
        scroll.addView(
            content,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val frame = Rect()
        anchor.getWindowVisibleDisplayFrame(frame)
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorTop = location[1]
        val anchorBottom = anchorTop + anchor.height
        val spaceBelow = (frame.bottom - anchorBottom).coerceAtLeast(0)
        val spaceAbove = (anchorTop - frame.top).coerceAtLeast(0)
        val maxHeight = options.size * dp(48) + dp(8)
        val available = (maxOf(spaceBelow, spaceAbove) - dp(8)).coerceAtLeast(0)
        val popupHeight = maxHeight.coerceAtMost(available).coerceAtLeast(dp(48))
        val openBelow = spaceBelow >= popupHeight + dp(4) || spaceBelow >= spaceAbove
        val yoff = if (openBelow) dp(4) else -(popupHeight + dp(4))
        val popupWidth = dp(180).coerceAtMost(
            (frame.width() - dp(32)).coerceAtLeast(dp(1))
        )

        val window = PopupWindow(
            scroll,
            popupWidth,
            popupHeight,
            true
        ).apply {
            elevation = dp(8).toFloat()
            isOutsideTouchable = true
            inputMethodMode = PopupWindow.INPUT_METHOD_NOT_NEEDED
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        popup = window
        window.showAsDropDown(anchor, 0, yoff, Gravity.END)
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    private fun optionRow(label: String, selected: Boolean, onClick: () -> Unit): LinearLayout {
        val row = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), 0, dp(16), 0)
            minimumHeight = dp(48)
            isFocusable = true
            isSelected = selected
            setBackgroundResource(selectableBackground())
            setOnClickListener { onClick() }
        }

        val text = TextView(activity).apply {
            this.text = label
            textSize = 15f
            typeface = AppFont.regular
            setTextColor(color(R.color.istan_text))
            includeFontPadding = true
        }
        row.addView(
            text,
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        )

        val check = ImageView(activity)
        check.setImageResource(R.drawable.check_24px)
        check.setColorFilter(color(R.color.istan_olive))
        check.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        check.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        row.addView(check, LinearLayout.LayoutParams(dp(20), dp(20)))

        return row
    }

    private fun selectableBackground(): Int {
        val typed = TypedValue()
        activity.theme.resolveAttribute(android.R.attr.selectableItemBackground, typed, true)
        return typed.resourceId
    }

    private fun color(colorRes: Int): Int = ThemePrefs.resolveColor(activity, colorRes)

    private fun dp(value: Int): Int = Math.round(value * activity.resources.displayMetrics.density)
}
