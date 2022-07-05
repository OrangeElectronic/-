package com.orange.tpms.ue.kt_frag

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.Ad_Program_Adpter
import com.orange.tpms.model.FunBean
import com.orange.tpms.ue.dialog.DialogDefine
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orange.tpms.ue.tire_hotel.Frag_Tire_Hotel_List_Detail
import com.orango.electronic.orange_og_lib.Callback.Program_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.getPrIdAuto
import com.orango.electronic.orange_og_lib.Dialog.single_program
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.fragment_frag__program__detail.view.*
import kotlinx.android.synthetic.main.program_false.*
import java.lang.Exception

//TireHotel需要上傳資料
class Frag_New_ProgramDetail(var upload: Boolean = false) :
    RootFragement(R.layout.fragment_frag__program__detail), Program_C {
    override fun prograam_NotProceed(type: Int) {
        handler.post { JzActivity.getControlInstance().closeDiaLog() }
        when (type) {
            //TimeOut
            -1 -> {
                Program_Finish(false)
                return
            }
            //2代超過一顆
            -2 -> {
                onlySupport1()
                return
            }
            //有2代和3代所以不燒錄
            -3 -> {
                onlySupport1()
                return
            }
            //沒有2代Code
            -4 -> {
                JzActivity.getControlInstance().getHandler().post {
                    if (!PublicBean.getSIISensor()) {
                        JzActivity.getControlInstance()
                            .showDiaLog(
                                true,
                                false,
                                object :
                                    SetupDialog(com.orango.electronic.orange_og_lib.R.layout.errorsensor) {
                                    override fun dismess() {
                                        JzActivity.getControlInstance().goMenu()
                                    }

                                    override fun keyevent(event: KeyEvent): Boolean {
                                        return true
                                    }

                                    override fun setup(rootview: Dialog) {
                                        rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.textView4).text =
                                            "jz.464".getFix()
                                    }
                                },
                                "single_program"
                            )
                    }
                }
            }
            0, 1 -> {
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().closeDiaLog()
                    JzActivity.getControlInstance().showDiaLog(true, false, single_program(type), "single_program")
                }
                return
            }
        }
    }

    var reProgram_times = 0
    lateinit var programAdapter: Ad_Program_Adpter
    var sensordara = ArrayList<SensorData>()
    override fun Program_Progress(i: Int) {
        if (!JzActivity.getControlInstance().getNowPageTag().equals("Frag_Program_Detail")) {
            JzActivity.getControlInstance().closeDiaLog("data_loading")
            return
        }
        handler.post {
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
                        if (i == 100) {
                            rootview.pass.text = "95%"
                        } else {
                            rootview.pass.text = "$i%"
                        }
                    }

                }, "data_loading")
        }
    }

    override fun Program_Finish(re: Boolean) {
        if (!JzActivity.getControlInstance().getNowPageTag().equals("Frag_Program_Detail")) {
            JzActivity.getControlInstance().closeDiaLog("data_loading")
            return
        }
        if (re) {
            /********************************COPY ID方式****************************************/
            val idCopy:()->Unit = {
                Log.e("DATA:", "燒錄成功")
                val copyId = ArrayList<String>()
                val sens=sensordara.filter { it.programResult != SensorData.STATE_SUCCESS }
                sens.map {
                    if(arrayOf("HOSTKA03","HOSWA306").indexOf(PublicBean.getS19())!=-1){
                        val id=StringBuffer(it.id.substring(2,8)+it.id.substring(0,2))
                        if(PublicBean.getS19()=="HOSTKA03"){
                            id.setCharAt(7,"8".toCharArray()[0])
                        }else{
                            id.setCharAt(7,"5".toCharArray()[0])
                        }
                        copyId.add(id.toString())
                    }else{
                        val id=StringBuffer(PublicBean.getDefineId())
                        for(a in id.indices){
                            if(arrayOf("O","X").indexOf(id.substring(a,a+1))!=-1){
                                id.setCharAt(a,it.id.substring(a,a+1).toCharArray()[0])
                            }
                        }
                        copyId.add(id.toString())
                    }
                }
                if(sens.isEmpty()){
                    Thread {
                        MysqDatabase.InsertMemory(Cmd_Og.tx_memory.toString(), "success",sensordara)
                    }.start()
                }else{
                    Cmd_Og.IdCoopy(copyId, copyId) {
                        Thread {
                            MysqDatabase.InsertMemory(
                                Cmd_Og.tx_memory.toString(),
                                "13-${sens.size}-${it.size}",
                                sensordara
                            )
                        }.start()
                        sensordara.map { ait ->
                            if (ait.programResult != SensorData.STATE_SUCCESS) { ait.programResult = SensorData.STATE_FAILED }
                            it.map {
                                if (ait.id.substring(8 - ait.idcount + 2) == it.id.substring(8 - it.idcount + 2)) {
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
                                    Log.e("sensordara", "${ait.kpa} | ${ait.c} | ${ait.bat} | ${ait.有無胎溫} | ${ait.有無電池}")
                                }
                            }
                        }
                    }
                }
            }
            /********************************自動讀取的方式****************************************/
            val autoRead: () -> Unit = {
                val sensorDataList = getPrIdAuto()
                sensordara.map { ait ->
                    if (ait.programResult != SensorData.STATE_SUCCESS) {
                        ait.programResult = SensorData.STATE_FAILED
                    }
                    sensorDataList.map {
                        Log.e("compare", "${ait.id.substring(8 - ait.idcount + 2)}")
                        Log.e("compare2", "${it.id.substring(8 - it.idcount + 2)}")
                        if (ait.id.substring(8 - ait.idcount + 2) == it.id.substring(8 - it.idcount + 2)) {
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
                            Log.e("sensordara", "${ait.kpa} | ${ait.c} | ${ait.bat} | ${ait.有無胎溫} | ${ait.有無電池}")
                        }
                    }
                }
                idCopy()
            }
            if (!OgPublic.autoRead) {
                Log.e("autoRead","autoRead")
                autoRead()
            } else {
                Log.e("idCopy","idCopy")
                idCopy()
            }
        } else {
            Log.e("Frag_New_ProgramDetail", "沒有成功")
            sensordara.map { ait ->
                if (ait.programResult != SensorData.STATE_SUCCESS) {
                    ait.programResult = SensorData.STATE_FAILED
                }
            }
        }
        handler.post {
            OgPublic.getInstance.playBeepSoundAndVibrate()
            JzActivity.getControlInstance().closeDiaLog()
            if (sensordara.filter { it.programResult == SensorData.STATE_SUCCESS }.size == PublicBean.ProgramNumber) {
                rootview.imageView49.setOnClickListener {
                    DialogDefine.sendDataToCustomer(sensordara)
                }
                rootview.textView8.text = "jz.275".getFix()
                rootview.textView8.setTextColor((0xFF1F333E).toInt())
                (JzActivity.getControlInstance()
                    .getActionBar() as Frag_Manager).back.setImageResource(
                    R.mipmap.menu
                )
                (JzActivity.getControlInstance()
                    .getActionBar() as Frag_Manager).back.setOnClickListener {
                    JzActivity.getControlInstance().goMenu()
                }
                if (upload) {
                    rootview.bt_program.text = "Hochladen"
                    rootview.bt_program.setTextColor(resources.getColor(R.color.color_orange))
                } else {
                    rootview.bt_program.text =
                        JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.Relearn_Procedure)
                }
                rootview.bt_program.setOnClickListener {
                    if (upload) {
                        Frag_Tire_Hotel_List_Detail.tpms.map { ite ->
                            sensordara.map {
                                Log.e(
                                    "wheel",
                                    "it.wheelPosition${it.wheelPosition}==ite[\"wheelPosition\"]==${ite["wheelPosition"]}"
                                )
                                if (it.wheelPosition == ite["wheelPosition"]) {
                                    ite["sensorID"] = it.id
                                    ite["bat"] = it.bat
                                    ite["pressure"] = "${it.kpa}"
                                    ite["temp"] = "${it.c}"
                                }
                            }
                        }
                        Frag_Tire_Hotel_List_Detail.uploadData()
                    } else {
                        JzActivity.getControlInstance().changePage(Frag_Relearm_Detail(), "Frag_Relearm_Detail", true)
                    }
                }
                rootview.bt_menue.text = "jz.118".getFix()
                rootview.bt_menue.setOnClickListener {
                    JzActivity.getControlInstance().goBack()
                }
            } else {
                rootview.bt_menue.text = "jz.9".getFix()
                rootview.bt_menue.setOnClickListener {
                    JzActivity.getControlInstance().goMenu()
                }
                rootview.bt_program.text = "jz.288".getFix()
                checkrepro()
            }
            programAdapter.notifyDataSetChanged()
        }
    }

    fun checkrepro() {
        //燒錄兩次失敗
        reProgram_times += 1
        FunBean.ReProgram(reProgram_times)
    }

    override fun viewInit() {
        rootview.fixLanguage()
        if (upload) {
            for (i in PublicBean.readable.indices) {
                if (PublicBean.readable[i]) {
                    sensordara.add(
                        SensorData(
                            arrayOf(
                                "LF",
                                "RF",
                                "RR",
                                "LR",
                                "SP"
                            )[i], arrayOf(
                                resources.jzString(R.string.app_fl),
                                resources.jzString(R.string.app_fr),
                                resources.jzString(R.string.app_rr),
                                resources.jzString(R.string.app_rl),
                                "SP"
                            )[i]
                        )
                    )
                }
            }
            PublicBean.ProgramNumber = sensordara.size
        } else {
            for (i in 0 until PublicBean.ProgramNumber) {
                sensordara.add(SensorData())
            }
        }
        rootview.tv_program_title.text = "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        rootview.bt_menue.setOnClickListener { GoMenu() }
        rootview.bt_program.setOnClickListener { Program() }
        Thread{
         handler.post {  JzActivity.getControlInstance()
                .showDiaLog(R.layout.dia_program_info, true, true, "dia_program_info") }
        }.start()

        rootview.rv_program.layoutManager = LinearLayoutManager(context)
        programAdapter = Ad_Program_Adpter(sensordara, this)
        rootview.rv_program.adapter = programAdapter

        PublicBean.getSIISensor()
    }

    override fun onKeyScan() {
        super.onKeyScan()
        val a = sensordara.filter { it.id.isEmpty() }
        if (a.isNotEmpty()) {
            PublicBean.scanSensor(object : PublicBean.scanBack {
                override fun getSensor(id: String) {
                    for (i in a) {
                        sensordara.map {
                            if (it.id == id) {
                                it.id = ""
                            }
                        }
                        i.id = id
                        programAdapter.notifyDataSetChanged()
                        return
                    }
                }
            })
        }
    }

    override fun onKeyTrigger() {
        super.onKeyTrigger()
        if (sensordara.filter { it.id.isNotEmpty() }.size == sensordara.size) {
            Program()
        } else {
            trigger()
        }
    }

    fun trigger() {
        try {
            Thread {
                TaskHandler.newInstance.runTaskOnce("onCommandRunning") {
                    MysqDatabase.programClock.Zeroing()
                    handler.post { EmptyDialog(R.layout.data_loading).show() }
                    val cancelUnit:()->Unit = { JzActivity.getControlInstance().closeDiaLog() }
                    var a = Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX())
                    if(Cmd_Og.cancel){
                        cancelUnit()
                        return@runTaskOnce
                    }else{
                        Thread.sleep(1000)
                        a = Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX(), false)
                        if(Cmd_Og.cancel){
                            cancelUnit()
                            return@runTaskOnce
                        }
                    }
                    //當讀取失敗時上傳錯誤回報
                    if ((a.type != "success") && (a.type != "cancel")) {
                        MysqDatabase.InsertMemory(Cmd_Og.tx_memory.toString(), a.type, Cmd_Og.sensorData)
                    }
                    for (i in 0 until a.sensorData.size) {
                        a.sensorData[i].wheelPosition = sensordara[i].wheelPosition
                        a.sensorData[i].wheelString = sensordara[i].wheelString
                        sensordara[i] = a.sensorData[i]
                        sensordara[i].id = a.sensorData[i].id
                    }
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog()
                        if (a.sensorData.size < PublicBean.ProgramNumber) {
                            if (a.sensorData.size == 0) {
                                JzActivity.getControlInstance()
                                    .toast("jz.300".getFix())
                            } else {
                                JzActivity.getControlInstance()
                                    .showDiaLog(false, false, object : SetupDialog(R.layout.program_number_rechose) {
                                        override fun dismess() {}
                                        override fun keyevent(event: KeyEvent): Boolean {
                                            if (event.keyCode == 4) {
                                                Cmd_Og.cancel = true
                                            }
                                            return false
                                        }

                                        override fun setup(rootview2: Dialog) {
                                            rootview2.ok.setOnClickListener {
                                                JzActivity.getControlInstance().closeDiaLog()
                                            }
                                            rootview2.yes.setOnClickListener {
                                                checkSupport()
                                                PublicBean.ProgramNumber = a.sensorData.size
                                                sensordara = a.sensorData
                                                programAdapter = Ad_Program_Adpter(sensordara, this@Frag_New_ProgramDetail)
                                                rootview.rv_program.adapter = programAdapter
                                                JzActivity.getControlInstance().closeDiaLog()
                                            }
                                        }
                                    }, "program_number_rechose")
                            }
                            return@post
                        } else {
                            checkSupport()
                        }
                    }
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkSupport() {
        OgPublic.getInstance.playBeepSoundAndVibrate()
        JzActivity.getControlInstance().closeDiaLog()
        if (sensordara.filter { it.senesorType == "S2" }
                .isNotEmpty() && !PublicBean.getSIISensor()) {
            JzActivity.getControlInstance().getHandler().post {
                if (!PublicBean.getSIISensor()) {
                    JzActivity.getControlInstance()
                        .showDiaLog(
                            true,
                            false,
                            object :
                                SetupDialog(com.orango.electronic.orange_og_lib.R.layout.errorsensor) {
                                override fun dismess() {
                                    JzActivity.getControlInstance().goMenu()
                                }

                                override fun keyevent(event: KeyEvent): Boolean {
                                    return true
                                }

                                override fun setup(rootview: Dialog) {
                                    rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.textView4).text =
                                        "jz.464".getFix()
                                }
                            },
                            "single_program"
                        )
                }
            }
            return
        }
        programAdapter.notifyDataSetChanged()
        if (PublicBean.ProgramNumber > 1 && sensordara.filter { it.senesorType == "S2" }.isNotEmpty()) {
            onlySupport1()
        } else {
            Program()
        }
    }

    fun onlySupport1() {
        PublicBean.ProgramNumber = 1
        JzActivity.getControlInstance()
            .showDiaLog(true, false, object : SetupDialog(R.layout.single_program) {
                override fun dismess() {
                    JzActivity.getControlInstance()
                        .changePage(Frag_New_ProgramDetail(), "Frag_Program_Detail", false)
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return true
                }

                override fun setup(rootview: Dialog) {
                    rootview.findViewById<TextView>(R.id.textView4).text = "jz.463".getFix()
                }
            }, "single_program")
    }

    fun Program() {
        MysqDatabase.programClock.Zeroing()
        PublicBean.SensorList = sensordara
        if (sensordara.filter { it.id.isNotEmpty() }.size == sensordara.size) {
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
                        rootview.pass.text = "0%"
                    }

                }, "data_loading")
            val thread=Thread{
                Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX())
                if(Cmd_Og.cancel){
                    handler.post { JzActivity.getControlInstance().closeDiaLog() }
                    return@Thread
                }
                TaskHandler.newInstance.runTaskOnce("onCommandRunning") {
                    PublicBean.SensorType = if (sensordara.any { it.senesorType == "S2" }) "S2" else "S3"
                    Cmd_Og.Program(
                        "00",
                        PublicBean.getHEX(),
                        Integer.toHexString(PublicBean.ProgramNumber),
                        PublicBean.getS19(),
                        this, sensordara
                    )
            }
            }
            thread.start()
        } else {
            onKeyTrigger()
        }
    }

}