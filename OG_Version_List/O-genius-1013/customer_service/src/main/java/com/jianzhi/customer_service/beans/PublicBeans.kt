package com.jianzhi.customer_service.beans

import android.util.Log
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import java.lang.Exception

class PublicBeans {
    companion object{
        var iscustomer=true
        var instanceint: PublicBeans?=null
        val instance: PublicBeans
            get() {
            if(instanceint ==null){
                instanceint =
                    PublicBeans()
            }
            return instanceint!!
        }
    }
    //------------------------------------------------------------------------機器人回答資料庫------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    class robotItem(var id:String,var name:String)
    //asset預設資料庫
    var robotDB=JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"robot.db")
    //現在資料庫版本號
    var nowDbVersion:String
    get() {
        return JzActivity.getControlInstance().getPro("nowDbVersion","nodata")
    }
    set(value) {
        return JzActivity.getControlInstance().setPro("nowDbVersion",value)
    }
    //取得問題
    fun getQustion(type:String):ArrayList<robotItem>{
        val arrayitem=ArrayList<robotItem>()
        try{
            var language=JzActivity.getControlInstance().getLanguage().toString().toLowerCase()
            if(language.contains("tw")){language="tw"}
            val sql="select  id,`${language}` from Ask where type='$type'"
            robotDB.query(sql, Sql_Result {
                arrayitem.add(robotItem(it.getString(0),it.getString(1)))
            })
        }catch (e:Exception){
            e.printStackTrace()
            val sql="select  id,`en` from Ask where type='$type'"
            robotDB.query(sql, Sql_Result {
                arrayitem.add(robotItem(it.getString(0),it.getString(1)))
            })
        }
        return  arrayitem
    }
    //取得回應
    fun getAnser(id:String):String{
        var arrayitem=""
        try{
            val sql="select  `${JzActivity.getControlInstance().getLanguage()}` from Anser where id=$id"
            robotDB.query(sql, Sql_Result {
                arrayitem=it.getString(0)
            })
        }catch (e:Exception){
            e.printStackTrace()
            val sql="select  `en`  from Anser where id='$id'"
            robotDB.query(sql, Sql_Result {
                arrayitem=it.getString(0)
            })
        }
        return  arrayitem
    }




}
fun String.getFix(): String {
    var b = this
    if (!b.contains("jz.")) {
        return b
    }
    b = b.replace("jz.", "")
    return loadNew(b)
}
fun loadNew(b: String): String {
    var b = b
    try {
        PublicBeans.instance.robotDB.query(
            "select `${JzActivity.getControlInstance().getLanguage()}` from language  where id=$b"
        ) {
            b = it.getString(0)
        }
        return b
    } catch (e: Exception) {
        try {
            PublicBeans.instance.robotDB.query(
                "select `en` from language where id=$b"
            ) {
                b = it.getString(0)
            }
            Log.e("leakLanguage", "" + JzActivity.getControlInstance().getLanguage())
            return b
        } catch (e: Exception) {
            return b
        }
    }
}