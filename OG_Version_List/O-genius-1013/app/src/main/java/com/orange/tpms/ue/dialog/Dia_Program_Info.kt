package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
//
class Dia_Program_Info:SetupDialog(R.layout.dia_program_info){
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
     return true
    }

    override fun setup(rootview: Dialog) {

    }

}