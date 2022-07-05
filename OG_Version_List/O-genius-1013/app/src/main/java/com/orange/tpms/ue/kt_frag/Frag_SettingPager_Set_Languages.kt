package com.orange.tpms.ue.kt_frag

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__set__languages.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Set_Languages() :
    JzFragement(R.layout.fragment_frag__setting_pager__set__languages) {
    var Arealist = ArrayList<String>()
    var areaposition = 0
    override fun viewInit() {
        rootview.fixLanguage()
        //JzActivity.getControlInstance().getPro("Languages", LOCALE_ENGLISH)
        Arealist.add("jz.440".getFix()) //"Select"
        Arealist.add("jz.441".getFix()) //"EU"
        Arealist.add("jz.442".getFix()) //"North America"
        Arealist.add("jz.443".getFix()) //"台灣"
        Arealist.add("jz.444".getFix()) //"中國大陸"
        Arealist.add("Japan") //"日本"
        val arrayAdapter = ArrayAdapter<String>(
            activity!!,
            R.layout.language_spinner, Arealist
        )
        rootview.Area_spinner.adapter = arrayAdapter
        rootview.Area_spinner.setSelection(
            JzActivity.getControlInstance().getPro("aIndex", 0),
            true
        )
        rootview.Area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                areaposition = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        rootview.Set_up_button.setOnClickListener {
            if (rootview.Area_spinner.selectedItem.toString().equals("Select")
            ) {
                return@setOnClickListener
            }
            JzActivity.getControlInstance().setPro("counrty", arrayOf("EU","EU","US","TW","CN","JP")[rootview.Area_spinner.selectedItemPosition])
            FileDowload.clearInit()
            JzActivity.getControlInstance().goMenu()
        }
    }


}
