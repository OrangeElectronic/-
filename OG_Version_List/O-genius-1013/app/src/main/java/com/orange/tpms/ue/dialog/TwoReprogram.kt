package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.PublicBean
import kotlinx.android.synthetic.main.program_false.*
import kotlinx.android.synthetic.main.sensor_way_dialog.yes

class TwoReprogram : SetupDialog(R.layout.reprogram_false)
{
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {
        rootview.yes.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            when(PublicBean.position)
            {
                PublicBean.ID_COPY_OBD ->{JzActivity.getControlInstance().goBack("Frag_Obd")}
                PublicBean.OBD_RELEARM ->{JzActivity.getControlInstance().goBack("Frag_Obd")}
                PublicBean.燒錄傳感器 ->{JzActivity.getControlInstance().goBack("Frag_Program_Sensor")}
                PublicBean.複製傳感器 ->{JzActivity.getControlInstance().goBack("Frag_Id_Copy")}
            }

        }
        rootview.ok.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance().goMenu() }
    }

}