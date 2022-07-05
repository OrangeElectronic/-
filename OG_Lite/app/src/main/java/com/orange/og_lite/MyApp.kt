package com.orange.og_lite

import android.app.Application
import com.orange.og_lite.Util.jzString
import com.orango.electronic.jzutil.JzUtil

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        JzUtil.setUp(applicationContext)
    }
}