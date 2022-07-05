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
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.jzchi.jzframework.tool.FormatConvert
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.New_Ad_obd
import com.orange.tpms.model.ObdBeans
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orango.electronic.orange_og_lib.Callback.Program_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Dialog.single_program
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.*
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.Car_and_init
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.LF_image
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.LR_image
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.RF_image
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.RR_image
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.ShowReadView
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.gomenu
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.read_MMY_Title
import kotlinx.android.synthetic.main.new_function_obd_id_copy.view.sending_data
import kotlinx.android.synthetic.main.normal_dialog.*
import kotlinx.android.synthetic.main.program_single.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_New_OBD_ID_copy(
    var sensorInitial: ArrayList<SensorData> = arrayListOf(),
    var wheelCount: Int = sensorInitial.size,
    var single: Boolean = false
) : RootFragement(R.layout.new_function_obd_id_copy),
    Program_C {
    var myitem = ObdBeans()
    lateinit var adapter: New_Ad_obd
    override fun prograam_NotProceed(type: Int) {
        JzActivity.getControlInstance().closeDiaLog()
        when (type) {
            //TimeOut
            -1 -> {
                Program_Finish(false)
                return
            }
            //沒有2代Code
            -4 -> {
                noSensorCode()
            }
            //
            0, 1 -> {
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


                }, "data_loading"
            )
        }
    }

    override fun enter() {
        super.enter()
        if(single){
            adapter.togglSingleProgram=false
            adapter.notifyDataSetChanged()
        }
    }

    override fun Program_Finish(b: Boolean) {
        if (b) {
            Thread.sleep(5000)
            copyCommand()
        } else {
            handler.post {
                OgPublic.getInstance.playBeepSoundAndVibrate()
                myitem.state[myitem.selectRows] = ObdBeans.PROGRAM_FALSE
                JzActivity.getControlInstance().closeDiaLog("data_loading")
                adapter.notifyDataSetChanged()
                switchBtn()
            }
        }
    }

    fun copyCommand() {
        val copyId = ArrayList<String>()
        var origId = myitem.OldSemsor[myitem.selectRows].id
        while (origId.length < 8) {
            origId = "0${origId}"
        }
        copyId.add(origId)
        //
        val newId = ArrayList<String>()
        var newid = myitem.NewSensor[myitem.selectRows].id
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
            var result = false
            if (it.size == 1) {
                result = true
                Thread {
                    MysqDatabase.insertCopyResult(
                        Cmd_Og.tx_memory.toString(),
                        "success"
                    );
                }.start()
                myitem.NewSensor[myitem.selectRows].id = myitem.OldSemsor[myitem.selectRows].id
                myitem.state[myitem.selectRows] = ObdBeans.PROGRAM_SUCCESS
            } else {
                Thread {
                    MysqDatabase.insertCopyResult(
                        Cmd_Og.tx_memory.toString(),
                        "10-1-0"
                    );
                }.start()
                myitem.state[myitem.selectRows] = ObdBeans.PROGRAM_FALSE
            }

            handler.post {
                if (result && wheelCount == 1) {
                    rootview.re_read.visibility = View.VISIBLE
                    rootview.re_read.setOnClickListener {
                        rootview.re_read.visibility = View.GONE
                        for (a in 0 until myitem.NewSensor.size) {
                            myitem.NewSensor[a].id = ""
                            myitem.OldSemsor[a].id = ""
                            myitem.state[a] = ObdBeans.PROGRAM_WAIT
                        }
                        switchBtn()
                        adapter.notifyDataSetChanged()
                    }
                }
                JzActivity.getControlInstance().closeDiaLog()
                OgPublic.getInstance.playBeepSoundAndVibrate()
                adapter.notifyDataSetChanged()
                switchBtn()
                if (result && PublicBean.position == PublicBean.ID_COPY_OBD) {
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
                }
            }
        }
    }

    override fun onLeft() {
        super.onLeft()
    }

    override fun onRight() {
        super.onRight()

    }

    override fun onTop() {
        super.onTop()
        if(single){return}
        var rows = myitem.selectRows - 1
        while (rows >= 0) {
            if (myitem.OldSemsor[rows].id.isNotEmpty()) {
                myitem.selectRows = rows
                break
            }
            rows--
        }
        switchBtn()
        adapter.notifyDataSetChanged()
    }

    override fun onDown() {
        super.onDown()
        if(single){return}
        var rows = myitem.selectRows + 1
        while (rows < myitem.OldSemsor.size) {
            if (myitem.OldSemsor[rows].id.isNotEmpty()) {
                myitem.selectRows = rows
                break
            }
            rows++
        }
        switchBtn()
        adapter.notifyDataSetChanged()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun viewInit() {
        if (single) {
            rootview.textView53.visibility = View.VISIBLE
        }
        rootview.fixLanguage()
        if (wheelCount == 1) {
            rootview.Car_and_init.visibility = View.GONE
        } else {
            rootview.Car_and_init.visibility = View.VISIBLE
            if (PublicBean.wheelCount() == 2) {
                rootview.motorre.visibility = View.VISIBLE
                rootview.Car_img_R.visibility = View.GONE
            } else {
                rootview.motorre.visibility = View.GONE
                rootview.Car_img_R.visibility = View.VISIBLE
            }
        }
        myitem.Tire_img.add(rootview.LF_image)
        myitem.Tire_img.add(rootview.RF_image)
        myitem.Tire_img.add(rootview.RR_image)
        myitem.Tire_img.add(rootview.LR_image)
        if (PublicBean.wheelCount() == 5) {
            myitem.Tire_img.add(TextView(JzActivity.getControlInstance().getRootActivity()))
        }
        rootview.gomenu.setOnClickListener { JzActivity.getControlInstance().goMenu() }
        rootview.read_MMY_Title.text = "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        adapter = New_Ad_obd(myitem, this)
        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        if (sensorInitial.isEmpty()) {
            if (wheelCount == 1) {
                myitem.add(
                    SensorData(),
                    SensorData(),
                    ObdBeans.PROGRAM_WAIT,
                    "1"
                )
            } else {
                for (a in 0 until wheelCount) {
                    myitem.add(
                        SensorData(),
                        SensorData(),
                        ObdBeans.PROGRAM_WAIT,
                        arrayOf(
                            resources.jzString(R.string.app_fl),
                            resources.jzString(R.string.app_fr),
                            resources.jzString(R.string.app_rr),
                            resources.jzString(R.string.app_rl),
                            "SP"
                        )[a]
                    )
                }
            }
        } else {
            var positon = 0
            sensorInitial.map {
                myitem.add(
                    it,
                    SensorData(),
                    ObdBeans.PROGRAM_WAIT,
                    it.wheelString
                )
                if (!it.isBlock && myitem.selectRows == -1) {
                    adapter.focus = myitem.selectRows * 2 + 1
                    adapter.position = positon
                    myitem.selectRows = positon
                }
                positon++
            }
        }
        rootview.sending_data.text = "jz.117".getFix()
        if (PublicBean.position == PublicBean.OBD_RELEARM) {
            insertObd()
        }
        rootview.sending_data.setOnClickListener {
            TaskHandler.newInstance.runTaskOnce("sending_data", runner {
                write()
            })
        }
        if (PublicBean.position == PublicBean.ID_COPY_OBD) {
            DownOBD()
        } else {
            adapter.notifyDataSetChanged()
        }
        switchBtn()
        if (single) { myitem.selectRows = 0 }
        if(!single){
            Thread{
                handler.post { JzActivity.getControlInstance().showDiaLog(true,false,SingleProgram(),"SingleProgram") }
            }.start()
        }

    }

    override fun onKeyTrigger() {
        super.onKeyTrigger()
        if (single) {
            EmptyDialog(R.layout.data_loading).show()
            Thread {
                val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog("data_loading")
                    OgPublic.getInstance.playBeepSoundAndVibrate()
                    adapter.togglSingleProgram=false
                    if (a.success) {
                        myitem.OldSemsor[0].id = a.id
                    } else {
                        JzActivity.getControlInstance().showDiaLog(
                            R.layout.data_loading_false,
                            true,
                            false,
                            "data_loading_false"
                        )
                    }
                    adapter.notifyDataSetChanged()

                }
            }.start()

        } else {
            write()
        }
    }

    fun insert(sensorData: SensorData) {
        JzActivity.getControlInstance().getHandler().post {
            OgPublic.getInstance.playBeepSoundAndVibrate()
            JzActivity.getControlInstance().closeDiaLog("data_loading")
            if (sensorData.id.isEmpty()) {
                JzActivity.getControlInstance().toast("jz.300".getFix())
            } else if (sensorData.senesorType == "S2") {
                if (!PublicBean.getSIISensor()) {
                    JzActivity.getControlInstance().getHandler().post {
                        if (!PublicBean.getSIISensor()) {
                            JzActivity.getControlInstance().showDiaLog(
                                true,
                                false,
                                object : SetupDialog(com.orango.electronic.orange_og_lib.R.layout.errorsensor) {
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
                    return@post
                } else if (adapter.beans.wheelPosition.size > 1) {
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

    fun read() {
        if (adapter.focus != -1) {
            adapter.togglSingleProgram=false
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
                if (isOriginal() || PublicBean.onlyCopy()) {
                    EmptyDialog(R.layout.data_loading).show()
                    Thread {
                        val a = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                        adapter.togglSingleProgram=false
                        insert(a)
                    }.start()
                } else {
                    Thread {
                        Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                        val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(), false)
                        if ((a.type != "success") && (a.type != "cancel")) {
                            Log.e("update", a.type)
                            MysqDatabase.insertCopyResult(Cmd_Og.tx_memory.toString(), a.type)
                        }
                        handler.post {
                            JzActivity.getControlInstance().closeDiaLog()
                        }
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
                JzActivity.getControlInstance().showDiaLog(R.layout.data_loading, false, false, "data_loading")
                Thread {
                    Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                    val a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX(), false)
                    handler.post {
                        JzActivity.getControlInstance().closeDiaLog()
                    }
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
                        if (event.keyCode == 4) {
                            Cmd_Og.cancel = true
                        }
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.tit.text =
                            JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.wait)
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
                                    (JzActivity.getControlInstance()
                                        .getRootActivity() as KtActivity).bleManager.BleHelper.disconnect()
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
        val obdFileList = FileJsonVersion.getLocal()
        Cmd_Og.tx_memory = StringBuffer()

        (activity!! as KtActivity).ObdCommand.Reboot()
        if (!(activity!! as KtActivity).ObdCommand.AskVersion()) {
            return false
        }
        val fileVersion =
            if ((activity!! as KtActivity).ObdCommand.isOBDII) obdFileList.obdList2[PublicBean.getOBD1() + "T"] else obdFileList.obdList[PublicBean.getOBD1()]
        (activity!! as KtActivity).ObdCommand.obdFile =
            if ((activity!! as KtActivity).ObdCommand.isOBDII) (PublicBean.getOBD1() + "T") else PublicBean.getOBD1()
        Log.e("isOBDII", "${(activity!! as KtActivity).ObdCommand.isOBDII}")
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
        return (JzActivity.getControlInstance().getRootActivity() as KtActivity).ObdCommand.WriteFlash((JzActivity.getControlInstance().getRootActivity() as KtActivity), 296)
    }

    fun SetId() {
        JzActivity.getControlInstance()
            .showDiaLog(false, true, object : SetupDialog(R.layout.normal_dialog) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if (event.keyCode == 4) {
                        Cmd_Og.cancel = true
                    }
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.tit.text =
                        JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.Data_Loading)
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
                    JzActivity.getControlInstance().toast(resources.jzString(R.string.wrongmmy))
                    JzActivity.getControlInstance().goBack("Frag_Obd")
                }
            }
        }.start()
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
        if (myitem.state[myitem.selectRows] == ObdBeans.PROGRAM_SUCCESS) {
            return
        }
        if (myitem.OldSemsor[myitem.selectRows].id.isEmpty()||(adapter.togglSingleProgram&&single)) {
            read()
            return
        }
        EmptyDialog(R.layout.data_loading).show()
        Thread {
            val a: Cmd_Og.GetPrInfoMation
            if (PublicBean.onlyCopy()) {
                a = Cmd_Og.GetPrInfoMation()
                a.sensorData = arrayListOf(Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower()))
            } else {
                Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
                a = Cmd_Og.GetPr("00", 1, PublicBean.getHEX())
            }
            handler.post {
                if (a.sensorData.size == 1) {
                    myitem.NewSensor[myitem.selectRows].id = a.sensorData[0].id
                    adapter.notifyDataSetChanged()
                    startCopy()
                } else {
                    OgPublic.getInstance.playBeepSoundAndVibrate()
                    myitem.state[myitem.selectRows] = ObdBeans.PROGRAM_FALSE
                    JzActivity.getControlInstance().closeDiaLog("data_loading")
                }
            }
        }.start()
    }

    fun startCopy() {
        Log.e("startCopy", "startCopy")
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
        if (PublicBean.onlyCopy()) {
            PublicBean.ProgramNumber = 1
            Thread { copyCommand() }.start()
            Log.e("onlyCopy", "onlyCopy")
            return
        }
        Thread {
            TaskHandler.newInstance.runTaskOnce(
                "startCo"
            ) {
                Log.e("Frag_Function_OBD", "Program_Init")
                val old = SensorData()
                old.id = myitem.OldSemsor[myitem.selectRows].id
                val new = SensorData()
                new.id = myitem.NewSensor[myitem.selectRows].id
                PublicBean.SensorList = ArrayList(arrayOf(old).toList())
                PublicBean.NewSensorList = ArrayList(arrayOf(new).toList())
                PublicBean.ProgramNumber = 1
                Cmd_Og.Program(
                    "00",
                    PublicBean.getHEX(),
                    Integer.toHexString(1),
                    PublicBean.getS19(),
                    this,
                    PublicBean.NewSensorList
                )
            }
        }.start()
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onKeyScan() {
        super.onKeyScan()
        if (single) {
            PublicBean.scanSensor(object : PublicBean.scanBack {
                override fun getSensor(id: String) {
                    myitem.OldSemsor[0].id = id
                    adapter.togglSingleProgram=false
                    adapter.notifyDataSetChanged()
                }
            })
        }

    }


    fun switchBtn() {
        if (myitem.selectRows == -1) {
            myitem.selectRows = 0
        }
        when (myitem.state[myitem.selectRows]) {
            ObdBeans.PROGRAM_WAIT -> {
                rootview.gomenu.text = "jz.135".getFix()
                rootview.gomenu.setOnClickListener {
                    JzActivity.getControlInstance()
                        .changePage(Frag_Relearm_Detail(true), "Frag_Function_Relearn_Procedure", true)
                }
                rootview.sending_data.setBackgroundResource(R.mipmap.botton_right_press)
                rootview.sending_data.text = "jz.13".getFix()
                rootview.sending_data.setOnClickListener {
                    write()
                }
            }
            ObdBeans.PROGRAM_FALSE -> {
                rootview.gomenu.text = "jz.135".getFix()
                rootview.gomenu.setOnClickListener {
                    JzActivity.getControlInstance()
                        .changePage(Frag_Relearm_Detail(true), "Frag_Function_Relearn_Procedure", true)
                }
                rootview.sending_data.setBackgroundResource(R.mipmap.btn_right)
                rootview.sending_data.text =
                    JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.app_re_program)
                rootview.sending_data.setOnClickListener {
                    write()
                }
            }
            ObdBeans.PROGRAM_SUCCESS -> {
                rootview.gomenu.text = "jz.9".getFix()
                rootview.gomenu.setOnClickListener {
                    JzActivity.getControlInstance().goMenu()
                }
                rootview.sending_data.setBackgroundResource(R.mipmap.btn_right)
                rootview.sending_data.text = "jz.135".getFix()
                rootview.sending_data.setOnClickListener {
                    JzActivity.getControlInstance()
                        .changePage(Frag_Relearm_Detail(true), "Frag_Function_Relearn_Procedure", true)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun checkComplete(): Boolean {
        return (myitem.OldSemsor[myitem.selectRows].id.isNotEmpty()) && (myitem.NewSensor[myitem.selectRows].id.isNotEmpty())
    }

    fun isOriginal(): Boolean {
        for (i in 0 until myitem.wheelPosition.size) {
            if (myitem.OldSemsor[i].id.isEmpty()) {
                return true
            }
        }
        return false
    }


}

/*
* 提示單科燒
* */
class SingleProgram : SetupDialog(R.layout.program_single) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return true
    }

    override fun setup(rootview: Dialog) {
        rootview.imageView45.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
        }

        rootview.imageView46.setMovieResource(R.mipmap.oneby)
    }
}