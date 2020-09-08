package com.hpcang.smart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.hpcang.smart.ui.util.SmartHelper


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

}
