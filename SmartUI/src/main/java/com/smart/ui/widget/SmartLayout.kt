package com.smart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.smart.ui.util.SmartHelper


class SmartLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val helper = SmartHelper(context, attrs, this)

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
        super.setOnClickListener { v ->
            l?.run {
                isSelected = !isSelected
                onClick(v)
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
        disableStrokeColor: Int? = null, selectedColor: Int? = null, selectedEndColor: Int? = null,
        selectedStrokeColor: Int? = null, rippleColor: Int? = null, maskDrawable: Drawable? = null,
        stroke: Int? = null, shape: Int? = null, orientation: GradientDrawable.Orientation? = null
    ) {
        if (color != null) {
            helper.color = ContextCompat.getColor(context, color)
        }
        if (endColor != null) {
            helper.endColor = ContextCompat.getColor(context, endColor)
        }
        if (disableColor != null) {
            helper.disableColor = ContextCompat.getColor(context, disableColor)
        }
        if (disableStrokeColor != null) {
            helper.disableStrokeColor = ContextCompat.getColor(context, disableStrokeColor)
        }
        if (selectedColor != null) {
            helper.selectedColor = ContextCompat.getColor(context, selectedColor)
        }
        if (selectedEndColor != null) {
            helper.selectedEndColor = ContextCompat.getColor(context, selectedEndColor)
        }
        if (selectedStrokeColor != null) {
            helper.selectedStrokeColor = ContextCompat.getColor(context, selectedStrokeColor)
        }
        if (rippleColor != null) {
            helper.rippleColor = ContextCompat.getColor(context, rippleColor)
        }
        if (maskDrawable != null) {
            helper.maskDrawable = maskDrawable
        }
        if (stroke != null) {
            helper.stroke = stroke
        }
        if (shape != null) {
            helper.shape = shape
        }
        if (orientation != null) {
            helper.orientation = orientation
        }
        helper.initBackground()
    }

    fun setRadius(radius: Float) {
        helper.initRadius(radius, radius, radius, radius)
        //onSizeChanged
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(width, height, paddingLeft, paddingTop, paddingRight, paddingBottom)
        }

        helper.initBackground()
    }

}
