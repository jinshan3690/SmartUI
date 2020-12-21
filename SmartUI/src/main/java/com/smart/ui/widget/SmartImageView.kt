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

    private val helper = SmartHelper(context, attrs, this)
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
        if(helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(w, h, paddingLeft, paddingTop, paddingRight, paddingBottom)
            strokeDrawable?.setBounds(0, 0, w, h)
        }
    }


    override fun onDraw(canvas: Canvas) {
        if(helper.strokeOverlay || helper.isCorner) {
            canvas.saveLayer(helper.layer, null, Canvas.ALL_SAVE_FLAG)

            super.onDraw(canvas)
            strokeDrawable?.draw(canvas)
            helper.onClipDraw(canvas)

            canvas.restore()
        }else{
            super.onDraw(canvas)
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

    fun setBackground(
        color: Int? = null, endColor: Int? = null, disableColor: Int? = null,strokeColor: Int? = null,
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
        if (strokeColor != null) {
            helper.strokeColor = ContextCompat.getColor(context, strokeColor)
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
        if(helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(width, height, paddingLeft, paddingTop, paddingRight, paddingBottom)
            strokeDrawable?.setBounds(0, 0, width, height)
        }
        helper.initBackground()
    }

}
