package com.orange.og_lite.Dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.reprogram_false.*

class Dia_Two_Repr : SetupDialog(R.layout.reprogram_false)
{
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {
        rootview.yes.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance().goBack("Frag_Function_Selection")
        }

}}