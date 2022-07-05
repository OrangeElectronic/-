package com.orange.tpms.ue.kt_frag


import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__relearm__detail.view.*


class Frag_Relearm_Detail(var goBack:Boolean=false) : RootFragement(R.layout.fragment_frag__relearm__detail) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.mmy_text.text="${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        rootview.resetWifi.text="OE Part # :\n${PublicBean.getOePart()}\n\n${"jz.462".getFix()}:\n${PublicBean.getS19()}${if(FileDowload.country=="EU") "" else ("/${PublicBean.getSencsorModel()}")}\n\n${"jz.135".getFix()}:\n${PublicBean.getreLarm()}"
        if(goBack){
            rootview.prog_bt.text = "jz.306".getFix()
        }
        rootview.prog_bt.setOnClickListener {
            if(goBack){
                JzActivity.getControlInstance().goBack()
            }else{
                GoMenu()
            }
             }
    }
}
