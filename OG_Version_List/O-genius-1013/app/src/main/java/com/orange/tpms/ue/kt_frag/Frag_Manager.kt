package com.orange.tpms.ue.kt_frag

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.HttpCommand.Fuction
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.signInBack
import com.orango.electronic.orangetxusb.SettingPagr.Frag_Languages
import kotlinx.android.synthetic.main.activity_kt.view.*

class Frag_Manager : JzFragement(R.layout.activity_kt) {
    lateinit var a: TextView
    lateinit var tit: TextView
    lateinit var titlebar: RelativeLayout
    lateinit var back: ImageView
    lateinit var logout: ImageView
    override fun viewInit() {
        rootview.fixLanguage()
        tit = rootview.findViewById(R.id.textView12)
        back = rootview.findViewById(R.id.back)
        logout = rootview.findViewById(R.id.logout)
        titlebar = rootview.findViewById(R.id.toolbar)
        back.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
        logout.setOnClickListener {
        }
        rootview.customer.setOnClickListener {
            JzActivity.getControlInstance()
                .changePage(Frag_Customer_Service(), "Frag_Customer_Service", true)
        }
        JzActivity.getControlInstance().setPro("countrySelect","nodata")
        if (OgPublic.admin == "nodata" || OgPublic.admin.isEmpty()) {
            OgPublic.fragid = R.id.frage
            OgPublic.signSuccess = object : signInBack {
                override fun result(a: Boolean) {
                    handler.post {
                            JzActivity.getControlInstance().setHome(Frag_home(), "Frag_home")
                    }
                    Thread { Fuction.AddIfNotValid("SP:" + OgPublic.deviceID, "OGenius") }.start()
                }
            }
            JzActivity.getControlInstance().changePage(Frag_Languages(0), "Frag_Languages", false)
        } else {
            handler.post {
                    JzActivity.getControlInstance().setHome(Frag_home(), "Frag_home")
            }
        }
    }
}