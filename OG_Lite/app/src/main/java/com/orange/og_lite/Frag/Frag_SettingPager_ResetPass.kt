package com.orange.og_lite.Frag

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orange.og_lite.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_Http_Command_Function.ResetPassword
import com.orange.og_lite.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__reset_pass.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_ResetPass : JzFragement(R.layout.fragment_frag__setting_pager__reset_pass) {

    override fun viewInit() {
        rootview.fixLanguage()
        var run=false
        var change_text = false

        rootview.send_E_mail.setOnClickListener {
            if(change_text == true)
            {
                JzActivity.getControlInstance().changePage(
                    Frag_SettingPager_Sign_in(),
                   "Frag_SettingPager_Sign_in",false)
            }

            if(run){
            return@setOnClickListener
            }

            JzActivity.getControlInstance().showDiaLog(R.layout.data_loading,false,false,"dataloading")
            var email=rootview.E_mail.text.toString()
            Thread{
                var isok= ResetPassword(email)
                handler.post {
                    run=false
                    JzActivity.getControlInstance().closeDiaLog()
                    if(isok){
                        change_text = true

                        rootview.send_E_mail.text = resources.jzString(R.string.Sign_in)
                        rootview.mail_content.text = resources.jzString(R.string.Reset_Email)
                        rootview.E_mail.visibility = View.GONE

                    }else{
                        Toast.makeText(act,  resources.jzString(R.string.nointernet), Toast.LENGTH_SHORT).show()
                    }


                }
            }.start() }

    }
}
