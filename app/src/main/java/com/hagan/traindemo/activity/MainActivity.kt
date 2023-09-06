package com.hagan.traindemo.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hagan.traindemo.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 原生方法获取展示手机联系人
        bt_to_native.setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }
        // web与原生交互获取展示手机联系人
        bt_to_web.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java))
        }

    }


}
