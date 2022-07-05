package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.ue.kt_frag.*

open class SensorWay : SetupDialog(R.layout.sensor_way_dialog) {
    override fun dismess() {
        if(JzActivity.getControlInstance().getNowPageTag()=="Frag_Program_Detail"){
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.dia_program_info, true, true, "dia_program_info")
        }
    }

    var temppass = ""
    override fun keyevent(event: KeyEvent): Boolean {
        Log.e("Dia", "$event")
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            JzActivity.getControlInstance().closeDiaLog()
        }
        if ((event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == 19 || event.keyCode == 20 || event.keyCode == 21 || event.keyCode == 22) && event.action == KeyEvent.ACTION_UP) {
            if (event.keyCode == 19 || event.keyCode == 20 || event.keyCode == 21 || event.keyCode == 22 || event.keyCode == 66) {
                if (event.keyCode == 19) {
                    temppass = ""
                }
                temppass = temppass + "${event.keyCode}"
                if (temppass == "1920212266") {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    JzActivity.getControlInstance().getRootActivity().startActivity(intent)
                }
            }
        }
        JzActivity.getControlInstance().closeDiaLog("SensorWay")
        return true
    }

        override fun setup(rootview: Dialog) {
            if (JzActivity.getControlInstance().getNowPageTag() == "Frag_Idcopy_Detail") {
                rootview.findViewById<TextView>(R.id.tit).text = JzActivity.getControlInstance()
                    .getRootActivity().resources.jzString(R.string.sensor_ID_number)
            }

        }

    }