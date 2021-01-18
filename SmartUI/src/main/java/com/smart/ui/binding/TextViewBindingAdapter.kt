package com.smart.ui.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.Filterable
import android.widget.ListAdapter
import androidx.annotation.Nullable
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.smart.ui.R
import com.smart.ui.util.SmartHelper
import com.smart.ui.widget.SmartEditText
import com.smart.ui.widget.SmartImageView
import com.smart.ui.widget.SmartLayout
import com.smart.ui.widget.SmartTextView
import java.util.*

object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["adapter"], requireAll = false)
    fun <T> setAdapter(view: SmartEditText, adapter: T) where T : ListAdapter, T : Filterable {
        view.setAdapter(adapter)
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_prefixIcon", "sl_prefixIconWidth", "sl_prefixIconHeight",
            "sl_prefixIconLeftPadding", "sl_prefixIconRightPadding"
        ], requireAll = false
    )
    fun setPrefixIcon(
        view: SmartEditText, prefixIcon: Drawable?, prefixIconWidth: Int?, prefixIconHeight: Int?,
        prefixIconLeftPadding: Int?, prefixIconRightPadding: Int?
    ) {
        view.updatePrefixIcon(
            leftPadding = prefixIconLeftPadding, rightPadding = prefixIconRightPadding,
            drawable = prefixIcon, width = prefixIconWidth, height = prefixIconHeight
        )
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_suffixIcon", "sl_suffixIconWidth", "sl_suffixIconHeight",
            "sl_suffixIconLeftPadding", "sl_suffixIconRightPadding"
        ], requireAll = false
    )
    fun setSuffixIcon(
        view: SmartEditText, suffixIcon: Drawable?, suffixIconWidth: Int?, suffixIconHeight: Int?,
        suffixIconLeftPadding: Int?, suffixIconRightPadding: Int?
    ) {
        view.updateSuffixIcon(
            leftPadding = suffixIconLeftPadding, rightPadding = suffixIconRightPadding,
            drawable = suffixIcon, width = suffixIconWidth, height = suffixIconHeight
        )
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_cancelIcon", "sl_cancelIconWidth", "sl_cancelIconHeight",
            "sl_cancelIconLeftPadding", "sl_cancelIconRightPadding"
        ], requireAll = false
    )
    fun setCancelIcon(
        view: SmartEditText, cancelIcon: Drawable?, cancelIconWidth: Int?, cancelIconHeight: Int?,
        cancelIconLeftPadding: Int?, cancelIconRightPadding: Int?
    ) {
        view.updateCancelIcon(
            leftPadding = cancelIconLeftPadding, rightPadding = cancelIconRightPadding,
            drawable = cancelIcon, width = cancelIconWidth, height = cancelIconHeight
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_length"], requireAll = false)
    fun setEditTextLength(view: SmartEditText, length: Int?) {
        view.length = length ?: -1
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_textColor"], requireAll = false)
    fun setEditTextColor(view: SmartEditText, textColor: Int?) {
        view.editText?.setTextColor(textColor ?: Color.parseColor("#333333"))
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_hintColor"], requireAll = false)
    fun setEditHintColor(view: SmartEditText, hintColor: Int?) {
        view.editText?.setHintTextColor(hintColor ?: Color.parseColor("#333333"))
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_textSize"], requireAll = false)
    fun setEditTextSize(view: SmartEditText, textSize: Float?) {
        view.editText?.textSize = textSize ?: 14f
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_textGravity"], requireAll = false)
    fun setEditTextGravity(view: SmartEditText, gravity: Int?) {
        view.editText?.gravity = gravity ?: 0
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_editEnabled"], requireAll = false)
    fun setEditEnabled(view: SmartEditText, editEnabled: Boolean?) {
        view.setEditEnabled(editEnabled ?: true)
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_inputType"], requireAll = false)
    fun setEditInputType(view: SmartEditText, inputType: Int?) {
        view.editText?.inputType = inputType ?: 1
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_imeOptions"], requireAll = false)
    fun setEditImeOptions(view: SmartEditText, imeOptions: Int?) {
        view.editText?.inputType = imeOptions ?: 0
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_textPaddingLeft", "sl_textPaddingTop", "sl_textPaddingRight", "sl_textPaddingBottom"
        ], requireAll = false
    )
    fun setEditTextPadding(
        view: SmartEditText,
        textPaddingLeft: Int?, textPaddingTop: Int?, textPaddingRight: Int?, textPaddingBottom: Int?
    ) {
        view.editText?.setPadding(
            textPaddingLeft ?: view.editText?.paddingLeft!!,
            textPaddingTop ?: view.editText?.paddingTop!!,
            textPaddingRight ?: view.editText?.paddingRight!!,
            textPaddingBottom ?: view.editText?.paddingBottom!!
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_digits"], requireAll = false)
    fun setDigits(view: SmartEditText, digits: String?) {
        if (!digits.isNullOrBlank()) {
            view.editText?.keyListener = DigitsKeyListener.getInstance(digits)
        } else {
            view.editText?.filters = arrayOf()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_text"], requireAll = false)
    fun setText(view: SmartEditText, text: String?) {
        if (view.getText() != text)
            view.editText?.setText(text)
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_hint"], requireAll = false)
    fun setHint(view: SmartEditText, hint: String?) {
        if (view.editText?.hint != hint)
            view.editText?.hint = hint
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "sl_text", event = "textAttrChanged")
    fun getText(view: SmartEditText): String? {
        return view.editText?.text?.toString()
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "sl_hint", event = "textAttrChanged")
    fun getHint(view: SmartEditText): String? {
        return view.editText?.hint?.toString()
    }

    @JvmStatic
    @BindingAdapter(value = ["textAttrChanged"], requireAll = false)
    fun setTextAttrChange(view: SmartEditText, listener: InverseBindingListener?) {
        listener?.let {
            view.editText?.doOnTextChanged { _, _, _, _ ->
                listener.onChange()
            }
        }
    }

    /**
     * 通用
     */
    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_radius", "sl_radiusLeftTop", "sl_radiusRightTop", "sl_radiusLeftBottom",
            "sl_radiusRightBottom"
        ], requireAll = false
    )
    fun setRadius(
        view: View, radius: Float = 0f, radiusLeftTop: Float?,
        radiusRightTop: Float?, radiusRightBottom: Float?, radiusLeftBottom: Float?
    ) {
        when (view) {
            is SmartLayout -> {
                val radiusPx = SmartHelper.dp2px(view.context, radius).toFloat()
                view.setRadius(
                    radiusPx,
                    leftTop = radiusLeftTop ?: radiusPx, rightTop = radiusRightTop ?: radiusPx,
                    leftBottom = radiusRightBottom ?: radiusPx,
                    rightBottom = radiusLeftBottom ?: radiusPx
                )
            }
            is SmartTextView -> {
                val radiusPx = SmartHelper.dp2px(view.context, radius).toFloat()
                view.setRadius(
                    radiusPx,
                    leftTop = radiusLeftTop ?: radiusPx, rightTop = radiusRightTop ?: radiusPx,
                    leftBottom = radiusRightBottom ?: radiusPx,
                    rightBottom = radiusLeftBottom ?: radiusPx
                )
            }
            is SmartImageView -> {
                val radiusPx = SmartHelper.dp2px(view.context, radius).toFloat()
                view.setRadius(
                    radiusPx,
                    leftTop = radiusLeftTop ?: radiusPx, rightTop = radiusRightTop ?: radiusPx,
                    leftBottom = radiusRightBottom ?: radiusPx,
                    rightBottom = radiusLeftBottom ?: radiusPx
                )
            }
            is SmartEditText -> {
                val radiusPx = SmartHelper.dp2px(view.context, radius).toFloat()
                view.setRadius(
                    radiusPx,
                    leftTop = radiusLeftTop ?: radiusPx, rightTop = radiusRightTop ?: radiusPx,
                    leftBottom = radiusRightBottom ?: radiusPx,
                    rightBottom = radiusLeftBottom ?: radiusPx
                )
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_enabled"], requireAll = false)
    fun setEnabled(view: View, enabled: Boolean?) {
        enabled?.let { view.isEnabled = it }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_background"], requireAll = false)
    fun setBackground(view: View, background: Drawable?) {
        background?.let { view.background = background }
        when (view) {
            is SmartLayout -> view.helper.background = background
            is SmartTextView -> view.helper.background = background
            is SmartImageView -> view.helper.background = background
            is SmartEditText -> view.helper.background = background
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_shadowColor"], requireAll = false)
    fun setShadowColor(view: View, shadowColor: Int?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            shadowColor?.let {
                when (view) {
                    is SmartLayout -> view.outlineSpotShadowColor = it
                    is SmartTextView -> view.outlineSpotShadowColor = it
                    is SmartImageView -> view.outlineSpotShadowColor = it
                    is SmartEditText -> view.outlineSpotShadowColor = it
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_color"], requireAll = false)
    fun setColor(view: View, color: Int?) {
        when (view) {
            is SmartLayout -> view.setColor(color)
            is SmartTextView -> view.setColor(color)
            is SmartImageView -> view.setColor(color)
            is SmartEditText -> view.setColor(color)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_endColor"], requireAll = false)
    fun setEndColor(view: View, endColor: Int?) {
        when (view) {
            is SmartLayout -> view.setEndColor(endColor)
            is SmartTextView -> view.setEndColor(endColor)
            is SmartImageView -> view.setEndColor(endColor)
            is SmartEditText -> view.setEndColor(endColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_orientation"], requireAll = false)
    fun setOrientation(view: View, orientation: GradientDrawable.Orientation?) {
        when (view) {
            is SmartLayout -> view.setOrientation(orientation)
            is SmartTextView -> view.setOrientation(orientation)
            is SmartImageView -> view.setOrientation(orientation)
            is SmartEditText -> view.setOrientation(orientation)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_rippleColor"], requireAll = false)
    fun setRippleColor(view: View, rippleColor: Int?) {
        when (view) {
            is SmartLayout -> view.setRippleColor(rippleColor)
            is SmartTextView -> view.setRippleColor(rippleColor)
            is SmartImageView -> view.setRippleColor(rippleColor)
            is SmartEditText -> view.setRippleColor(rippleColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_disableColor"], requireAll = false)
    fun setDisableColor(view: View, disableColor: Int?) {
        when (view) {
            is SmartLayout -> view.setDisableColor(disableColor)
            is SmartTextView -> view.setDisableColor(disableColor)
            is SmartImageView -> view.setDisableColor(disableColor)
            is SmartEditText -> view.setDisableColor(disableColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_disableStrokeColor"], requireAll = false)
    fun setDisableStrokeColor(view: View, disableStrokeColor: Int?) {
        when (view) {
            is SmartLayout -> view.setDisableStrokeColor(disableStrokeColor)
            is SmartTextView -> view.setDisableStrokeColor(disableStrokeColor)
            is SmartImageView -> view.setDisableStrokeColor(disableStrokeColor)
            is SmartEditText -> view.setDisableStrokeColor(disableStrokeColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_maskDrawable"], requireAll = false)
    fun setMaskDrawable(view: View, maskDrawable: Drawable?) {
        when (view) {
            is SmartLayout -> view.setMaskDrawable(maskDrawable)
            is SmartTextView -> view.setMaskDrawable(maskDrawable)
            is SmartImageView -> view.setMaskDrawable(maskDrawable)
            is SmartEditText -> view.setMaskDrawable(maskDrawable)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_stroke"], requireAll = false)
    fun setStroke(view: View, stroke: Int?) {
        when (view) {
            is SmartLayout -> view.setStroke(stroke)
            is SmartTextView -> view.setStroke(stroke)
            is SmartImageView -> view.setStroke(stroke)
            is SmartEditText -> view.setStroke(stroke)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_strokeColor"], requireAll = false)
    fun setStrokeColor(view: View, strokeColor: Int?) {
        when (view) {
            is SmartLayout -> view.setStrokeColor(strokeColor)
            is SmartTextView -> view.setStrokeColor(strokeColor)
            is SmartImageView -> view.setStrokeColor(strokeColor)
            is SmartEditText -> view.setStrokeColor(strokeColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_strokeOverlay"], requireAll = false)
    fun setStrokeOverlay(view: View, strokeOverlay: Boolean?) {
        when (view) {
            is SmartLayout -> view.setStrokeOverlay(strokeOverlay)
            is SmartTextView -> view.setStrokeOverlay(strokeOverlay)
            is SmartImageView -> view.setStrokeOverlay(strokeOverlay)
            is SmartEditText -> view.setStrokeOverlay(strokeOverlay)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_selectedStrokeColor"], requireAll = false)
    fun setSelectedStrokeColor(view: View, selectedStrokeColor: Int?) {
        when (view) {
            is SmartLayout -> view.setSelectedStrokeColor(selectedStrokeColor)
            is SmartTextView -> view.setSelectedStrokeColor(selectedStrokeColor)
            is SmartImageView -> view.setSelectedStrokeColor(selectedStrokeColor)
            is SmartEditText -> view.setSelectedStrokeColor(selectedStrokeColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_selectedColor"], requireAll = false)
    fun setSelectedColor(view: View, selectedColor: Int?) {
        when (view) {
            is SmartLayout -> view.setSelectedColor(selectedColor)
            is SmartTextView -> view.setSelectedColor(selectedColor)
            is SmartImageView -> view.setSelectedColor(selectedColor)
            is SmartEditText -> view.setSelectedColor(selectedColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_selectedEndColor"], requireAll = false)
    fun setSelectedEndColor(view: View, selectedEndColor: Int?) {
        when (view) {
            is SmartLayout -> view.setSelectedEndColor(selectedEndColor)
            is SmartTextView -> view.setSelectedEndColor(selectedEndColor)
            is SmartImageView -> view.setSelectedEndColor(selectedEndColor)
            is SmartEditText -> view.setSelectedEndColor(selectedEndColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sl_shape"], requireAll = false)
    fun setShape(view: View, shape: Int?) {
        when (view) {
            is SmartLayout -> view.setShape(shape)
            is SmartTextView -> view.setShape(shape)
            is SmartImageView -> view.setShape(shape)
            is SmartEditText -> view.setShape(shape)
        }
    }

}
