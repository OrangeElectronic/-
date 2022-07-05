package com.orange.tpms.ue.kt_frag


import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orangetxusb.Adapter.ShowModel
import kotlinx.android.synthetic.main.fragment_frag__select_modle.view.*
import kotlinx.android.synthetic.main.plate_info.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_SelectModle : RootFragement(R.layout.fragment_frag__select_modle) {
    override fun viewInit() {
        rootview.fixLanguage()
        modle= PublicBean.getModel()
        adapter=ShowModel(modle)
        rootview.rv_model.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(activity)
        rootview.rv_model.adapter=adapter
    }

    private var modle: ArrayList<String> = ArrayList()
    lateinit var adapter:ShowModel


    override fun onTop() {
        FocusReset(-1)
    }
    override fun onDown() {
        FocusReset(1)
    }
    fun FocusReset(re:Int){
        if(adapter.focus+re>=0&&adapter.focus+re<modle.size){
            adapter.focus+=re
        }
        rootview.rv_model.scrollToPosition(adapter.focus)
        adapter.notifyDataSetChanged()
    }

    override fun enter() {
        PublicBean.selectMmy.model = modle.get(adapter.focus)
        PublicBean.selectMmy.displayModel=modle[adapter.focus]
        JzActivity.getControlInstance().changePage(Frag_SelectYear(),"Frag_SelectYear",true)
    }

}
