package com.hagan.traindemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hagan.traindemo.activity.NativeActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 按钮点击获取手机联系人
        bt_to_native.setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }

    }


}
