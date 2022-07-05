package com.orange.og_lite.Frag


import android.content.res.Resources
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.Util.fixLanguage
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.*
import com.orange.og_lite.Dialog.Da_Function_Enter_Sensor_ID
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Dialog.Dia_Two_Repr
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.adapter.Ad_Function_Program
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.Function_Program_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SensorData
import com.orange.og_lite.callback.Dia_SelectBaclk
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.ShowReadView
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Program(val quantity: String) :
    JzFragement(R.layout.fragment_frag__function__program) {

    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem = Function_Program_Item()
    lateinit var adapter: Ad_Function_Program

    override fun viewInit() {
        rootview.fixLanguage()
        PublicBeans.programNumber = quantity.toInt()
        rootview.read_MMY_Title2.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        rootview.check_text.text = "${SensorData.getPre()}".replace(":", "")
        rootview.check_text3.text = "${SensorData.getTem()}".replace(":", "")
        myitem = Function_Program_Item()
        adapter = Ad_Function_Program(this, myitem)
        rootview.ShowReadView.layoutManager = GridLayoutManager(
            activity,
            1
        ) as RecyclerView.LayoutManager?
        rootview.ShowReadView.adapter = adapter

        for (i in 0 until quantity.toInt()) {
            myitem.add("", Function_Program_Item.PROGRAM_WAIT, "", "", "")
        }

        adapter.notifyDataSetChanged()

        myitem.rowcount = quantity.toInt()
        adapter.notifyDataSetChanged()

        JzActivity.getControlInstance().showDiaLog(false, false,
            Da_Function_Enter_Sensor_ID(
                Dia_SelectBaclk {
                    adapter = Ad_Function_Program(this, myitem)
                    rootview.ShowReadView.adapter = adapter
                    when (PublicBeans.selectWay) {
                        PublicBeans.KEY_IN -> {
                            rootview.sending_data2.text =
                                resources.jzString(R.string.app_sensor_info_read)
                        }
                        PublicBeans.READ -> {
                            rootview.sending_data2.text =
                                resources.jzString(R.string.app_sensor_info_read)
                        }
                        PublicBeans.SCAN -> {
                            rootview.sending_data2.text =
                                resources.jzString(R.string.app_sensor_info_read)
                        } }
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.dia_program_info, true, true, "dia_program_info")
                }
            ), "Da_Function_Enter_Sensor_ID")
        handler.postDelayed({
            JzActivity.getControlInstance().closeDiaLog("dia_program_info")
        },3000)

        rootview.menu.setOnClickListener { JzActivity.getControlInstance().goMenu() }

        rootview.sending_data2.setOnClickListener {

            JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")

            TaskHandler.newInstance.runTaskOnce("sending_data2", runner {
                if (checkComplete()) {
                    Log.e("write", "write")
                    write()
                } else {
                    Log.e("read", "read")
                    read()
                }
                //JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")
            })

            //JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")
        }

    }

    fun read() {

        when (PublicBeans.selectWay) {
            PublicBeans.KEY_IN -> {
                Command.getPrid(object :
                    Command.Companion.idback {
                    override fun result(id: String, pre: String, tem: String, bat: String) {
                        insert(id, pre, tem, bat)
                    }
                })
            }
            PublicBeans.READ -> {
                Command.getPrid(object :
                    Command.Companion.idback {
                    override fun result(id: String, pre: String, tem: String, bat: String) {
                        insert(id, pre, tem, bat)
                    }
                })
            }
            PublicBeans.SCAN -> {
                Command.getPrid(object :
                    Command.Companion.idback {
                    override fun result(id: String, pre: String, tem: String, bat: String) {
                        insert(id, pre, tem, bat)
                    }
                })

            }
        }
    }

    var programfal = 0
    var haveProgram = false
    override fun onResume() {
        super.onResume()
    }

    var lastProgramToggle=false
    fun write() {
        lastProgramToggle=true
        Command.onProgram=true
        Command.Program(myitem,
            object : Command.Companion.notify {
                override fun result() {
                    lastProgramToggle=false
                    handler.postDelayed({
                     if(!lastProgramToggle){Command.onProgram=false}
                    },30*1000)
                    haveProgram = true
                    val a = JzActivity.getControlInstance().findFragByTag("Page_MainActivity")
                    if (a is Page_MainActivity) {
                        a.setMenuBt()
                    }
                    if (myitem.state.contains(Function_Program_Item.PROGRAM_FALSE)) {
                        rootview.show_content.text = resources.jzString(R.string.Programming_failed)
                        rootview.sending_data2.text = "jz.134".getFix()
                        rootview.sending_data4.visibility = View.GONE
                    } else {
                        Page_MainActivity.toggleMenu(true)
                        rootview.sending_data4.setOnClickListener {
                            JzActivity.getControlInstance().changePage(
                                Frag_Function_Relearn_Procedure(1),
                                "Frag_Function_Relearn_Procedure",
                                true
                            )
                        }
                        rootview.menu.setOnClickListener {
                            JzActivity.getControlInstance().goBack()
                        }
                        rootview.menu.text = resources.jzString(R.string.Program)
                        Thread {
                            Log.e("programResult","true")
                            for(i in 0 until PublicBeans.lastFunction.OldSemsor.size){
                                PublicBeans.lastFunction.NewSensor.clear()
                                PublicBeans.lastFunction.NewSensor.add(myitem.programId[i])
                            }
                            MysqDatabase.InsertMemory(
                                OgCommand.tx_memory.toString(),
                                "success"
                            );
                        }.start()
                        rootview.sending_data4.visibility = View.VISIBLE
                        rootview.show_content.text =
                            resources.jzString(R.string.Programming_completed)
                        rootview.sending_data2.visibility = View.GONE
                        rootview.sending_data2.text = resources.jzString(R.string.app_program)
                }
                    adapter.notifyDataSetChanged()}
            })
    }

    fun insert(id: String, pre: String, tem: String, bat: String) {
        if (myitem.programId.contains(id)) {
            JzActivity.getControlInstance().toast("jz.289".getFix())
            return
        }

        var readold = true

        if (PublicBeans.Program_position != -1) {
            myitem.programId[PublicBeans.Program_position] = id
            PublicBeans.Program_position = -1
        } else {
            for (i in 0 until myitem.rowcount) {
                if (myitem.programId[i].isEmpty()) {
                    myitem.programId[i] = id
                    myitem.壓力[i] = pre
                    myitem.溫度[i] = tem
                    myitem.電力[i] = bat
                    readold = false
                    break
                }
            }
        }

        if (checkComplete()) {
            rootview.sending_data2.text = resources.jzString(R.string.app_program)

        }

        adapter.notifyDataSetChanged()
    }

    fun checkComplete(): Boolean {

        for (i in 0 until myitem.rowcount) {

            var exit = ArrayList<String>()
            exit.clear()
            for (j in 0 until myitem.rowcount) {
                if (i != j) {
                    exit.add(myitem.programId[j])
                }
            }

            if (myitem.programId[i].isEmpty() || exit.contains(myitem.programId[i]) || myitem.programId[i].trim() == "") {
                rootview.sending_data2.text = "jz.231".getFix()
                //Log.e("isEmpty","false")
                return false
            }
        }
        rootview.sending_data2.text = resources.jzString(R.string.app_program)
        //Log.e("isEmpty","true")
        return true
    }

}
