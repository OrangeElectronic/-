package com.orange.tpms

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JavaScriptInterFace
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orango.electronic.orange_og_lib.Callback.Program_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Command.Cmd_Rfid
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.Systool
import com.orango.electronic.orange_og_lib.beans.SensorData
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import kotlinx.android.synthetic.main.frag_test_engineer_mode.view.*
import java.io.File

object GlitterInterFace {
    var handler= Handler()
    var scanText= JavaScriptInterFace("scanText"){
            request ->
        var finish=false
        val dataReceiver = object : HardwareApp.DataReceiver {
            override fun scanReceive() {
            }

            override fun scanMsgReceive(content: String) {
                finish=true
                request.responseValue["result"]=true
                request.responseValue["data"]=content
                request.finish()
            }

            override fun uart2MsgReceive(content: String) {}
        }
        HardwareApp.getInstance().addDataReceiver(dataReceiver)
        HardwareApp.getInstance().scan()
        Thread.sleep(3000)
        HardwareApp.getInstance().removeDataReceiver(dataReceiver)
        if(!finish){ request.responseValue["result"]=false
            request.finish()}

    }
    var webRout=JavaScriptInterFace("webRout"){
        it.responseValue["webRout"]= FileDowload.serverRout
        it.finish()
    }
    var ogAccount=JavaScriptInterFace("OGAccount"){
        it.responseValue["account"]= OgPublic.admin
        it.finish()
    }
    var cancel=JavaScriptInterFace("cancel") {
        Cmd_Og.cancel = true
        it.finish()
    }
    var program=JavaScriptInterFace("startProgram"){
            request->
        MysqDatabase.programClock.Zeroing()
        val sensorData=request.receiveValue["data"] as ArrayList<MutableMap<String,Any>>
        val sensordara = ArrayList<SensorData>()
        sensorData.map {
            val data= SensorData()
            data.id=it["id"].toString()
            data.idcount= PublicBean.getIdcount()
            sensordara.add(data)
        }
        PublicBean.selectMmy.make=request.receiveValue["make"].toString()
        PublicBean.selectMmy.model=request.receiveValue["model"].toString()
        PublicBean.selectMmy.year=request.receiveValue["year"].toString()
        PublicBean.ProgramNumber=sensordara.size
        PublicBean.SensorList=sensordara
        PublicBean.SensorType = if (sensordara.any { it.senesorType == "S2" }) "S2" else "S3"
        Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX())
        Cmd_Og.Program(
            "00",
            PublicBean.getHEX(),
            Integer.toHexString(PublicBean.ProgramNumber),
            PublicBean.getS19(),
            object : Program_C {
                override fun Program_Progress(i: Int) {
                    handler.post { GlitterActivity.instance().webRoot.evaluateJavascript("glitter.share.loadingProgress(${i})",null) }
                }

                override fun Program_Finish(result: Boolean) {
                    if (result) {
                        Log.e("DATA:", "燒錄成功")
                        Thread.sleep(5000)
                        val copyId=ArrayList<String>()
                        sensordara.map {
                            val id=StringBuffer(PublicBean.getDefineId())
                            for(a in id.indices){
                                if(arrayOf("O","X").indexOf(id.substring(a,a+1))!=-1){
                                    id.setCharAt(a,it.id.substring(a,a+1).toCharArray()[0])
                                }
                            }
                            copyId.add(id.toString())
                        }
                        Cmd_Og.IdCoopy(copyId,copyId){
                            if (sensordara.size == it.size) {
                                Thread {
                                    MysqDatabase.InsertMemory(
                                        Cmd_Og.tx_memory.toString(),
                                        "success"
                                        ,sensordara)
                                }.start()
                            }else{
                                Thread {
                                    MysqDatabase.InsertMemory(
                                        Cmd_Og.tx_memory.toString(),
                                        "5-${sensordara.size}-${it.size}"
                                        ,sensordara)
                                }.start()
                            }
                            sensordara.map { ait ->
                                it.map {
                                    if (ait.programResult != SensorData.STATE_SUCCESS) {
                                        ait.programResult = SensorData.STATE_FAILED
                                    }
                                    if (ait.id.substring(8 -   ait.idcount + 2) == it.id.substring(8 - it.idcount + 2)) {
                                        ait.c = it.c
                                        ait.id = it.id
                                        ait.vol = it.vol
                                        ait.idcount = it.idcount
                                        ait.bat = if (it.bat == "ok") "1" else "0"
                                        ait.kpa = it.reKpa
                                        ait.有無電壓 = it.有無電壓
                                        ait.有無胎溫 = it.有無胎溫
                                        ait.有無電池 = it.有無電池
                                        ait.programResult = SensorData.STATE_SUCCESS
                                        ait.success = true
                                        Log.e(
                                            "sensordara",
                                            "${ait.kpa} | ${ait.c} | ${ait.bat} | ${ait.有無胎溫} | ${ait.有無電池}"
                                        )
                                    }
                                }
                            }
                        }
                        handler.post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            val data:ArrayList<MutableMap<String,Any>> = arrayListOf()
                            sensordara.map{
                                val dd:MutableMap<String,Any> = mutableMapOf()
                                dd["result"]=it.programResult == SensorData.STATE_SUCCESS
                                dd["id"]=it.id.toString()
                                dd["kpa"]=it.kpa.toString()
                                dd["c"]=it.c.toString()
                                dd["bat"]=it.bat.toString()
                                data.add(dd)
                            }
                            request.responseValue["data"]=data
                            request.responseValue["result"]=sensordara.filter { it.programResult == SensorData.STATE_SUCCESS }.size == PublicBean.ProgramNumber
                            request.finish()
                        }
                    } else {
                        Log.e("Frag_New_ProgramDetail", "沒有成功")
                        sensordara.map { ait ->
                            if (ait.programResult != SensorData.STATE_SUCCESS) {
                                ait.programResult =
                                    SensorData.STATE_FAILED
                            }
                        }
                        sensorData.map {
                            it["result"]=false
                        }
                        request.responseValue["data"]=sensorData
                        request.responseValue["result"]=false
                        request.finish()
                    }
                }

                override fun prograam_NotProceed(type: Int) {
                    request.responseValue["result"]=false
                    request.finish()
                }
            }, sensordara
        )
    }
    var readMt=JavaScriptInterFace("readMtSensor") {
        handler.post { EmptyDialog(R.layout.data_loading).show() }
        PublicBean.ProgramNumber = (it.receiveValue["count"] as Double).toInt()
        PublicBean.selectMmy.make = it.receiveValue["make"].toString()
        PublicBean.selectMmy.model = it.receiveValue["model"].toString()
        PublicBean.selectMmy.year = it.receiveValue["year"].toString()
        var a = Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX())
        Thread.sleep(5000)
        //第一次讀假的要讓sensor reset用
        if (!Cmd_Og.cancel) {
            a = Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX(), false)
        }
        //當讀取失敗時上傳錯誤回報
        if ((a.type != "success") && (a.type != "cancel")) {
            MysqDatabase.InsertMemory(Cmd_Og.tx_memory.toString(), a.type, Cmd_Og.sensorData)
        }
        val sensorData = arrayListOf<MutableMap<String, Any>>()
        a.sensorData.map {
            val map: MutableMap<String, Any> = mutableMapOf()
            map["id"] = it.id
            map["idCount"] = it.idcount.toString()
            map["bat"]=it.bat
            map["kpa"]=it.kpa
            map["c"]=it.c
            map["vol"]=it.vol
            sensorData.add(map)
        }
        it.responseValue["data"] = sensorData
        handler.post {
            OgPublic.getInstance.playBeepSoundAndVibrate()
            JzActivity.getControlInstance().closeDiaLog()
            it.finish()
        }
    }
    var getInt=JavaScriptInterFace("getInt") {
        val name = it.receiveValue["name"].toString()
        it.responseValue["data"] = JzActivity.getControlInstance().getPro(name, 2).toString()
        it.finish()
    }
    var scanSensor=JavaScriptInterFace("scanSensor") { request ->
        PublicBean.scanSensorNoDia(object : PublicBean.scanBack {
            override fun getSensor(id: String) {
                if (id.split(",").size == 4) {
                    Thread {
                        Log.e("rout", "http://orangeapi.orange-electronic.com/api/Sensor/${id.split(",")[3]}")
                        val data =
                            "http://orangeapi.orange-electronic.com/api/Sensor/${id.split(",")[3]}".postRequest2(
                                1000 * 20,
                                ""
                            )
                        if (data !== null) {
                            handler.post {
                                GlitterActivity.instance().webRoot.evaluateJavascript(
                                    "publicBeans.scanBack($data);",
                                    null
                                )
                            }
                            request.responseValue["data"]=data
                            request.finish()
                        }else{
                            request.finish()
                        }
                    }.start()
                } else {
                    handler.post {
                        GlitterActivity.instance().webRoot.evaluateJavascript(
                            "publicBeans.scanBack( [{SensorId : \"$id\" }]);",
                            null
                        )
                        request.responseValue["data"]=id
                        request.finish()
                    }
                }
            }
        })
    }
    var hideKeyBoard=JavaScriptInterFace("hideKeyBoard") {
        GlitterActivity.instance().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    var hideNavagation=JavaScriptInterFace("hideNavagation") {
        Systool.setNaVaGation(true)
    }
    var playBeet=JavaScriptInterFace("playBeet") {
        OgPublic.getInstance.playBeepSoundAndVibrate()
        it.finish()
    }
    var getPressureUnit=JavaScriptInterFace("getPressureUnit") {
        it.responseValue["data"]=SensorData.getPre()
        it.finish()
    }
    var getTemUnit=JavaScriptInterFace("getTemUnit") {
        it.responseValue["data"]=SensorData.getTem()
        it.finish()
    }
    var readSensor=JavaScriptInterFace("readSensor") { request ->
        PublicBean.selectMmy.make = request.receiveValue["make"].toString()
        PublicBean.selectMmy.model = request.receiveValue["model"].toString()
        PublicBean.selectMmy.year = request.receiveValue["year"].toString()
        val sensorData = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
        request.responseValue["result"]=sensorData.success
        if(sensorData.success){
            var map:MutableMap<String,Any> = mutableMapOf()
            map["id"]=sensorData.id
            map["kpa"]=sensorData.kpa
            map["c"]=sensorData.c
            map["bat"]=sensorData.bat
            request.responseValue["data"] = map
        }
        request.finish()
    }
    var getMmyInterFace=JavaScriptInterFace("getMmyInterFace"){
        request ->
        PublicBean.selectMmy.make=request.receiveValue["make"].toString()
        PublicBean.selectMmy.model=request.receiveValue["model"].toString()
        PublicBean.selectMmy.year=request.receiveValue["year"].toString()
        PublicBean.selectMmy.mmyData["reltext"]=PublicBean.getreLarm()
        request.responseValue["data"]=PublicBean.selectMmy.mmyData
        request.finish()
    }
    var idCopy=JavaScriptInterFace("idCopy"){
            request ->
        val copyId = ArrayList<String>()
        var origId = request.receiveValue["originID"].toString()
        while (origId.length < 8) {
            origId = "0${origId}"
        }
        copyId.add(origId)
        val newId = ArrayList<String>()
        var newid = request.receiveValue["newID"].toString()
        while (newid.length < 8) {
            newid = "0${newid}"
        }
        if (arrayOf("HOSTKA03", "HOSWA306").indexOf(PublicBean.getS19()) != -1) {
            val id = StringBuffer(newid.substring(2, 8) + newid.substring(0, 2))
            if (PublicBean.getS19() == "HOSTKA03") {
                id.setCharAt(7, "8".toCharArray()[0])
            } else {
                id.setCharAt(7, "5".toCharArray()[0])
            }
            newId.add(id.toString())
        } else {
            val id = StringBuffer(PublicBean.getDefineId())
            for (a in id.indices) {
                if (arrayOf("O", "X").indexOf(id.substring(a, a + 1)) != -1) {
                    id.setCharAt(a, newid.substring(a, a + 1).toCharArray()[0])
                }
            }
            newId.add(id.toString())
        }
        Cmd_Og.IdCoopy(copyId, newId) {
            if (it.size == 1) {
                Thread {
                    MysqDatabase.insertCopyResult(
                        Cmd_Og.tx_memory.toString(),
                        "success"
                    );
                }.start()
                val map:MutableMap<String,Any> = mutableMapOf()
                map["id"]=it[0].id
                map["kpa"]=it[0].kpa
                map["c"]=it[0].c
                map["bat"]=it[0].bat
                request.responseValue["data"]=map
              request.responseValue["result"]=true
            } else {
                Thread {
                    MysqDatabase.insertCopyResult(
                        Cmd_Og.tx_memory.toString(),
                        "10-1-0"
                    );
                }.start()
                request.responseValue["result"]=false
            }
            request.finish()
        }
    }
    var getMmy=JavaScriptInterFace("getMMY") { request ->
        val mapArray: ArrayList<MutableMap<String, Any>> = arrayListOf()
        OgPublic.getInstance.itemDAO.query(request.receiveValue["string"].toString()) {
            val map: MutableMap<String, Any> = mutableMapOf()
            for (a in 0 until it.columnCount) {
                if (it.getString(a) != null) {
                    map[it.getColumnName(a)] = it.getString(a)
                }
            }
            mapArray.add(map)
        }
        request.responseValue["result"] = true
        request.responseValue["data"] = mapArray
        request.finish()
    }
    var getLan=JavaScriptInterFace("getLan") { request ->
        val language: MutableMap<String, String> = mutableMapOf()
        OgPublic.getInstance.onlinelanDB.query(
            "select `${
                JzActivity.getControlInstance().getLanguage()
            }`,id from language"
        ) {
            language[it.getString(1)] = it.getString(0)
        }
        request.responseValue["result"] = true
        request.responseValue["data"] = language
        request.finish()
    }

    var writeFile=JavaScriptInterFace("writeFile"){
        request ->
        try {
            val data=request.receiveValue["data"].toString()
            val name=request.receiveValue["name"].toString()
            var rout=request.receiveValue["rout"].toString()
            rout=GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath
            File(rout).mkdir()
            val fileRout="${rout}/${name}"
            Log.e("writeFile",fileRout)
            val file=File(fileRout)
            file.writeText(data)
            request.responseValue["result"]=true
            request.finish()
        }catch (e:Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }

    val readFile=JavaScriptInterFace("readFile"){
        request ->
        try {
            val name=request.receiveValue["name"].toString()
            var rout=request.receiveValue["rout"].toString()
            rout=GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath
            if(File("${rout}/${name}").exists()){
                Log.e("fileSize",""+File("${rout}/${name}").readBytes().size)
                request.responseValue["data"]=File("${rout}/${name}").readText()
                request.responseValue["result"]=true
            }else{
                request.responseValue["result"]=false
            }
            request.finish()
        }catch (e:Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }

    val deleteFile=JavaScriptInterFace("deleteFile"){
            request ->
        try {
            val name=request.receiveValue["name"].toString()
            val rout=request.receiveValue["rout"].toString()
            val fileRout=File("${GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath}/${name}")
            if(fileRout.exists()){
                request.responseValue["data"]=fileRout.delete()
                request.responseValue["result"]=true
            }else{
                request.responseValue["result"]=true
            }
            request.finish()
        }catch (e:Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }
    val deleteRout=JavaScriptInterFace("deleteRout"){
            request ->
        try {
            var rout=request.receiveValue["rout"].toString()
            rout=GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath
            if(File(rout).exists()){
                File(rout).listFiles().map { it.delete() }
                request.responseValue["result"]=true
            }else{
                request.responseValue["result"]=true
            }
            request.finish()
        }catch (e:Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }
    //列出所有檔案
    val listFile=JavaScriptInterFace("listFile"){
        request ->
        try{
            var rout=request.receiveValue["rout"].toString()
            rout=GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath
            val limit=(request.receiveValue["limit"] as Double).toInt()
            val data:ArrayList<MutableMap<String,String>> = ArrayList()
            if( File(rout).exists()){
                var index=0
                for( it in  File(rout).listFiles()){
                    if(index==limit){break}
                    val map:MutableMap<String,String> = mutableMapOf()
                    map["name"]=it.name
                    map["data"]=it.readText(charset = Charsets.UTF_8)
                    Log.e("fileSize",""+it.readBytes().size)
                    data.add(map)
                    index++
                }
                request.responseValue["data"]=data
                request.responseValue["result"]=true
            }else{
                request.responseValue["result"]=false
            }
            request.finish()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }
    //列出所有檔案路徑
    val listFileRout=JavaScriptInterFace("listFileRout"){
            request ->
        try{
            var rout=request.receiveValue["rout"].toString()
            rout=GlitterActivity.instance().getExternalFilesDir(rout)!!.absolutePath
            val limit=(request.receiveValue["limit"] as Double).toInt()
            val data:ArrayList<MutableMap<String,String>> = ArrayList()
            if( File(rout).exists()){
                var index=0
                for( it in  File(rout).listFiles()){
                    if(index==limit){break}
                    val map:MutableMap<String,String> = mutableMapOf()
                    map["rout"]=it.absolutePath
                    map["name"]=it.name
                    data.add(map)
                    index++
                }
                request.responseValue["data"]=data
                request.responseValue["result"]=true
            }else{
                request.responseValue["result"]=false
            }
            request.finish()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            request.responseValue["result"]=false
            request.finish()
        }
    }
//取得ＨＳＮ/TSN
    val getHsn=JavaScriptInterFace("getHsn"){
    request->
    val mmyList = PublicBean.getMmyByHsn(
        request.receiveValue["hsn"].toString(),
        request.receiveValue["tsn"].toString()
    )
    request.responseValue["data"]=mmyList
    request.responseValue["result"]=true
    request.finish()
}
    //顯示土師
    @SuppressLint("ShowToast")
    val toast=JavaScriptInterFace("toast"){
        request->
        handler.post {
            Toast.makeText(GlitterActivity.instance(),request.receiveValue["text"].toString(),Toast.LENGTH_SHORT).show()
            request.finish()
        }

    }
    //讀取RFID
    val readRFID=JavaScriptInterFace("readRFID"){
        request ->
        Cmd_Rfid.startRfid()
        Cmd_Rfid.readRfid {
            if(it!=null){
                val rfid=it
                Cmd_Rfid.readUserArea(rfid){ it2 ->
                    request.responseValue["rfid"]=it
                    request.responseValue["dot"]=it2.toString()
                    request.responseValue["result"]=(it2!=null)
                    request.finish()
                }
            }else{
                request.responseValue["result"]=false
                request.finish()
            }
        }
    }
    //寫入RFID
    val writeRFID=JavaScriptInterFace("writeRFID"){
            request ->
        Cmd_Rfid.startRfid()
        Cmd_Rfid.writeUserArea(request.receiveValue["rfid"].toString(),request.receiveValue["dot"].toString()){ it2 ->
            request.responseValue["result"]=it2
            request.finish()
        }
    }
    //打開RFID
    val openRFID=JavaScriptInterFace("openRFID"){
        request ->
        Cmd_Rfid.startRfid()
        request.finish()
    }
    //讀取RfID Sensor
    val readRfSensor=JavaScriptInterFace("readRfSensor"){
            request ->
        Cmd_Og.openTpms()
        Cmd_Og.rfidRead{
            if(it==null){
                request.responseValue["result"]=false
            }else{
                val map:MutableMap<String,Any> = mutableMapOf()
                map["id"]=it.id
                map["rfid"]=it.rfIdData
                map["storage"]=it.storageData
                request.responseValue["data"]=map
                request.responseValue["result"]=true
            }
            request.finish()
        }

    }
    //寫入RfID Sensor
    val writeRfSensor=JavaScriptInterFace("writeRfSensor"){
            request ->
        Cmd_Og.openTpms()
        Cmd_Og.rfidRead{
            if(it==null){
                request.responseValue["result"]=false
            }else{
                Cmd_Og.writeRfSensor(request.receiveValue["data1"].toString(),request.receiveValue["data2"].toString()){
                    request.responseValue["result"]=it
                    request.finish()
                }
            }
        }
    }
}