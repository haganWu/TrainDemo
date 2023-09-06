package com.hagan.traindemo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hagan.traindemo.R
import com.hagan.traindemo.constants.KtConstants
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.custom_title_bar.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()
    }

    private fun init() {
        titleTextView.text = "Web获取联系人"
        btnBack.setOnClickListener {
            finish()
        }
        web_view.loadUrl(KtConstants.WEB_URL)
    }


}