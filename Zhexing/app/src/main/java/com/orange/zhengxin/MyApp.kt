package com.orange.zhengxin

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.GlitterActivity.Companion.instance
import com.jianzhi.glitter.JavaScriptInterFace
import com.orange.ble_plugin.Glitter_BLE


class MyApp : Application() {
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        //跨平台開發套件
        Glitter_BLE(applicationContext, scanTiming = 0.5).create()
        GlitterActivity.setUp("file:///android_asset/appData", appName = "appData")
        JavaScriptInterFace("getDeviceName") {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val name = if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else capitalize(manufacturer) + " " + model
            it.responseValue["result"] = name
            it.finish()
        }
        JavaScriptInterFace("openQrScanner") {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val intent = Intent(instance(), ScannerActivity::class.java)
                GlitterActivity.instance().startActivity(intent)
            }
        }
        super.onCreate()
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }


}
