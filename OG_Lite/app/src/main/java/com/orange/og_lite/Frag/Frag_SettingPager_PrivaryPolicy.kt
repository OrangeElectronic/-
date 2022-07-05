package com.orange.og_lite.Frag

import android.view.View
import androidx.fragment.app.Fragment
import com.orange.og_lite.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__privary_policy.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_PrivaryPolicy(val changePlace:Int) : JzFragement(R.layout.fragment_frag__setting_pager__privary_policy) {

//    companion object {
//        var changePlace=0
//    }

    override fun viewInit() {
        rootview.fixLanguage()

        if (changePlace == 0)
        {   rootview.agree.text = "jz.1".getFix()
            rootview.registration.text = "jz.2".getFix()
        }
        else
        {   rootview.agree.text = "jz.9".getFix()
            rootview.agree.setBackgroundResource(R.mipmap.btn_rectangle)
            rootview.registration.visibility = View.GONE}


        rootview.agree.setOnClickListener {
            if (changePlace == 0) {
            JzActivity.getControlInstance().changePage(
                Frag_SettingPager_Sign_in(),
                "Frag_SettingPager_Sign_in", false)
            } else {
            JzActivity.getControlInstance().goMenu()
            }
        }
        rootview.registration.setOnClickListener{
            android.os.Process.killProcess(android.os.Process.myPid())

        }

    }

}
