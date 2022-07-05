package com.orange.tpms.ue.kt_frag


import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.ShowYear
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__select_year.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_SelectYear : RootFragement(R.layout.fragment_frag__select_year) {

    private var year: ArrayList<String> = ArrayList()
    lateinit var adapter: ShowYear

    override fun viewInit() {
        rootview.fixLanguage()
        year = PublicBean.getYear()
        rootview.rv_year.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        adapter = ShowYear(year)
        rootview.rv_year.adapter = adapter
    }

    override fun onTop() {
        FocusReset(-1)
    }

    override fun onDown() {
        FocusReset(1)
    }

    fun FocusReset(re: Int) {
        if (adapter.focus + re >= 0 && adapter.focus + re < year.size) {
            adapter.focus += re
        }
        rootview.rv_year.scrollToPosition(adapter.focus)
        adapter.notifyDataSetChanged()
    }

    override fun enter() {
        PublicBean.selectMmy.year = year[adapter.focus]
        PublicBean.selectMmy.displayYear = year[adapter.focus]
        PublicBean.setFavorite(PublicBean.selectMmy.make, PublicBean.selectMmy.model, PublicBean.selectMmy.year)
        ChangePageService.changePage()
    }
}
