package com.orange.tpms.ue.kt_frag


import androidx.fragment.app.Fragment
import com.jianzhi.customer_service.beans.getFix
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_language.view.*
import java.util.*
import java.util.logging.Handler

/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Setting_Lan : RootFragement(R.layout.frag_language) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.bt_select_en.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("en"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_cn.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("zh-rCN"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_tr.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("tw"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_ita.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("it"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_deu.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("de"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_sk.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("sk"))
            OgPublic.getInstance.resrart()
        }
        rootview.bt_select_da.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("da"))
            OgPublic.getInstance.resrart()
        }
    }
}
