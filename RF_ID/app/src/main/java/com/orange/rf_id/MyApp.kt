package com.orange.rf_id

import android.app.Application
import android.util.Log
import android.webkit.JavascriptInterface
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JsInterFace
import android.zyapi.CommonApi
import com.orango.electronic.orange_og_lib.hardware.HardwareApp

class MyApp : Application() {
    override fun onCreate() {
        GlitterActivity.addJsInterFace(arrayOf(JsInterFace(HardwareController(), "HardwareController")))
        GlitterActivity.setUp("file:///android_asset/appData", appName = "appData")
        super.onCreate()
    }
    inner class HardwareController {
        @JavascriptInterface
        fun start() {
            Log.e("HardwareController","start")
            HardwareApp.getInstance()
                .setApplication(this@MyApp)       //设置Application
                .setEnableHareware(true)   //是否开启硬件功能，可用于关闭调试UI
                .onCreate()
        }
        @JavascriptInterface
        fun open9V(a:Boolean){
            HardwareApp.getInstance().open9V(a)
        }
        @JavascriptInterface
        fun open5V(a:Boolean){
            HardwareApp.getInstance().open5V(a)
        }
        @JavascriptInterface
        fun setGpio1(a:Boolean){
            HardwareApp.getInstance().setGpio1V(a)
        }
        @JavascriptInterface
        fun getGpio(a:Int):Int{
           return HardwareApp.getCommonApi().getGpioIn(a)
        }
        @JavascriptInterface
        fun setGpio(a:Int,b:Int){
            HardwareApp.getCommonApi().setGpioMode(a, b);
            HardwareApp.getCommonApi().setGpioDir(a, b);
            HardwareApp.getCommonApi().setGpioOut(a, b);
        }
    }
}

