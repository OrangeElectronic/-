package com.orango.electronic.orange_og_lib.Frag


import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__go_reset.view.*


class Frag_GoReset : JzFragement(R.layout.fragment_frag__go_reset) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.button2.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
    }
}
