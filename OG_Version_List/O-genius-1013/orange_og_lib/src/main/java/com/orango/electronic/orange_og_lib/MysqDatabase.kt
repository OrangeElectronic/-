package com.orango.electronic.orange_og_lib

import android.util.Log
import com.example.jztaskhandler.JzClock
import com.google.gson.Gson
import com.orango.electronic.jzutil.*
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import com.orango.electronic.orange_og_lib.beans.SensorData
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object MysqDatabase {

    data class ErrorMessageJson(
        var errorTitle: String,
        var mmy: String,
        var directFit: String,
        var account: String,
        var errorType: String,
        var tx: String,
        var time: String,
        var harwareVersion: String = OgPublic.hardWareVersion,
        var softVersion: String = OgPublic.versionCodes.toString(),
        var fwVersion: String = OgPublic.MCU_NUMBER
    )

    var ip = "${serverRout}/exsql"
    //插入SQL字串
    fun insertSql(string:String){
        val sql = OgPublic.getInstance.sqlite
        sql.exsql(
            "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  `sql` varchar NOT NULL\n)"
        )
        Thread {
            val a = ip.postRequest(15 * 1000, string)
            if (a == null) {
                sql.exsql("delete  from `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                sql.exsql("insert into `updateResult` (sql) values ('${string.toHex()}')")
            }
        }.start()
    }
    //插入燒錄紀錄
    var programClock = JzClock()
    fun InsertMemory(memory: String, errortype: String, sensorID: ArrayList<SensorData>,mmyVersion:String="DataListVersion".getObject<FileJsonVersion>()!!.mmyVersion) {
        try {
            for (i in 0 until sensorID.size) {
                insertSensorData(
                    PublicBean.SensorList[i].id,
                    sensorID[i].id,
                    errortype,
                    "Program"
                )
            }
            if ((errortype != "success") && (errortype != "1-${PublicBean.ProgramNumber}-${PublicBean.ProgramNumber}")
                && (errortype != "13-${PublicBean.ProgramNumber}-${PublicBean.ProgramNumber}")) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OG Program False->${PublicBean.getS19()}",
                    "${PublicBean.selectMmy.make}/${PublicBean.selectMmy.model}/${PublicBean.selectMmy.year}",
                    PublicBean.getS19(),
                    PublicBean.admin.replace("\"","\\\"").replace("'","\\\'"),
                    errortype,
                    memory,
                    date
                ).storeObject(date, "ErrorMessageJson")
            }

            var idstring = ""
            sensorID.map {
                if (idstring.isNotEmpty()) {
                    idstring += "|"
                }
                idstring += it.id
            }
            Cmd_Og.tx_memory = StringBuffer()
            val sql = OgPublic.getInstance.sqlite
            val time= SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.GERMANY).format(Date())
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls = "insert into  `ogmemory`.transfermemory (tx,errortype,serialnumber,make,model,year,account,number,devicetype,directfit,sensorType,version,sensorID,apkVersion,`database`,harewareVersion,ip,country,state,exTime,inTire,time) values " +
                        "('${memory}','$errortype','${OgPublic.deviceID}','${PublicBean.selectMmy.make}'," +
                        "'${PublicBean.selectMmy.model}','${PublicBean.selectMmy.year}','${PublicBean.admin.replace("\"","\\\"").replace("'","\\\'")}',${PublicBean.ProgramNumber},'OG','${PublicBean.getS19()}','${PublicBean.SensorType}',${OgPublic.MCU_NUMBER},'${
                        idstring
                        }','${OgPublic.versionCodes}','${mmyVersion}','${OgPublic.hardWareVersion}','${PublicBean.lastIP}','${PublicBean.lastCountry}','${PublicBean.lastState}',${programClock.stop()},'${
                            (sensorID.any { it.kpa > 100 })
                        }','$time')"
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a == null) {
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
        bat: String = "NA",
        mmyVersion:String="DataListVersion".getObject<FileJsonVersion>()!!.mmyVersion,
        exTime:String="0"
    ) {
        try {
            if (arrayOf("success","11-2","11-3","11-2-1","11-3-1","11","11-4").indexOf(error)==-1) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OG Read False->${PublicBean.getS19()}",
                    "${PublicBean.selectMmy.make}/${PublicBean.selectMmy.model}/${PublicBean.selectMmy.year}",
                    PublicBean.getS19(),
                    PublicBean.admin.replace("\"","\\\"").replace("'","\\\'"),
                    error,
                    memory,
                    date
                ).storeObject(date, "ErrorMessageJson")
            }
            insertSensorData(id, id, error, "Read")
            Cmd_Og.tx_memory = StringBuffer()
            val sql = OgPublic.getInstance.sqlite
            sql.exsql(
                "CREATE TABLE if not exists  updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val time= SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.GERMANY).format(Date())
            val sqls = "insert into `ogmemory`.triggerinfo (time,make,model,year,admin,tx,type,serial,sensorid,tem,pre,bat,version,LF_power,directfit,ip,`database`,apkVersion,harewareVersion,country,state,exTime) values " +
                        "('$time','${PublicBean.selectMmy.make.replace(
                            "'",
                            "''"
                        )}','${PublicBean.selectMmy.model.replace("'", "''")}'," +
                        "'${PublicBean.selectMmy.year.replace(
                            "'",
                            "''"
                        )}','${PublicBean.admin.replace("\"","\\\"").replace("'","\\\'")}','$memory','$error','${OgPublic.deviceID}','$id','$tem','$pre','$bat',${OgPublic.MCU_NUMBER},'${PublicBean.getLFProtocol()}','${PublicBean.getS19()}','${PublicBean.lastIP}','${mmyVersion}','${OgPublic.versionCodes}','${OgPublic.hardWareVersion}'," +
                    "'${PublicBean.lastCountry}','${PublicBean.lastState}',${exTime})"
            Log.e("sqls",sqls)
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a == null) {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //插入OBD紀錄
    fun insertOBD(memory: String, error: String) {
        try {
            val datalist="DataListVersion".getObject<FileJsonVersion>()!!
            val protocal=PublicBean.getOBD1()
            val mmyVersion=datalist.mmyVersion
            val obdVersion=datalist.obdList[protocal]
            val time= SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.GERMANY).format(Date())
            if (!error.contains("Success")) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
                ErrorMessageJson(
                    "OG OBD False->${PublicBean.getOBD1()}",
                    "${PublicBean.selectMmy.make}/${PublicBean.selectMmy.model}/${PublicBean.selectMmy.year}",
                    PublicBean.getOBD1(),
                    PublicBean.admin.replace("\"","\\\"").replace("'","\\\'"),
                    error,
                    memory,
                    date
                ).storeObject(date, "ErrorMessageJson")
            }
            Cmd_Og.tx_memory = StringBuffer()
            val sql = OgPublic.getInstance.sqlite
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls = "insert into  `ogmemory`.`obd_infomation` (time,make,model,year,admin,tx,type,serial,obdVersion,protocal,ip,`database`,apkVersion,harewareVersion,country,state) values " +
                        "('$time','${PublicBean.selectMmy.make.replace(
                            "'",
                            "''"
                        )}','${PublicBean.selectMmy.model.replace(
                            "'",
                            "''"
                        )}','${PublicBean.selectMmy.year.replace(
                            "'",
                            "''"
                        )}','${PublicBean.admin.replace("\"","\\\"").replace("'","\\\'")}','$memory','$error','${OgPublic.deviceID}','${obdVersion}','$protocal','${PublicBean.lastIP}','${mmyVersion}','${OgPublic.versionCodes}','${OgPublic.hardWareVersion}','${PublicBean.lastCountry}','${PublicBean.lastState}')"
            Thread {
                val a = ip.postRequest(15 * 1000, sqls)
                if (a == null) {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //插入Copy紀錄
    var copyClock = JzClock()
    fun insertCopyResult(memory: String, error: String,
                         mmyVersion:String="DataListVersion".getObject<FileJsonVersion>()!!.mmyVersion) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
            if (!arrayOf("success","6-1-1","7-1-1").contains(error)) {
                ErrorMessageJson(
                    "OG Copy False->${PublicBean.getS19()}",
                    "${PublicBean.selectMmy.make}/${PublicBean.selectMmy.model}/${PublicBean.selectMmy.year}",
                    PublicBean.getS19(),
                    PublicBean.admin.replace("\"","\\\"").replace("'","\\\'"),
                    error,
                    memory,
                    date
                ).storeObject(date, "ErrorMessageJson")
            }

            Cmd_Og.tx_memory = StringBuffer()
            val newSensorList:ArrayList<String> = arrayListOf()
            for (i in 0 until PublicBean.SensorList.size) {
                newSensorList.add(PublicBean.SensorList[i].id)
                insertSensorData(
                    PublicBean.SensorList[i].id,
                    PublicBean.NewSensorList[i].id,
                    error,
                    "Copy"
                )
            }
            if (PublicBean.selectMmy.selectFun === PublicBean.SelectFun.Plate) {
                insertPlateMemory(newSensorList)
            }
            val sql = OgPublic.getInstance.sqlite
            sql.exsql(
                "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "  `sql` varchar NOT NULL\n)"
            )
            val sqls = "insert into `ogmemory`.`copy_result` (serial,account,tx,make,model,year,number,errortype,device_type,sensorType,version,directfit,ip,`database`,apkVersion,harewareVersion,country,state,exTime,sensorID,updatesensorID) values " +
                        "('${OgPublic.deviceID}','${PublicBean.admin.replace("\"","\\\"").replace("'","\\\'")}','$memory','${PublicBean.selectMmy.make.replace(
                            "'",
                            "''"
                        )}','${PublicBean.selectMmy.model.replace(
                            "'",
                            "''"
                        )}','${PublicBean.selectMmy.year.replace(
                            "'",
                            "''"
                        )}','${PublicBean.ProgramNumber}','$error','OG','${PublicBean.SensorType}',${OgPublic.MCU_NUMBER},'${PublicBean.getS19()}','${PublicBean.lastIP}','${mmyVersion}','${OgPublic.versionCodes}','${OgPublic.hardWareVersion}','${PublicBean.lastCountry}','${PublicBean.lastState}'" +
                    ",${copyClock.stop()},'${PublicBean.SensorList[0].id}','${PublicBean.NewSensorList[0].id}')"
            println(sqls)
            Thread {
                Log.e("上傳COpy", "start")
                val a = ip.postRequest(15 * 1000, sqls)
                Log.e("上傳COpy", a + "res")
                if (a == null) {
                    sql.exsql("delete from  `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //插入Sensor紀錄
    fun insertSensorData(
        oldSensor: String,
        newSensor: String,
        errortype: String,
        function: String
    ) {
        try {
            val sql = OgPublic.getInstance.sqlite
            Thread {
                Cmd_Og.tx_memory = StringBuffer()
                sql.exsql(
                    "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "  `sql` varchar NOT NULL\n)"
                )
                val sqls =
                    "replace into  orange_userdata.sensordata (SensorId,Serialnumber,Account,Make,Model,Year,Devicetype,Directfit,SensorType,Version,Time,Errortype,NewsensorId,`Function`) values " +
                            "('${oldSensor}','${OgPublic.deviceID}','${PublicBean.admin.replace("\"","\\\"").replace("'","\\\'")}','${PublicBean.selectMmy.make}','${PublicBean.selectMmy.model}','${PublicBean.selectMmy.year}','OG','${PublicBean.getS19()}','${PublicBean.getSencsorModel()}','${OgPublic.versionCodes}','${SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss"
                            ).format(Date())}','${errortype}','${newSensor}','${function}')"
                Log.e("插入Sqls", sqls)
                val a = ip.postRequest(15 * 1000, sqls)
                if (a == null) {
                    sql.exsql("delete  from `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //插入車牌ID紀錄
    fun insertPlateMemory(newSensorList:ArrayList<String>) {
        try {
            val sql = OgPublic.getInstance.sqlite
            Thread {
                Cmd_Og.tx_memory = StringBuffer()
                sql.exsql(
                    "CREATE TABLE if not exists updateResult (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "  `sql` varchar NOT NULL\n)"
                )
                val sqls =
                    "replace into plate_recognize.plate_memory (mmy,sensorID,plate) values ('${Gson().toJson(
                        PublicBean.selectMmy
                    )}','${Gson().toJson(newSensorList)}','${PublicBean.selectPlate}')"
                val a = ip.postRequest(15 * 1000, sqls)
                if (a == null) {
                    sql.exsql("delete  from `updateResult`  where rowid <= (select MAX(rowid-1000)) from `updateResult`)")
                    sql.exsql("insert into `updateResult` (sql) values ('${sqls.toHex()}')")
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
