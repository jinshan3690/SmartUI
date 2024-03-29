package com.smart.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.smart.ui.R
import com.smart.ui.util.SmartHelper


class SmartTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    val helper = SmartHelper(context, attrs, this)

    //TextView
    private val mDrawableWidth: IntArray = intArrayOf(-1, -1, -1, -1)
    private val mDrawableHeight: IntArray = intArrayOf(-1, -1, -1, -1)
    private val mDrawableColor: IntArray = intArrayOf(0, 0, 0, 0)

    //多行对齐
    private var compat = false
    private var compatEllipsize = false
    private var compatTextHeight: Float = 0f// 单行文字高度
    private var compatTextLineSpaceExtra = 0f // 额外的行间距
    private val compatLines: ArrayList<String> = ArrayList() // 分割后的行
    private val compatTailLines: ArrayList<Int> = ArrayList() // 尾行
    private var compatAlign = Align.ALIGN_LEFT // 默认最后一行左对齐
    private var firstCalc = true // 初始化计算
    private var originalHeight = 0 //原始高度
    private var originalLineCount = 0 //原始行数
    private var originalPaddingBottom = 0 //原始paddingBottom
    private var setPaddingFromMe = false

    // 尾行对齐方式
    enum class Align {
        ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT // 居中，居左，居右,针对段落最后一行
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartTextView)

        //全角半角混排
        compat = typedArray.getBoolean(R.styleable.SmartTextView_sl_compat, false)
        compatEllipsize =
            typedArray.getBoolean(R.styleable.SmartTextView_sl_compat_ellipsize, false)
        if (compat) {
            originalPaddingBottom = paddingBottom

            val alignStyle = typedArray.getInt(R.styleable.SmartTextView_sl_compat_align, 0)
            compatAlign = when (alignStyle) {
                1 -> Align.ALIGN_CENTER
                2 -> Align.ALIGN_RIGHT
                else -> Align.ALIGN_LEFT
            }
        }
        //icon
        mDrawableColor[0] =
            typedArray.getColor(R.styleable.SmartTextView_sl_compoundDrawableLeftColor, 0)
        mDrawableColor[1] =
            typedArray.getColor(R.styleable.SmartTextView_sl_compoundDrawableTopColor, 0)
        mDrawableColor[2] =
            typedArray.getColor(R.styleable.SmartTextView_sl_compoundDrawableRightColor, 0)
        mDrawableColor[3] =
            typedArray.getColor(R.styleable.SmartTextView_sl_compoundDrawableBottomColor, 0)

        mDrawableWidth[0] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableLeftWidth,
            -1
        )
        mDrawableHeight[0] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableLeftHeight,
            -1
        )
        mDrawableWidth[1] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableTopWidth,
            -1
        )
        mDrawableHeight[1] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableTopHeight,
            -1
        )
        mDrawableWidth[2] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableRightWidth,
            -1
        )
        mDrawableHeight[2] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableRightHeight,
            -1
        )
        mDrawableWidth[3] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableBottomWidth,
            -1
        )
        mDrawableHeight[3] = typedArray.getDimensionPixelSize(
            R.styleable.SmartTextView_sl_compoundDrawableBottomHeight,
            -1
        )

        initCompoundDrawableSize()

        typedArray.recycle()

        helper.initTextView(this)
    }

    private fun initCompoundDrawableSize() {
        val drawables = compoundDrawables
        for (index in drawables.indices) {
            val drawable = drawables[index] ?: continue

            val realBounds = drawable.bounds

            if (mDrawableWidth[index] > 0) {
                realBounds.right = realBounds.left + mDrawableWidth[index]
            }
            if (mDrawableHeight[index] > 0) {
                realBounds.bottom = realBounds.top + mDrawableHeight[index]
            }

            drawable.bounds = realBounds
            if (mDrawableColor[index] != 0)
                drawable.colorFilter =
                    PorterDuffColorFilter(mDrawableColor[index], PorterDuff.Mode.SRC_IN)
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
    }

    /**
     * 裁剪子View
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(w, h, paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
        helper.changeTextColor(this, isSelected)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onMeasureLines()
    }

    private fun onMeasureLines() {
        //首先进行高度调整
        if (compat && firstCalc) {
            val text = text.toString()
            compatLines.clear()
            compatTailLines.clear()

            // 文本含有换行符时，分割单独处理
            val items = text.split("\\n".toRegex()).toTypedArray()
            val innerWidth = measuredWidth -
                    (if (helper.strokeOverlay) paddingLeft else paddingLeft + if (helper.strokeOverlay) 0 else helper.stroke) -
                    (if (helper.strokeOverlay) paddingRight else paddingRight + if (helper.strokeOverlay) 0 else helper.stroke)
            for (item in items) {
                calc(paint, item, innerWidth)
            }

            //使用替代textview计算原始高度与行数
            measureTextViewHeight(
                text, paint.textSize, innerWidth
            )

            //获取行高
            compatTextHeight = 1.0f * originalHeight / originalLineCount
            compatTextLineSpaceExtra =
                compatTextHeight * (lineSpacingMultiplier - 1) + lineSpacingExtra

            //计算实际高度,加上多出的行的高度(一般是减少)
//            var heightGap = ((textLineSpaceExtra + textHeight) * (lines.size -
//                    originalLineCount)).toInt()
            setPaddingFromMe = true

            //调整textview的paddingBottom来缩小底部空白
//            setPadding(
//                paddingLeft, paddingTop, paddingRight, originalPaddingBottom + heightGap
//            )
            firstCalc = false
        }

//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

//        if (compat && heightMode != MeasureSpec.EXACTLY)
//            setMeasuredDimension(
//                widthMeasureSpec,
//                ((compatTextLineSpaceExtra + compatTextHeight) * (compatLines.size)).toInt() + paddingTop + paddingBottom
//            )
    }

    override fun onDraw(canvas: Canvas) {
        if (helper.strokeOverlay || helper.isCorner) {
            canvas.saveLayer(helper.layer, null, Canvas.ALL_SAVE_FLAG)

            if (compat) {
                drawText(canvas)
            } else {
                super.onDraw(canvas)
            }

            helper.onClipDraw(canvas)

            canvas.restore()
        } else {
            if (compat) {
                drawText(canvas)
            } else {
                super.onDraw(canvas)
            }
        }

    }

    private fun drawText(canvas: Canvas) {
        var width = measuredWidth
        val fm = paint.fontMetrics
        var firstHeight = textSize - (fm.bottom - fm.descent + fm.ascent - fm.top)
        val gravity = gravity
        if (gravity and 0x1000 == 0) { // 是否垂直居中
            firstHeight += (compatTextHeight - firstHeight) / 2
        }
        val paddingTop = paddingTop
        val paddingLeft = paddingLeft + if (helper.strokeOverlay) 0 else helper.stroke
        val paddingRight = paddingRight + if (helper.strokeOverlay) 0 else helper.stroke
        width = width - paddingLeft - paddingRight
        for (i in compatLines.indices) {
            val drawY = i * compatTextHeight + firstHeight
            val line = compatLines[i]
            // 绘画起始x坐标
            var drawSpacingX = paddingLeft.toFloat()
            val gap = width - paint.measureText(line)
//                var interval = gap / (line.length - 1)
            var interval = 0f

            // 绘制最后一行
            if (compatTailLines.contains(i)) {
                interval = 0f
                if (compatAlign == Align.ALIGN_CENTER) {
                    drawSpacingX += gap / 2
                } else if (compatAlign == Align.ALIGN_RIGHT) {
                    drawSpacingX += gap
                }
            }
            canvas.drawText(
                line, drawSpacingX, drawY +
                        paddingTop + compatTextLineSpaceExtra * i, paint
            )
//            for (j in line.indices) {
//                val drawX = paint.measureText(line.substring(0, j)) + interval * j
//                canvas.drawText(
//                    line.substring(j, j + 1), drawX + drawSpacingX, drawY +
//                            paddingTop + compatTextLineSpaceExtra * i, paint
//                )
//            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (compat) {
            firstCalc = true
        }
        super.setText(text, type)
        requestLayout()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (compat) {
            if (!setPaddingFromMe) {
                originalPaddingBottom = bottom
            }
            setPaddingFromMe = false
        }
        super.setPadding(left, top, right, bottom)
    }

    fun setTextWeight(weight: Int = 0) {
        helper.setTextWeight(this, weight)
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

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        helper.changeTextColor(this, isSelected)
    }

    /**
     * 设置尾行对齐方式
     *
     * @param align 对齐方式
     */
    fun setCompatAlign(align: Align) {
        this.compatAlign = align
        invalidate()
    }

    /**
     * 设置尾行省略
     *
     * @param enabled 是否开启
     */
    fun setCompatEllipsize(enabled: Boolean) {
        this.compatEllipsize = enabled
        invalidate()
    }

    /**
     * 计算每行应显示的文本数
     *
     * @param text 要计算的文本
     */
    private fun calc(paint: Paint, text: String, width: Int) {
        if (text.isBlank()) {
            while (minLines > compatLines.size) {
                addCompatLines("\n")
            }
            return
        }
        var startPosition = 0 // 起始位置
        var endPosition = 1 // 结束位置
        val sb = java.lang.StringBuilder()
        if (paint.measureText(text) < width) {
            sb.append(text)
            addCompatLines(sb.toString())
        } else {
            while (startPosition < text.length) {
                val end = if (endPosition + 1 > text.length) text.length else endPosition + 1
                val measureWidth = paint.measureText(text.substring(startPosition, end))
                if (measureWidth >= width || endPosition == text.length) {
                    addCompatLines(text.substring(startPosition, endPosition))
                    startPosition = endPosition
                }
                endPosition += 1
            }
        }

        while (minLines > compatLines.size) {
            addCompatLines("\n")
        }

        compatTailLines.add(compatLines.size - 1)
    }

    private fun addCompatLines(lineText: String) {
        if (compatEllipsize && compatLines.size == maxLines) {
            val index = compatLines.size - 1
            val oldText = compatLines[index]
            if (oldText.length > 1 && oldText.lastIndexOf("...") == -1) {
                compatLines[index] = oldText.substring(0, oldText.length - 2).plus("...")
            }
        } else if (compatLines.size < maxLines || maxLines == -1) {
            compatLines.add(lineText)
        }
    }

    /**
     * 获取文本实际所占高度，辅助用于计算行高,行数
     *
     * @param text        文本
     * @param textSize    字体大小
     * @param deviceWidth 屏幕宽度
     */
    private fun measureTextViewHeight(text: String, textSize: Float, deviceWidth: Int) {
        val textView = TextView(context)
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.EXACTLY)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        textView.measure(widthMeasureSpec, heightMeasureSpec)
        originalLineCount = textView.lineCount
        originalHeight = textView.measuredHeight
    }

    fun setTextColor(
        textColor: Int? = null, textSelectedColor: Int? = null, textDisableColor: Int? = null, isRes: Boolean = true
    ) {
        if (textColor != null) {
            if (isRes)
                setTextColor(ContextCompat.getColor(context, textColor))
            else
                setTextColor(textColor)
        }
        if (textSelectedColor != null) {
            if (isRes)
                helper.textSelectedColor = ContextCompat.getColor(context, textSelectedColor)
            else
                helper.textSelectedColor = textSelectedColor
        }
        if (textDisableColor != null) {
            if (isRes)
                helper.textDisableColor = ContextCompat.getColor(context, textDisableColor)
            else
                helper.textDisableColor = textDisableColor
        }
        helper.initTextView(this)
    }

    fun setTextGradient(
        textEndColor: List<String> = listOf(), textEndStep: List<Float> = listOf(),
        textSelectedEndColor: List<String> = listOf(), textSelectedEndStep: List<Float> = listOf()
    ) {
        helper.textEndColor = textEndColor.joinToString(",")
        helper.textEndStep = textEndStep.joinToString(",")
        helper.textSelectedEndColor = textSelectedEndColor.joinToString(",")
        helper.textSelectedEndStep = textSelectedEndStep.joinToString(",")

        helper.changeTextColor(this, isSelected)
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