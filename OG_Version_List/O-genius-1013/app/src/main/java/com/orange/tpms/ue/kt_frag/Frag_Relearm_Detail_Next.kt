package com.orange.tpms.ue.kt_frag


import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__relearm__detail.view.*


class Frag_Relearm_Detail_Next : RootFragement(R.layout.fragment_frag__relearm__detail) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.mmy_text.text="${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        rootview.resetWifi.text="OE Part # :\n${PublicBean.getOePart()}\n\n${"jz.462".getFix()}:\n${PublicBean.getS19()}${if(FileDowload.country=="EU") "" else ("/${PublicBean.getSencsorModel()}")}\n\n${"jz.135".getFix()}:\n${PublicBean.getreLarm()}"
        rootview.imageView11.visibility= View.VISIBLE
        rootview.next.visibility=View.VISIBLE
        rootview.next.setOnClickListener {
            ChangePageService.changePage()
        }
        rootview.prog_bt.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        rootview.next.background=resources!!.getDrawable(R.mipmap.btn_right,null)
        rootview.prog_bt.background=resources!!.getDrawable(R.mipmap.btn_letf,null)
        rootview.openback.visibility=if(PublicBean.openOrClose()) View.VISIBLE else View.GONE
        if(PublicBean.position == PublicBean.OBD_RELEARM){
          when(PublicBean.getRelearmMode()){
              "0"->{
                  rootview.next.visibility=View.GONE
                  rootview.prog_bt.setBackgroundResource(R.color.buttoncolor)
              }
              "1"->{
              }
              "2"->{
              }
              "3"->{
                  rootview.next.setOnClickListener {
                      JzActivity.getControlInstance().changePage(
                              Frag_Check_Sensor_Read(),
                              "Frag_Check_Sensor_Read",
                              true
                          )
                  }
              }
              "6"->{
                  rootview.next.text = "jz.13".getFix()
                  rootview.next.setOnClickListener {
                      if (PublicBean.wheelCount() == 2) {
                          JzActivity.getControlInstance().changePage(
                              Frag_Motor_Select(false),
                              "Frag_Motor_Select",
                              true
                          )
                      } else {
                          JzActivity.getControlInstance().changePage(
                              Frag_New_Idcopy_Selection(),
                              "Frag_Function_ID_Copy_Selection",
                              true
                          )
                      }
                  }
              }
          }
        }
    }

    override fun enter() {
        rootview.next.performClick()
    }
}
