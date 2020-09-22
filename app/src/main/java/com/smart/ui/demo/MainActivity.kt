package com.smart.ui.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.smart.ui.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var viewDataBinding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewDataBinding?.apply {
            name = "点击右侧ico清空"
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.sl1 -> {
                Toast.makeText(this, viewDataBinding?.name, Toast.LENGTH_SHORT).show()
            }
            R.id.sl2 -> {

            }
            R.id.sl3 -> {

            }
            R.id.sl4, R.id.st4 -> {
                view.isEnabled = false
            }
        }
    }

}