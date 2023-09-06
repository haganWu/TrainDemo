package com.hagan.traindemo.jsInterface

import android.app.AlertDialog
import android.content.Context
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.hagan.traindemo.model.ContactModel
import com.hagan.traindemo.utils.ContactsPhoneUtils
import com.hagan.traindemo.utils.L
import com.hagan.traindemo.views.X5WebView

class MyJavaScriptInterface(private val mContext: Context, private val mWebView: X5WebView) {
    /**
     * @author WuHaiheng
     * @date 2023-09-06 13:56
     * @description window.AndroidJSBridge.androidTestFunction1(' xxxx ') 调用该方法，APP 会弹出一个 Alert 对话框，
     * 对话框中的内容为 JavaScript 传入的字符串@param str android 只能接收基本数据类型参数 ，不能接收引用类型的数据（Object、Array）。
     * JSON.stringify(Object) -> String
     */
    @JavascriptInterface
    fun androidTestFunction1(str: String?) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage(str)
        builder.setNegativeButton("确定-Native", null)
        builder.create().show()
    }

    /**
     * @return 返回值的内容为："androidTestFunction2方法的返回值"
     * @author WuHaiheng
     * @date 2023-09-06 13:57
     * @description 调用该方法，方法会返回一个返回值给 javaScript 端
     */
    @JavascriptInterface
    fun androidTestFunction2(): String {
        return "androidTestFunction2方法的返回值"
    }

    /**
     * @author HaganWu
     * @date 2023-09-06 13:54
     * @description 调用本地方法获取联系人
     */
    @JavascriptInterface
    fun androidNativeGetContacts(s: String?): String {
        L.e("MyJavaScriptInterface - 前端传递s:$s")
        val contactsList: ArrayList<ContactModel> = ContactsPhoneUtils.getContactsList(mContext)
        L.e("MyJavaScriptInterface - 获取联系人列表:$contactsList")
        val gson = Gson()
        val jsonArray = gson.toJson(contactsList)
        L.e("MyJavaScriptInterface - 获取联系人列表 Json:$jsonArray")
        return jsonArray
    }

    /**
     * @author WuHaiheng
     * @date 2023-09-06 15:52
     * @description 调用本地方法拨打电话
     */
    @JavascriptInterface
    fun androidNativeCallPhone(phoneNumber: String) {
        L.e("MyJavaScriptInterface - 前端调用拨打电话:$phoneNumber")
        ContactsPhoneUtils.callTelephone(mContext, phoneNumber)
    }
}