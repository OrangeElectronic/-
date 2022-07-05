package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.Util.fixLanguage
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.Util.Util_Http_Command_Function
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__set__up__password.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Set_Up_Password : JzFragement(R.layout.fragment_frag__setting_pager__set__up__password) {

    override fun viewInit() {
        rootview.fixLanguage()
        rootview.button13.setOnClickListener {
            if(PublicBeans.admin!=rootview.editText3.text.toString()){
                JzActivity.getControlInstance().toast(resources.jzString(R.string.Incorrect_information))
            }else{
                if(rootview.editText4.text.toString()==rootview.editText5.text.toString()){
                    JzActivity.getControlInstance().showDiaLog(R.layout.data_loading,false,false,"dataloading")
                    Thread{
                        if(Util_Http_Command_Function.resetPassword(rootview.editText4.text.toString())){
                            handler.post {
                                JzActivity.getControlInstance().toast(resources.jzString(R.string.update_success))
                            }
                        }else{
                            handler.post {
                                JzActivity.getControlInstance().toast(resources.jzString(R.string.updatefault))
                            }
                        }
                        handler.post {JzActivity.getControlInstance().closeDiaLog()  }
                    }.start()
                }else{
                    JzActivity.getControlInstance().toast(resources.jzString(R.string.confirm_password))
                }
            }
        }


    }
}
