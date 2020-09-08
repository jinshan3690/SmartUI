package com.hpcang.smart.smart.ui.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        when(view.id){
            R.id.sl1 ->{

            }
            R.id.sl2 ->{

            }
            R.id.sl3 ->{

            }
            R.id.sl4,R.id.st4  ->{
                view.isEnabled = false
            }
        }
    }

}