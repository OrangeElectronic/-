package com.orange.tpms.ue.kt_frag

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.RequestFun
import com.orange.tpms.service.ChangePageService
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_confirm_hsn.*
import kotlinx.android.synthetic.main.frag_confirm_hsn.view.*

class Frag_Confirm_Hsn:JzFragement(R.layout.frag_confirm_hsn) {
    override fun viewInit() {
        rootview.make.text = PublicBean.selectMmy.displayMake
        rootview.model.text = PublicBean.selectMmy.displayModel
        rootview.year.text = PublicBean.selectMmy.displayYear
        rootview.imageView50.setImageResource(
            resources.getIdentifier(
            PublicBean.selectMmy.mmyData["Make_Img"].toString(),
            "mipmap",
            JzActivity.getControlInstance().getRootActivity().packageName
        ))
        rootview.textView77.setOnClickListener {
            Thread{
                MysqDatabase.insertSql("""insert into `orange_userdata`.`hsn_tsn_confirm` (make,model,year,hsn,tsn,confirm)
                values ('${PublicBean.selectMmy.make}','${PublicBean.selectMmy.model}','${PublicBean.selectMmy.year}','
                ${PublicBean.selectMmy.hsn}','${PublicBean.selectMmy.tsn}',1)    
                """)
            }.start()
            ChangePageService.changePage()
        }
        rootview.textView75.setOnClickListener {
            Thread{
                MysqDatabase.insertSql("""insert into `orange_userdata`.`hsn_tsn_confirm` (make,model,year,hsn,tsn,confirm)
                values ('${PublicBean.selectMmy.make}','${PublicBean.selectMmy.model}','${PublicBean.selectMmy.year}','
                ${PublicBean.selectMmy.hsn}','${PublicBean.selectMmy.tsn}',0)    
                """)
            }.start()
            JzActivity.getControlInstance().changePage(Frag_SelectMake(), "Frag_SelectMake", true)
        }
        rootview.fixLanguage()
    }
}