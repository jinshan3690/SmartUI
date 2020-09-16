package com.smart.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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

}
