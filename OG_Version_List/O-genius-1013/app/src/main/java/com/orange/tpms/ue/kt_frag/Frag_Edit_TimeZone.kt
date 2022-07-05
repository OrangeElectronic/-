package com.orange.tpms.ue.kt_frag

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__auto__lock.view.*
import java.util.*
import kotlin.collections.ArrayList

class Frag_Edit_TimeZone:JzFragement(R.layout.fragment_frag__auto__lock) {
    var listMap:MutableMap<String,String> = mutableMapOf()
    var list= ArrayList<String>()
    override fun viewInit() {
        OgPublic.getInstance.onlinelanDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}`,`value` from timeZone  ",
            Sql_Result {
                listMap[it.getString(0)] = it.getString(1)
                list.add(it.getString(0))
            })
        rootview.textView18.text = "jz.510".getFix()
        rootview.fixLanguage()
        val lanAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, list)
        rootview.spinner.adapter=lanAdapter
        Log.e("timeZone", Gson().toJson(TimeZone.getAvailableIDs()))
        rootview.setup.setOnClickListener {
            val am = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.setTimeZone(listMap[list[rootview.spinner.selectedItemPosition]]);
            JzActivity.getControlInstance().goBack()
        }
    }
}