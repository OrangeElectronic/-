package com.orange.og_lite.adapter

import com.onsemi.protocol.utility.Log
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Frag.Frag_Function_Check_Sensor_Read
import com.orange.og_lite.Frag.Frag_Function_Relearn_Procedure
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import kotlinx.android.synthetic.main.adapter_ad_modle.view.*
import java.util.ArrayList


class Ad_Year(val name: ArrayList<Util_MmySql_module>):JzAdapter(R.layout.adapter_ad_modle)
{
    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.ad_modle_name.text=name[position].year
        holder.mView.ad_modle_button.setOnClickListener {
            PublicBeans.year=name[position].year
            //PublicBeans.model= name[position].modele
             Log.e("publicPosition","${PublicBeans.position}")
            if(PublicBeans.position == PublicBeans.讀取) {
                JzActivity.getControlInstance().changePage(
                    Frag_Function_Check_Sensor_Read(),
                    "Frag_Function_Check_Sensor_Read", true)
            }
            else if(PublicBeans.position == PublicBeans.原廠學碼步驟) {
                JzActivity.getControlInstance().changePage(
                    Frag_Function_Relearn_Procedure(
                        1
                    ),"Frag_Function_Relearn_Procedure",true)
            }
            else
            {
                JzActivity.getControlInstance().changePage(
                    Frag_Function_Relearn_Procedure(
                        0
                    ),"Frag_Function_Relearn_Procedure",true)
            }

            mMainActivity.item_favorite.exsql(
                "CREATE TABLE   IF NOT EXISTS myFavoritetable (\n" +
                        "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    make VARCHAR NOT NULL,\n" +
                        "    model VARCHAR NOT NULL,\n" +
                        "    year VARCHAR NOT NULL\n" +
                        ");\n"
            )
            mMainActivity.item_favorite.exsql("delete from myFavoritetable where make='${PublicBeans.make}' and model='${PublicBeans.model}' and year='${PublicBeans.year}' ")
            //插入資料
            mMainActivity.item_favorite.exsql(
                "insert or replace into myFavoritetable(make,model,year) " +
                        "values ( '" + PublicBeans.make + "','" + PublicBeans.model + "','" + PublicBeans.year + "' )"
            )
        }
    }

    override fun sizeInit(): Int {
        return name.size
    }

}