package com.orange.tpms.ue.kt_frag


import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.ShowItemImage
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__select_make.view.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_SelectMake : RootFragement(R.layout.fragment_frag__select_make) {
    override fun viewInit() {
        rootview.fixLanguage()
        carLogoAdapter = ShowItemImage(make)
        rootview.rv_makes.layoutManager= androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        rootview.rv_makes.adapter= carLogoAdapter
    }

    private var make: ArrayList<Util_MmySql_module> = PublicBean.getMake()
    lateinit var carLogoAdapter:ShowItemImage

    override fun onLeft() {
        FocusReset(-1)
    }
    override fun onRight() {
        FocusReset(1)
    }
    override fun onTop() {
        FocusReset(-2)
    }
    override fun onDown() {
        FocusReset(2)
    }
    fun FocusReset(re:Int){
        if(carLogoAdapter.focus+re>=0&&carLogoAdapter.focus+re<make.size){
            carLogoAdapter.focus+=re
        }
        rootview.rv_makes.scrollToPosition(carLogoAdapter.focus)
        carLogoAdapter.notifyDataSetChanged()
    }

    override fun enter() {
        PublicBean.selectMmy.make = make[carLogoAdapter.focus].make
        PublicBean.selectMmy.displayMake= make[carLogoAdapter.focus].make
        JzActivity.getControlInstance().changePage(Frag_SelectModle(), "Frag_car_model", true)
    }
}
