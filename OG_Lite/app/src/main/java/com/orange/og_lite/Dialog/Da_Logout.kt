package com.orange.og_lite.Dialog

import android.app.Dialog
import android.view.KeyEvent
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.R

class Da_Logout : SetupDialog(R.layout.logout)
{
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
            JzActivity.getControlInstance().setPro("admin", "nodata")
           JzActivity.getControlInstance().closeApp() }

            rootview.findViewById<TextView>(R.id.cancel).setOnClickListener {
                //rootview.dismiss()
                JzActivity.getControlInstance().closeDiaLog() }
    }

}