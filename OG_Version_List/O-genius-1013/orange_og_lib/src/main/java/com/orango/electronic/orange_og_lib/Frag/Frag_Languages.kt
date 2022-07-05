package com.orango.electronic.orangetxusb.SettingPagr


import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.tool.LanguageUtil.*
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_language.view.*
import java.util.*


class Frag_Languages(var place: Int) : JzFragement(R.layout.frag_language) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.bt_select_en.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("en"))
            change()
        }
        rootview.bt_select_cn.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("zh-rCN") )
            change()
        }
        rootview.bt_select_tr.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("tw"))
            change()
        }
        rootview.bt_select_ita.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("it"))
            change()
        }
        rootview.bt_select_deu.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("de"))
            change()
        }
        rootview.bt_select_sk.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("sk"))
            change()
        }
        rootview.bt_select_da.setOnClickListener {
            JzActivity.getControlInstance().setLanguage(Locale("da"))
            change()
        }
    }

    fun change() {
        if (place == 0) {
            JzActivity.getControlInstance().changePage(FragPolicy(0),  "PrivaryPolicy", false)
        }
    }

}
