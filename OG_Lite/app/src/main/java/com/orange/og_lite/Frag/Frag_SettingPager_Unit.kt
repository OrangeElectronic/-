package com.orange.og_lite.Frag

import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement

import com.orange.og_lite.R
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__unit.view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Unit : JzFragement(R.layout.fragment_frag__setting_pager__unit) {

    var Tem = ArrayList<String>()
    var Pre = ArrayList<String>()
    var Numeral = ArrayList<String>()

    override fun viewInit() {
        Tem.clear()
        Pre.clear()
        Numeral.clear()

        Pre.add("jz.377".getFix())
        Pre.add("jz.379".getFix())
        Pre.add("jz.378".getFix())
        val arrayAdapter1 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Pre)
        rootview.tire_spinner.adapter = arrayAdapter1

        Tem.add("jz.375".getFix())
        Tem.add("jz.376".getFix())
        val arrayAdapter2 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Tem)
        rootview.tem_spinner.adapter = arrayAdapter2

        Numeral.add("Auto")
        Numeral.add("Dec")
        Numeral.add("Hex")
        val arrayAdapter3 = ArrayAdapter<String>(activity!!,
            R.layout.area_spinner, Numeral)
        rootview.numeral_spinner.adapter = arrayAdapter3

        rootview.tire_spinner.setSelection(JzActivity.getControlInstance().getPro("Pre",1),true)
        rootview.tem_spinner.setSelection(JzActivity.getControlInstance().getPro("Tem",0),true)

        rootview.enter.setOnClickListener {
            JzActivity.getControlInstance().setPro("Pre",rootview.tire_spinner.selectedItemPosition)
            JzActivity.getControlInstance().setPro("Tem",rootview.tem_spinner.selectedItemPosition)
            JzActivity.getControlInstance().goBack()
        }

    }

}
