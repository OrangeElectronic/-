package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.ue.tire_hotel.Frag_Hotel_First_Info
import kotlinx.android.synthetic.main.dia_hotel_nodata.*
import kotlinx.android.synthetic.main.hinttag.*

class Dia_Hint(var tit:String) :SetupDialog(R.layout.hinttag){
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
       return true
    }

    override fun setup(rootview: Dialog) {
        rootview.textView35.text = tit
    }

}