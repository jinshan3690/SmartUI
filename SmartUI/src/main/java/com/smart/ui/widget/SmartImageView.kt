package com.smart.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.smart.ui.R
import com.smart.ui.util.SmartHelper


class SmartImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    val helper = SmartHelper(context, attrs, this)
    private var strokeDrawable: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartImageView)

        typedArray.recycle()

        strokeDrawable = helper.createDrawable(color = *intArrayOf(Color.TRANSPARENT))
    }

    /**
     * 裁剪子View
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(w, h, paddingLeft, paddingTop, paddingRight, paddingBottom)
            strokeDrawable?.setBounds(0, 0, w, h)
        }
    }


    override fun onDraw(canvas: Canvas) {
        if (helper.strokeOverlay || helper.isCorner) {
            canvas.saveLayer(helper.layer, null, Canvas.ALL_SAVE_FLAG)

            super.onDraw(canvas)
            strokeDrawable?.draw(canvas)
            helper.onClipDraw(canvas)

            canvas.restore()
        } else {
            super.onDraw(canvas)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (helper != null && !helper.throttle) {
            super.setOnClickListener { v ->
                l?.run {
                    isSelected = !isSelected
                    onClick(v)
                }
            }
        } else {
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

    fun setBackground(
        color: Int? = null, endColor: Int? = null, disableColor: Int? = null,
        strokeColor: Int? = null, disableStrokeColor: Int? = null, selectedColor: Int? = null,
        selectedEndColor: Int? = null, selectedStrokeColor: Int? = null, focusedColor: Int? = null,
        focusedEndColor: Int? = null, focusedStrokeColor: Int? = null, rippleColor: Int? = null,
        maskDrawable: Drawable? = null, stroke: Int? = null, shape: Int? = null,
        orientation: GradientDrawable.Orientation? = null, isRes: Boolean = false
    ) {
        helper.setBackground(
            color, endColor, disableColor, strokeColor, disableStrokeColor,
            selectedColor, selectedEndColor, selectedStrokeColor, focusedColor, focusedEndColor,
            focusedStrokeColor, rippleColor, maskDrawable, stroke, shape, orientation, isRes
        )
    }

    fun setColor(color: Int? = null, isRes: Boolean = false) {
        helper.setColor(color, isRes)
    }

    fun setEndColor(endColor: Int? = null, isRes: Boolean = false) {
        helper.setEndColor(endColor, isRes)
    }

    fun setDisableColor(disableColor: Int? = null, isRes: Boolean = false) {
        helper.setDisableColor(disableColor, isRes)
    }

    fun setStrokeColor(strokeColor: Int? = null, isRes: Boolean = false) {
        helper.setStrokeColor(strokeColor, isRes)
    }

    fun setDisableStrokeColor(disableStrokeColor: Int? = null, isRes: Boolean = false) {
        helper.setDisableStrokeColor(disableStrokeColor, isRes)
    }

    fun setSelectedColor(selectedColor: Int? = null, isRes: Boolean = false) {
        helper.setSelectedColor(selectedColor, isRes)
    }

    fun setSelectedEndColor(selectedEndColor: Int? = null, isRes: Boolean = false) {
        helper.setSelectedEndColor(selectedEndColor, isRes)
    }

    fun setSelectedStrokeColor(selectedStrokeColor: Int? = null, isRes: Boolean = false) {
        helper.setSelectedStrokeColor(selectedStrokeColor, isRes)
    }

    fun setFocusedColor(focusedColor: Int? = null, isRes: Boolean = false) {
        helper.setFocusedColor(focusedColor, isRes)
    }

    fun setFocusedEndColor(focusedEndColor: Int? = null, isRes: Boolean = false) {
        helper.setFocusedEndColor(focusedEndColor, isRes)
    }

    fun setFocusedStrokeColor(focusedStrokeColor: Int? = null, isRes: Boolean = false) {
        helper.setFocusedStrokeColor(focusedStrokeColor, isRes)
    }

    fun setRippleColor(rippleColor: Int? = null, isRes: Boolean = false) {
        helper.setRippleColor(rippleColor, isRes)
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
