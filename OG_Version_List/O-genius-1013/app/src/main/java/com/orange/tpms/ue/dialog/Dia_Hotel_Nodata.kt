package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.ue.tire_hotel.Frag_Hotel_First_Info
import kotlinx.android.synthetic.main.dia_hotel_nodata.*

class Dia_Hotel_Nodata :SetupDialog(R.layout.dia_hotel_nodata){
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
       return true
    }

    override fun setup(rootview: Dialog) {
        rootview.textView51.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog("Dia_Hotel_Nodata")
            JzActivity.getControlInstance()
                .changePage(Frag_Hotel_First_Info(), "Frag_Hotel_First_Info", false)
        }
        rootview.textView52.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog("Dia_Hotel_Nodata")
        }
    }

}