package com.orange.tpms.ue.kt_frag

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.triggerModel
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_check_mmy.view.*
import kotlinx.android.synthetic.main.frag_check_mmy.view.make
import kotlinx.android.synthetic.main.frag_check_mmy.view.model
import kotlinx.android.synthetic.main.frag_check_mmy.view.year
import kotlinx.android.synthetic.main.plate_info.view.*

class Frag_Check_Mmy(var arrayTire: ArrayList<triggerModel>) :JzFragement(R.layout.frag_check_mmy){
    val make=ArrayList<String>()
    var model=ArrayList<String>()
    var year=ArrayList<String>()
    var first=true
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.setup2.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Tire_List(arrayTire),"Frag_Check_Mmy",true)
        }
        rootview.make.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                PublicBean.selectMmy.make=rootview.make.selectedItem.toString()
                GetModel()
            }

            override  fun onNothingSelected(parent: AdapterView<*>) {
                // TODO Auto-generated method stub
            }
        }
        rootview.model.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                PublicBean.selectMmy.model=rootview.model.selectedItem.toString()
                GetYear()
            }

            override  fun onNothingSelected(parent: AdapterView<*>) {
                // TODO Auto-generated method stub
            }
        }
        rootview.year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                PublicBean.selectMmy.year=rootview.year.selectedItem.toString()
            }

            override  fun onNothingSelected(parent: AdapterView<*>) {
                // TODO Auto-generated method stub
            }
        }
        GetMake()
    }
    fun GetYear(){
        year.clear()
        if(rootview.model.selectedItem==null){return}
        if(rootview.make.selectedItem==null){return}
        year=PublicBean.getYear()
        if(first&&arrayTire.size>0){
            if(year.any { it == arrayTire[0].year }){
            year.remove(arrayTire[0].year)
            year.add(0,arrayTire[0].year)
            }
            first=!first}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, year)
        rootview.year.adapter=arrayAdapter
    }

    fun GetModel(){
        model.clear()
        model=PublicBean.getModel();
        if(rootview.make.selectedItem==null){return}
        if(first&&arrayTire.size>0){
            if(model.any { it == arrayTire[0].model }){
            model.remove(arrayTire[0].model)
            model.add(0,arrayTire[0].model)}}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, model)
        rootview.model.adapter=arrayAdapter
    }

    fun GetMake(){
        make.clear()
        for(i in PublicBean.getMake()){
            make.add(i.make)
        }
        if(first&&arrayTire.size>0){
            if(make.any { it == arrayTire[0].make }){
                make.remove(arrayTire[0].make)
                make.add(0,arrayTire[0].make)
            }
         }
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, make )
        rootview.make.adapter=arrayAdapter
    }
}