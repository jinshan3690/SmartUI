package com.hpcang.smart.ui.util

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.hpcang.smart.ui.widget.R
import kotlin.math.max
import kotlin.math.min

class SmartHelper(var context: Context?, var attrs: AttributeSet?, var view: View?) {

    private var clipPaint = Paint()
    private var clipPath = Path()
    var isCorner: Boolean = false
    private var cornerRadiusArray = FloatArray(8)
    var layer = RectF()
    var background: Drawable? = null

    var stroke: Int = 0
    private var strokeColor: Int = 0
    var strokeOverlay: Boolean = false
    var color: Int = 0
    private var endColor: Int = 0
    private var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    private var rippleColor: Int = 0
    private var shadowColor: Int = 0
    private var maskDrawable: Drawable? = null
    private var disableColor: Int = 0
    private var selectedStrokeColor: Int = 0
    private var selectedColor: Int = 0
    private var selectedEndColor: Int = 0
    private var shape: Int = GradientDrawable.RECTANGLE
    private var defaultStateListAnim: Boolean = false
    private var stateListAnim: StateListAnimator? = null
    private var defaultDuration: Long = 200
    private var elevationNormal: Float = 0f
    private var elevationPressed: Float = 0f
    private var translationZNormal: Float = 0f
    private var translationZPressed: Float = 0f

    //TextView
    private var textDisableColor: Int = 0
    private var textSelectedColor: Int = 0
    private var textEndColor: String? = null
    private var textSelectedEndColor: String? = null
    private var textEndStep: String? = null
    private var textSelectedEndStep: String? = null

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.SmartLayout)
        typedArray?.run {
            background = typedArray.getDrawable(R.styleable.SmartLayout_sl_background)
            color = typedArray.getColor(R.styleable.SmartLayout_sl_color, Color.TRANSPARENT)
            endColor = typedArray.getColor(R.styleable.SmartLayout_sl_endColor, -999)
            orientation = when (typedArray.getInt(R.styleable.SmartLayout_sl_orientation, 6)) {
                0 -> GradientDrawable.Orientation.TOP_BOTTOM
                1 -> GradientDrawable.Orientation.TR_BL
                2 -> GradientDrawable.Orientation.RIGHT_LEFT
                3 -> GradientDrawable.Orientation.BR_TL
                4 -> GradientDrawable.Orientation.BOTTOM_TOP
                5 -> GradientDrawable.Orientation.BL_TR
                6 -> GradientDrawable.Orientation.LEFT_RIGHT
                else -> GradientDrawable.Orientation.TL_BR
            }
            disableColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_disableColor, 999
            )
            rippleColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_rippleColor, getColorWithAlpha(0.08f, Color.BLACK)
            )
            shadowColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_shadowColor, Color.BLACK
            )
            maskDrawable = typedArray.getDrawable(R.styleable.SmartLayout_sl_maskDrawable)
            shape = typedArray.getInt(R.styleable.SmartLayout_sl_shape, 0)
            stroke = typedArray.getDimension(R.styleable.SmartLayout_sl_stroke, 0f).toInt()
            strokeColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_strokeColor, Color.parseColor("#DEDEDE")
            )
            strokeOverlay = typedArray.getBoolean(
                R.styleable.SmartLayout_sl_strokeOverlay, false
            )
            selectedColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_selectedColor, -999
            )
            selectedEndColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_selectedEndColor, -999
            )
            selectedStrokeColor = typedArray.getColor(
                R.styleable.SmartLayout_sl_selectedStrokeColor, -999
            )
            val radius = typedArray.getDimension(R.styleable.SmartLayout_sl_radius, 0f)
            val radiusLeftTop =
                typedArray.getDimension(R.styleable.SmartLayout_sl_radiusLeftTop, radius)
            val radiusRightTop =
                typedArray.getDimension(R.styleable.SmartLayout_sl_radiusRightTop, radius)
            val radiusLeftBottom =
                typedArray.getDimension(R.styleable.SmartLayout_sl_radiusLeftBottom, radius)
            val radiusRightBottom =
                typedArray.getDimension(R.styleable.SmartLayout_sl_radiusRightBottom, radius)

            elevationNormal =
                typedArray.getDimension(R.styleable.SmartLayout_sl_elevationNormal, 0f)
            translationZNormal =
                typedArray.getDimension(R.styleable.SmartLayout_sl_translationZNormal, 0f)
            elevationPressed =
                typedArray.getDimension(
                    R.styleable.SmartLayout_sl_elevationPressed,
                    elevationNormal
                )
            translationZPressed = typedArray.getDimension(
                R.styleable.SmartLayout_sl_translationZPressed,
                translationZNormal
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view?.elevation = elevationNormal
                view?.translationZ = translationZNormal

                val stateAnim =
                    typedArray.getResourceId(R.styleable.SmartLayout_sl_stateListAnim, 0)
                if (stateAnim != 0)
                    stateListAnim = AnimatorInflater.loadStateListAnimator(context, stateAnim)
            }

            defaultStateListAnim =
                typedArray.getBoolean(R.styleable.SmartLayout_sl_defaultStateListAnim, false)

            typedArray.recycle()

            cornerRadiusArray.run {
                set(0, radiusLeftTop)
                set(1, radiusLeftTop)

                set(2, radiusRightTop)
                set(3, radiusRightTop)

                set(4, radiusRightBottom)
                set(5, radiusRightBottom)

                set(6, radiusLeftBottom)
                set(7, radiusLeftBottom)
            }

            cornerRadiusArray.forEach {
                if (it != 0f) {
                    isCorner = true
                }
            }

            if (stroke != 0 && !strokeOverlay)
                view?.setPadding(
                    (view?.paddingLeft ?: 0) + stroke, (view?.paddingTop ?: 0) + stroke,
                    (view?.paddingRight ?: 0) + stroke, (view?.paddingBottom ?: 0) + stroke
                )

            if (background == null) {
                initBackground()
            } else {
                view?.background = background
            }

            initStateListAnim()
            initClip()
        }
    }

    private fun initBackground() {
        val stateListDrawable = StateListDrawable()
        val backgroundDrawable = createDrawable(
            color = *intArrayOf(color, endColor)
        )

        if (disableColor != 999) {
            val disableDrawable = createDrawable(
                color = *intArrayOf(disableColor)
            )

            stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disableDrawable)
        }
        if (selectedColor != -999 || selectedStrokeColor != -999) {
            if (selectedColor == -999)
                selectedColor = Color.TRANSPARENT
            if (selectedStrokeColor == -999)
                selectedStrokeColor = Color.TRANSPARENT
            val selectedDrawable = createDrawable(
                strokeColor = selectedStrokeColor,
                color = *intArrayOf(selectedColor, selectedEndColor)
            )
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
                selectedDrawable
            )
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                backgroundDrawable
            )
        } else {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), backgroundDrawable)
        }

        view?.background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view?.foreground = RippleDrawable(
                    ColorStateList.valueOf(rippleColor), null, maskDrawable ?: backgroundDrawable
                )
                stateListDrawable
            } else {
                val rippleDrawable = RippleDrawable(
                    ColorStateList.valueOf(rippleColor), stateListDrawable, maskDrawable
                )
                rippleDrawable
            }
        } else {
            stateListDrawable
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            view?.outlineSpotShadowColor = shadowColor
            //发散
//            view?.outlineAmbientShadowColor = shadowColor
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun initStateListAnim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (stateListAnim == null && defaultStateListAnim) {
                val xPressedAnim = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.025f).apply {
                    duration = defaultDuration
                }
                val yPressedAnim = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.025f).apply {
                    duration = defaultDuration
                }
                val zPressedAnim = ObjectAnimator.ofFloat(
                    view, View.TRANSLATION_Z, translationZNormal, translationZPressed
                )
                    .apply {
                        duration = defaultDuration
                    }
                val ePressedAnim = ObjectAnimator.ofFloat(
                    view, "elevation", elevationNormal, elevationPressed
                )
                    .apply {
                        duration = defaultDuration
                    }
                val xNormalAnim = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f).apply {
                    duration = defaultDuration
                }
                val yNormalAnim = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f).apply {
                    duration = defaultDuration
                }
                val zNormalAnim =
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, translationZNormal).apply {
                        duration = defaultDuration
                    }
                val eNormalAnim = ObjectAnimator.ofFloat(view, "elevation", elevationNormal).apply {
                    duration = defaultDuration
                }

                stateListAnim = StateListAnimator()
                val pressedAnim = AnimatorSet().apply {
                    play(xPressedAnim).with(yPressedAnim).with(zPressedAnim).with(ePressedAnim)
                }
                val normalAnim = AnimatorSet().apply {
                    play(xNormalAnim).with(yNormalAnim).with(zNormalAnim).with(eNormalAnim)
                }

                stateListAnim?.addState(intArrayOf(android.R.attr.state_pressed), pressedAnim)
                stateListAnim?.addState(intArrayOf(-android.R.attr.state_pressed), normalAnim)
            }
            this.view?.stateListAnimator = stateListAnim
        }
    }

    private fun initClip() {
//        view?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//关掉硬件加速
        clipPaint.isAntiAlias = true
    }

    /**
     *  计算裁剪
     */
    fun onSizeChanged(
        w: Int, h: Int, paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int
    ) {
        val stroke = if (strokeOverlay) stroke else 0
        layer.set(0f, 0f, w.toFloat(), h.toFloat())
        val areas = RectF()
        areas.left = paddingLeft.toFloat() + stroke
        areas.top = paddingTop.toFloat() + stroke
        areas.right = w - paddingRight.toFloat() - stroke
        areas.bottom = h - paddingBottom.toFloat() - stroke
        clipPath.reset()
        clipPath.addRoundRect(areas, cornerRadiusArray, Path.Direction.CW)
    }

    /**
     * 裁剪背景
     */
    fun onClipDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            clipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawPath(clipPath, clipPaint)
        } else {
            clipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            val path = Path()
            path.addRect(
                0f, 0f, layer.width(), layer.height(), Path.Direction.CW
            )
            path.op(clipPath, Path.Op.DIFFERENCE)
            canvas.drawPath(path, clipPaint)
        }
    }

    /**
     * 获取背景
     */
    fun createDrawable(
        cornerRadiusArray: FloatArray = this.cornerRadiusArray, shape: Int = this.shape,
        stroke: Int = this.stroke, strokeColor: Int = this.strokeColor, vararg color: Int
    ): GradientDrawable {
        val colors = color.filter { it != -999 }.toIntArray()
        val drawable = GradientDrawable()
        if (colors.size > 1) {
            drawable.orientation = orientation
            drawable.colors = colors
        } else {
            drawable.setColor(colors[0])
        }
        drawable.setStroke(stroke, strokeColor)
        drawable.cornerRadii = cornerRadiusArray
        val radius = cornerRadiusArray.filter { it > 0 }
        if (radius.isNotEmpty()) {
            drawable.gradientRadius = radius.first()
        }
        //必须最后设置
        drawable.shape = shape
        return drawable
    }

    /**
     * 给color添加透明度
     * @param alpha 透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    private fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = min(255, max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }

    //TextView
    fun initTextView(textView: TextView) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.SmartTextView)
        typedArray?.run {
            textSelectedColor = typedArray.getColor(
                R.styleable.SmartTextView_sl_textSelectedColor, 0
            )
            textDisableColor = typedArray.getColor(
                R.styleable.SmartTextView_sl_textDisableColor, 0
            )
            textEndColor = typedArray.getString(R.styleable.SmartTextView_sl_textEndColors)
            textSelectedEndColor =
                typedArray.getString(R.styleable.SmartTextView_sl_textSelectedEndColors)
            textEndStep = typedArray.getString(R.styleable.SmartTextView_sl_textEndStep)
            textSelectedEndStep =
                typedArray.getString(R.styleable.SmartTextView_sl_textSelectedEndStep)

            val style = typedArray.getInteger(R.styleable.SmartTextView_sl_textStyle, 0)
            if (style == 0) {
                textView.typeface = Typeface.DEFAULT
                textView.paint.isFakeBoldText = false
            } else if (style == 1) {
                textView.typeface = Typeface.DEFAULT
                textView.paint.isFakeBoldText = true
            }

            typedArray.recycle()

            initTextColor(textView)
        }
    }

    private fun initTextColor(textView: TextView) {
        if (textEndColor.isNullOrBlank()) {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                    intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    textView.textColors.defaultColor,
                    if (textSelectedColor == 0) textView.textColors.defaultColor else textSelectedColor,
                    if (textDisableColor == 0) textView.textColors.defaultColor else textDisableColor
                )
            )
            textView.setTextColor(colorStateList)
        }
    }

    fun changeTextColor(textView: TextView, selected: Boolean) {
        if (!textEndColor.isNullOrBlank() && !textEndStep.isNullOrBlank()) {
            if (selected && !textSelectedEndColor.isNullOrBlank() && !textSelectedEndStep.isNullOrBlank()) {
                val mLinearGradient = LinearGradient(
                    0f, 0f,
                    textView.measuredWidth.toFloat() - textView.paddingLeft - textView.paddingRight,
                    0f, stringToColors(textSelectedEndColor!!), stringToStep(textSelectedEndStep!!),
                    Shader.TileMode.CLAMP
                )
                textView.paint.shader = mLinearGradient
            } else {
                val mLinearGradient = LinearGradient(
                    0f, 0f,
                    textView.measuredWidth.toFloat() - textView.paddingLeft - textView.paddingRight,
                    0f, stringToColors(textEndColor!!), stringToStep(textEndStep!!),
                    Shader.TileMode.CLAMP
                )
                textView.paint.shader = mLinearGradient
            }
        }
    }

    //string转int[]
    private fun stringToColors(str: String): IntArray {
        val list = str.split(",", "，", " ").toTypedArray()
        val result = IntArray(list.size)
        for (i in list.indices) {
            result[i] = Color.parseColor(list[i])
        }
        return result
    }

    //string转float[]
    private fun stringToStep(str: String): FloatArray {
        val list = str.split(",", "，", " ").toTypedArray()
        val result = FloatArray(list.size)
        for (i in list.indices) {
            result[i] = list[i].toFloat()
        }
        return result
    }

    companion object{
        /**
         * dp转px
         */
        @JvmStatic
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics
            ).toInt()
        }

        /**
         * sp转px
         */
        @JvmStatic
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, spVal, context.resources.displayMetrics
            ).toInt()
        }
    }

}