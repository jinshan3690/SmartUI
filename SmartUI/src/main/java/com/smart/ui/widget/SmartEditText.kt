package com.smart.ui.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.*
import android.text.method.DigitsKeyListener
import android.text.method.TextKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import com.smart.ui.R
import com.smart.ui.binding.TextViewBindingAdapter
import com.smart.ui.util.SmartHelper

class SmartEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), OnFocusChangeListener, TextWatcher, OnEditorActionListener {

    private val helper = SmartHelper(context, attrs, this)

    var editText: SmartEdit? = null
    var prefixIcon: LinearLayout? = null
    var suffixIcon: LinearLayout? = null
    var cancelIcon: LinearLayout? = null
    var textChangedListener: TextChangedListener? = null
    var focusChangeListener: FocusChangeListener? = null
    var specharsListener: SpecharsListener? = null
    var searchListener: SearchListener? = null
    var doneListener: DoneListener? = null
    var length = -1

    private var prefixIconDrawable: Drawable? = null
    private var prefixIconWidth = 0
    private var prefixIconHeight = 0
    private var prefixIconLeftPadding = 0
    private var prefixIconRightPadding = 0
    private var suffixIconDrawable: Drawable? = null
    private var suffixIconWidth = 0
    private var suffixIconHeight = 0
    private var suffixIconLeftPadding = 0
    private var suffixIconRightPadding = 0
    private var cancelIconDrawable: Drawable? = null
    private var cancelIconWidth = 0
    private var cancelIconHeight = 0
    private var cancelIconLeftPadding = 0
    private var cancelIconRightPadding = 0

    private var textLeftPadding = 0
    private var textTopPadding = 0
    private var textRightPadding = 0
    private var textBottomPadding = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartEditText)

        prefixIconDrawable = typedArray.getDrawable(R.styleable.SmartEditText_sl_prefixIcon)
        prefixIconWidth = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_prefixIconWidth, SmartHelper.dp2px(context, 15f)
        )
        prefixIconHeight = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_prefixIconHeight, SmartHelper.dp2px(context, 15f)
        )
        prefixIconLeftPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_prefixIconLeftPadding, 0
        )
        prefixIconRightPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_prefixIconRightPadding, 0
        )

        suffixIconDrawable = typedArray.getDrawable(R.styleable.SmartEditText_sl_suffixIcon)
        suffixIconWidth = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_suffixIconWidth, SmartHelper.dp2px(context, 15f)
        )
        suffixIconHeight = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_suffixIconHeight, SmartHelper.dp2px(context, 15f)
        )
        suffixIconLeftPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_suffixIconLeftPadding, 0
        )
        suffixIconRightPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_suffixIconRightPadding, 0
        )

        cancelIconDrawable = typedArray.getDrawable(R.styleable.SmartEditText_sl_cancelIcon)
        cancelIconWidth = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_cancelIconWidth, SmartHelper.dp2px(context, 15f)
        )
        cancelIconHeight = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_cancelIconHeight, SmartHelper.dp2px(context, 15f)
        )
        cancelIconLeftPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_cancelIconLeftPadding, 0
        )
        cancelIconRightPadding = typedArray.getDimensionPixelSize(
            R.styleable.SmartEditText_sl_cancelIconRightPadding, 0
        )

        textLeftPadding =
            typedArray.getDimensionPixelSize(R.styleable.SmartEditText_sl_textPaddingLeft, 0)
        textRightPadding =
            typedArray.getDimensionPixelSize(R.styleable.SmartEditText_sl_textPaddingRight, 0)
        textTopPadding =
            typedArray.getDimensionPixelSize(R.styleable.SmartEditText_sl_textPaddingTop, 0)
        textBottomPadding =
            typedArray.getDimensionPixelSize(R.styleable.SmartEditText_sl_textPaddingBottom, 0)

        initView()

        length = typedArray.getInt(R.styleable.SmartEditText_sl_length, -1)

        editText?.gravity = typedArray.getInt(R.styleable.SmartEditText_sl_textGravity, 0)
        editText?.setText(typedArray.getText(R.styleable.SmartEditText_sl_text))
        editText?.setTextColor(
            typedArray.getColor(
                R.styleable.SmartEditText_sl_textColor, Color.parseColor("#333333")
            )
        )
        editText?.hint = typedArray.getString(R.styleable.SmartEditText_sl_hint)
        editText?.setHintTextColor(
            typedArray.getColor(
                R.styleable.SmartEditText_sl_hintColor, Color.parseColor("#666666")
            )
        )
        editText?.setTextSize(
            TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(
                R.styleable.SmartEditText_sl_textSize, SmartHelper.sp2px(context, 14f).toFloat()
            )
        )
        val multiLint = typedArray.getBoolean(
            R.styleable.SmartEditText_sl_multiLine, false
        )
        val inputType =
            if (multiLint) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            else InputType.TYPE_CLASS_TEXT

        editText?.inputType =
            when (typedArray.getInt(R.styleable.SmartEditText_sl_inputType, -1)) {
                1 -> inputType or InputType.TYPE_TEXT_VARIATION_PASSWORD
                2 -> inputType or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else -> inputType
            }

        val digits = typedArray.getString(R.styleable.SmartEditText_sl_digits)
        if (!digits.isNullOrBlank()) {
            editText?.keyListener = DigitsKeyListener.getInstance(digits)
        }
        setEditEnabled(typedArray.getBoolean(R.styleable.SmartEditText_sl_editEnabled, true))

        editText?.imeOptions =
            when (typedArray.getInt(R.styleable.SmartEditText_sl_imeOptions, -1)) {
                2 -> EditorInfo.IME_ACTION_NEXT
                3 -> EditorInfo.IME_ACTION_SEND
                4 -> EditorInfo.IME_ACTION_SEARCH
                5 -> EditorInfo.IME_ACTION_GO
                else -> EditorInfo.IME_ACTION_DONE
            }

        typedArray.recycle()

        editText?.let { helper.initTextView(it) }
    }

    private fun initView() {
        orientation = HORIZONTAL

        if (prefixIconDrawable != null) {
            prefixIcon = createIcon(
                prefixIconDrawable, prefixIconWidth, prefixIconHeight,
                prefixIconLeftPadding, prefixIconRightPadding
            )
            addView(prefixIcon)
        }

        editText =
            SmartEdit(context, textLeftPadding, textRightPadding, textTopPadding, textBottomPadding)

        editText?.onFocusChangeListener = this
        editText?.addTextChangedListener(this)
        editText?.setOnEditorActionListener(this)
        editText?.post { editText?.setSelection(editText?.length() ?: 0) }
        addView(editText)

        if (cancelIconDrawable != null) {
            cancelIcon = createIcon(
                cancelIconDrawable, cancelIconWidth, cancelIconHeight,
                cancelIconLeftPadding, cancelIconRightPadding
            )
            cancelIcon?.setOnClickListener { setText(null) }
            addView(cancelIcon)
        }

        if (suffixIconDrawable != null) {
            suffixIcon = createIcon(
                suffixIconDrawable, suffixIconWidth, suffixIconHeight,
                suffixIconLeftPadding, suffixIconRightPadding
            )
            addView(suffixIcon)
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) { // 焦点变化时
        focusChangeListener?.onFocusChange(v, hasFocus)

        hideCancelIcon()
    }

    override fun afterTextChanged(s: Editable?) {
        textChangedListener?.afterTextChanged(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        textChangedListener?.beforeTextChanged(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (length != -1 && length < s?.length ?: 0) {
            editText?.setText(s?.subSequence(0, length))
            editText?.setSelection(length)
        }
        hideCancelIcon()

        textChangedListener?.onTextChanged(s, start, before, count)
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchListener?.search(v.text.toString())
            val mInputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } else if (actionId == EditorInfo.IME_ACTION_DONE) {
            doneListener?.done(v.text.toString())
        }
        return false
    }

    /**
     * 过滤字符
     */
    fun setProhibitEmoji() {
        val filters =
            arrayOf(getInputFilterProhibitEmoji(), getInputFilterProhibitSP())
        editText?.filters = filters
    }

    private fun getInputFilterProhibitEmoji(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            val buffer = StringBuffer()
            var i = start
            while (i < end) {
                val codePoint = source[i]
                if (!getIsEmoji(codePoint)) {
                    buffer.append(codePoint)
                } else {
                    specharsListener?.warn("Emoji")
//                    ToastUtil.showShortToast("不支持输入表情")
                    i++
                    i++
                    continue
                }
                i++
            }
            if (source is Spanned) {
                val sp = SpannableString(buffer)
                TextUtils.copySpansFrom(
                    source, start, end, null,
                    sp, 0
                )
                sp
            } else {
                buffer
            }
        }
    }

    private fun getIsEmoji(codePoint: Char): Boolean {
        return !(codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA
                || codePoint.toInt() == 0xD
                || codePoint.toInt() in 0x20..0xD7FF
                || codePoint.toInt() in 0xE000..0xFFFD
                || codePoint.toInt() in 0x10000..0x10FFFF)
    }

    private fun getInputFilterProhibitSP(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            val buffer = StringBuffer()
            var i = start
            while (i < end) {
                val codePoint = source[i]
                if (!getIsSp(codePoint)) {
                    buffer.append(codePoint)
                } else {
                    specharsListener?.warn("char")
//                    ToastUtil.showShortToast("不能输入特殊字符")
                    i++
                    i++
                    continue
                }
                i++
            }
            if (source is Spanned) {
                val sp = SpannableString(buffer)
                TextUtils.copySpansFrom(
                    source, start, end, null, sp, 0
                )
                sp
            } else {
                buffer
            }
        }
    }

    private fun getIsSp(codePoint: Char): Boolean {
        return Character.getType(codePoint) > Character.LETTER_NUMBER
    }

    /**
     * 裁剪子View
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(w, h, paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
        editText?.let { helper.changeTextColor(it, isSelected) }
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

    fun setTypeface(tf: Typeface?) {
        if (tf == Typeface.DEFAULT_BOLD) {
            editText?.paint?.isFakeBoldText = true
        } else {
            editText?.setTypeface(tf)
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

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        editText?.let { helper.changeTextColor(it, isSelected) }
    }

    override fun setEnabled(enabled: Boolean) {
        setEditEnabled(enabled)
        super.setEnabled(enabled)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setEditEnabled(enabled: Boolean) {
        suffixIcon?.isEnabled = enabled
        cancelIcon?.isEnabled = enabled
        prefixIcon?.isEnabled = enabled
        editText?.isFocusable = enabled
        editText?.isFocusableInTouchMode = enabled
        if (enabled) {
            editText?.setOnTouchListener(null)
        } else {
            editText?.setOnTouchListener { _, motionEvent ->
                return@setOnTouchListener disabledTouch(editText?.parent as ViewGroup, motionEvent)
            }
        }
        hideCancelIcon()
    }

    private fun disabledTouch(view: ViewGroup?, motionEvent: MotionEvent): Boolean {
        val result = view?.onTouchEvent(motionEvent) ?: false
        if (!result) {
            return disabledTouch(view?.parent as ViewGroup, motionEvent)
        }
        return result
    }

    fun setDigits(digits: String?) {
        if (!digits.isNullOrBlank()) {
            editText?.keyListener = DigitsKeyListener.getInstance(digits)
        } else {
            editText?.keyListener = TextKeyListener.getInstance()
        }
    }

    fun createIcon(
        drawable: Drawable?, width: Int, height: Int, paddingLeft: Int, paddingRight: Int
    ): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        linearLayout.setPadding(paddingLeft, 0, paddingRight, 0)
        linearLayout.gravity = Gravity.CENTER

        val icon = SmartImageView(context)
        icon.setImageDrawable(drawable)
        icon.layoutParams = LayoutParams(width, height)
        linearLayout.addView(icon)

        return linearLayout
    }

    fun hideCancelIcon() {
        if (getText()?.length ?: 0 > 0 && editText?.isEnabled == true && editText?.isFocused == true) {
            cancelIcon?.visibility = View.VISIBLE
        } else {
            cancelIcon?.visibility = View.GONE
        }
    }

    fun setText(text: CharSequence?) {
        TextViewBindingAdapter.setText(this, text?.toString())
        editText?.setSelection(text?.length ?: 0)
    }

    fun getText(): String? {
        return TextViewBindingAdapter.getText(this)
    }

    @SuppressLint("ViewConstructor")
    class SmartEdit(
        context: Context, textLeftPadding: Int = 0, textRightPadding: Int = 0,
        textTopPadding: Int = 0, textBottomPadding: Int = 0, attrs: AttributeSet? = null
    ) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

        init {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f
            ) as MarginLayoutParams
            layoutParams = params
            setPadding(textLeftPadding, textTopPadding, textRightPadding, textBottomPadding)
            background = null
        }
    }

    interface SearchListener {
        fun search(text: String?)
    }

    interface DoneListener {
        fun done(text: String?)
    }

    interface SpecharsListener {
        fun warn(type: String)
    }

    interface FocusChangeListener {
        fun onFocusChange(v: View, hasFocus: Boolean)
    }

    abstract class TextChangedListener {

        open fun beforeTextChanged(
            s: CharSequence?, start: Int, count: Int, after: Int
        ) {
        }

        open fun onTextChanged(
            s: CharSequence?, start: Int, before: Int, count: Int
        ) {
        }

        open fun afterTextChanged(s: Editable?) {}

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

    fun setTextColor(
        textColor: Int? = null, textSelectedColor: Int? = null, textDisableColor: Int? = null
    ) {
        if (textColor != null) {
            setTextColor(ContextCompat.getColor(context, textColor))
        }
        if (textSelectedColor != null) {
            helper.textSelectedColor = ContextCompat.getColor(context, textSelectedColor)
        }
        if (textDisableColor != null) {
            helper.textDisableColor = ContextCompat.getColor(context, textDisableColor)
        }
        editText?.let { helper.initTextView(it) }
    }

    fun setTextGradient(
        textEndColor: List<String> = listOf(), textEndStep: List<Float> = listOf(),
        textSelectedEndColor: List<String> = listOf(), textSelectedEndStep: List<Float> = listOf()
    ) {
        helper.textEndColor = textEndColor.joinToString(",")
        helper.textEndStep = textEndStep.joinToString(",")
        helper.textSelectedEndColor = textSelectedEndColor.joinToString(",")
        helper.textSelectedEndStep = textSelectedEndStep.joinToString(",")

        editText?.let { helper.changeTextColor(it, isSelected) }
    }

    fun setRadius(radius: Float) {
        helper.initRadius(radius, radius, radius, radius)
        //onSizeChanged
        if (helper.strokeOverlay || helper.isCorner) {
            helper.onSizeChanged(
                width, height, paddingLeft, paddingTop, paddingRight, paddingBottom
            )
        }
        editText?.let { helper.changeTextColor(it, isSelected) }

        helper.initBackground()
    }

}