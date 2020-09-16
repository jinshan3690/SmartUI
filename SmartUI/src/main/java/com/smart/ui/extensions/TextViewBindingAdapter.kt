package com.smart.ui.extensions

import android.text.Editable
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.smart.ui.widget.SmartEditText


@BindingAdapter(value = ["text"], requireAll = false)
fun setText(view: SmartEditText, text: CharSequence?) {
    view.setText(text)
}

@InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
fun getText(view: SmartEditText): Editable? {
    return view.getText()
}

@BindingAdapter(value = ["textAttrChanged"], requireAll = false)
fun setTextAttrChange(view: SmartEditText, listener: InverseBindingListener?) {
    listener?.let {
        view.editText?.doOnTextChanged { _, _, _, _ ->
            listener.onChange()
        }
    }
}
