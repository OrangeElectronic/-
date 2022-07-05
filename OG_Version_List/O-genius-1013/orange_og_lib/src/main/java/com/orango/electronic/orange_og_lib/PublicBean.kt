package com.orango.electronic.orange_og_lib

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.gson.Gson
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.SensorData
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import kotlinx.android.synthetic.main.data_loading.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

object PublicBean {
    //Mmy是透過什麼方式進行選擇的
    enum class SelectFun {
        Manual, Scan, Plate, Favorite, Hsn
    }

    //選擇的MMY和選擇得操作種類
     class SelectMmy{
        var hsn:String=""
        var tsn:String=""
        var make: String=""
        var model: String=""
        //避免SQL反覆查詢導致Crash
        var year: String =""
            set(value) {
                field=value
                val sql="""select * from `Summary table` where `Make`='${make}' and `Model`='${model}' and `Year`='${value}' limit 0,1"""
                OgPublic.getInstance.itemDAO.query(sql) {
                    val map: MutableMap<String, Any> = mutableMapOf()
                    for (a in 0 until it.columnCount) {
                        if (it.getString(a) != null) {
                            map[it.getColumnName(a)] = it.getString(a)
                        }
                    }
                    Log.e("mmyData", Gson().toJson(map))
                    mmyData=map
                }
            }
        var displayMake=""
        var displayModel=""
        var displayYear=""
        var selectFun: SelectFun=SelectFun.Manual
        //
        var mmyData:MutableMap<String,Any> = mutableMapOf()
     }

    var selectMmy = SelectMmy()

    //選擇的車牌
    var selectPlate = ""

    //上次獲取到的IP位置
    var lastIP = ""

    //上次獲取到的國家
    var lastCountry: String
        get() {
            return JzActivity.getControlInstance().getPro("lastCountry", "NA")
        }
        set(value) {
            JzActivity.getControlInstance().setPro("lastCountry", value)
        }
    //是否關閉沒電無法操作功能
    var closeDisCharge:Boolean
    get(){
        return JzActivity.getControlInstance().getPro("disCharge",false)
    }
    set(value) {
        JzActivity.getControlInstance().setPro("disCharge",value)
    }

    //上次獲取到的周別
    var lastState: String
        get() {
            return JzActivity.getControlInstance().getPro("lastState", "NA")
        }
        set(value) {
            JzActivity.getControlInstance().setPro("lastState", value)
        }
    var SerialNum = ""
    var SensorType = "S3"
    val admin get() = JzActivity.getControlInstance().getPro("admin", "nodata")
    val password get() = JzActivity.getControlInstance().getPro("password", "nodata")
    var position = 1
    var ProgramNumber = 1
    val 檢查傳感器 = 1
    val 燒錄傳感器 = 2
    val 複製傳感器 = 3
    val 設定 = 4
    val 學碼步驟 = 5
    val 掃描Mmy = 6
    val 掃描Sensor = 7
    val PAD_COPY = 8
    val PAD_PROGRAM = 9
    val Go_Web = 10
    val ID_COPY_OBD = 11
    val OBD_RELEARM = 12
    val 車牌辨識 = 13
    val TireHotel = 14
    val TireHotel_serch = 15
    val TireHotel_insert = 16
    var ScanType = 4
    var SensorList: ArrayList<SensorData> = ArrayList()
    var NewSensorList: ArrayList<SensorData> = ArrayList()
    var CopySuccessID = ArrayList<String>()
    lateinit var CopySuccess: ArrayList<Boolean>
    var Update = false
    var readable = arrayOf(false, false, false, false, false)

    //取得id數量
    fun getIdcount(): Int {
        try {
            return selectMmy.mmyData["ID_Count"].toString().toInt()
        }catch (e:Exception){
            return 8
        }
    }
    //取得定義ID
    fun getDefineId():String{
        val it= selectMmy.mmyData
        return it["ID1"].toString()+it["ID2"].toString()+it["ID3"].toString()+it["ID4"].toString()
    }

    //取得S19
    fun getS19(): String {
        val it= selectMmy.mmyData
        return it["Direct Fit"].toString()
    }
    //OE Read
    fun getOERead(): String {
        val it= selectMmy.mmyData
        var oeRead=(it["OE read"] ?: "00").toString()
        while (oeRead.length<2){ oeRead="0$oeRead" }
        return oeRead
    }
    //取輪胎數量
    fun wheelCount(): Int {
        val it= selectMmy.mmyData
        return it["Wheel_Count"].toString().toInt()
    }

    //取得HEX
    fun getHEX(): String {
        var data = selectMmy.mmyData["OBD Product No. (hex)"].toString()
        data = if (data.length == 4) {
            data.substring(2, 4)
        } else {
            "00"
        }
        return data
    }

    //取得HEX
    fun getLFProtocol(): String {
        var data = selectMmy.mmyData["OG LF Protocol Num"].toString()
        while (data.length < 2) {
            data = "0$data"
        }
        return data
    }

    //取得Make
    fun getMake(): ArrayList<Util_MmySql_module> {
        val it = ArrayList<Util_MmySql_module>()
        val item = OgPublic.getInstance.itemDAO
        var sql = ""
        if (position == ID_COPY_OBD || position == OBD_RELEARM) {
            sql =
                "select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  replacer order by `Make` asc"
        } else {
            sql =
                "select distinct `Make`,`Make_img` from `Summary table` where `Make` IS NOT NULL and `Make_img` not in('NA')  replacer order by `Make` asc"
        }
        when (position) {
            OBD_RELEARM -> {
                sql = sql.replace("replacer", "and `OG Auto` != '4'")
            }
            ID_COPY_OBD -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' and OBD1 not in('NA')")
            }
            複製傳感器 -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' ")
            }
            燒錄傳感器 -> {
                sql = sql.replace("replacer", "and `OG Programe`='True' ")
            }
            檢查傳感器 -> {
                sql = sql.replace("replacer", "and `OG Read`='True'  ")
            }
        }
        sql = sql.replace("replacer", "")
        item.query(sql) { result ->
            val module = Util_MmySql_module()
            //val data = result.getString(0)
            module.make = result.getString(0)
            module.image = result.getString(1)
            it.add(module)
        }
        return it
    }

    //取得Model
    fun getModel(): ArrayList<String> {
        val it = ArrayList<String>()
        val item = OgPublic.getInstance.itemDAO
        var sql = ""
        if (position == ID_COPY_OBD || position == OBD_RELEARM) {
            sql =
                "select distinct `Model` from `Summary table` where `Make` = '${selectMmy.make}'  replacer"
        } else {
            sql =
                "select distinct `Model` from `Summary table` where `Make` = '${selectMmy.make}'  replacer"
        }
        when (position) {
            OBD_RELEARM -> {
                sql = sql.replace("replacer", "and `OG Auto` != '4'")
            }
            ID_COPY_OBD -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' and OBD1 not in('NA')")
            }
            燒錄傳感器 -> {
                sql = sql.replace("replacer", "and `OG Programe`='True' ")
            }
            複製傳感器 -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' ")
            }
            檢查傳感器 -> {
                sql = sql.replace("replacer", "and `OG Read`='True'  ")
            }
        }
        sql = sql.replace("replacer", "")
        item.query(sql, Sql_Result { result ->
            it.add(result.getString(0))
        })
        return it
    }

    interface scanBack {
        fun getSensor(id: String)
    }

    //取得ScanSensor
    fun scanSensor(callback: scanBack) {
        val scanDate = "${Date()}"
        JzActivity.getControlInstance()
            .showDiaLog(false, true, object : SetupDialog(R.layout.data_loading) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if (event.keyCode == 4) {
                        Cmd_Og.cancel = true
                    }
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.pass.visibility = View.VISIBLE
                    rootview.pass.text = "jz.270".getFix()
                }

            }, scanDate)
        var sensorid = ""
        var havaScan = false
        val dataReceiver = object : HardwareApp.DataReceiver {
            override fun scanReceive() {
            }

            override fun scanMsgReceive(content: String) {
                havaScan = true
                OgPublic.getInstance.playBeepSoundAndVibrate()
                JzActivity.getControlInstance().closeDiaLog(scanDate)
                Log.v("yhd-", "content:$content")
                //兼容三种
                if (!content.contains(":") && !content.contains("*")) {
                    if (content != "nofound") {
                        JzActivity.getControlInstance()
                            .toast("jz.284".getFix())
                    } else {
                        JzActivity.getControlInstance()
                            .toast("jz.291".getFix())
                    }
                    return
                }

                if (content.contains("**")) {
                    JzActivity.getControlInstance()
                        .toast("jz.284".getFix())
                } else {
                    if (content.split(":", "*").size >= 3) {
                        sensorid = content.split(":", "*")[1]
                    }
                }
                if (sensorid.isNotEmpty()) {
                    callback.getSensor(sensorid)
                }
                HardwareApp.getInstance().removeDataReceiver(this)
            }

            override fun uart2MsgReceive(content: String) {
            }
        }
        HardwareApp.getInstance().addDataReceiver(dataReceiver)
        HardwareApp.getInstance().scan()
        Thread {
            Thread.sleep(3000)
            HardwareApp.getInstance().removeDataReceiver(dataReceiver)
            JzActivity.getControlInstance().getHandler().post {
                JzActivity.getControlInstance().closeDiaLog(scanDate)
                if (!havaScan) {
                    OgPublic.getInstance.playBeepSoundAndVibrate()
                }
            }
        }.start()
    }

    //取得ScanSensor
    fun scanSensorNoDia(callback: scanBack) {
        val scanDate = "${Date()}"
        var sensorid = ""
        var havaScan = false
        val dataReceiver = object : HardwareApp.DataReceiver {
            override fun scanReceive() {
            }

            override fun scanMsgReceive( content: String) {
                havaScan = true
                if (content.split(",").size == 4) {
                    callback.getSensor(content)
                    HardwareApp.getInstance().removeDataReceiver(this)
                } else {
                    val content=content.replace(" ","").replace("\n","").replace("\t","").replace("\r","")
                    Log.v("yhd-", "content:$content length:${content.length}")
                    if (content.contains("**")) {
                    } else {
                        if (content.split(":", "*").size >= 3) {
                            sensorid = content.split(":", "*")[1]
                        }else{
                            if(arrayOf(12,15).indexOf(content.length)!=-1){
                                sensorid=content
                                callback.getSensor(sensorid)
                            }
                        }
                    }

                    if (sensorid.isNotEmpty()) {
                        callback.getSensor(sensorid)
                    }
                    HardwareApp.getInstance().removeDataReceiver(this)
                }

            }

            override fun uart2MsgReceive(content: String) {
            }
        }
        HardwareApp.getInstance().addDataReceiver(dataReceiver)
        HardwareApp.getInstance().scan()
        Thread {
            Thread.sleep(3000)
            HardwareApp.getInstance().removeDataReceiver(dataReceiver)
        }.start()
    }

    //取得Year
    fun getYear(): ArrayList<String> {
        val it = ArrayList<String>()
        val item = OgPublic.getInstance.itemDAO
        var sql = ""
        if (position == ID_COPY_OBD || position == OBD_RELEARM) {
            sql =
                "select distinct `Year` from `Summary table` where `Make` = '${selectMmy.make}' and `Model` = '${selectMmy.model}'  replacer"
        } else {
            sql =
                "select distinct `Year` from `Summary table` where `Make` = '${selectMmy.make}' and `Model` = '${selectMmy.model}'  replacer"
        }
        when (position) {
            OBD_RELEARM -> {
                sql = sql.replace("replacer", "and `OG Auto` != '4'")
            }
            ID_COPY_OBD -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' and OBD1 not in('NA')")
            }
            燒錄傳感器 -> {
                sql = sql.replace("replacer", "and `OG Programe`='True' ")
            }
            複製傳感器 -> {
                sql = sql.replace("replacer", "and `OG CopyID`='True' ")
            }
            檢查傳感器 -> {
                sql = sql.replace("replacer", "and `OG Read`='True'  ")
            }
        }
        sql = sql.replace("replacer", "")
        item.query(sql, Sql_Result { result ->
            it.add(result.getString(0))
        })
        return it
    }
    //判斷mmy是否存在
    fun mmyExists():Boolean{
        val item = OgPublic.getInstance.itemDAO
        var exists=false
        item.query("select count(1) from `Summary table` where " +
                "`Make` = '${selectMmy.make}' and `Model` = '${selectMmy.model}' and `Year` = '${selectMmy.year}'") { result ->
            exists=result.getString(0)!="0"
        }
        return exists
    }
    //取得單位類型
    fun isDec(): Boolean {
        val swi = JzActivity.getControlInstance().getPro("Ns", 0)
        return when (swi) {
            0 -> {
                selectMmy.mmyData["HEX/DEC"].toString() == "DEC"
            }
            1 -> {
                true
            }
            2 -> {
                false
            }
            else -> false
        }
    }

    //取得LfPower
    fun getLfPower(): String {
        return selectMmy.mmyData["LF Power"].toString()
    }

    //取得OBD燒錄檔
    fun getOBD1(): String {
        return selectMmy.mmyData["OBD1"].toString()
    }

    //取得Lf
    fun getLf(): String {
        return selectMmy.mmyData["Lf"].toString()
    }

    //取得OePartNumber
    fun getOePart(): String {

        return selectMmy.mmyData["OE Part Num"].toString()
    }

    //取得SensorModel
    fun getSencsorModel(): String {
        return selectMmy.mmyData["Sensor"].toString()
    }

    //判斷有無二代CODE
    fun getSIISensor(): Boolean {
        return File("/sdcard/files18/${getS19()}.s18").exists()
    }

    //取得三代燒錄檔
    fun getS3Code(): StringBuilder {
        try {
            var sb = StringBuilder()
            val fo = FileInputStream("/sdcard/files19/${getS19()}.s19")
            val fr = InputStreamReader(fo)
            val br = BufferedReader(fr)
            sb = StringBuilder()
            while (br.ready()) {
                val s = br.readLine()
                if (s != null && s != "null") {
                    sb.append(s)
                }
            }
            return sb
        } catch (e: Exception) {
            e.printStackTrace()
            return StringBuilder()
        }
    }

    fun onlyCopy(): Boolean {
        var res = true
        try {
            val item = OgPublic.getInstance.itemDAO
            item.query(
                "select `OG CIDP` from `Summary table` where  `Direct Fit`='${getS19()}' limit 0,1",
                Sql_Result {
                    res = (it.getString(0) == "False")
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    //取得二代燒錄檔
    fun getS2Code(): StringBuilder {
        var sb = StringBuilder()
        val fo = FileInputStream("/sdcard/files18/${getS19()}.s18")
        val fr = InputStreamReader(fo)
        val br = BufferedReader(fr)
        sb = StringBuilder()
        br.readLine()
        while (br.ready()) {
            val s = br.readLine()
            if (s != null && s != "null") {
                sb.append(s)
            }
        }
        return sb
    }

    //取得Relearm
    fun getreLarm(): String {
        val a = JzActivity.getControlInstance().getLanguage()
        var colname = "Relearn Procedure (English)"
        when (a) {
            Locale("tw") -> {
                colname = "Relearn Procedure (Traditional Chinese)"
            }
            Locale("zh-rCN") -> {
                colname = "Relearn Procedure (Jane)"
            }
            Locale("de") -> {
                colname = "Relearn Procedure (German)"
            }
            Locale("en") -> {
                colname = "Relearn Procedure (English)"
            }
            Locale("it") -> {
                colname = "Relearn Procedure (Italian)"
            }
        }
        return selectMmy.mmyData[colname].toString()
    }

    //取得MMy 藉由 mmy number
    fun GoOk(code: String) {
        val sql =
            "select  `Make`,`Model`,`Year`,`Make_Img`  from `Summary table` where `Direct Fit` not in('NA')  and `MMY number`='$code' limit 0,1"
        val item = OgPublic.getInstance.itemDAO
        item.query(
            sql
        ) {
            selectMmy.make = it.getString(0)
            selectMmy.model = it.getString(1)
            selectMmy.year = it.getString(2)
        }
    }

    //設定我的最愛
    fun setFavorite(make: String, model: String, year: String) {
        val sql = "CREATE TABLE if not exists `favorite` (\n" +
                "    make  VARCHAR,\n" +
                "    model VARCHAR,\n" +
                "    year  VARCHAR,\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");\n"
        val item = OgPublic.getInstance.favoriteDAO
        item.exsql(sql)
        deleteFavorite(make, model, year)
        item.exsql("insert or replace into `favorite` (make,model,year) values ('$make','$model','$year')")
    }

    //取得我的最愛
    fun getFavorite(): ArrayList<favorite> {
        val iem: ArrayList<favorite> = ArrayList()
        val sql = "CREATE TABLE if not exists `favorite` (\n" +
                "    make  VARCHAR,\n" +
                "    model VARCHAR,\n" +
                "    year  VARCHAR,\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");\n"
        val item = OgPublic.getInstance.favoriteDAO
        item.exsql(sql)
        item.query("select * from favorite order by id desc limit 0,50", Sql_Result {
            val fit = it
            val item2 = OgPublic.getInstance.itemDAO
            var sql2 = "select count(1) from `Summary table` where `Make` = '${it.getString(0)}' and `Model` = '${
                it.getString(1)
            }'  and year='${it.getString(2)}'   replacer"
            when (position) {
                OBD_RELEARM -> {
                    sql2 = sql2.replace("replacer", "and `OG Auto` != '4'")
                }
                ID_COPY_OBD -> {
                    sql2 = sql2.replace("replacer", "and `OG CopyID`='True' and OBD1 not in('NA')")
                }
                燒錄傳感器 -> {
                    sql2 = sql2.replace("replacer", "and `OG Programe`='True' ")
                }
                複製傳感器 -> {
                    sql2 = sql2.replace("replacer", "and `OG CopyID`='True' ")
                }
                檢查傳感器 -> {
                    sql2 = sql2.replace("replacer", "and `OG Read`='True'  ")
                }
            }

            sql2 = sql2.replace("replacer", "")
            item2.query(sql2, Sql_Result {
                if (it.getString(0) != "0") {
                    iem.add(
                        favorite(
                            fit.getString(0),
                            fit.getString(1),
                            fit.getString(2)
                        )
                    )
                }
            })
        })
        return iem
    }

    //刪除我的最愛
    fun deleteFavorite(make: String, model: String, year: String): ArrayList<favorite> {
        val iem: ArrayList<favorite> = ArrayList()
        val sql = "CREATE TABLE if not exists `favorite` (\n" +
                "    make  VARCHAR,\n" +
                "    model VARCHAR,\n" +
                "    year  VARCHAR,\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");\n"
        val item = OgPublic.getInstance.favoriteDAO
        item.exsql(sql)
        item.exsql("delete from `favorite` where make='$make' and model='$model' and year='$year'")
        return iem
    }

    //取得是否開關背蓋
    fun openOrClose(): Boolean {
        try {
            return selectMmy.mmyData["openback"].toString() == "True"
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //取得學馬步驟類型
    fun getRelearmMode(): String {
        try {
            val item = OgPublic.getInstance.itemDAO
            var name = ""
            Log.e("PublicBeanRelearmMode", "->$name")
            return selectMmy.mmyData["OG Auto"].toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return "0"
        }
    }

    data class mmySelect(var make: String, var model: String, var year: String, var dir: String = "NA",var displayMake: String,var displayModel: String,var displayYear: String)

    //HSN/TSN取得MMY
    fun getMmyByHsn(hsn: String, tsn: String): ArrayList<mmySelect> {
        try {
            val data = ArrayList<mmySelect>()
            val item = OgPublic.getInstance.itemDAO
            item.query(
                "select `SNTSN`.`make`,`SNTSN`.`model`,`SNTSN`.`year`,`Summary table`.`Direct Fit`,`SNTSN`.`Handelsname`,`SNTSN`.`Fabrikmarke` from `SNTSN`,`Summary table` where (HSN='${hsn}' and TSN='${tsn}') and (`SNTSN`.Make=`Summary table`.make and `SNTSN`.Model=`Summary table`.model and `SNTSN`.Year=`Summary table`.year)"
            ) {
                data.add(mmySelect(it.getString(0), it.getString(1), it.getString(2), it.getString(3),it.getString(4),it.getString(5),it.getString(2)))
            }
            return data
        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList<mmySelect>()
        }
    }
}

class favorite(var make: String, var model: String, var year: String)