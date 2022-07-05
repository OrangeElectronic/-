package com.orango.electronic.orange_og_lib.Dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.R

class NeedOpen :SetupDialog(R.layout.need_open){
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
      return false
    }

    override fun setup(rootview: Dialog) {

    }
}