package com.orange.tpms.ue.kt_frag

import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.triggerModel
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__function__id__copy__selection.view.*

class Frag_Tire_List(var arrayTire: ArrayList<triggerModel>) :
    JzFragement(R.layout.fragment_frag__function__id__copy__selection) {
    var canrun = true
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.title.setText("${PublicBean.selectMmy.make}/${PublicBean.selectMmy.model}/${PublicBean.selectMmy.year}")
        rootview.textView45.text="jz.474".getFix()
        rootview.lf_img.setOnClickListener {
            val item=arrayTire.filter { it.wheelPosition == "LF" }
            if(item.isEmpty()){
                JzActivity.getControlInstance().toast("jz.473".getFix())
            }else{
                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(item[0]),"Frag_TireHotel_Detail",true)
            }
        }
        rootview.lr_img.setOnClickListener {
            val item=arrayTire.filter { it.wheelPosition == "LR" }
            if(item.isEmpty()){
                JzActivity.getControlInstance().toast("jz.473".getFix())
            }else{
                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(item[0]),"Frag_TireHotel_Detail",true)
            }
        }
        rootview.rr_img.setOnClickListener {
            val item=arrayTire.filter { it.wheelPosition == "RR" }
            if(item.isEmpty()){
                JzActivity.getControlInstance().toast("jz.473".getFix())
            }else{
                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(item[0]),"Frag_TireHotel_Detail",true)
            }
        }
        rootview.rf_img.setOnClickListener {
            val item=arrayTire.filter { it.wheelPosition == "RF" }
            if(item.isEmpty()){
                JzActivity.getControlInstance().toast("jz.473".getFix())
            }else{
                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(item[0]),"Frag_TireHotel_Detail",true)
            }
        }
        rootview.spare_tire_L.setOnClickListener {
            val item=arrayTire.filter { it.wheelPosition == "SP" }
            if(item.isEmpty()){
                JzActivity.getControlInstance().toast("jz.473".getFix())
            }else{
                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(item[0]),"Frag_TireHotel_Detail",true)
            }
        }
        rootview.All_L.visibility = View.GONE
        rootview.two_button.visibility = View.GONE
    }


//    inner class tireadapter : JzAdapter(R.layout.ad_all_tire) {
//        override fun sizeInit(): Int {
//            return arrayTire.size
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.mView.name.text = arrayTire[position].wheelPosition
//            holder.mView.setOnClickListener {
//                JzActivity.getControlInstance().changePage(Frag_TireHotel_Detail(arrayTire[position]),"Frag_TireHotel_Detail",true)
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
    }
}