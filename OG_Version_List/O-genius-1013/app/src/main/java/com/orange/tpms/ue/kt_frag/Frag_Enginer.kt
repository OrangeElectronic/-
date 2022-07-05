package com.orange.tpms.ue.kt_frag


import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__enginer.view.*

class Frag_Enginer : RootFragement(R.layout.fragment_frag__enginer) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.connect.setOnClickListener {
            if(rootview.pass.text.toString()=="orangetpms"){
                JzActivity.getControlInstance().closeApp()
            }else{
                JzActivity.getControlInstance().toast(resources!!.jzString(R.string.errorpass))
            }
        }
    }

}
