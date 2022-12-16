package com.hagan.traindemo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hagan.lib_base.base.adapter.CommonAdapter
import com.hagan.lib_base.base.adapter.CommonViewHolder
import com.hagan.traindemo.model.ContactModel
import com.hagan.traindemo.utils.L
import com.hagan.traindemo.utils.NumberUtils
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


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
        setContentView(R.layout.activity_main)
        // 初始化展示列表
        initRecyclerView()
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

    private fun requestPermission(permission: Array<String>, granted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this).runtime().permission(permission)
                .onGranted(Action { granted() }).start()
        }

    }

    private fun loadContact() {
        L.e("MainActivity -> loadContact 11")
        var tempContactList: ArrayList<ContactModel> = ArrayList()
        val resolver = contentResolver
        val cursor = resolver.query(
            contactUri,
            arrayOf(contactName, contactNumber),
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {

                val data = ContactModel(
                    it.getString(it.getColumnIndex(contactName)), NumberUtils.dealWithPhoneNumber(
                        it.getString(
                            it.getColumnIndex(
                                contactNumber
                            )
                        )
                    )
                )
                tempContactList.add(data)
            }
        }
        L.e("MainActivity -> loadContact contactList:${tempContactList}")
        cursor.close()
        mContactList.clear()
        mContactList.addAll(tempContactList)
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommonAdapter(
            mContactList,
            object : CommonAdapter.OnBindDataListener<ContactModel> {
                override fun onBindViewHolder(
                    mode: ContactModel,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    viewHolder.getView(R.id.ll_item).setOnClickListener {
                        L.e("拨打电话:${mode.contactNumber}")
                        var intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:${mode.contactNumber}")
                        startActivity(intent)
                    }
                    viewHolder.setText(R.id.tv_name, mode.contactName)
                    viewHolder.setText(R.id.tv_number, mode.contactNumber)
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.layout_contacts_item
                }

            })
        mRecyclerView.adapter = adapter
    }
}
