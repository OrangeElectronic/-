package com.orange.og_lite

import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.orange.og_lite.Util.fixLanguage
import com.example.jztaskhandler.TaskHandler
import com.jianzhi.jzblehelper.FormatConvert.StringHexToByte
import com.jianzhi.jzblehelper.FormatConvert.bytesToHex
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.*
import com.orange.og_lite.beans.PublicBeans.Companion.DongleState
import com.orange.og_lite.callback.Copy_C
import com.orange.og_lite.callback.Program_C
import com.orange.og_lite.callback.Update_C
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.loading_view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.xor

class Command {
    //abc
    companion object {
        var rx = ""
        var onProgram:Boolean = false
        var lastFinish=TaskHandler.newInstance.clock()
        var obdCommand = ObdCommand()
        var ogCommand = OgCommand()
        val handler: Handler
            get() {
                return JzActivity.getControlInstance().getRootActivity().handler
            }
        var demo = false
        var clock = TaskHandler.newInstance.clock()
        private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
        private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
        fun Send(a: String) {
            try {
                if(!OgCommand.canSend){return}
                Thread.sleep(100)
                OgCommand.waitTx =true
                rx = ""
                if (!(JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()
                ) {
                    handler.post {
                        (JzActivity.getControlInstance()
                            .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                    }
                    return
                }
                if(!(JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(
                        addcheckbyte(a),
                        rxUUID,
                        TXUUID
                    )){
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.disconnect()
                    Thread.sleep(100)
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                    return
                }
                val clock=TaskHandler.newInstance.clock()
                while (OgCommand.waitTx){
                    if(clock.stop()>1){
                        (JzActivity.getControlInstance()
                            .getRootActivity() as MainActivity).manager.Ble_Helper.disconnect()
                        Thread.sleep(100)
                        (JzActivity.getControlInstance()
                            .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                        break
                    }
                    Thread.sleep(100)
                }
            }catch (e:Exception){e.printStackTrace()}

        }

        fun addcheckbyte(com: String): String {
            val a = StringHexToByte(com)
            var checkbyte = a[0]
            for (i in 1 until a.size - 2) {
                checkbyte = checkbyte xor a[i]
            }
            a[a.size - 2] = checkbyte
            return bytesToHex(a)
        }

        interface callback {
            fun result(a: Boolean)
        }

        interface idback {
            fun result(id: String, pre: String, tem: String, bat: String)
        }

        interface notify {
            fun result()
        }


        fun updateOgMcu() {
            JzActivity.getControlInstance()
                .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.jzString(R.string.Updating)
                    }
                }, "loading_view")

            Thread {
                ogCommand.WriteBootloader(132, object : Update_C {
                    override fun Finish(a: Boolean) {
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance().toast(if (a) "success" else "false")
                        }
                    }

                    override fun Updateing(progress: Int) {
                        handler.post {
                            JzActivity.getControlInstance()
                                .showDiaLog(
                                    false,
                                    false,
                                    object : SetupDialog(R.layout.loading_view) {
                                        override fun dismess() {

                                        }

                                        override fun keyevent(event: KeyEvent): Boolean {
                                            return false
                                        }

                                        override fun setup(rootview: Dialog) {
                                            rootview.tit.text =
                                                rootview.context.resources.jzString(R.string.Updating) + "...${progress}%"
                                        }
                                    },
                                    "loading_view"
                                )
                        }

                    }
                })
            }.start()

        }


        fun getObd(myitem: ObdBeans, result: callback) {
            JzActivity.getControlInstance()
                .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.jzString(R.string.wait)
                    }
                }, "loading_view")
            if (demo) {
                Thread {
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog("loading_view")
                        val geta = true
                        if (geta) {
                            for (i in 0 until myitem.OldSemsor.count()) {
                                myitem.OldSemsor[i] = "0000000$i"
                            }
                        }
                        result.result(geta)
                    }
                }.start()
            } else {
                Thread {
                    if (goState(BootloaderState.Bootloader)) {
                        val a = obdCommand.loadObdApp()
                        if (a) {
                            Thread.sleep(3000)
                            if (obdCommand.gerID(myitem)) {
                                Thread.sleep(1000)
                                handler.post {
                                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                                    result.result(true)
                                }
                            } else {
                                handler.post {
                                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                                    result.result(false)
                                }
                            }
                        } else {
                            handler.post {
                                MysqDatabase.insertOBD(
                                    OgCommand.tx_memory.toString(),
                                    "LoadingFalse"
                                )
                                JzActivity.getControlInstance().closeDiaLog("loading_view")
                                result.result(false)
                            }
                        }
                    } else {
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                            result.result(false)
                        }
                    }

                }.start()
            }
        }

        fun getSensorID(myitem: ArrayList<TextView>, result: callback) {
            JzActivity.getControlInstance()
                .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.jzString(R.string.wait)
                    }
                }, "loading_view")
            Thread {
                Thread.sleep(3000)
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog("loading_view")
                    val geta = true
                    if (geta) {
                        for (i in 0 until myitem.count()) {
                            myitem[i].text = "0000000$i"
                        }
                    }
                    result.result(geta)
                }
            }.start()

        }

        fun getSensorData(myitem: Function_Check_Sensor_Item, resultt: callback) {
            if(myitem.focus==-1){return}
            JzActivity.getControlInstance()
                .showDiaLog(R.layout.data_loading, false, true, "data_loading")
            if (demo) {
                Thread {
                    Thread.sleep(3000)
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog("loading_view")
                        val geta = true
                        if (geta) {

                            var a = arrayOf("aaaaaaaa", "bbbbbbbb", "cccccccc", "dddddddd").random()
                            for (i in 0 until myitem.ID.count()) {
                                if (myitem.ID[i] == a) {
                                    myitem.ID[i] = ""
                                    myitem.Psi[i] = ""
                                    myitem.C[i] = ""
                                    myitem.BAT[i] = ""
                                    myitem.VOLT[i] = ""
                                    break
                                }
                            }

                            JzActivity.getControlInstance().closeDiaLog()

                            if (!myitem.ID.contains("")) {
                                myitem.focus = -1
                                return@post
                            }

                            myitem.ID[myitem.focus] = a

                            for (i in 0 until myitem.ID.count()) {
                                if (myitem.ID[i] == "") {
                                    myitem.focus = i
                                    break
                                }
                            }

//                            for (i in 0 until myitem.ID.count()) {
//                                myitem.ID[i] = "0000000$i"
//                            }
                        }
                        resultt.result(geta)
                    }
                }.start()
            } else {
                Thread {
                    if (goState(BootloaderState.Og_App)) {
                        ogCommand.Send("0AE100030100F5")
                        Thread.sleep(200)
                        val result = ogCommand.GetId()
                        ogCommand.Send("0AE100030200F5")
                        Thread.sleep(200)
                        ogCommand.叫一下()
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog()
                            if (result.success) {
                                for (i in 0 until myitem.ID.count()) {
                                    if (myitem.ID[i] == result.id) {
                                        myitem.ID[i] = ""
                                        myitem.Psi[i] = ""
                                        myitem.C[i] = ""
                                        myitem.BAT[i] = ""
                                        myitem.VOLT[i] = ""
                                        break
                                    }
                                }
                                  Log.e("myitem.focus","${myitem.focus}")
                                myitem.ID[myitem.focus] = result.id
                                 myitem.Psi[myitem.focus] = "" + result.kpa
                                myitem.C[myitem.focus] = if (result.有無胎溫) "" + result.c else "NA"
                                myitem.BAT[myitem.focus] =
                                    "${if (result.有無電池) if (result.bat == "1") "ok" else "low" else "NA"}"
                                myitem.VOLT[myitem.focus] =
                                    if (result.有無電壓) "" + result.vol else "NA"

                                if (!myitem.ID.contains("")) {
                                    myitem.focus = -1
                                    resultt.result(true)
                                    return@post
                                }

                                for (i in 0 until myitem.ID.count()) {
                                    if (myitem.ID[i] == "") {
                                        myitem.focus = i
                                        break
                                    }
                                }
                                resultt.result(true)
//                                for (i in 0 until myitem.ID.count()) {
//                                    if (myitem.ID[i] == "" && i != 0) {
//                                        myitem.ID[i] = result.id
//                                        myitem.Psi[i] = "" + result.kpa
//                                        myitem.C[i] = if (result.有無胎溫) "" + result.c else "NA"
//                                        myitem.BAT[i] =
//                                            "${if (result.有無電池) if (result.bat == "1") "ok" else "low" else "NA"}"
//                                        myitem.VOLT[i] = if (result.有無電壓) "" + result.vol else "NA"
//                                        break
//                                    }
//                                }

                            } else {
                                resultt.result(false)
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                        }
                    } else {
                        handler.post {
                            resultt.result(false)
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    }
                }.start()
            }
        }

        fun readId(result: idback) {
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {
                        BleManager.toggleReadSensor=true
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        BleManager.toggleReadSensor=false
                        rootview.tit.visibility = View.GONE
                        rootview.cancel.visibility = View.VISIBLE
                        rootview.cancel.setOnClickListener {
                            ogCommand.cancel = true
                        }
                    }
                }, "loading_view")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog("loading_view")
                result.result(
                    arrayOf("aaaaaaaa", "bbbbbbbb", "cccccccc", "dddddddd").random(),
                    "NA",
                    "NA",
                    "NA"
                )
            } else {
                Thread {
                    if (goState(BootloaderState.Og_App)) {
                        ogCommand.Send("0AE100030100F5")
                        Thread.sleep(200)
                        val a = ogCommand.GetId()
                        ogCommand.Send("0AE100030200F5")
                        Thread.sleep(200)
                        ogCommand.叫一下()
                        handler.post {
                            if (a.success) {
                                result.result(a.id, "NA", "NA", "NA")
                            } else {
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    } else {
                        handler.post {
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }
                }.start()
            }
        }

        fun getPrid(result: idback) {
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {
                        BleManager.toggleReadSensor=true
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        BleManager.toggleReadSensor=false
                        rootview.tit.visibility = View.GONE
                        rootview.cancel.visibility = View.VISIBLE
                        rootview.cancel.setOnClickListener {
                            ogCommand.cancel = true
                        }
                    }
                }, "loading_view")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog("loading_view")
                result.result(
                    arrayOf("aaaaaaaa", "bbbbbbbb", "cccccccc", "dddddddd").random(),
                    "NA",
                    "NA",
                    "NA"
                )
            } else {
                Thread {
                    if (goState(BootloaderState.Og_App)) {
                        onProgram=true
                        ogCommand.Send("0AE100030100F5")
                        Thread.sleep(200)
                        var a=ogCommand.GetPr("00", PublicBeans.programNumber, PublicBeans.getHEX())
                        if(!ogCommand.cancel){
                            Thread.sleep(5000)
                             a = ogCommand.GetPr("00", PublicBeans.programNumber, PublicBeans.getHEX())
                        }
                        Thread.sleep(200)
                        ogCommand.Send("0AE100030200F5")
                        Thread.sleep(200)
                        ogCommand.叫一下()
                        onProgram=false
                        handler.post {
                            if (a.size >= 0) {
                                for (i in a) {
                                    result.result(
                                        i.id,
                                        "" + i.kpa,
                                        if (i.有無胎溫) "" + i.c else "NA",
                                        "${if (i.有無電池) if (i.bat == "1") "ok" else "low" else "NA"}"
                                    )
                                }
                            } else {
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.data_loading_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    } else {
                        handler.post {
                            JzActivity.getControlInstance().showDiaLog(
                                R.layout.data_loading_false,
                                true,
                                false,
                                "data_loading_false"
                            )
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                    }
                }.start()
            }
        }

        fun 叫一下() {
            if (goState(BootloaderState.Bootloader)) {
                Send("0AE200030000F5")
            }
        }

        fun writeID(myitem: ObdBeans, resultn: notify) {
            PublicBeans.lastFunction=myitem
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {
                        BleManager.toggleReadSensor=true
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = JzActivity.getControlInstance()
                            .getRootActivity().resources.jzString(R.string.Programming)
                        BleManager.toggleReadSensor=false

                    }
                }, "loading_view")
            if (demo) {
                for (i in 0 until myitem.wheelPosition.size) {
                    myitem.state[i] =
                        arrayListOf(ObdBeans.PROGRAM_SUCCESS, ObdBeans.PROGRAM_FALSE).random()

                }
                JzActivity.getControlInstance().closeDiaLog()
                resultn.result()
            } else {
                Thread {
                    var have100 = false
                    if (goState(BootloaderState.Og_App)) {
                        ogCommand.Program(
                            Integer.toHexString(myitem.getReadable()),
                            object : Program_C {
                                override fun Program_Progress(i: Int) {
                                    handler.post {
                                        JzActivity.getControlInstance()
                                            .showDiaLog(

                                                false,
                                                true,
                                                object : SetupDialog(R.layout.loading_view) {
                                                    override fun dismess() {
                                                        BleManager.toggleReadSensor=true
                                                    }

                                                    override fun keyevent(event: KeyEvent): Boolean {
                                                        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                                                            ogCommand.cancel = true
                                                        }
                                                        return false
                                                    }

                                                    override fun setup(rootview: Dialog) {
                                                        BleManager.toggleReadSensor=false
                                                        rootview.tit.visibility = View.VISIBLE
                                                        if (i > 95 || have100) {
                                                            have100 = true
                                                            rootview.tit.text =
                                                                rootview.context.resources.jzString(
                                                                    R.string.Programming
                                                                ) + "...95%"
                                                        } else {
                                                            rootview.tit.text =
                                                                rootview.context.resources.jzString(
                                                                    R.string.Programming
                                                                ) + "...$i%"
                                                        }
                                                    }
                                                },
                                                "loading_view"
                                            )
                                    }
                                }

                                override fun Program_Finish(a: Boolean) {
                                    if (a) {
                                        Log.e("DATA:", "燒錄成功")
                                        Thread.sleep(3000)
                                        val result = ogCommand.GetPrId()
                                        for (i in result) {
                                            for (check in 0 until myitem.getNewSensorReader().size) {
                                                val d = myitem.getNewSensorReader()[check]
                                                if (d.substring(
                                                        d.length - 4,
                                                        d.length
                                                    ) == i.id.substring(
                                                        4,
                                                        8
                                                    )
                                                ) {
                                                    myitem.replaceOldSensor(d, i.id)
                                                }
                                            }
                                            Log.e("DATA:", "成功id:" + i.id)
                                        }
                                        Thread {
                                            Thread.sleep(2000)
                                            ogCommand.IdCopy(object : Copy_C {
                                                var resultc = true
                                                override fun Copy_Finish(a: Boolean) {
                                                    ogCommand.叫一下()
                                                    handler.post {
                                                        if (!a) {
                                                            resultc = false
                                                            for (i in 0 until myitem.wheelPosition.size) {
                                                                myitem.state[i] =
                                                                    ObdBeans.PROGRAM_FALSE
                                                            }
                                                        }
                                                        MysqDatabase.insertCopyResult(
                                                            OgCommand.tx_memory.toString(),
                                                            if (resultc) "success" else "timeout"
                                                        );
                                                        JzActivity.getControlInstance()
                                                            .closeDiaLog()
                                                        resultn.result()
                                                    }
                                                }

                                                override fun Copy_Next(
                                                    success: Boolean,
                                                    position: Int
                                                ) {
                                                    handler.post {
                                                        JzActivity.getControlInstance().showDiaLog(

                                                            false,
                                                            true,
                                                            object :
                                                                SetupDialog(R.layout.loading_view) {
                                                                override fun dismess() {
                                                                    BleManager.toggleReadSensor=true
                                                                }

                                                                override fun keyevent(event: KeyEvent): Boolean {
                                                                    if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                                                                        ogCommand.cancel = true
                                                                    }
                                                                    return false
                                                                }

                                                                override fun setup(rootview: Dialog) {
                                                                    rootview.tit.visibility =
                                                                        View.VISIBLE
                                                                    rootview.tit.text =
                                                                        "95%"
                                                                    BleManager.toggleReadSensor=false
                                                                }

                                                            },
                                                            "loading_view"
                                                        )
                                                    }
                                                }
                                            }, PublicBeans.getIdcount(), myitem)
                                        }.start()
                                    } else {
                                        ogCommand.叫一下()
                                        handler.post {
                                            JzActivity.getControlInstance().closeDiaLog()
                                            for (i in 0 until myitem.wheelPosition.size) {
                                                myitem.state[i] = ObdBeans.PROGRAM_FALSE
                                            }
                                            resultn.result()
                                        }

                                    }
                                }
                            }
                            , myitem.getNewSensorReader()
                        )
                    } else {
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                            resultn.result()
                        }
                    }
                }.start()

            }

        }

        fun checkID(myitem: ArrayList<TextView>, image: ArrayList<TextView>, result: notify) {

            CheckBeans.CHECK =
                (arrayListOf(CheckBeans.CHECK_SUCCESS, CheckBeans.CHECK_FALSE).random())

            result.result()
        }

        fun writeOBD(myitem: ObdBeans, result: notify) {
            JzActivity.getControlInstance()
                .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = rootview.context.resources.jzString(R.string.wait)
                    }
                }, "loading_view")
            if (demo) {
                for (i in 0 until myitem.wheelPosition.size) {
                    myitem.state[i] =
                        arrayListOf(ObdBeans.PROGRAM_SUCCESS, ObdBeans.PROGRAM_FALSE).random()
                }
                result.result()
                JzActivity.getControlInstance().closeDiaLog("loading_view")
            } else {
                Thread {
                        val a = obdCommand.setTire(myitem)
                        for (i in 0 until myitem.wheelPosition.size) {
                            myitem.state[i] =
                                if (a) ObdBeans.PROGRAM_SUCCESS else ObdBeans.PROGRAM_FALSE
                            myitem.OldSemsor[i] = myitem.NewSensor[i]
                        }
                        handler.post {
                            result.result()
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                        }
                }.start()
            }
        }

        fun Program(myitem: Function_Program_Item, resultt: notify) {
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.loading_view) {
                    override fun dismess() {
                        BleManager.toggleReadSensor=true
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                            ogCommand.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        BleManager.toggleReadSensor=false
                        rootview.tit.visibility = View.GONE
                        rootview.cancel.visibility = View.VISIBLE
                        rootview.cancel.setOnClickListener {
                            ogCommand.cancel = true
                        }
                    }
                }, "loading_view")
            if (demo) {
                JzActivity.getControlInstance().closeDiaLog()
                for (i in 0 until myitem.rowcount) {
                    myitem.state[i] = arrayListOf(
                        Function_Program_Item.PROGRAM_SUCCESS,
                        Function_Program_Item.PROGRAM_FALSE
                    ).random()
                }
                resultt.result()
            } else {
                Thread {

                    if (goState(BootloaderState.Og_App)) {
                        ogCommand.Send("0AE100030100F5")
                        Thread.sleep(200)
                        ogCommand.GetPr("00", PublicBeans.programNumber, PublicBeans.getHEX())
                        Thread.sleep(1000 * 5)
                        var program100 = false
                        ogCommand.Program(
                            Integer.toHexString(myitem.rowcount),
                            object : Program_C {
                                override fun Program_Progress(i: Int) {
                                    handler.post {
                                        JzActivity.getControlInstance().showDiaLog(

                                            false,
                                            true,
                                            object : SetupDialog(R.layout.loading_view) {
                                                override fun dismess() {
                                                    BleManager.toggleReadSensor=true
                                                }

                                                override fun keyevent(event: KeyEvent): Boolean {
                                                    if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                                                        ogCommand.cancel = true
                                                    }
                                                    return false
                                                }

                                                override fun setup(rootview: Dialog) {
                                                    BleManager.toggleReadSensor=false
                                                    rootview.tit.visibility =
                                                        View.VISIBLE
                                                    if (i > 95) {
                                                        program100 = true
                                                    }
                                                    if (program100) {
                                                        rootview.tit.text =
                                                            rootview.context.resources.jzString(R.string.Programming) + "...95%"
                                                    } else {
                                                        rootview.tit.text =
                                                            rootview.context.resources.jzString(R.string.Programming) + "...$i%"

                                                    }

                                                }

                                            },
                                            "loading_view"
                                        )
                                    }
                                }

                                override fun Program_Finish(a: Boolean) {
                                    if (!a) {
                                        ogCommand.Send("0AE100030200F5")
                                        Thread.sleep(500)
                                        ogCommand.叫一下()
                                        handler.post {
                                            for (i in 0 until myitem.rowcount) {
                                                myitem.state[i] =
                                                    Function_Program_Item.PROGRAM_FALSE
                                            }
                                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                                            resultt.result()
                                        }
                                        return
                                    }
                                    val result = ogCommand.GetPrId()
                                    Thread.sleep(200)
                                    ogCommand.Send("0AE100030200F5")
                                    Thread.sleep(200)
                                    ogCommand.叫一下()
                                    handler.post {
                                        for (i in 0 until myitem.rowcount) {
                                            myitem.state[i] = Function_Program_Item.PROGRAM_FALSE
                                            for (b in result) {
                                                var compareid = myitem.programId[i]
                                                while (compareid.length < 8) {
                                                    compareid = "0${compareid}"
                                                }
                                                if (b.id.substring(8 - PublicBeans.getIdcount() + 2) == compareid.substring(
                                                        8 - PublicBeans.getIdcount() + 2
                                                    )
                                                ) {
                                                    myitem.programId[i] = b.id.substring(8 - b.idcount)
                                                    myitem.state[i] = Function_Program_Item.PROGRAM_SUCCESS
                                                    myitem.壓力[i] = if (b.success) b.kpa.toString() else "NA"
                                                    myitem.溫度[i] =if (b.success && b.有無胎溫) b.c.toString() else "NA"
                                                    myitem.電力[i] =if (b.success && b.有無電池)  (if (b.bat == "1") "ok" else "low") else "NA"
                                                }
                                            }
                                        }
                                        JzActivity.getControlInstance().closeDiaLog("loading_view")
                                        resultt.result()
                                    }
                                }
                            }, ArrayList(myitem.programId)
                        )
                    } else {
                        handler.post {
                            for (i in 0 until myitem.rowcount) {
                                myitem.state[i] = Function_Program_Item.PROGRAM_FALSE
                            }
                            JzActivity.getControlInstance().closeDiaLog("loading_view")
                            resultt.result()
                        }
                    }

                }.start()
            }
        }

        fun getState(): Boolean {
            try {
                ogCommand.Send(("0A0000030000F5"))
                clock.Zeroing()
                while (true) {
                    if (clock.stop() > 3) {
                        return false
                    }
                    if (rx.length >= 14) {
                        when (rx.substring(8, 10)) {
                            "01" -> {
                                DongleState = BootloaderState.Bootloader
                            }
                            "02" -> {
                                DongleState = BootloaderState.Og_App
                            }
                            "03" -> {
                                DongleState = BootloaderState.Obd_App
                            }
                        }
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
//        0A10000E0100002B00000000010000003FF5
//        0A10000E01000020000000000100000034F5
        fun goState(state: BootloaderState): Boolean {
            try {
//                Send(addcheckbyte("0A0D00030200F5"))
//                return true
                when (state) {
                    BootloaderState.Bootloader -> {
                        ogCommand.Send("0A0D00030000F5")
//                        if (Util_FileDowload.oldVersion) {
//                            ogCommand.Send("0A0D00030000F5")
//                        } else {
//                            return true
//                        }
                    }
                    BootloaderState.Og_App -> {
                        ogCommand.Send("0A0D00030100F5")
                    }
                    BootloaderState.Obd_App -> {
                        ogCommand.Send("0A0D00030200F5")
                    }
                }
                Thread.sleep(200)
                clock.Zeroing()
                return true
                //                    handler.post {
//                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.disconnect()
//                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
//                        JzActivity.getControlInstance()
//                            .showDiaLog( false, false,
//                                Da_Scan_ble(object : connectBack {
//                                    override fun connec() {
//                                        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.stopScan()
//                                    }
//                                }), "connectble"
//                            )
//                    }
            } catch (e: Exception) {
                Log.e("錯誤", "d")
                e.printStackTrace()
                return false;
            }
        }

        fun readSN(): Boolean {
            Log.e("SN", "跳轉成功")
            Send("0ADF00010000F5")
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return false
                }
                if (rx.length == 24) {
                    JzActivity.getControlInstance().setPro("SN", rx.substring(8, 20))
                    Log.d("BLECommand.rx", "SN:" + rx.substring(8, 20))
                    return true
                }
                Thread.sleep(100)
            }
        }

        fun setClose(a: Int): Boolean {
            Log.e("setclose", "跳轉成功")
            var time = Integer.toHexString(a)
            while (time.length < 4) {
                time = "0$time"
            }
            Send("0AE50004${time}00F5")
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return false
                }
                if (rx.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        }

        fun getBattery(): Boolean {
            Log.e("setclose", "跳轉成功")
            Send("0AEE00000000F5")
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return false
                }
                if (rx.length == 14) {
                    Log.e("getBattery", rx.substring(8, 10))
                    JzActivity.getControlInstance().setPro("battery", rx.substring(8, 10))
                    return true
                }
                Thread.sleep(100)
            }
        }
        fun ueedUpdateBootloader(): Boolean {
            try {
                Log.e("setclose", "跳轉成功")
                Send("0AEC00010000F5")
                clock.Zeroing()
                while (true) {
                    if (clock.stop() > 2) {
                        return false
                    }
                    if (rx.length == 14 && rx.substring(0,8)=="F5EC0103") {
                        JzActivity.getControlInstance().setPro("bootloaderVersion",rx.substring(8,10))
                        return true
                    }
                    Thread.sleep(100)
                }
            } catch (E: Exception) {
                E.printStackTrace()
                return false
            }
        }
    }
}