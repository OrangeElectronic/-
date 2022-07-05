package com.example.customerlibrary.beans

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
    val robotDB:JzSqlHelper get() {
        return if(nowDbVersion=="nodata") tempDB else  robotOnline
    }
    //asset預設資料庫
    var tempDB=JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"robot.db")
    //網路資料庫
    var robotOnline=JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"robotOnline.db")
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
            if(language.contains("tw")){language="tw"}else{language="zh-rcn"}
            val sql="select  `id`,`${language}` from Ask where type='$type'"
            robotDB.query(sql, Sql_Result {
                arrayitem.add(robotItem(it.getString(0),it.getString(1)))
            })
        }catch (e:Exception){
            e.printStackTrace()
            val sql="select  `id`,`en` from Ask where type='$type'"
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
            val sql="select  `${JzActivity.getControlInstance().getLanguage()}` from Anser where id='$id'"
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




    //------------------------------------------------------------------------多國語言資料庫------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    val languageDB: JzSqlHelper
        get() {
            if(templanDB==null){
                templanDB= JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"templanDB.db")
                templanDB!!.close()
                templanDB!!.init_ByAsset("language.db")
                templanDB!!.create()
                return templanDB!!
            }else{
                return templanDB!!
            }
        }
    var templanDB: JzSqlHelper?=null
    var onlinelanDB: JzSqlHelper = JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"onlinelanDB.db")
    val testLan :Boolean get() {
        return JzActivity.getControlInstance().getPro("testlan",false)
    }
    //----------------------------------------------------------------------------------所有用戶資料庫----------------------------------------------------
    var userInformation:JzSqlHelper =
        JzSqlHelper(JzActivity.getControlInstance().getRootActivity(),"userInformation.db")

    init {
        tempDB.close()
        tempDB.init_ByAsset("customerQustion.db")
        tempDB.create()
        userInformation.exsql(
            "CREATE TABLE   IF NOT EXISTS Bs_Contact (\n" +
                    "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    admin VARCHAR NOT NULL UNIQUE,\n" +
                    "    pick VARCHAR NOT NULL\n," +
                     "needinit INTEGER NOT NULL"+
                    ");\n")

}

}