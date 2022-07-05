package com.orange.tpms.ue.kt_frag

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.orango.electronic.orange_og_lib.Util.jzString
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.plate_info.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Frag_Plate_Info(var id:String):JzFragement(R.layout.plate_info) {
    val sql:JzSqlHelper get(){return OgPublic.getInstance.sqlite}
    var first=true
    var lastMake=""
    var lasrModel=""
    var lasrYear=""
    override fun viewInit() {
        rootview.fixLanguage()
        sql.exsql("CREATE TABLE if not exists  `vemem`  (\n" +
                "    number     VARCHAR PRIMARY KEY,\n" +
                "    make       VARCHAR,\n" +
                "    model      VARCHAR,\n" +
                "    year       VARCHAR,\n" +
                "    changetime VARCHAR\n" +
                ");\n")
        sql.query("select * from vemem where number='$id'", Sql_Result {
            lastMake=it.getString(1)
            lasrModel=it.getString(2)
            lasrYear=it.getString(3)
            rootview.text5.setText(it.getString(4))
        })
        if(rootview.text5.text.isEmpty()){
            rootview.text5.setText("無")
        }
        GetMake()
        rootview.text1.setText(id)
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
        rootview.setup.setOnClickListener {
            sql.exsql("delete from vemem where number='${rootview.text1.text}'")
            sql.exsql("insert into vemem (number,make,model,year,changetime) values ('${rootview.text1.text}','${rootview.make.selectedItem}','${rootview.model.selectedItem}','${rootview.year.selectedItem}','${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}')")
            JzActivity.getControlInstance().toast(resources!!.jzString(R.string.update_success))
        }
        rootview.button9.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

    }
    val make=ArrayList<String>()
    var model=ArrayList<String>()
    var year=ArrayList<String>()
    fun GetYear(){
        year.clear()
        if(rootview.model.selectedItem==null){return}
        if(rootview.make.selectedItem==null){return}
        year=PublicBean.getYear()
        if(first){
            year.remove(lasrYear)
            year.add(0,lasrYear)
        first=!first}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, year)
        rootview.year.adapter=arrayAdapter
    }

    fun GetModel(){
        model.clear()
        model=PublicBean.getModel();
        if(rootview.make.selectedItem==null){return}
        if(first){
            model.remove(lasrModel)
            model.add(0,lasrModel)}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, model)
        rootview.model.adapter=arrayAdapter
    }

    fun GetMake(){
        make.clear()
        for(i in PublicBean.getMake()){
            make.add(i.make)
        }
        if(first){
            make.remove(lastMake)
            make.add(0,lastMake)}
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.spinner, make )
        rootview.make.adapter=arrayAdapter
    }
}