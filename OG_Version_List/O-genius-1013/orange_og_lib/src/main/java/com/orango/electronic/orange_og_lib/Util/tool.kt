package com.orango.electronic.orange_og_lib.Util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.orange_og_lib.ScreenReceiver
import java.lang.reflect.Method

object Systool {
    var haveFig=false
    //使用原生Web
    fun hookWebView() {
        if(haveFig){return}
        haveFig=true
        var factoryClass: Class<*>? = null
        try {
            factoryClass = Class.forName("android.webkit.WebViewFactory")
            var getProviderClassMethod: Method? = null
            var sProviderInstance: Any? = null
            if (Build.VERSION.SDK_INT == 23) {
                Log.e("BuildSDK_INT","getProviderClassMethod")
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass")
                if(getProviderClassMethod==null){
                    Log.e("BuildSDK_INT","getProviderClassMethod->null")
                    return }
                getProviderClassMethod.isAccessible = true
                val providerClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
                val delegateClass = Class.forName("android.webkit.WebViewDelegate")
                val constructor = providerClass.getConstructor(delegateClass)
                if (constructor != null) {
                    constructor.isAccessible = true
                    val constructor2 = delegateClass.getDeclaredConstructor()
                    constructor2.isAccessible = true
                    sProviderInstance = constructor.newInstance(constructor2.newInstance())
                }
            } else if (Build.VERSION.SDK_INT == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass")
                getProviderClassMethod.isAccessible = true
                val providerClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
                val delegateClass = Class.forName("android.webkit.WebViewDelegate")
                val constructor = providerClass.getConstructor(delegateClass)
                if (constructor != null) {
                    constructor.isAccessible = true
                    val constructor2 = delegateClass.getDeclaredConstructor()
                    constructor2.isAccessible = true
                    sProviderInstance = constructor.newInstance(constructor2.newInstance())
                }
            } else if (Build.VERSION.SDK_INT == 21) {// Android 21无WebView安全限制
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass")
                getProviderClassMethod.isAccessible = true
                val providerClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
                sProviderInstance = providerClass.newInstance()
            }
            if (sProviderInstance != null) {
                val field = factoryClass.getDeclaredField("sProviderInstance")
                field.isAccessible = true
                field.set("sProviderInstance", sProviderInstance)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //自動關機
    fun screenReceiver(context: Context) {
        val sOnBroadcastReciver = ScreenReceiver()
        val recevierFilter = IntentFilter()
        recevierFilter.addAction(Intent.ACTION_SCREEN_ON)
        recevierFilter.addAction(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(sOnBroadcastReciver, recevierFilter)
    }

    //隱藏上下BAR
    fun setNaVaGation(hide: Boolean) {
        val intent = Intent("hbyapi.intent.action_hide_navigationbar")
        intent.putExtra("hide", hide)
        JzActivity.getControlInstance().getRootActivity().sendBroadcast(intent)
        val intent2 = Intent("hbyapi.intent.action_lock_panelbar")
        intent2.putExtra("state", hide)
        JzActivity.getControlInstance().getRootActivity().sendBroadcast(intent2)
    }

    //打開GPS
    fun openGPS(a: Boolean) {
        Settings.Secure.setLocationProviderEnabled(
            JzActivity.getControlInstance().getRootActivity().contentResolver,
            LocationManager.GPS_PROVIDER,
            a
        )
    }
}