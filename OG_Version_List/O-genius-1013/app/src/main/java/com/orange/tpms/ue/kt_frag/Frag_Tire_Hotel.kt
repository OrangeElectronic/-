package com.orange.tpms.ue.kt_frag

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.Api_Tire_Hotel
import com.orange.tpms.R
import com.orange.tpms.requestBack
import com.orange.tpms.triggerModel
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.frag_tire_hotel.view.*

class Frag_Tire_Hotel :JzFragement(R.layout.frag_tire_hotel){
    override fun viewInit() {
        rootview.fixLanguage()
      rootview.next.setOnClickListener {
          if(rootview.platetext.text.isEmpty()){return@setOnClickListener}
          dataRequest(rootview.platetext.text.toString())
      }
    }
    fun dataRequest( plate:String) {

        JzActivity.getControlInstance()
            .showDiaLog(R.layout.data_loading, false, false, "data_loading")
        Api_Tire_Hotel.request(plate, object : requestBack {
            override fun reback(a: Any) {
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().closeDiaLog("data_loading")
                    if (a == false) {
                        JzActivity.getControlInstance().toast("jz.210".getFix())
                    } else {
                        JzActivity.getControlInstance().changePage(Frag_Check_Mmy(a as ArrayList<triggerModel>),"Frag_Check_Mmy",true)
                    }
                }
            }
        })
    }
}