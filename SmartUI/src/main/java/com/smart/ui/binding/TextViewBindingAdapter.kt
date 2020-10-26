package com.smart.ui.binding

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

    @JvmStatic
    @BindingAdapter(value = ["sl_hint"], requireAll = false)
    fun setHint(view: SmartEditText, text: String?) {
        if (view.getText() != text)
            view.editText?.hint = text
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "sl_hint", event = "textAttrChanged")
    fun getHint(view: SmartEditText): String? {
        return view.editText?.hint?.toString()
    }

    @JvmStatic
    @BindingAdapter(value = ["textAttrChanged"], requireAll = false)
    fun setTextAttrChange(view: SmartEditText, listener: InverseBindingListener?) {
        listener?.let {
            view.editText?.doOnTextChanged { _, _, _, _ ->
                listener.onChange()
            }
        }
    }

}
