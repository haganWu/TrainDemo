package com.hagan.traindemo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.hagan.traindemo.model.ContactModel

object ContactsPhoneUtils {

    fun getContactsList(context: Context): ArrayList<ContactModel> {
        //数据库目标地址
        val contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        //查询条件 - 姓名
        val contactName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        // 查询条件 - 号码
        val contactNumber = ContactsContract.CommonDataKinds.Phone.NUMBER
        var tempContactList: ArrayList<ContactModel> = ArrayList()
        val resolver = context.contentResolver
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
        L.e("ContactsUtils -> getContactsList contactList:${tempContactList}")
        cursor.close()
        return tempContactList;
    }

    fun callTelephone(context: Context, phoneNumber: String) {
        L.e("拨打电话:${phoneNumber}")
        var intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${phoneNumber}")
        context.startActivity(intent)
    }

}