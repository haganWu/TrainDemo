package com.hagan.traindemo.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hagan.lib_base.base.adapter.CommonAdapter
import com.hagan.lib_base.base.adapter.CommonViewHolder
import com.hagan.traindemo.R
import com.hagan.traindemo.model.ContactModel
import com.hagan.traindemo.utils.ContactsPhoneUtils
import com.hagan.traindemo.utils.L
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_native.*
import kotlinx.android.synthetic.main.custom_title_bar.*

class NativeActivity : AppCompatActivity() {


    //数据库目标地址
    private val contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    //查询条件 姓名 - 号码
    private val contactName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    private val contactNumber = ContactsContract.CommonDataKinds.Phone.NUMBER


    //所有联系人列表
    var mContactList: ArrayList<ContactModel> = ArrayList()
    private lateinit var adapter: CommonAdapter<ContactModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)
        // 初始化展示列表
        initRecyclerView()
        titleTextView.text = "Native获取联系人"
        btnBack.setOnClickListener {
            finish()
        }
        // 按钮点击获取手机联系人
        bt_get_contacts.setOnClickListener {
            val permissions = arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
            )
            if (checkPermission(permissions)) {
                loadContact()
            } else {
                requestPermission(permissions) { loadContact() }
            }
        }

    }

    /**
     * 检查权限
     */
    private fun checkPermission(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 请求权限
     */
    private fun requestPermission(permission: Array<String>, granted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this).runtime().permission(permission)
                .onGranted(Action { granted() }).start()
        }

    }

    private fun loadContact() {
        mContactList.clear()
        mContactList.addAll(ContactsPhoneUtils.getContactsList(this))
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        rv_contacts.layoutManager = LinearLayoutManager(this)
        adapter = CommonAdapter(
            mContactList,
            object : CommonAdapter.OnBindDataListener<ContactModel> {
                override fun onBindViewHolder(
                    mode: ContactModel,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    viewHolder.getView(R.id.tv_call).setOnClickListener {
                        ContactsPhoneUtils.callTelephone(this@NativeActivity, mode.contactNumber)
                    }
                    viewHolder.setText(R.id.tv_name, mode.contactName)
                    viewHolder.setText(R.id.tv_number, mode.contactNumber)
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.layout_contacts_item
                }

            })
        rv_contacts.adapter = adapter
    }
}