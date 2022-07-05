package com.orango.electronic.orange_og_lib.Dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orango.electronic.orange_og_lib.Frag.Frag_Wifi
import com.orango.electronic.orange_og_lib.Frag.connectBack
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.WifiConnector.WifiUtils
import kotlinx.android.synthetic.main.dia_check_wifi.*

class Dia_Check_WiFi : SetupDialog(R.layout.dia_check_wifi) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        rootview.resetWifi.setOnClickListener {
            WifiUtils.getInstance(JzActivity.getControlInstance().getRootActivity()).closeWifi()
            JzActivity.getControlInstance().changePage(Frag_Wifi(object : connectBack {
                override fun result(a: Boolean) {
                    if (a) {
                        JzActivity.getControlInstance().goBack()
                    }
                }
            }), "Frag_Wifi", true)
        }
        rootview.cancel.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
        }
        rootview.retry.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance()
                .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        }
    }
}