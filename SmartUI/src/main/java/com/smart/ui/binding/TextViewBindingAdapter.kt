package com.smart.ui.binding

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
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
    @BindingAdapter(
        value = [
            "sl_prefixIcon", "sl_prefixIconWidth", "sl_prefixIconHeight", "sl_prefixIconLeftPadding",
            "sl_prefixIconRightPadding", "sl_suffixIcon", "sl_suffixIconWidth", "sl_suffixIconHeight",
            "sl_suffixIconLeftPadding", "sl_suffixIconRightPadding", "sl_cancelIcon", "sl_cancelIconWidth",
            "sl_cancelIconHeight", "sl_cancelIconLeftPadding", "sl_cancelIconRightPadding",
            "sl_textColor", "sl_textSize", "sl_hintColor", "sl_textGravity",
            "sl_editEnabled", "sl_length", "sl_inputType", "sl_imeOptions",
            "sl_textPaddingLeft", "sl_textPaddingTop", "sl_textPaddingRight", "sl_textPaddingBottom"
        ], requireAll = false
    )
    fun setEditText(
        view: SmartEditText, prefixIcon: Drawable?, prefixIconWidth: Int?, prefixIconHeight: Int?,
        prefixIconLeftPadding: Int?, prefixIconRightPadding: Int?, suffixIcon: Drawable?,
        suffixIconWidth: Int?, suffixIconHeight: Int?, suffixIconLeftPadding: Int?,
        suffixIconRightPadding: Int?, cancelIcon: Drawable?, cancelIconWidth: Int?,
        cancelIconHeight: Int?, cancelIconLeftPadding: Int?, cancelIconRightPadding: Int?,
        textColor: Int?, textSize: Float?, hintColor: Int?, textGravity: Int?,
        editEnabled: Boolean?, length: Int?, inputType: Int?, imeOptions: Int?,
        textPaddingLeft: Int?, textPaddingTop: Int?, textPaddingRight: Int?, textPaddingBottom: Int?
    ) {
        view.updatePrefixIcon(
            leftPadding = prefixIconLeftPadding, rightPadding = prefixIconRightPadding,
            drawable = prefixIcon, width = prefixIconWidth, height = prefixIconHeight
        )
        view.updateSuffixIcon(
            leftPadding = suffixIconLeftPadding, rightPadding = suffixIconRightPadding,
            drawable = suffixIcon, width = suffixIconWidth, height = suffixIconHeight
        )
        view.updateCancelIcon(
            leftPadding = cancelIconLeftPadding, rightPadding = cancelIconRightPadding,
            drawable = cancelIcon, width = cancelIconWidth, height = cancelIconHeight
        )

        textColor?.let { view.editText?.setTextColor(it) }
        hintColor?.let { view.editText?.setHintTextColor(it) }
        textSize?.let { view.editText?.setTextSize(it) }
        textGravity?.let { view.editText?.gravity = textGravity }
        editEnabled?.let { view.setEditEnabled(it) }
        length?.let { view.length = it }
        inputType?.let { view.editText?.inputType = it }
        imeOptions?.let { view.editText?.imeOptions = it }
        view.editText?.setPadding(textPaddingLeft?:view.editText?.paddingLeft!!,
            textPaddingTop?:view.editText?.paddingTop!!,
            textPaddingRight?:view.editText?.paddingRight!!,
            textPaddingBottom?:view.editText?.paddingBottom!!)
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

    @JvmStatic
    @BindingAdapter(
        value = [
            "sl_enabled", "sl_background", "sl_color", "sl_endColor", "sl_orientation",
            "sl_rippleColor", "sl_shadowColor", "sl_disableColor", "sl_disableStrokeColor",
            "sl_maskDrawable", "sl_stroke", "sl_strokeColor", "sl_strokeOverlay", "sl_selectedStrokeColor",
            "sl_selectedColor", "sl_selectedEndColor", "sl_shape", "sl_radius", "sl_radiusLeftTop",
            "sl_radiusRightTop", "sl_radiusLeftBottom", "sl_radiusRightBottom"
        ], requireAll = false
    )
    fun setBaseConfig(
        view: View, enabled: Boolean?, background: Drawable?, color: Int?, endColor: Int?,
        orientation: GradientDrawable.Orientation?, rippleColor: Int?, shadowColor: Int?,
        disableColor: Int?, disableStrokeColor: Int?, maskDrawable: Drawable?, stroke: Int?,
        strokeColor: Int?, strokeOverlay: Boolean?, selectedStrokeColor: Int?, selectedColor: Int?,
        selectedEndColor: Int?, shape: Int?, radius: Float?, radiusLeftTop: Float?,
        radiusRightTop: Float?, radiusRightBottom: Float?, radiusLeftBottom: Float?
    ) {
        enabled?.let { view.isEnabled = it }
        background?.let { view.background = background }
        when (view) {
            is SmartLayout -> {
                radius?.let {
                    val radiusPx = SmartHelper.dp2px(view.context, it).toFloat()
                    view.setRadius(
                        radiusPx,
                        leftTop = radiusLeftTop ?: radiusPx,
                        rightTop = radiusRightTop ?: radiusPx,
                        leftBottom = radiusRightBottom ?: radiusPx,
                        rightBottom = radiusLeftBottom ?: radiusPx
                    )
                }
                if (background == null)
                    view.setBackgroundRes(
                        color = color, endColor = endColor, disableColor = disableColor,
                        strokeColor = strokeColor, disableStrokeColor = disableStrokeColor,
                        selectedColor = selectedColor, selectedEndColor = selectedEndColor,
                        selectedStrokeColor = selectedStrokeColor, rippleColor = rippleColor,
                        maskDrawable = maskDrawable, stroke = stroke, shape = shape,
                        orientation = orientation
                    )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    shadowColor?.let { view.outlineSpotShadowColor = it }
                }
            }
            is SmartTextView -> {
                radius?.let {
                    val radiusPx = SmartHelper.dp2px(view.context, it).toFloat()
                    view.setRadius(
                        radiusPx,
                        leftTop = radiusLeftTop ?: radiusPx,
                        rightTop = radiusRightTop ?: radiusPx,
                        leftBottom = radiusRightBottom ?: radiusPx,
                        rightBottom = radiusLeftBottom ?: radiusPx
                    )
                }
                if (background == null)
                    view.setBackgroundRes(
                        color = color, endColor = endColor, disableColor = disableColor,
                        strokeColor = strokeColor, disableStrokeColor = disableStrokeColor,
                        selectedColor = selectedColor, selectedEndColor = selectedEndColor,
                        selectedStrokeColor = selectedStrokeColor, rippleColor = rippleColor,
                        maskDrawable = maskDrawable, stroke = stroke, shape = shape,
                        orientation = orientation
                    )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    shadowColor?.let { view.outlineSpotShadowColor = it }
                }
            }
            is SmartImageView -> {
                radius?.let {
                    val radiusPx = SmartHelper.dp2px(view.context, it).toFloat()
                    view.setRadius(
                        radiusPx,
                        leftTop = radiusLeftTop ?: radiusPx,
                        rightTop = radiusRightTop ?: radiusPx,
                        leftBottom = radiusRightBottom ?: radiusPx,
                        rightBottom = radiusLeftBottom ?: radiusPx
                    )
                }
                if (background == null)
                    view.setBackgroundRes(
                        color = color, endColor = endColor, disableColor = disableColor,
                        strokeColor = strokeColor, disableStrokeColor = disableStrokeColor,
                        selectedColor = selectedColor, selectedEndColor = selectedEndColor,
                        selectedStrokeColor = selectedStrokeColor, rippleColor = rippleColor,
                        maskDrawable = maskDrawable, stroke = stroke, shape = shape,
                        orientation = orientation
                    )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    shadowColor?.let { view.outlineSpotShadowColor = it }
                }
            }
            is SmartEditText -> {
                radius?.let {
                    val radiusPx = SmartHelper.dp2px(view.context, it).toFloat()
                    view.setRadius(
                        radiusPx,
                        leftTop = radiusLeftTop ?: radiusPx,
                        rightTop = radiusRightTop ?: radiusPx,
                        leftBottom = radiusRightBottom ?: radiusPx,
                        rightBottom = radiusLeftBottom ?: radiusPx
                    )
                }
                if (background == null)
                    view.setBackgroundRes(
                        color = color, endColor = endColor, disableColor = disableColor,
                        strokeColor = strokeColor, disableStrokeColor = disableStrokeColor,
                        selectedColor = selectedColor, selectedEndColor = selectedEndColor,
                        selectedStrokeColor = selectedStrokeColor, rippleColor = rippleColor,
                        maskDrawable = maskDrawable, stroke = stroke, shape = shape,
                        orientation = orientation
                    )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    shadowColor?.let { view.outlineSpotShadowColor = it }
                }
            }
        }
    }

}
