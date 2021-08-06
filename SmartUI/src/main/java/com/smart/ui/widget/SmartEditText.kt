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
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.smart.ui.R
import com.smart.ui.util.SmartHelper

class SmartEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), OnFocusChangeListener, TextWatcher,
    OnEditorActionListener {

    val helper = SmartHelper(context, attrs, this)

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
    private var prefixIconRotation: Float = 0f
    private var prefixIconWidth = 0
    private var prefixIconHeight = 0
    private var prefixIconLeftPadding = 0
    private var prefixIconRightPadding = 0
    private var suffixIconDrawable: Drawable? = null
    private var suffixIconRotation: Float = 0f
    private var suffixIconWidth = 0
    private var suffixIconHeight = 0
    private var suffixIconLeftPadding = 0
    private var suffixIconRightPadding = 0
    private var cancelIconDrawable: Drawable? = null
    private var cancelIconRotation: Float = 0f
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
        prefixIconRotation = typedArray.getFloat(
            R.styleable.SmartEditText_sl_prefixIconRotation, 0f
        )
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
        suffixIconRotation = typedArray.getFloat(
            R.styleable.SmartEditText_sl_suffixIconRotation, 0f
        )
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
        cancelIconRotation = typedArray.getFloat(
            R.styleable.SmartEditText_sl_cancelIconRotation, 0f
        )
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
        editText?.inputType = typedArray.getInt(R.styleable.SmartEditText_sl_inputType, 1)

        val digits = typedArray.getString(R.styleable.SmartEditText_sl_digits)
        if (!digits.isNullOrBlank()) {
            editText?.keyListener = DigitsKeyListener.getInstance(digits)
        }
        setEditEnabled(typedArray.getBoolean(R.styleable.SmartEditText_sl_editEnabled, true))

        editText?.imeOptions = typedArray.getInt(R.styleable.SmartEditText_sl_imeOptions, 0)

        typedArray.recycle()

        editText?.let { helper.initTextView(it) }
    }

    private fun initView() {
        orientation = HORIZONTAL

        addPrefixIcon()

        editText =
            SmartEdit(
                context,
                textLeftPadding,
                textRightPadding,
                textTopPadding,
                textBottomPadding
            )
        editText?.onFocusChangeListener = this
        editText?.addTextChangedListener(this)
        editText?.setOnEditorActionListener(this)
        editText?.setOnFocusChangeListener { v, hasFocus ->
            isSelected = hasFocus
            if (hasFocus)
                editText?.setSelection(editText?.length() ?: 0)
        }
        editText?.post { editText?.setSelection(editText?.length() ?: 0) }
        addView(editText)

        addCancelIcon()
        addSuffixIcon()
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

    fun <T> setAdapter(adapter: T) where T : ListAdapter, T : Filterable {
        editText?.setAdapter<T>(adapter)
    }

    fun showDropDown() {
        editText?.showDropDown()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) && editText?.isFocusable == true && editText?.isFocusableInTouchMode == true) {
            editText?.requestFocus()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (helper != null && !helper.throttle) {
            super.setOnClickListener { v ->
                if (editText?.isFocusable == true && editText?.isFocusableInTouchMode == true) {
//                    editText?.requestFocus()
                } else {
                    l?.run {
                        isSelected = !isSelected
                        onClick(v)
                    }
                }
            }
        } else {
            var prev = 0L
            super.setOnClickListener { v ->
                if (editText?.isFocusable == true && editText?.isFocusableInTouchMode == true) {
//                    editText?.requestFocus()
                } else {
                    val now = System.currentTimeMillis()

                    if (now - prev >= helper.throttleInterval) {
                        isSelected = !isSelected
                        l?.onClick(v)
                        prev = System.currentTimeMillis()
                    }
                }
            }
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        editText?.let { helper.changeTextColor(it, isSelected) }
    }

    override fun setEnabled(enabled: Boolean) {
        setEditEnabled(enabled)
        suffixIcon?.isEnabled = enabled
        prefixIcon?.isEnabled = enabled
        super.setEnabled(enabled)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setEditEnabled(enabled: Boolean) {
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
        @IdRes id: Int?, drawable: Drawable?, width: Int, height: Int,
        paddingLeft: Int, paddingRight: Int, rotation: Float = 0f
    ): LinearLayout {
        val linearLayout = LinearLayout(context)
        id?.let { linearLayout.id = id }
        linearLayout.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        linearLayout.setPadding(paddingLeft, 0, paddingRight, 0)
        linearLayout.gravity = Gravity.CENTER
        linearLayout.isEnabled = isEnabled

        val icon = SmartImageView(context)
        icon.rotation = rotation
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
//        TextViewBindingAdapter.setText(this, text?.toString(),editText?.hint?.toString())
        editText?.setText(text)
        editText?.setSelection(text?.length ?: 0)
    }

    fun getText(): String? {
        return editText?.text?.toString()
    }

    private fun addPrefixIcon() {
        if (prefixIconDrawable != null) {
            prefixIcon = createIcon(
                R.id.prefix_icon, prefixIconDrawable, prefixIconWidth, prefixIconHeight,
                prefixIconLeftPadding, prefixIconRightPadding, prefixIconRotation
            )
            addView(prefixIcon)
        }
    }

    private fun addCancelIcon() {
        if (cancelIconDrawable != null) {
            cancelIcon = createIcon(
                R.id.cancel_icon, cancelIconDrawable, cancelIconWidth, cancelIconHeight,
                cancelIconLeftPadding, cancelIconRightPadding, cancelIconRotation
            )
            cancelIcon?.setOnClickListener { setText(null) }
            addView(cancelIcon)
        }
    }

    private fun addSuffixIcon() {
        if (suffixIconDrawable != null) {
            suffixIcon = createIcon(
                R.id.suffix_icon, suffixIconDrawable, suffixIconWidth, suffixIconHeight,
                suffixIconLeftPadding, suffixIconRightPadding, suffixIconRotation
            )
            addView(suffixIcon)
        }
    }

    fun updatePrefixIcon(
        leftPadding: Int? = null, rightPadding: Int? = null, drawable: Drawable? = null,
        width: Int? = null, height: Int? = null, rotation: Float = 0f
    ) {
        leftPadding?.let { this.prefixIconLeftPadding = it }
        rightPadding?.let { this.prefixIconRightPadding = it }
        this.prefixIconDrawable = drawable
        width?.let { this.prefixIconWidth = it }
        height?.let { this.prefixIconHeight = it }
        this.prefixIconRotation = rotation

        for (v: View in children) {
            if (v.id == R.id.prefix_icon) {
                v.setPadding(prefixIconLeftPadding, 0, prefixIconRightPadding, 0)
                if ((v as ViewGroup).childCount > 0) {
                    val image = v.getChildAt(0) as ImageView
                    image.setImageDrawable(prefixIconDrawable)
                    image.layoutParams.width = prefixIconWidth
                    image.layoutParams.height = prefixIconHeight
                    image.rotation = prefixIconRotation
                }
            }
            break
        }
    }

    fun updateCancelIcon(
        leftPadding: Int? = null, rightPadding: Int? = null, drawable: Drawable? = null,
        width: Int? = null, height: Int? = null, rotation: Float = 0f
    ) {
        leftPadding?.let { this.cancelIconLeftPadding = it }
        rightPadding?.let { this.cancelIconRightPadding = it }
        this.cancelIconDrawable = drawable
        width?.let { this.cancelIconWidth = it }
        height?.let { this.cancelIconHeight = it }
        this.cancelIconRotation = rotation

        for (v: View in children) {
            if (v.id == R.id.cancel_icon) {
                v.setPadding(cancelIconLeftPadding, 0, cancelIconRightPadding, 0)
                if ((v as ViewGroup).childCount > 0) {
                    val image = v.getChildAt(0) as ImageView
                    image.setImageDrawable(cancelIconDrawable)
                    image.layoutParams.width = cancelIconWidth
                    image.layoutParams.height = cancelIconHeight
                    image.rotation = cancelIconRotation
                }
            }
            break
        }
    }

    fun updateSuffixIcon(
        leftPadding: Int? = null, rightPadding: Int? = null, drawable: Drawable? = null,
        width: Int? = null, height: Int? = null, rotation: Float = 0f
    ) {
        leftPadding?.let { this.suffixIconLeftPadding = it }
        rightPadding?.let { this.suffixIconRightPadding = it }
        suffixIconDrawable = drawable
        width?.let { this.suffixIconWidth = it }
        height?.let { this.suffixIconHeight = it }
        this.suffixIconRotation = rotation

        for (v: View in children) {
            if (v.id == R.id.suffix_icon) {
                v.setPadding(suffixIconLeftPadding, 0, suffixIconRightPadding, 0)
                if ((v as ViewGroup).childCount > 0) {
                    val image = v.getChildAt(0) as ImageView
                    image.setImageDrawable(suffixIconDrawable)
                    image.layoutParams.width = suffixIconWidth
                    image.layoutParams.height = suffixIconHeight
                    image.rotation = suffixIconRotation
                }
            }
            break
        }
    }

    @SuppressLint("ViewConstructor")
    class SmartEdit(
        context: Context,
        textLeftPadding: Int = 0,
        textRightPadding: Int = 0,
        textTopPadding: Int = 0,
        textBottomPadding: Int = 0,
        attrs: AttributeSet? = null
    ) : androidx.appcompat.widget.AppCompatAutoCompleteTextView(context, attrs) {

        init {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f
            ) as ViewGroup.MarginLayoutParams
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

    fun setTextColor(
        textColor: Int? = null, textSelectedColor: Int? = null, textDisableColor: Int? = null, isRes: Boolean = true
    ) {
        if (textColor != null) {
            if (isRes)
                editText?.setTextColor(ContextCompat.getColor(context, textColor))
            else
                editText?.setTextColor(textColor)
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