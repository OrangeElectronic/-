package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.check_update.*

class Dia_check_update() : SetupDialog(R.layout.check_update) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {
        rootview.content.text = JzActivity.getControlInstance().getPro("Firebasebody", "nodata")
        rootview.cancel.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
        }
        rootview.yes.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance()
                .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        }
        if (JzActivity.getControlInstance().getPro("Firebasebody", "nodata").trim().isEmpty() || JzActivity.getControlInstance().getPro("Firebasebody", "nodata").trim().equals("nodata")
        ) {
            rootview.content.visibility = View.GONE
        }
    }
}