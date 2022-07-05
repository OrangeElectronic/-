package com.orange.tpms.ue.kt_frag


import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__go_reset.view.*


class Frag_GoReset : RootFragement(R.layout.fragment_frag__go_reset) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.button2.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
    }
}
