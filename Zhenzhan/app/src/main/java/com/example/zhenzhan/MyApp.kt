package com.example.zhenzhan

import android.app.Application
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JsInterFace
import com.orange.ble_plugin.Glitter_BLE

class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        Glitter_BLE(applicationContext).create()
        GlitterActivity.setUp("file:///android_asset/appData",appName = "appData")
    }
}