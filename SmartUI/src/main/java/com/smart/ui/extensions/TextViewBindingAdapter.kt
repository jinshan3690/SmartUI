package com.smart.ui.extensions

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.smart.ui.widget.SmartEditText

object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["sl_text"], requireAll = false)
    fun setText(view: SmartEditText, text: String?) {
        if (view.getText() != text)
            view.editText?.setText(text)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "sl_text", event = "textAttrChanged")
    fun getText(view: SmartEditText): String? {
        return view.editText?.text?.toString()
    }

    private var textChanged: TextWatcher? = null

    @JvmStatic
    @BindingAdapter(value = ["textAttrChanged"], requireAll = false)
    fun setTextAttrChange(view: SmartEditText, listener: InverseBindingListener?) {
        listener?.let {
            if (textChanged == null)
                textChanged = view.editText?.doOnTextChanged { _, _, _, _ ->
                    listener.onChange()
                }
        }
    }

}
