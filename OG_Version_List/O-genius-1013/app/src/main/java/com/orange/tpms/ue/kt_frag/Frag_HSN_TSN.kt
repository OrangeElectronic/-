package com.orange.tpms.ue.kt_frag

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.frag_hsn_tsn.view.*

class Frag_HSN_TSN : JzFragement(R.layout.frag_hsn_tsn) {

    override fun viewInit() {
        rootview.sn_setup.setOnClickListener {
            JzActivity.getControlInstance().hideKeyBoard()
            if (rootview.hsnt.text.isEmpty() || rootview.tsnt.text.isEmpty()) {
                JzActivity.getControlInstance().toast("jz.476".getFix())
                return@setOnClickListener
            } else {
                val mmyList = PublicBean.getMmyByHsn(
                    rootview.hsnt.text.toString(),
                    rootview.tsnt.text.toString().toUpperCase()
                )
                if (mmyList.isEmpty()) {
                    JzActivity.getControlInstance().toast("jz.477".getFix())
                    return@setOnClickListener
                } else {
                    PublicBean.selectMmy.hsn=rootview.hsnt.text.toString()
                    PublicBean.selectMmy.tsn=rootview.tsnt.text.toString()
                    if (mmyList.size == 1) {
                        PublicBean.selectMmy.make = mmyList[0].make
                        PublicBean.selectMmy.model = mmyList[0].model
                        PublicBean.selectMmy.year = mmyList[0].year
                        PublicBean.selectMmy.displayMake=mmyList[0].displayMake
                        PublicBean.selectMmy.displayModel=mmyList[0].displayModel
                        PublicBean.selectMmy.displayYear=mmyList[0].displayYear
                        if(mmyList[0].dir=="INDIRECT"){
                            JzActivity.getControlInstance().changePage(
                                Frag_Relearm_Detail(),
                                "Frag_Relearm_Detail",
                                true
                            )
                        }else{
                            JzActivity.getControlInstance().changePage(Frag_Confirm_Hsn(),"Frag_Confirm_Hsn",true)
                           // ChangePageService.changePage()
                        }

                    }else{
                        JzActivity.getControlInstance().changePage(Frag_Check_Hsn(mmyList),"Frag_Check_Hsn",true)
                    }
                }
            }
        }
    }
}