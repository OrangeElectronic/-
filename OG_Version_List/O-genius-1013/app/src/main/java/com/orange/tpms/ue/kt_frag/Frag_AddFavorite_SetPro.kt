package com.orange.tpms.ue.kt_frag


import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_add_favorite.view.*
import kotlin.collections.ArrayList


class   Frag_AddFavorite_SetPro : RootFragement(R.layout.fragment_add_favorite) {
    override fun viewInit() {
        rootview.fixLanguage()
        GetMake()
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
        rootview.button7.setOnClickListener {
            if(rootview.model.selectedItem==null){  return@setOnClickListener}
            if(rootview.make.selectedItem==null){  return@setOnClickListener}
            if(rootview.year.selectedItem==null){ return@setOnClickListener }
            if(!data.contains("${rootview.make.selectedItem}☆${rootview.model.selectedItem}☆${rootview.year.selectedItem}")){ data.add("${rootview.make.selectedItem}☆${rootview.model.selectedItem}☆${rootview.year.selectedItem}")}
            SetFav()
            JzActivity.getControlInstance().goBack()
        }
        rootview.button3.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
    }

    var data= ArrayList<String>()
    fun GetYear(){
        if(rootview.model.selectedItem==null){return}
        if(rootview.make.selectedItem==null){return}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, PublicBean.getYear())
        rootview.year.adapter=arrayAdapter
    }
    fun GetModel(){
        if(rootview.make.selectedItem==null){return}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, PublicBean.getModel())
        rootview.model.adapter=arrayAdapter
    }
    fun GetMake(){
        val make=ArrayList<String>()
        for(i in PublicBean.getMake()){
            make.add(i.make)
        }
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, make )
        rootview.make.adapter=arrayAdapter
    }
    fun SetFav(){
        PublicBean.setFavorite(
            PublicBean.selectMmy.make,
            PublicBean.selectMmy.model,
            PublicBean.selectMmy.year)
    }

}
