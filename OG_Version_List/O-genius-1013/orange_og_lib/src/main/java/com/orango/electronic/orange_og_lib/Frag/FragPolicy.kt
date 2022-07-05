package com.orango.electronic.orangetxusb.SettingPagr


import android.widget.Button
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orango.electronic.orange_og_lib.Frag.Frag_Sign_in
import com.orango.electronic.orange_og_lib.Frag.Frag_Wifi
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Frag.connectBack
import com.orango.electronic.orange_og_lib.Util.fixLanguage


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class FragPolicy(var place:Int) : JzFragement(R.layout.fragment_privary_policy) {
    override fun viewInit() {
        rootview.fixLanguage()
        (rootview.findViewById(R.id.button5) as Button).setOnClickListener {
            //            act.finish()
        }
        (rootview.findViewById(R.id.button6) as Button).setOnClickListener {
            if(place==0){
                JzActivity.getControlInstance().changePage(Frag_Wifi(
                    object : connectBack {
                        override fun result(a: Boolean) {
                            JzActivity.getControlInstance().changePage(Frag_Sign_in(),  "Frag_Sign_in", true)
                        }
                    }),"Frag_Wifi",false)
            }else{
                JzActivity.getControlInstance().goBack()
            }

        }
    }


}
