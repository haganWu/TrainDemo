package com.hagan.traindemo.utils

import android.os.Build
import android.util.Log
import com.hagan.traindemo.BuildConfig

object L {

    private const val TAG: String = "hagan"

    fun d(text: String?) {
        if (BuildConfig.DEBUG) {
            text?.let {
                Log.d(TAG, it)
            }
        }
    }

    fun i(text: String?) {
        if (BuildConfig.DEBUG) {
            text?.let {
                Log.i(TAG, it)
            }
        }
    }

    fun e(text: String?) {
        if (BuildConfig.DEBUG) {
            text?.let {
                Log.e(TAG, it)
            }
        }
    }
}