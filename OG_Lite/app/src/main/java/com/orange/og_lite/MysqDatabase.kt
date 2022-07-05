package com.orange.og_lite

import android.location.Location
import com.onsemi.protocol.utility.Log
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.PublicBeans.Companion.MCU_NUMBER
import com.orange.og_lite.beans.PublicBeans.Companion.bleVersion
import com.orange.og_lite.beans.PublicBeans.Companion.versionCodes
import com.orange.og_lite.beans.SensorData
import com.orango.electronic.jzutil.*
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*


object MysqDatabase {
    data class ErrorMessageJson(
        var errorTitle: String,
        var mmy: String,
        var directFit: String,
        var account: String,
        var errorType: String,
        var tx: String,
        var time: String,
        var harwareVersion:String= PublicBeans.hardWareVersion,
        var softVersion:String=versionCodes.toString(),
        var fwVersion:String=PublicBeans.bleVersion,
        var bleVersion:String=PublicBeans.bleVersion
    )
    var ip = "https://bento2.orange-electronic.com/exsql"
    //插入燒錄紀錄
    fun InsertMemory(memory: String, errortype: String) {
        try {
            for (i in 0 until PublicBeans.lastFunction.OldSemsor.size) {
                insertSensorData( PublicBeans.lastFunction.OldSemsor[i] ,PublicBeans.lastFunction.NewSensor[i], errortype, "Program")
            }
            if(errortype!="success"){
                val date= SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OGlite(Android)->Program False",
                    "${PublicBeans.make}/${PublicBeans.model}/${PublicBeans.year}",
                    PublicBeans.getS19(),
                    PublicBeans.admin,
                    errortype,
                    memory,
                    date
                ).storeObject(date,"ErrorMessageJson")
            }

            var idstring = ""
            PublicBeans.lastFunction.OldSemsor.map {
                if (idstring.isNotEmpty()) {
                    idstring += "|"
                }
                idstring += it
            }
            
            OgCommand.tx_memory = StringBuffer()
            val sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls =
                "insert into  `oglitememory`.transfermemory (tx,errortype,serialnumber,make,model,year,account,number,devicetype,directfit,sensorType,version,sensorID) values " +
                        "('${memory}','$errortype','${PublicBeans.SN}','${PublicBeans.make}'," +
                        "'${PublicBeans.model}','${PublicBeans.year}','${PublicBeans.admin}',${PublicBeans.programNumber},'OG-lite','${PublicBeans.getS19()}','S3','${bleVersion}','${
                        idstring
                        }')"
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a != "success") {
                    sql.exsql("delete  from `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //插入Trigger紀錄
    fun insertTrigger(
        memory: String,
        error: String,
        id: String,
        tem: String = "NA",
        pre: String = "NA",
        bat: String = "NA"
    ) {
        try {
            Log.e("trigger","插入trigger紀錄")
            if(error!="success"){
                val date= SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OGlite(Android)->Read False",
                    "${PublicBeans.make}/${PublicBeans.model}/${PublicBeans.year}",
                    PublicBeans.getS19(),
                    PublicBeans.admin,
                    error,
                    memory,
                    date
                ).storeObject(date,"ErrorMessageJson")
            }
            insertSensorData(id, id, error, "Read")
            OgCommand.tx_memory = StringBuffer()
                   val sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
            sql.exsql(
                "CREATE TABLE if not exists  updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls =
                "insert into `oglitememory`.triggerinfo (make,model,year,admin,tx,type,serial,sensorid,tem,pre,bat,version,area) values " +
                        "('${PublicBeans.make.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.model.replace("'", "''")}'," +
                        "'${PublicBeans.year.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.admin}','$memory','$error','${PublicBeans.SN}','$id','$tem','$pre','$bat','${bleVersion}','${Util_FileDowload.country}')"
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a != "success") {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        }catch (e: Exception){e.printStackTrace()}

    }

    //插入OBD紀錄
    fun insertOBD(memory: String, error: String) {
        try {
            if(!error.contains("Success")){
                val date= SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OGlite(Android)->OBD False",
                    "${PublicBeans.make}/${PublicBeans.model}/${PublicBeans.year}",
                    PublicBeans.getOBDName()?:"NA",
                    PublicBeans.admin,
                    error,
                    memory,
                    date
                ).storeObject(date,"ErrorMessageJson")
            }
            OgCommand.tx_memory = StringBuffer()
                   val sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls =
                "insert into  `oglitememory`.`obd_infomation` (make,model,year,admin,tx,type,serial,version) values " +
                        "('${PublicBeans.make.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.model.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.year.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.admin}','$memory','$error','${PublicBeans.SN}','${String(MCU_NUMBER.hexToByte())}')"
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a != "success") {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        }catch (e: Exception){e.printStackTrace()}
    }

    //插入Copy紀錄
    fun insertCopyResult(memory: String, error: String) {
        try{
            val date= SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
            if(error!="success"){
                ErrorMessageJson(
                    "OGlite(Android)->COPY False",
                    "${PublicBeans.make}/${PublicBeans.model}/${PublicBeans.year}",
                    PublicBeans.getS19(),
                    PublicBeans.admin,
                    error,
                    memory,
                    date
                ).storeObject(date,"ErrorMessageJson")
            }

            OgCommand.tx_memory = StringBuffer()
            for (i in 0 until PublicBeans.lastFunction.NewSensor.size) {
                insertSensorData(
                    PublicBeans.lastFunction.OldSemsor[i],
                    PublicBeans.lastFunction.NewSensor[i],
                    error,
                    "Copy"
                )
            }
                   val sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls =
                "insert into `oglitememory`.`copy_result` (serial,account,tx,make,model,year,number,errortype,device_type,sensorType,version) values " +
                        "('${PublicBeans.SN}','${PublicBeans.admin}','$memory','${PublicBeans.make.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.model.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.year.replace(
                            "'",
                            "''"
                        )}','${PublicBeans.programNumber}','$error','OG-lite','S3','${PublicBeans.bleVersion}')"
            Thread {
                android.util.Log.e("上傳COpy", "start")
                val a = ip.postRequest(15 * 1000, sqls)
                android.util.Log.e("上傳COpy", a + "res")
                if (a != "success") {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()   }catch (e: Exception){e.printStackTrace()}
    }

    //插入Sensor紀錄
    fun insertSensorData(
        oldSensor: String,
        newSensor: String,
        errortype: String,
        function: String
    ) {
        try{
                   val sql=(JzActivity.getControlInstance().getRootActivity() as MainActivity).sqlmemnory
            Thread {
                OgCommand.tx_memory = StringBuffer()
                sql.exsql(
                    "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "  `sql` varchar NOT NULL\n)"
                )
                val sqls =
                    "replace into  orange_userdata.sensordata (SensorId,Serialnumber,Account,Make,Model,Year,Devicetype,Directfit,SensorType,Version,Time,Errortype,NewsensorId,`Function`) values " +
                            "('${oldSensor}','${PublicBeans.SN}','${PublicBeans.admin}','${PublicBeans.make}','${PublicBeans.model}','${PublicBeans.year}','OG-lite','${PublicBeans.getS19()}','${PublicBeans.getSencsorModel()}','${PublicBeans.versionCodes}','${SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss"
                            ).format(Date())}','${errortype}','${newSensor}','${function}')"
                android.util.Log.e("插入Sqls", sqls)
                val a = ip.postRequest(15 * 1000, sqls)
                if (a != "success") {
                    sql.exsql("delete  from `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        }catch (e: Exception){e.printStackTrace()}
    }
}