package vasuki.istanpdf.presentation

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import vasuki.istanpdf.AppFont
import vasuki.istanpdf.R
import vasuki.istanpdf.ThemePrefs
import vasuki.istanpdf.model.NormalizeTarget
import vasuki.istanpdf.model.PageOrientation

class DocumentOptionsSheet(
    private val activity: Activity,
    private val showPageSize: Boolean,
    private val showOrientation: Boolean,
    private val showEditRange: Boolean,
    normalization: NormalizeTarget,
    orientation: PageOrientation,
    private val onNormalization: (NormalizeTarget) -> Unit,
    private val onOrientation: (PageOrientation) -> Unit,
    private val onEditRange: () -> Unit
) {
    private var selectedNormalization = normalization
    private var selectedOrientation = orientation
    private var activeDropdown: RoundedDropdown? = null

    fun show() {
        val dialog = BottomSheetDialog(activity)
        val scroll = ScrollView(activity)
        scroll.isFillViewport = true
        val root = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(16), 0, dp(24))
        }
        scroll.addView(
            root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val title = text("Document Options", 20, R.color.istan_text, true)
        title.setPadding(dp(24), dp(8), dp(24), dp(16))
        root.addView(title)

        if (showPageSize) {
            val targets = listOf(NormalizeTarget.NONE) + NormalizeTarget.presets
            val setting = addSettingRow(
                root,
                R.drawable.crop_free_24px,
                "Page Size",
                selectedNormalization.label
            )
            val open = {
                activeDropdown?.dismiss()
                activeDropdown = RoundedDropdown(
                    activity = activity,
                    anchor = setting.pill,
                    options = targets.map { it.label },
                    selectedIndex = targets.indexOf(selectedNormalization),
                    onSelect = { index ->
                        val target = targets[index]
                        selectedNormalization = target
                        setting.value.text = "${target.label} \u25BE"
                        setting.pill.contentDescription = "Page Size: ${target.label}"
                        onNormalization(target)
                    }
                ).also { it.show() }
            }
            setting.row.setOnClickListener { open() }
            setting.pill.setOnClickListener { open() }
        }

        if (showOrientation) {
            val options = PageOrientation.values().toList()
            val setting = addSettingRow(
                root,
                R.drawable.crop_rotate_24px,
                "Orientation",
                selectedOrientation.label
            )
            val open = {
                activeDropdown?.dismiss()
                activeDropdown = RoundedDropdown(
                    activity = activity,
                    anchor = setting.pill,
                    options = options.map { it.label },
                    selectedIndex = options.indexOf(selectedOrientation),
                    onSelect = { index ->
                        val orientation = options[index]
                        selectedOrientation = orientation
                        setting.value.text = "${orientation.label} \u25BE"
                        setting.pill.contentDescription = "Orientation: ${orientation.label}"
                        onOrientation(orientation)
                    }
                ).also { it.show() }
            }
            setting.row.setOnClickListener { open() }
            setting.pill.setOnClickListener { open() }
        }

        if (showEditRange) {
            val row = LinearLayout(activity).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(24), 0, dp(24), 0)
                minimumHeight = dp(52)
                isFocusable = true
                setBackgroundResource(rowBackground())
                setOnClickListener {
                    activeDropdown?.dismiss()
                    activeDropdown = null
                    dialog.dismiss()
                    onEditRange()
                }
            }

            val icon = ImageView(activity)
            icon.setImageResource(R.drawable.edit_minimal_24px)
            icon.setColorFilter(color(R.color.istan_text_muted))
            icon.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            row.addView(icon, LinearLayout.LayoutParams(dp(24), dp(24)))

            val label = text("Edit Range", 16, R.color.istan_text, false)
            label.setPadding(dp(16), 0, 0, 0)
            row.addView(label, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

            val chevron = ImageView(activity)
            chevron.setImageResource(R.drawable.chevron_right_24px)
            chevron.setColorFilter(color(R.color.istan_text_muted))
            chevron.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            row.addView(chevron, LinearLayout.LayoutParams(dp(22), dp(22)))

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(52)
            )
            params.topMargin = dp(8)
            root.addView(row, params)
        }

        dialog.setOnDismissListener {
            activeDropdown?.dismiss()
            activeDropdown = null
        }
        dialog.setContentView(scroll)
        dialog.show()
        val sheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        sheet?.let {
            BottomSheetStyle.apply(activity, dialog, it)
            BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun addSettingRow(
        root: LinearLayout,
        iconRes: Int,
        label: String,
        value: String
    ): SettingRow {
        val row = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(24), 0, dp(24), 0)
            minimumHeight = dp(56)
            isFocusable = true
            setBackgroundResource(rowBackground())
        }

        val icon = ImageView(activity)
        icon.setImageResource(iconRes)
        icon.setColorFilter(color(R.color.istan_olive))
        icon.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        row.addView(icon, LinearLayout.LayoutParams(dp(24), dp(24)))

        val labelView = text(label, 16, R.color.istan_text, false)
        labelView.setPadding(dp(16), 0, dp(12), 0)
        row.addView(labelView, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val pill = MaterialCardView(activity).apply {
            radius = dp(20).toFloat()
            cardElevation = 0f
            strokeWidth = dp(1)
            strokeColor = color(R.color.istan_outline)
            setCardBackgroundColor(color(R.color.istan_surface))
            minimumHeight = dp(40)
            minimumWidth = dp(96)
            isClickable = true
            isFocusable = true
            contentDescription = "$label: $value"
        }

        val valueView = TextView(activity).apply {
            text = "$value \u25BE"
            textSize = 14f
            typeface = AppFont.medium
            setTextColor(color(R.color.istan_text))
            gravity = Gravity.CENTER
            includeFontPadding = false
            maxLines = 1
            setPadding(dp(16), dp(8), dp(16), dp(8))
        }
        pill.addView(
            valueView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )

        val pillParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pillParams.gravity = Gravity.CENTER_VERTICAL
        row.addView(pill, pillParams)

        root.addView(
            row,
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(56))
        )

        return SettingRow(row, pill, valueView)
    }

    private fun rowBackground(): Int {
        val typed = android.util.TypedValue()
        activity.theme.resolveAttribute(android.R.attr.selectableItemBackground, typed, true)
        return typed.resourceId
    }

    private fun text(value: String, sp: Int, colorRes: Int, bold: Boolean): TextView {
        return TextView(activity).apply {
            text = value
            textSize = sp.toFloat()
            setTextColor(color(colorRes))
            typeface = if (bold) AppFont.semiBold else AppFont.regular
            includeFontPadding = true
        }
    }

    private fun color(colorRes: Int): Int = ThemePrefs.resolveColor(activity, colorRes)

    private fun dp(value: Int): Int = Math.round(value * activity.resources.displayMetrics.density)

    private data class SettingRow(
        val row: LinearLayout,
        val pill: MaterialCardView,
        val value: TextView
    )
}
