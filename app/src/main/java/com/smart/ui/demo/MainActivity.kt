package com.smart.ui.demo

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.MutableLiveData
import com.smart.ui.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var viewDataBinding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("2","1"))
        viewDataBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewDataBinding?.hint1 = true
        viewDataBinding?.stroke = 0
        viewDataBinding?.apply {
            name = "点击右侧ico清空"
            hint = "这是一个多功能EditText11111"
            text = "多行"
            this.adapter = adapter
        }
        viewDataBinding?.set?.suffixIcon?.setOnClickListener {
            Toast.makeText(this,"点击回调2222",Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.set?.prefixIcon?.setOnClickListener {
            Toast.makeText(this,"点击回调333",Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.sl3?.setOnClickListener {
            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.set?.setOnClickListener {
            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
        }
//        viewDataBinding?.set2?.setOnClickListener {
//            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
//        }
        viewDataBinding?.set3?.setOnClickListener {
            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.set2?.suffixIcon?.setOnClickListener {
            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.set3?.suffixIcon?.setOnClickListener {
            Toast.makeText(this,"点击回调4444"+it.isSelected,Toast.LENGTH_SHORT).show()
        }
        viewDataBinding?.st26?.setOnClickListener {
            viewDataBinding?.st26?.setText(viewDataBinding?.st26?.text.toString() + "1")
        }
    }

//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (MotionEvent.ACTION_DOWN == event.action && null != currentFocus) {
//            val mInputMethodManager =
//                getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
//            return mInputMethodManager.hideSoftInputFromWindow(window.decorView.findFocus()?.windowToken?:window.currentFocus?.windowToken, 0)
//        }
//        return false
//        return super.dispatchTouchEvent(event)
//    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.st1 -> {
               viewDataBinding?.st1?.setBackground(R.color.colorPrimary)
            }
            R.id.st2 -> {
                viewDataBinding?.st2?.setTextColor(textColor = R.color.colorWhite)
            }
            R.id.set -> {
                Toast.makeText(this,"点击回调1111",Toast.LENGTH_SHORT).show()
            }
            R.id.sl4, R.id.st4 -> {
                view.isEnabled = false
            }
            R.id.smartEditText->{
                viewDataBinding?.hint1 =  !(viewDataBinding?.hint1?:false)
                Toast.makeText(this,"点击回调",Toast.LENGTH_SHORT).show()
                viewDataBinding?.set3?.showDropDown()
            }
            R.id.smartEditText1->{
                viewDataBinding?.stroke = if(aaa) 50 else 100
                aaa = !aaa
            }
        }
    }

    var aaa:Boolean = false

}