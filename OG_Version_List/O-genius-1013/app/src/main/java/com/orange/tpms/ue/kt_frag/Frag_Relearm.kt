package com.orange.tpms.ue.kt_frag


import android.widget.RelativeLayout
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__relearm.view.*

class Frag_Relearm : RootFragement(R.layout.fragment_frag__relearm) {
    var position = 0
    var btn = ArrayList<RelativeLayout>()
    override fun viewInit() {
        rootview.fixLanguage()
        btn.add(rootview.b2)
        btn.add(rootview.b1)
        btn.add(rootview.b3)
        rootview.b1.setOnClickListener {
            PublicBean.ScanType = PublicBean.掃描Mmy
            JzActivity.getControlInstance().changePage(Frag_Scan(), "Frag_Scan", true)
        }
        rootview.b2.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_SelectMake(), "Frag_SelectMake", true)
        }
        rootview.b3.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Favorite(), "Frag_Favorite", true)
        }
        rootview.b1.isSelected = true
    }


    override fun onTop() {
        ChangePosition(-1)
    }

    override fun onDown() {
        ChangePosition(1)
    }

    fun ChangePosition(a: Int) {
        for (i in btn) {
            i.isSelected = false
        }
        if (position + a in 0..2) {
            position += a
        }
        btn[position].isSelected = true
    }

    override fun enter() {
        btn[position].performClick()
    }

}
