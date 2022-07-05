package com.orango.electronic.orange_og_lib.Util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.FileDowload.clearInit

object  keyEventLinstener{
    var temppass = ""
    fun keyEventListener(event: KeyEvent,acr:Activity):Boolean{
        if(isSoftShowing(JzActivity.getControlInstance().getRootActivity())){
            return true
        }
        Log.e("event", "" + event)
        if (event.action == KeyEvent.ACTION_UP) {//只处理按下的动画,抬起的动作忽略
            if(event.keyCode == 19||event.keyCode == 20||event.keyCode == 21||event.keyCode == 22||event.keyCode == 66||event.keyCode == 19||event.keyCode == 8||event.keyCode == 9||event.keyCode == 10
                ||event.keyCode == 11||event.keyCode == 12||event.keyCode == 13||event.keyCode == 66){
                Log.v("yhd-", "event:${event.keyCode}")
                if (event.keyCode == 19) {
                    temppass = ""
                }
                temppass += "${event.keyCode}"
                if (temppass == "1920212266") {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    acr.startActivity(intent)
                } else if (temppass == "1920202121222266") {
                    JzActivity.getControlInstance().setPro("beta",!OgPublic.beta)
                    clearInit()
                    JzActivity.getControlInstance()
                        .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
                }else if(temppass == "19891011121366"){
                    JzActivity.getControlInstance().setPro("testlan",!OgPublic.getInstance.testLan)
                }
                if (temppass.length > 50) {
                    temppass = ""
                }
                return false
            }

                return true
        } else if ((event.action == KeyEvent.ACTION_DOWN)) {
            return !(event.keyCode == 19 || event.keyCode == 20 || event.keyCode == 21 || event.keyCode == 22 || event.keyCode == 66)
        }
        return false
    }
    fun isSoftShowing(activity: Activity): Boolean {
        //获取当屏幕内容的高度
        val screenHeight = activity.window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        //DecorView即为activity的顶级view
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom
    }
}