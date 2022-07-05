package com.orange.tpms.ue.kt_frag

import android.widget.ArrayAdapter
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.triggerModel
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_tire_hotel_detail.view.*
import java.text.SimpleDateFormat

class Frag_TireHotel_Detail(var model: triggerModel) :
    JzFragement(R.layout.frag_tire_hotel_detail) {

    override fun viewInit() {
        rootview.fixLanguage()
        rootview.sensorId.setText(model.sensorid)
        rootview.pre.setText(model.pre)
        rootview.tem.setText(model.tem)
        rootview.bat.setText(model.bat)
        rootview.location.setText(model.location)
        rootview.deep.setText(model.tire_depth)
        val date=SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s").parse(model.time)
        rootview.text5.text=SimpleDateFormat("yyyy-MM-dd HH:mm").format(date)
        rootview.button9.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
    }

}