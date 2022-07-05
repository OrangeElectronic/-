package com.orange.tpms.ue.kt_frag

//
import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.tool.FormatConvert
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.Ad_obd
import com.orange.tpms.model.ObdBeans
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.tire_hotel.Frag_Tire_Hotel_List_Detail
import com.orango.electronic.orange_og_lib.Callback.Copy_C
import com.orango.electronic.orange_og_lib.Callback.Program_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Dialog.single_program
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.MysqDatabase.copyClock
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.ShowReadView
import kotlinx.android.synthetic.main.normal_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_OBD_ID_copy(var upload:Boolean=false) : RootFragement(R.layout.fragment_frag__function__obd__id_copy),
    Program_C {
    var myitem = ObdBeans()
    lateinit var adapter: Ad_obd
    override fun prograam_NotProceed(type: Int) {
        JzActivity.getControlInstance().closeDiaLog()
        when(type){
            //TimeOut
            -1->{
                Program_Finish(false)
                return
            }
            //2代超過一顆
            -2->{
                needSingle()
                return
            }
            //有2代和3代所以不燒錄
            -3->{
                needSingle()
                return
            }
            //沒有2代Code
            -4->{
                noSensorCode()
            }
            0 , 1 -> {
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().closeDiaLog()
                    JzActivity.getControlInstance()
                        .showDiaLog(true, false, single_program(type), "single_program")
                }
                return
            }
        }
    }
    override fun Program_Progress(i: Int) {
        handler.post {
            JzActivity.getControlInstance().showDiaLog(
                false,
                true,
                object : SetupDialog(R.layout.data_loading) {
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
                        rootview.pass.text = "$i%"
                    }


                },
                "data_loading"
            )
        }
    }

    override fun enter() {
        super.enter()
        adapter.position = adapter.getEmpty() / 2
        adapter.focus = adapter.getEmpty()
        adapter.notifyDataSetChanged()
        checkComplete()
    }

    override fun Program_Finish(b: Boolean) {
        if (b) {
            Log.e("DATA:", "燒錄成功")
            PublicBean.ProgramNumber = PublicBean.SensorList.size
            Thread.sleep(5000)
            val result = Cmd_Og.GetPrId(
                PublicBean.getHEX(),
                PublicBean.getLfPower()
            )
            if (PublicBean.ProgramNumber != result.size) {
                Thread {
                    MysqDatabase.insertCopyResult(Cmd_Og.tx_memory.toString(), "9-${PublicBean.ProgramNumber}-${result.size}");
                }.start()
            }
            handler.post {
                copyCommand(result)
            }
        } else {
            handler.post {
                OgPublic.getInstance.playBeepSoundAndVibrate()
                for (i in 0 until myitem.wheelPosition.size) {
                    myitem.state[i] = ObdBeans.PROGRAM_FALSE
                }
                JzActivity.getControlInstance().closeDiaLog("data_loading")
                adapter.notifyDataSetChanged()
                switchBtn()
            }
        }
    }
    fun copyCommand( sensorList: java.util.ArrayList<SensorData>){
        Thread {
            Cmd_Og.IdCopy(object : Copy_C {
                var resultc = true
                override fun Copy_Finish(a: Boolean) {
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog("data_loading")
                        OgPublic.getInstance.playBeepSoundAndVibrate()
                        if (!a) {
                            resultc = false
                            for (i in 0 until myitem.wheelPosition.size) {
                                myitem.state[i] = ObdBeans.PROGRAM_FALSE
                            }
                        }
                        Thread{
                            MysqDatabase.insertCopyResult(
                                Cmd_Og.tx_memory.toString(),
                                if (resultc) "success" else "10-${PublicBean.ProgramNumber}-${myitem.state.filter { it==ObdBeans.PROGRAM_SUCCESS }.size}"
                            );
                        }.start()
                        adapter.notifyDataSetChanged()
                        switchBtn()
                        if(resultc&&PublicBean.position==PublicBean.ID_COPY_OBD){
                            JzActivity.getControlInstance().showDiaLog(true,false,object :SetupDialog(R.layout.dia_obd_goback_hint){
                                override fun dismess() {

                                }

                                override fun keyevent(event: KeyEvent): Boolean {
                                    return true
                                }

                                override fun setup(rootview: Dialog) {
                                }

                            },"dia_obd_goback_hint")
                        }
                    }
                }
                override fun Copy_Next(success: Boolean, position: Int) {

                    handler.post {
                        resultc = resultc && success
                        myitem.state[position] =
                            if (success) ObdBeans.PROGRAM_SUCCESS else ObdBeans.PROGRAM_FALSE
                    }
                }
            }, PublicBean.getHEX(), PublicBean.getIdcount())
        }.start()
    }
    override fun onLeft() {
        super.onLeft()
        if (adapter.focus - 1 >= 0 && PublicBean.position == PublicBean.複製傳感器) {
            adapter.focus -= 1
            adapter.position = adapter.focus / 2
            adapter.notifyDataSetChanged()
        }
    }

    override fun onRight() {
        super.onRight()
        if (adapter.focus + 1 < myitem.wheelPosition.size * 2 && PublicBean.position == PublicBean.複製傳感器) {
            adapter.focus += 1
            adapter.position = adapter.focus / 2
            adapter.notifyDataSetChanged()
        }
    }

    override fun onTop() {
        super.onTop()
        if (adapter.focus - 2 >= 0) {
            adapter.focus -= 2
            adapter.position = adapter.focus / 2
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDown() {
        super.onDown()
        if (adapter.focus + 2 < myitem.wheelPosition.size * 2) {
            adapter.focus += 2
            adapter.position = adapter.focus / 2
            adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun viewInit() {
        rootview.fixLanguage()
        rootview.gomenu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        Log.e("取得obd", "data:${PublicBean.getOBD1()}")
        myitem.Tire_img.add(rootview.LF_image)
        myitem.Tire_img.add(rootview.RF_image)
        myitem.Tire_img.add(rootview.RR_image)
        myitem.Tire_img.add(rootview.LR_image)
        if (PublicBean.wheelCount() == 5) { //myitem.Tire_img.add(rootview.SP_image)
        myitem.Tire_img.add(TextView(JzActivity.getControlInstance().getRootActivity()))
        }
        rootview.read_MMY_Title.text =
            "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        adapter = Ad_obd(myitem, this)
        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        for (i in PublicBean.readable.indices) {
            if (PublicBean.readable[i]) {
                if ((i < PublicBean.wheelCount())) {
                    myitem.add(
                        SensorData(),
                        SensorData(),
                        ObdBeans.PROGRAM_WAIT,
                        arrayOf(
                            resources!!.jzString(R.string.app_fl),
                            resources!!.jzString(R.string.app_fr),
                            resources!!.jzString(R.string.app_rr),
                            resources!!.jzString(R.string.app_rl),
                            "SP"
                        )[i]
                    )
                }
            }
        }

        if (PublicBean.wheelCount() == 5) {
            rootview.Car_image.setBackgroundResource(R.mipmap.img_car_five_tire)
        }
        if(PublicBean.wheelCount() == 2){
            rootview.motorre.visibility=View.VISIBLE
            rootview.Car_img_R.visibility=View.GONE
        }else{
            rootview.motorre.visibility=View.GONE
            rootview.Car_img_R.visibility=View.VISIBLE
        }
        adapter.notifyDataSetChanged()
        rootview.sending_data.text = resources!!.jzString(R.string.app_sensor_info_read)
        if (PublicBean.position == PublicBean.OBD_RELEARM) {
            insertObd()
        }
        adapter.notifyDataSetChanged()

        rootview.sending_data.setOnClickListener {
            TaskHandler.newInstance.runTaskOnce("sending_data", runner {
                if (checkComplete()) {
                    write()
                } else {
                    read()
                }
            })
        }
        if (PublicBean.position == PublicBean.ID_COPY_OBD) {
            DownOBD()
        } else if(PublicBean.selectMmy.selectFun == PublicBean.SelectFun.Plate && Frag_Plate_Recognize.plateMemory.isNotEmpty()){
            val sensorId:ArrayList<String> = Gson().fromJson(Frag_Plate_Recognize.plateMemory[0]["sensorID"].toString(),object :
                TypeToken<ArrayList<String>>() {}.type)
            for (i in 0 until myitem.wheelPosition.size) {
                var string=""
                if(i<sensorId.size){
                    string= sensorId[i]
                }
                when (myitem.wheelPosition[i]) {
                    resources.jzString(R.string.app_fl) -> {
                        myitem.OldSemsor[i].id = string
                    }
                    resources.jzString(R.string.app_fr) -> {
                        myitem.OldSemsor[i].id = string
                    }
                    resources.jzString(R.string.app_rr) -> {
                        myitem.OldSemsor[i].id = string
                    }
                    resources.jzString(R.string.app_rl) -> {
                        myitem.OldSemsor[i].id = string
                    }
                    "SP" -> {
                        myitem.OldSemsor[i].id = string
                    }
                }
            }
            adapter = Ad_obd(myitem, this@Frag_Function_OBD_ID_copy)
            rootview.ShowReadView.adapter = adapter
        }else{
            adapter = Ad_obd(myitem, this@Frag_Function_OBD_ID_copy)
            rootview.ShowReadView.adapter = adapter
        }

    }

    override fun onKeyTrigger() {
        super.onKeyTrigger()
        TaskHandler.newInstance.runTaskOnce("sending_data", runner {
            if (adapter.focus == -1) {
                write()
            } else {
                read()
            }
        })
    }

    fun insertObd() {
        for (i in 0 until myitem.wheelPosition.size) {
            if (myitem.NewSensor[i].id.isEmpty()) {
                myitem.NewSensor[i] = myitem.OldSemsor[i]
            }
        }
    }

    fun DownOBD() {
        handler.post {
            JzActivity.getControlInstance()
                .showDiaLog(false, true, object : SetupDialog(R.layout.normal_dialog) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.wait)
                    }

                }, "normal_dialog")
        }
        Thread {
            val a = loadingOBD()
            handler.post {
                if (a) {
                    SetId()
                } else {
                    JzActivity.getControlInstance().closeDiaLog()
                    JzActivity.getControlInstance()
                        .showDiaLog(false, false, object : SetupDialog(R.layout.program_false) {
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return false
                            }

                            override fun setup(rootview: Dialog) {
                                rootview.findViewById<TextView>(R.id.ok).setOnClickListener {
                                    (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.disconnect()
                                    JzActivity.getControlInstance().closeDiaLog()
                                    JzActivity.getControlInstance().goBack("Frag_Obd")
                                }
                                rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
                                    JzActivity.getControlInstance().closeDiaLog()
                                    DownOBD()
                                }
                            }

                        }, "program_false")
                }
            }
        }.start()
    }

    fun loadingOBD(): Boolean {
        val obdFileList= FileJsonVersion.getLocal()
        Cmd_Og.tx_memory = StringBuffer()

        (activity!! as KtActivity).ObdCommand.Reboot()
        if(!(activity!! as KtActivity).ObdCommand.AskVersion()){return false}
        val fileVersion=if( (activity!! as KtActivity).ObdCommand.isOBDII) obdFileList.obdList2[PublicBean.getOBD1()+"T"] else obdFileList.obdList[PublicBean.getOBD1()]
        (activity!! as KtActivity).ObdCommand.obdFile=if( (activity!! as KtActivity).ObdCommand.isOBDII) (PublicBean.getOBD1()+"T") else PublicBean.getOBD1()
        Log.e("isOBDII","${(activity!! as KtActivity).ObdCommand.isOBDII}")
        if ((activity!! as KtActivity).ObdCommand.AppVersion == FormatConvert.bytesToHex(
                fileVersion.toString().replace(
                    ".srec",
                    ""
                ).toByteArray()
            )
        ) {
            if ((activity!! as KtActivity).ObdCommand.GoApp()) {
                return true
            }
        } else {
            if (!(activity!! as KtActivity).ObdCommand.WriteVersion(fileVersion.toString()) || !(activity!! as KtActivity).ObdCommand.GoBootloader()) {
                return false
            }
        }
        Thread.sleep(2000)
        return (JzActivity.getControlInstance().getRootActivity() as KtActivity).ObdCommand.WriteFlash(
            (JzActivity.getControlInstance().getRootActivity() as KtActivity),
            296
        )
    }

    fun SetId() {
        JzActivity.getControlInstance()
            .showDiaLog(false, true, object : SetupDialog(R.layout.normal_dialog) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.tit.text = JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.Data_Loading)
                }

            }, "normal_dialog")
        Thread {
            val a =
                (activity!! as KtActivity).ObdCommand.GetId(if (PublicBean.wheelCount() == 5) "05" else "04")
            handler.post {
                JzActivity.getControlInstance().closeDiaLog()
                Log.e("dialog", "關閉dialog")
                if (a.success) {
                    for (i in 0 until myitem.wheelPosition.size) {
                        when (myitem.wheelPosition[i]) {
                            resources!!.jzString(R.string.app_fl) -> {
                                myitem.OldSemsor[i].id = a.LF
                            }
                            resources!!.jzString(R.string.app_fr) -> {
                                myitem.OldSemsor[i].id = a.RF
                            }
                            resources!!.jzString(R.string.app_rr) -> {
                                myitem.OldSemsor[i].id = a.RR
                            }
                            resources!!.jzString(R.string.app_rl) -> {
                                myitem.OldSemsor[i].id = a.LR
                            }
                            "SP" -> {
                                myitem.OldSemsor[i].id = a.SP
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.e("ID", a.LF)
                    Log.e("ID", a.RF)
                    Log.e("ID", a.LR)
                    Log.e("ID", a.RR)
                    Log.e("ID", a.SP)
                } else {
                    JzActivity.getControlInstance().closeDiaLog()
                    Log.e("dialog", "關閉dialog")
                    JzActivity.getControlInstance().toast(resources!!.jzString(R.string.wrongmmy))
                    JzActivity.getControlInstance().goBack("Frag_Obd")
                }
            }
        }.start()
    }

    fun read() {
        if (adapter.focus != -1) {
            adapter.insertID(adapter.position)
            return
        }
        when (PublicBean.position) {
            PublicBean.OBD_RELEARM -> {
                JzActivity.getControlInstance()
                    .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                Thread {
                    val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                    insert(a)
                }.start()
            }
            PublicBean.複製傳感器 -> {
                if (isOriginal()||PublicBean.onlyCopy()) {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                    Thread {
                        val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                        insert(a)
                    }.start()
                } else {
                    Thread {
                        Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                        val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(),false)
                        if((a.type!="success")&&(a.type!="cancel")){
                            Log.e("update",a.type)
                            MysqDatabase.insertCopyResult(Cmd_Og.tx_memory.toString(),a.type)
                        }
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog() }
                        if (a.sensorData.any { it.senesorType == "S2" } && !PublicBean.getSIISensor()) {
                            handler.post {
                                noSensorCode()
                            }
                            return@Thread
                        }
                        if (a.sensorData.size == 1) {
                            insert(a.sensorData[0])
                        } else {
                            handler.post {
                                OgPublic.getInstance.playBeepSoundAndVibrate()
                                JzActivity.getControlInstance().closeDiaLog("data_loading")
                                JzActivity.getControlInstance().toast("jz.300".getFix())
                            }
                        }
                    }.start()
                }
            }
            PublicBean.ID_COPY_OBD -> {
                JzActivity.getControlInstance()
                    .showDiaLog(R.layout.data_loading, false, false, "data_loading")
                Thread {
                    Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                    val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(),false)
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog() }
                    if (a.sensorData.filter { it.senesorType == "S2" }.isNotEmpty() && !PublicBean.getSIISensor()) {
                        handler.post {
                            noSensorCode()
                        }
                        return@Thread
                    }
                    if (a.sensorData.size == 1) {
                        insert(a.sensorData[0])
                    } else {
                        handler.post {
                            OgPublic.getInstance.playBeepSoundAndVibrate()
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            JzActivity.getControlInstance().toast("jz.300".getFix())
                        }
                    }
                }.start()
            }
        }
    }

    fun noSensorCode() {
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
                                rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.textView4)
                                    .text = "jz.464".getFix()
                            }
                        },
                        "single_program"
                    )
            }
        }
    }

    fun write() {
        copyClock.Zeroing()
        if ((!(JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.isConnect()) && PublicBean.position != PublicBean.複製傳感器) {
            handler.post {
                (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.scan { result ->
                    JzActivity.getControlInstance().closeDiaLog()
                    if (result) {
                        write()
                    }
                }
            }
            return
        }
        rootview.show_content.text = JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.Programming_do_not_move_sensors)
        when (PublicBean.position) {
            PublicBean.ID_COPY_OBD -> {
                startCopy()
            }

            PublicBean.OBD_RELEARM -> {
                write_OBD學碼()
            }

            PublicBean.複製傳感器 -> {
                startCopy()
            }
        }
    }

    fun startCopy() {
        PublicBean.SensorList = myitem.OldSemsor
        PublicBean.NewSensorList = myitem.NewSensor
        PublicBean.ProgramNumber = myitem.NewSensor.size
        JzActivity.getControlInstance().closeDiaLog()
        JzActivity.getControlInstance().showDiaLog(false, true, object : SetupDialog(R.layout.data_loading) {
                override fun dismess() {
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if (event.keyCode == 4) {
                        Cmd_Og.cancel = true
                    }
                    return false
                }

                override fun setup(rootview: Dialog) {}
            }, "data_loading")
        if(PublicBean.onlyCopy()){
            copyCommand(PublicBean.NewSensorList)
            return
        }
        Thread {
            val sensordata = ArrayList<SensorData>()
            for (i in PublicBean.NewSensorList) {
                val sens = SensorData()
                sens.id = i.id
                sensordata.add(sens)
            }
            TaskHandler.newInstance.runTaskOnce("startCo"
            ) {
                Log.e("Frag_Function_OBD", "Program_Init")
                Cmd_Og.GetPr("00", PublicBean.ProgramNumber, PublicBean.getHEX())
                Cmd_Og.Program(
                    "00",
                    PublicBean.getHEX(),
                    Integer.toHexString(adapter.beans.NewSensor.size),
                    PublicBean.getS19(), this, adapter.beans.NewSensor
                )
            }
        }.start()
    }

    fun write_OBD學碼() {
        JzActivity.getControlInstance()
            .showDiaLog(false, true, object : SetupDialog(R.layout.normal_dialog) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.tit.text = JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.Data_Loading)
                }

            }, "normal_dialog")
        Thread {
            if (loadingOBD()) {
                val a =
                    (activity!! as KtActivity).ObdCommand.GetId(if (PublicBean.wheelCount() == 5) "05" else "04")
                if (a.success) {
                    val writeItem = if (PublicBean.wheelCount() == 5) arrayOf(
                        a.LF,
                        a.RF,
                        a.RR,
                        a.LR,
                        a.SP
                    ) else arrayOf(a.LF, a.RF, a.RR, a.LR)
                    for (i in 0 until myitem.wheelPosition.size) {
                        when (myitem.wheelPosition[i]) {
                            resources!!.jzString(R.string.app_fl) -> {
                                writeItem[0] = myitem.NewSensor[i].id
                            }
                            resources!!.jzString(R.string.app_fr) -> {
                                writeItem[1] = myitem.NewSensor[i].id
                            }
                            resources!!.jzString(R.string.app_rr) -> {
                                writeItem[2] = myitem.NewSensor[i].id
                            }
                            resources!!.jzString(R.string.app_rl) -> {
                                writeItem[3] = myitem.NewSensor[i].id
                            }
                            "SP" -> {
                                writeItem[4] = myitem.NewSensor[i].id
                            }
                        }
                    }
                    Thread.sleep(1000)
                    (activity!! as KtActivity).ObdCommand.setTireId(ArrayList(writeItem.asList())
                    ) {
                        if (it) {
                            handler.post {
                                MysqDatabase.insertOBD(
                                    Cmd_Og.tx_memory.toString(),
                                    "WriteSuccess"
                                )
                                JzActivity.getControlInstance()
                                    .showDiaLog(true, false, object : SetupDialog(R.layout.dia_obd_goback_hint) {
                                        override fun dismess() {

                                        }

                                        override fun keyevent(event: KeyEvent): Boolean {
                                            return true
                                        }

                                        override fun setup(rootview: Dialog) {
                                        }

                                    }, "dia_obd_goback_hint")
                                JzActivity.getControlInstance().closeDiaLog("normal_dialog")
                                for (i in 0 until myitem.wheelPosition.size) {
                                    myitem.OldSemsor[i] = myitem.NewSensor[i]
                                    myitem.state[i] = ObdBeans.PROGRAM_SUCCESS
                                }
                                switchBtn()
                            }
                        } else {
                            handler.post {
                                MysqDatabase.insertOBD(
                                    Cmd_Og.tx_memory.toString(),
                                    "WriteFalse"
                                )
                                for (i in 0 until myitem.wheelPosition.size) {
                                    myitem.state[i] = ObdBeans.PROGRAM_FALSE
                                }
                                falseRetry()
                                switchBtn()
                            }
                        }
                    }

                } else {
                    handler.post {
                        MysqDatabase.insertOBD(Cmd_Og.tx_memory.toString(), "ReadTimeout")
                        falseRetry()
                    }
                }
            } else {
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    MysqDatabase.insertOBD(Cmd_Og.tx_memory.toString(), "LoadingFalse")
                    JzActivity.getControlInstance().showDiaLog(true,false,object :SetupDialog(R.layout.obd_connect_error){
                        override fun dismess() {
                            (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.disconnect()
                        }

                        override fun keyevent(event: KeyEvent): Boolean {
                          return true
                        }

                        override fun setup(rootview: Dialog) {
                        }
                    },"obd_connect_error")
                }
            }
        }.start()


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onKeyScan() {
        super.onKeyScan()
        PublicBean.scanSensor(object : PublicBean.scanBack{
            override fun getSensor(id: String) {
                if(PublicBean.position == PublicBean.OBD_RELEARM){
                    val sens=SensorData()
                    sens.id=id
                    insert(sens)
                    return
                }
                if (adapter.focus != -1) {
                    if (adapter.focus % 2 == 0) {
                        adapter.beans.OldSemsor = ArrayList(adapter.beans.OldSemsor.map {
                            if (it.id == id) SensorData() else it
                        })
                        adapter.beans.OldSemsor[adapter.focus / 2].id = id
                    } else {
                        adapter.beans.NewSensor = ArrayList(adapter.beans.NewSensor.map {
                            if (it.id == id) SensorData() else it
                        })
                        adapter.beans.NewSensor[adapter.focus / 2].id = id
                    }
                    adapter.focus = adapter.getEmpty()
                    adapter.notifyDataSetChanged()
                    checkComplete()
                } else {
                    return
                }
            }
        })
    }

    fun falseRetry() {
        JzActivity.getControlInstance().closeDiaLog("normal_dialog")
        JzActivity.getControlInstance().showDiaLog(
            false,
            false,
            object : SetupDialog(R.layout.retry) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.findViewById<TextView>(R.id.cancel).setOnClickListener {
                        JzActivity.getControlInstance()
                            .goBack("Frag_Function_Selection")
                        JzActivity.getControlInstance()
                            .closeDiaLog()
                    }
                }
            },
            "retry"
        )
    }

    fun switchBtn() {
        adapter.notifyDataSetChanged()
        (JzActivity.getControlInstance().getActionBar() as Frag_Manager).back.setImageResource(R.mipmap.menu)
        (JzActivity.getControlInstance().getActionBar() as Frag_Manager).back.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

        if (myitem.state.contains(ObdBeans.PROGRAM_FALSE)) {
            Log.e("ID Copy", "false")
            rootview.show_content.text = resources!!.jzString(R.string.Programming_failed)
            rootview.gomenu.text = "jz.9".getFix()
            rootview.gomenu.setOnClickListener {
                JzActivity.getControlInstance().goMenu()
            }
            rootview.sending_data.text = resources!!.jzString(R.string.app_re_program)
            rootview.sending_data.setOnClickListener {
                write()
            }
        } else {
            rootview.show_content.text = resources!!.jzString(R.string.Programming_completed)
            //rootview.gomenu.setText(R.string.reselectTire)
            rootview.gomenu.text = "jz.437".getFix()
            rootview.gomenu.setOnClickListener {
                JzActivity.getControlInstance().goBack()
            }
            Log.e("ID Copy", "success")
            if (upload) {
                rootview.sending_data.text = "Hochladen"
                rootview.sending_data.setTextColor(resources.getColor(R.color.color_orange))
            }else{
                rootview.sending_data.text = resources!!.jzString(R.string.Relearn_Procedure)
            }
            rootview.sending_data.setOnClickListener {
                if (upload) {
                    for (i in 0 until myitem.wheelPosition.size) {
                        val item=Frag_Tire_Hotel_List_Detail.tpms
                        val index=arrayOf(
                            resources.jzString(R.string.app_fl),
                            resources.jzString(R.string.app_fr),
                            resources.jzString(R.string.app_rr),
                            resources.jzString(R.string.app_rl),
                            "SP"
                        ).indexOf(myitem.wheelPosition[i])
                        item[i]["sensorID"] = myitem.OldSemsor[i].id
                        item[i]["bat"] = PublicBean.NewSensorList[i].bat
                        item[i]["pressure"] ="${PublicBean.NewSensorList[i].kpa}"
                        item[i]["temp"] = "${PublicBean.NewSensorList[i].c}"
                    }
                    Frag_Tire_Hotel_List_Detail.uploadData()
                } else {
                    JzActivity.getControlInstance()
                        .changePage(Frag_Relearm_Detail(), "Frag_Function_Relearn_Procedure", true)
                }
            }
        }
    }

    fun insert(sensorData: SensorData) {
        JzActivity.getControlInstance().getHandler().post {
            OgPublic.getInstance.playBeepSoundAndVibrate()
            JzActivity.getControlInstance().closeDiaLog("data_loading")
            if (sensorData.id.isEmpty()) {
                JzActivity.getControlInstance().toast("jz.300".getFix())
            }else if(sensorData.senesorType=="S2"){
                if(!PublicBean.getSIISensor()){
                    JzActivity.getControlInstance().getHandler().post {
                        if(!PublicBean.getSIISensor()){
                            JzActivity.getControlInstance().showDiaLog(true, false, object : SetupDialog(com.orango.electronic.orange_og_lib.R.layout.errorsensor){
                                    override fun dismess() {
                                        JzActivity.getControlInstance().goMenu()
                                    }

                                    override fun keyevent(event: KeyEvent): Boolean {
                                        return true
                                    }

                                    override fun setup(rootview: Dialog) {
                                        rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.textView4).text="jz.464".getFix()
                                    }
                                }, "single_program")
                        }
                    }
                    return@post
                }else if(adapter.beans.wheelPosition.size>1){
                    needSingle()
                }
            }
        }

        var readold = true
        if (PublicBean.position != PublicBean.OBD_RELEARM) {
            for (i in 0 until myitem.wheelPosition.size) {
                if (myitem.OldSemsor[i].id.isEmpty()) {

                    if (myitem.OldSemsor.filter { it.id == sensorData.id }.isNotEmpty()) {
                        JzActivity.getControlInstance().toast("jz.289".getFix())
                        return
                    }
                    myitem.OldSemsor[i] = sensorData
                    readold = false
                    break
                }
            }
        }
        if (readold) {
            for (i in 0 until myitem.wheelPosition.size) {
                if (myitem.NewSensor[i].id.isEmpty()) {
                    if (myitem.NewSensor.filter { it.id == sensorData.id }.isNotEmpty()) {
                        JzActivity.getControlInstance().toast("jz.289".getFix())
                        return
                    }
                    myitem.NewSensor[i].id = sensorData.id
                    break
                }
            }
        }
        handler.post {
            checkComplete()
            adapter.notifyDataSetChanged()
        }
    }

    fun checkComplete(): Boolean {
        val idcount = PublicBean.getIdcount()
        if (PublicBean.position == PublicBean.OBD_RELEARM) {
            for (i in 0 until myitem.wheelPosition.size) {
                if ((myitem.NewSensor[i].id.isEmpty())) {
                    return false
                }
            }
        } else {
            for (i in 0 until myitem.wheelPosition.size) {
                if ((myitem.NewSensor[i].id.isEmpty()) || (myitem.OldSemsor[i].id.isEmpty())) {
                    return false
                }
            }
        }
        if (!myitem.state.contains(ObdBeans.PROGRAM_FALSE) && !myitem.state.contains(ObdBeans.PROGRAM_SUCCESS)) {
            when (PublicBean.position) {
                PublicBean.OBD_RELEARM -> {
                    rootview.sending_data.text = resources!!.jzString(R.string.transfer)
                }
                PublicBean.ID_COPY_OBD -> {
                    rootview.sending_data.text = resources!!.jzString(R.string.app_program)

                }
                PublicBean.複製傳感器 -> {
                    rootview.sending_data.text = resources!!.jzString(R.string.app_program)
                }
            }
        }

        return true
    }

    fun isOriginal(): Boolean {
        for (i in 0 until myitem.wheelPosition.size) {
            if (myitem.OldSemsor[i].id.isEmpty()) {
                return true
            }
        }
        return false
    }
    fun needSingle(){
        JzActivity.getControlInstance()
            .showDiaLog(true, false, object :SetupDialog(R.layout.single_program){
                override fun dismess() {
                    JzActivity.getControlInstance().goBack()
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return true
                }

                override fun setup(rootview: Dialog) {
                    rootview.findViewById<TextView>(R.id.textView4).text="jz.463".getFix()
                }
            }, "single_program")
    }
}
