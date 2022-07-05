package com.orango.electronic.orange_og_lib.Dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.errorsensor.*

class single_program(var i:Int):SetupDialog(R.layout.errorsensor) {
    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {
        OgPublic.getInstance.playBeepSoundAndVibrate()
        rootview.textView4.text="jz.45$i".getFix()
    }

    override fun dismess() {
    }

}