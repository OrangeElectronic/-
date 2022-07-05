package com.orange.og_lite.Frag

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.orange.og_lite.Util.jzString
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.Page_MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__function__relearn__procedure.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Relearn_Procedure(val changeFinal: Int) :
    JzFragement(R.layout.fragment_frag__function__relearn__procedure) {

    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    var text_content_String: String = ""

    override fun viewInit() {
        rootview.fixLanguage()
        rootview.Read_MMY_Title.text =
            PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        query_Relearn_Procedure()

        if (changeFinal == 0) {
            rootview.next.setOnClickListener {
                Log.e("changePlace", PublicBeans.position.toString())
                PublicBeans.changeSwitch()
            }

            if(PublicBeans.position == PublicBeans.OBD學碼){
                when(PublicBeans.getRelearmMode()){
                    "0"->{
                        rootview.next.visibility=View.GONE
                        rootview.menu.setBackgroundResource(R.mipmap.btn_rectangle)
                        //rootview.prog_bt.setBackgroundResource(R.color.buttoncolor)
                    }
                    "1"->{

                    }
                    "2"->{

                    }
                    "3"->{
                        rootview.next.setOnClickListener {
                            JzActivity.getControlInstance().changePage(
                                Frag_Function_Check_Sensor_Read(),
                               "Frag_Function_Check_Sensor_Read", true
                            )
                        }
                    }
                }
            }
        } else {
            Log.e("else","elseInit")
            rootview.next.visibility = View.GONE
            rootview.menu.setBackgroundResource(R.mipmap.btn_rectangle)
        }
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

    }

    fun query_Relearn_Procedure() {
        val a = JzActivity.getControlInstance().getLanguage()
        var colname = "`Relearn Procedure (English)`"
        when (a) {
            Locale("tw") -> {
                colname = "`Relearn Procedure (Traditional Chinese)`"
            }
            Locale("zh-rCN") -> {
                colname = "`Relearn Procedure (Jane)`"
            }
            Locale("de")  -> {
                colname = "`Relearn Procedure (German)`"
            }
            Locale("en")  -> {
                colname = "`Relearn Procedure (English)`"
            }
            Locale("it")  -> {
                colname = "`Relearn Procedure (Italian)`"
            }
        }

        mMainActivity.item
            .query(
                "select distinct " + colname + " from `Summary table` where `Make` = '" + PublicBeans.make + "'" + "and `Model` = '" + PublicBeans.model + "'" + "and `Year` = '" + PublicBeans.year + "' limit 0,1",
                Sql_Result { result ->
                    text_content_String = result.getString(0)
                    rootview.text_content.text =
                        "OE Part # :\n${PublicBeans.getOePart()}\n\nFor OrangeSensor:\n${PublicBeans.SencsorModel()}\n\nRelearn:\n${text_content_String}"
                })
    }

}
