package com.smart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.smart.ui.util.SmartHelper


class SmartLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    val helper = SmartHelper(context, attrs, this)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(w, h, paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (helper.strokeOverlay || helper.isCorner) {
            canvas.saveLayer(helper.layer, null, Canvas.ALL_SAVE_FLAG)

            super.dispatchDraw(canvas)
            helper.onClipDraw(canvas)

            canvas.restore()
        } else {
            super.dispatchDraw(canvas)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if(helper != null && !helper.throttle) {
            super.setOnClickListener { v ->
                l?.run {
                    isSelected = !isSelected
                    onClick(v)
                }
            }
        }else {
            var prev = 0L
            super.setOnClickListener { v ->
                val now = System.currentTimeMillis()

                if (now - prev >= helper.throttleInterval) {
                    isSelected = !isSelected
                    l?.onClick(v)
                    prev = System.currentTimeMillis()
                }
            }
        }
    }

    /**
     * 只能拥有一个子View
     */
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        val count = super.getChildCount()
//        if (count > 1) {
//            throw RuntimeException("最多只支持1个子View，Most only support three sub view")
//        }
//    }

    fun setBackground(
        color: Int? = null, endColor: Int? = null, disableColor: Int? = null,
        strokeColor: Int? = null, disableStrokeColor: Int? = null, selectedColor: Int? = null,
        selectedEndColor: Int? = null, selectedStrokeColor: Int? = null, rippleColor: Int? = null,
        maskDrawable: Drawable? = null, stroke: Int? = null, shape: Int? = null,
        orientation: GradientDrawable.Orientation? = null
    ) {
        helper.setBackground(
            color, endColor, disableColor, strokeColor, disableStrokeColor,
            selectedColor, selectedEndColor, selectedStrokeColor, rippleColor, maskDrawable,
            stroke, shape, orientation
        )
    }

    fun setColor(color: Int? = null) {
        helper.setColor(color)
    }

    fun setEndColor(endColor: Int? = null) {
        helper.setEndColor(endColor)
    }

    fun setDisableColor(disableColor: Int? = null) {
        helper.setDisableColor(disableColor)
    }

    fun setStrokeColor(strokeColor: Int? = null) {
        helper.setStrokeColor(strokeColor)
    }

    fun setDisableStrokeColor(disableStrokeColor: Int? = null) {
        helper.setDisableStrokeColor(disableStrokeColor)
    }

    fun setSelectedColor(selectedColor: Int? = null) {
        helper.setSelectedColor(selectedColor)
    }

    fun setSelectedEndColor(selectedEndColor: Int? = null) {
        helper.setSelectedEndColor(selectedEndColor)
    }

    fun setSelectedStrokeColor(selectedStrokeColor: Int? = null) {
        helper.setSelectedStrokeColor(selectedStrokeColor)
    }

    fun setRippleColor(rippleColor: Int? = null) {
        helper.setRippleColor(rippleColor)
    }

    fun setMaskDrawable(maskDrawable: Drawable? = null) {
        helper.setMaskDrawable(maskDrawable)
    }

    fun setStroke(stroke: Int? = null) {
        helper.setStroke(stroke)
    }

    fun setShape(shape: Int? = null) {
        helper.setShape(shape)
    }

    fun setOrientation(orientation: GradientDrawable.Orientation? = null) {
        helper.setOrientation(orientation)
    }

    fun setStrokeOverlay(strokeOverlay: Boolean?) {
        strokeOverlay?.let { helper.changeStrokeOverlay(it) }
    }

    fun setRadius(
        radius: Float, leftTop: Float? = radius, rightTop: Float? = radius,
        leftBottom: Float? = radius, rightBottom: Float? = radius
    ) {
        helper.setRadius(radius, leftTop, rightTop, leftBottom, rightBottom)
    }

}
