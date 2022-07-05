package com.orange.og_lite

import android.util.Log
import com.example.customerlibrary.AdminBeans
import com.example.customerlibrary.ChatPage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.permission_C
import com.orange.og_lite.Page_MainActivity
import com.orange.og_lite.R

class Logo_Page : JzFragement(R.layout.activity_main) {
    override fun viewInit() {
        Thread {
            Thread.sleep(2000)
            while (!(JzActivity.getControlInstance()
                    .getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()
            ) {
                Thread.sleep(1000)
            }
            handler.post {
                requestPermission()
                JzActivity.getControlInstance().setUpActionBar(Page_MainActivity())
                JzActivity.getControlInstance().toggleActionBar(true)
            }
        }.start()

    }

    fun requestPermission() {
        JzActivity.getControlInstance().permissionRequest(arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
            object : permission_C {
                var siz = 0
                override fun requestFalse(a: String) {
                    JzActivity.getControlInstance().toast("必須啟用權限進行檔案存取!")
                    requestPermission()
                }

                override fun requestSuccess(a: String) {
                    siz += 1
                    Log.e("request", a)
                    if (siz >= 2) {

                    }
                }
            }, 10
        )
    }
}