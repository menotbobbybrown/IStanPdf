package vasuki.istanpdf.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.google.android.material.card.MaterialCardView
import vasuki.istanpdf.R
import vasuki.istanpdf.ThemePrefs

object ShadcnDrawables {
    const val CARD_RADIUS = 16
    const val DIALOG_RADIUS = 20
    const val BUTTON_RADIUS = 12
    const val BADGE_RADIUS = 8
    const val PILL_RADIUS = 100

    enum class ButtonVariant { DEFAULT, SECONDARY, OUTLINE, GHOST }

    fun card(context: Context, radiusDp: Int = CARD_RADIUS, muted: Boolean = false): GradientDrawable {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resolve(context, if (muted) R.color.istan_surface_high else R.color.istan_surface))
        background.cornerRadius = dp(context, radiusDp).toFloat()
        background.setStroke(dp(context, 1), resolve(context, R.color.istan_outline))
        return background
    }

    fun thumbnailFrame(context: Context, radiusDp: Int = 6): GradientDrawable {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resolve(context, R.color.istan_outline))
        background.cornerRadius = dp(context, radiusDp).toFloat()
        return background
    }

    fun dashedCard(context: Context, radiusDp: Int = CARD_RADIUS): GradientDrawable {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resolve(context, R.color.istan_surface))
        background.cornerRadius = dp(context, radiusDp).toFloat()
        background.setStroke(dp(context, 1), resolve(context, R.color.istan_outline), dp(context, 4).toFloat(), dp(context, 4).toFloat())
        return background
    }

    fun badge(context: Context, radiusDp: Int = BADGE_RADIUS): GradientDrawable {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resolve(context, R.color.istan_surface_high))
        background.cornerRadius = dp(context, radiusDp).toFloat()
        background.setStroke(dp(context, 1), resolve(context, R.color.istan_outline))
        return background
    }

    fun input(context: Context, radiusDp: Int = BUTTON_RADIUS): GradientDrawable {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setColor(resolve(context, R.color.istan_surface))
        background.cornerRadius = dp(context, radiusDp).toFloat()
        background.setStroke(dp(context, 1), resolve(context, R.color.istan_outline))
        return background
    }

    fun button(context: Context, variant: ButtonVariant, radiusDp: Int = BUTTON_RADIUS): Drawable {
        val content = GradientDrawable()
        content.shape = GradientDrawable.RECTANGLE
        content.cornerRadius = dp(context, radiusDp).toFloat()
        when (variant) {
            ButtonVariant.DEFAULT -> {
                content.setColor(primary(context))
            }
            ButtonVariant.SECONDARY -> {
                content.setColor(resolve(context, R.color.istan_surface_high))
            }
            ButtonVariant.OUTLINE -> {
                content.setColor(Color.TRANSPARENT)
                content.setStroke(dp(context, 1), resolve(context, R.color.istan_outline))
            }
            ButtonVariant.GHOST -> {
                content.setColor(Color.TRANSPARENT)
            }
        }
        val rippleBase = when (variant) {
            ButtonVariant.DEFAULT -> primaryForeground(context)
            else -> resolve(context, R.color.istan_text)
        }
        return RippleDrawable(ColorStateList.valueOf(withAlpha(rippleBase, 0.12f)), content, null)
    }

    fun applyCard(card: MaterialCardView, radiusDp: Int = CARD_RADIUS, muted: Boolean = false) {
        val context = card.context
        card.setCardBackgroundColor(resolve(context, if (muted) R.color.istan_surface_high else R.color.istan_surface))
        card.radius = dp(context, radiusDp).toFloat()
        card.strokeWidth = dp(context, 1)
        card.strokeColor = resolve(context, R.color.istan_outline)
        card.cardElevation = 0f
        card.useCompatPadding = false
    }

    fun applyButton(card: MaterialCardView, variant: ButtonVariant, radiusDp: Int = BUTTON_RADIUS) {
        val context = card.context
        card.radius = dp(context, radiusDp).toFloat()
        card.cardElevation = 0f
        card.useCompatPadding = false
        when (variant) {
            ButtonVariant.DEFAULT -> {
                card.setCardBackgroundColor(primary(context))
                card.strokeWidth = 0
            }
            ButtonVariant.SECONDARY -> {
                card.setCardBackgroundColor(resolve(context, R.color.istan_surface_high))
                card.strokeWidth = 0
            }
            ButtonVariant.OUTLINE -> {
                card.setCardBackgroundColor(Color.TRANSPARENT)
                card.strokeWidth = dp(context, 1)
                card.strokeColor = resolve(context, R.color.istan_outline)
            }
            ButtonVariant.GHOST -> {
                card.setCardBackgroundColor(Color.TRANSPARENT)
                card.strokeWidth = 0
            }
        }
        val rippleBase = if (variant == ButtonVariant.DEFAULT) primaryForeground(context) else resolve(context, R.color.istan_text)
        card.rippleColor = ColorStateList.valueOf(withAlpha(rippleBase, 0.12f))
    }

    fun primary(context: Context): Int = resolve(context, R.color.istan_olive)

    fun primaryForeground(context: Context): Int =
        if (ThemePrefs.isDark(context)) Color.BLACK else Color.WHITE

    fun buttonTextColor(context: Context, variant: ButtonVariant): Int =
        if (variant == ButtonVariant.DEFAULT) primaryForeground(context) else resolve(context, R.color.istan_text)

    fun outline(context: Context): Int = resolve(context, R.color.istan_outline)

    fun text(context: Context): Int = resolve(context, R.color.istan_text)

    fun textMuted(context: Context): Int = resolve(context, R.color.istan_text_muted)

    private fun withAlpha(color: Int, alpha: Float): Int {
        val value = (alpha * 255f).toInt().coerceIn(0, 255)
        return Color.argb(value, Color.red(color), Color.green(color), Color.blue(color))
    }

    private fun resolve(context: Context, colorRes: Int): Int = ThemePrefs.resolveColor(context, colorRes)

    private fun dp(context: Context, value: Int): Int =
        (value * context.resources.displayMetrics.density + 0.5f).toInt()
}
