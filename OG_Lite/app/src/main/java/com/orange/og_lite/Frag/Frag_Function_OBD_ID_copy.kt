package com.orange.og_lite.Frag

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.og_lite.Util.jzString
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Command.*
import com.orange.og_lite.Command.Companion.getObd
import com.orange.og_lite.Dialog.Da_Function_Enter_Sensor_ID
import com.orange.og_lite.Dialog.Da_InsertAndRemove_tool
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.adapter.Ad_obd
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Dia_SelectBaclk
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.*
import com.orange.og_lite.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.ShowReadView
import kotlinx.android.synthetic.main.fragment_frag__function__show__read.view.*
import kotlinx.android.synthetic.main.retry.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_OBD_ID_copy : JzFragement(R.layout.fragment_frag__function__obd__id_copy) {
    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    var myitem = ObdBeans()
    lateinit var adapter: Ad_obd
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.gomenu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        myitem.Tire_img.add(rootview.LF_image)
        myitem.Tire_img.add(rootview.RF_image)
        myitem.Tire_img.add(rootview.RR_image)
        myitem.Tire_img.add(rootview.LR_image)
        if (PublicBeans.wheelCount() == 5) {
            //myitem.Tire_img.add(rootview.SP_image)
            myitem.Tire_img.add(rootview.SP_image)
        }
        rootview.read_MMY_Title.text =
            PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        adapter = Ad_obd(myitem, this)
        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        for (i in PublicBeans.readable.indices) {
            if (PublicBeans.readable[i]) {
                if ((i < PublicBeans.wheelCount())) {
                    myitem.add(
                        "",
                        "",
                        ObdBeans.PROGRAM_WAIT,
                        arrayOf(
                            resources.jzString(R.string.app_fl),
                            resources.jzString(R.string.app_fr),
                            resources.jzString(R.string.app_rr),
                            resources.jzString(R.string.app_rl),
                            "SP"
                        )[i]
                    )
                }
            }
        }

        if (PublicBeans.wheelCount() == 5) {
            rootview.Car_image.setBackgroundResource(R.mipmap.img_car_five_tire)
        }
        adapter.notifyDataSetChanged()
        rootview.sending_data.text = resources.jzString(R.string.app_sensor_info_read)
        if (PublicBeans.position == PublicBeans.OBD學碼) {
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
        if (PublicBeans.position == PublicBeans.OBD複製) {
            getObd(myitem, object : Companion.callback {
                override fun result(a: Boolean) {
                    if (a) {
                        JzActivity.getControlInstance().showDiaLog(false, false,
                            Da_Function_Enter_Sensor_ID(
                                Dia_SelectBaclk {
                                    if (PublicBeans.position == PublicBeans.OBD複製) {
                                        JzActivity.getControlInstance().showDiaLog(
                                            true,
                                            false,
                                            Da_InsertAndRemove_tool(-1),
                                            "Da_InsertAndRemove_tool"
                                        )
                                    }
                                    adapter.notifyDataSetChanged()
                                }
                            ), "Da_Function_Enter_Sensor_ID")
                    } else {

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
                                    rootview.cancel.setOnClickListener {
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
                }
            })
        } else {
            JzActivity.getControlInstance().showDiaLog(false, false,
                Da_Function_Enter_Sensor_ID(
                    Dia_SelectBaclk {
                        adapter.beans=myitem
                        adapter.toEmpty()
                        rootview.ShowReadView.adapter = adapter
                        adapter.canShowKeyBoard=true
                    }
                ), "Da_Function_Enter_Sensor_ID")
        }
        adapter.toEmpty()
    }

    fun insertObd() {
        for (i in 0 until myitem.wheelPosition.size) {
            if (myitem.NewSensor[i].isEmpty()) {
                myitem.NewSensor[i] = myitem.OldSemsor[i]
            }
        }
    }

    fun read() {
        if (adapter.focus != -1) {
            adapter.insertID(adapter.position)
            return
        }
        when (PublicBeans.position) {
            PublicBeans.OBD學碼 -> {
                Command.readId(object : Companion.idback {
                    override fun result(id: String, pre: String, tem: String, bat: String) {
                        insert(id)
                    }
                })
            }
            PublicBeans.複製ID -> {
                if (isOriginal()) {
                    Command.readId(object : Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            insert(id)
                        }
                    })
                } else {
                    PublicBeans.programNumber = 1
                    Command.getPrid(object :
                        Companion.idback {
                        override fun result(id: String, pre: String, tem: String, bat: String) {
                            insert(id)
                        }
                    })
                }
            }
            PublicBeans.OBD複製 -> {
                PublicBeans.programNumber = 1
                Command.getPrid(object :
                    Companion.idback {
                    override fun result(id: String, pre: String, tem: String, bat: String) {
                        insert(id)
                    }
                })
            }
        }
    }

    fun write() {

        adapter.canShowKeyBoard = false
        rootview.show_content.text = resources.jzString(R.string.Programming_do_not_move_sensors)
        adapter.focus = -1
        when (PublicBeans.position) {
            PublicBeans.OBD複製 -> {
                Command.onProgram=true
                PublicBeans.programNumber = myitem.state.size
                Command.writeID(myitem, object : Companion.notify {
                    override fun result() {
                        Command.onProgram=false
                        handler.post {
                            if(!adapter.beans.state.contains(ObdBeans.PROGRAM_FALSE)){
                                Page_MainActivity.toggleMenu(true)
                                toggleMenu=true
                            }
                            adapter.notifyDataSetChanged()
                            adapter.canShowKeyBoard = true
                            switchBtn()
                        }
                    }
                })
            }

            PublicBeans.OBD學碼 -> {
                PublicBeans.programNumber = myitem.state.size
                if (!myitem.state.contains(ObdBeans.PROGRAM_FALSE) && !myitem.state.contains(
                        ObdBeans.PROGRAM_SUCCESS
                    )
                ) {
                    JzActivity.getControlInstance()
                        .showDiaLog(
                            true,
                            true,
                            Da_InsertAndRemove_tool(0),
                            "Da_InsertAndRemove_tool"
                        )
                } else {
                    write_OBD學碼()
                }
            }

            PublicBeans.複製ID -> {
                Command.onProgram=true
                PublicBeans.programNumber = myitem.state.size
                Command.writeID(myitem, object : Companion.notify {
                    override fun result() {
                        Command.onProgram=false
                        handler.post {
                            if(!adapter.beans.state.contains(ObdBeans.PROGRAM_FALSE)){
                                Page_MainActivity.toggleMenu(true)
                                toggleMenu=true
                            }
                            switchBtn()
                            adapter.notifyDataSetChanged()
                            adapter.canShowKeyBoard = true
                        }
                    }
                })
            }
        }

    }

    fun write_OBD學碼() {
        getObd(myitem, object : Companion.callback {
            override fun result(a: Boolean) {
                handler.post {
                    if (a) {
                        adapter.notifyDataSetChanged()
                        Command.writeOBD(myitem, object : Companion.notify {
                            override fun result() {
                                if (!myitem.state.contains(ObdBeans.PROGRAM_FALSE)) {
                                    JzActivity.getControlInstance().showDiaLog(
                                        true,
                                        false,
                                        Da_InsertAndRemove_tool(-1),
                                        "Da_InsertAndRemove_tool"
                                    )
                                    MysqDatabase.insertOBD(
                                        OgCommand.tx_memory.toString(),
                                        "WriteSuccess"
                                    )
                                    Page_MainActivity.toggleMenu(true)
                                    toggleMenu=true
                                } else {
                                    MysqDatabase.insertOBD(
                                        OgCommand.tx_memory.toString(),
                                        "WriteFalse"
                                    )
                                }
                                switchBtn()
                                adapter.notifyDataSetChanged()
                            }
                        })
                    } else {

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
                                    rootview.cancel.setOnClickListener {
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
                    adapter.canShowKeyBoard = true
                }
            }
        })
    }

    fun switchBtn() {

        val mMainActivity = JzActivity.getControlInstance().findFragByTag("Page_MainActivity")
        if (mMainActivity is Page_MainActivity) {
            handler.post {
                mMainActivity.setMenuBt()
            }
        }

        if (myitem.state.contains(ObdBeans.PROGRAM_FALSE)) {
            Log.e("ID Copy", "false")
            rootview.show_content.text = resources.jzString(R.string.Programming_failed)
            rootview.gomenu.text = "jz.9".getFix()
            rootview.gomenu.setOnClickListener {
                JzActivity.getControlInstance().goMenu()
            }
            rootview.sending_data.text = resources.jzString(R.string.app_re_program)
            rootview.sending_data.setOnClickListener {
                write()
            }
        } else {
            rootview.show_content.text = resources.jzString(R.string.Programming_completed)
            //rootview.gomenu.setText(R.string.reselectTire)
            rootview.gomenu.text = resources.jzString(R.string.reselectTire)
            rootview.gomenu.setOnClickListener {
                JzActivity.getControlInstance().goBack()
            }
            Log.e("ID Copy", "success")
            rootview.sending_data.text = resources.jzString(R.string.Relearn_Procedure)
            rootview.sending_data.setOnClickListener {
                JzActivity.getControlInstance().changePage(
                    Frag_Function_Relearn_Procedure(1),
                    "Frag_Function_Relearn_Procedure",
                    true
                )
            }
        }
    }
    var toggleMenu=false
    override fun onResume() {
        super.onResume()
        if(toggleMenu){
            Page_MainActivity.toggleMenu(true)
        }
    }
    fun insert(id: String) {
        var readold = true
        if (PublicBeans.position != PublicBeans.OBD學碼) {
            for (i in 0 until myitem.wheelPosition.size) {
                if (myitem.OldSemsor[i].isEmpty()) {
                    if (myitem.OldSemsor.contains(id)) {
                        JzActivity.getControlInstance().toast("jz.289".getFix())
                        return
                    }
                    myitem.OldSemsor[i] = id
                    readold = false
                    break
                }
            }
        }
        if (readold) {
            for (i in 0 until myitem.wheelPosition.size) {
                if (myitem.NewSensor[i].isEmpty()) {
                    if (myitem.NewSensor.contains(id)) {
                        JzActivity.getControlInstance().toast("jz.289".getFix())
                        return
                    }
                    myitem.NewSensor[i] = id
                    break
                }
            }
        }

        checkComplete()
        adapter.toEmpty()
    }

    fun checkComplete(): Boolean {
        val idcount = PublicBeans.getIdcount()
        if (PublicBeans.position == PublicBeans.OBD學碼) {
            for (i in 0 until myitem.wheelPosition.size) {
                if ((myitem.NewSensor[i].isEmpty()) || (myitem.NewSensor[i].trim() == "")) {
                    rootview.sending_data.text = "jz.231".getFix()
                    return false
                }
            }
        } else {
            for (i in 0 until myitem.wheelPosition.size) {
                if ((myitem.NewSensor[i].isEmpty()) || (myitem.OldSemsor[i].isEmpty()) || (myitem.NewSensor[i].trim() == "") || (myitem.OldSemsor[i].trim() == "")) {
                    rootview.sending_data.text = "jz.231".getFix()
                    return false
                }
            }
        }

        //Log.e("isEmpty","true")
        if (!myitem.state.contains(ObdBeans.PROGRAM_FALSE) && !myitem.state.contains(ObdBeans.PROGRAM_SUCCESS)) {
            when (PublicBeans.position) {
                PublicBeans.OBD學碼 -> {
                    rootview.sending_data.text = resources.jzString(R.string.transfer)
                }
                PublicBeans.OBD複製 -> {
                    rootview.sending_data.text = resources.jzString(R.string.app_program)

                }
                PublicBeans.複製ID -> {
                    rootview.sending_data.text = resources.jzString(R.string.app_program)
                }
            }
        }

        return true
    }

    fun isOriginal(): Boolean {
        for (i in 0 until myitem.wheelPosition.size) {
            if (myitem.OldSemsor[i].isEmpty()) {
                return true
            }
        }
        return false
    }
}
