package com.orange.tpms.ue.kt_frag


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.Ad_Function_Check_Sensor_Id_Copy
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.beans.SensorData
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.jzString
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__copy.view.*

class Frag_Check_Sensor_Read_IdCopy_Selection() : RootFragement(R.layout.fragment_frag__check__sensor__copy) {
    lateinit var adapter: Ad_Function_Check_Sensor_Id_Copy
    var myitem = ArrayList<SensorData>()
    var wheelC = PublicBean.wheelCount()
    override fun viewInit() {
        rootview.fixLanguage()
        if (wheelC < 6) {
            for (i in 0 until wheelC) {
                myitem.add(
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
        } else {
            for (i in 0 until wheelC) {
                myitem.add(SensorData("${i + 1}", "${i + 1}"))
            }
        }

        PublicBean.ProgramNumber = myitem.size

        rootview.Read_MMY_Title.text =
            "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"


        when (wheelC) {
            2 -> {
                rootview.motorre.visibility = View.VISIBLE
            }
            4 -> {
                rootview.cw_car.visibility = View.VISIBLE
            }
            5 -> {
                rootview.cw_car.visibility = View.VISIBLE
            }
            6 -> {
                rootview.Car6Data_R.visibility = View.VISIBLE
            }
            10 -> {
                rootview.CarData_R.visibility = View.VISIBLE
            }
        }
        adapter = Ad_Function_Check_Sensor_Id_Copy(myitem, rootview.cw_car, rootview.motorre,this)
        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        adapter.notifyDataSetChanged()
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance()
                .changePage(Frag_Relearm_Detail(true), "Frag_Function_Relearn_Procedure", true)
        }

        rootview.read.setOnClickListener {
            if(adapter.focus!=-1){
                trigger()
                return@setOnClickListener
            }
            myitem.map {   it.isBlock=it.id.isEmpty()}
            //當為tireHotel 讀取完要跳資料上傳
           if(myitem.any { it.id.isNotEmpty() }){
               JzActivity.getControlInstance().changePage(Frag_Function_New_OBD_ID_copy(sensorInitial = myitem), "Frag_Function_New_OBD_ID_copy", true)
           }else{
           }
        }

    }

    override fun enter() {
        super.enter()
        rootview.read.performClick()
    }
    override fun anyKey() {
        super.anyKey()
    }

    override fun onTop() {
        super.onTop()
        adapter.focus = if (adapter.focus - 1 >= 0) adapter.focus - 1 else adapter.focus
        if (adapter.focus >= 0) {
            rootview.ShowReadView.scrollToPosition(adapter.focus)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDown() {
        super.onDown()
        adapter.focus = if (adapter.focus + 1 < myitem.size) adapter.focus + 1 else adapter.focus
        if (adapter.focus >= 0) {
            rootview.ShowReadView.scrollToPosition(adapter.focus)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onKeyTrigger() {
        trigger()
    }

    fun trigger() {
        if (adapter.focus == -1) {
            return
        }
        Thread {
            TaskHandler.newInstance.runTaskOnce("read") {
                handler.post { EmptyDialog(R.layout.data_loading).show() }
                val sensorData = Cmd_Og.GetId(PublicBean.getHEX(), PublicBean.getLfPower())
                if (sensorData.success) {
                    for (i in 0 until myitem.size) {
                        if (myitem[i].id == sensorData.id) {
                            myitem[i] = SensorData(myitem[i].wheelPosition, myitem[i].wheelString)
                            break
                        }
                    }
                    sensorData.wheelPosition = myitem[adapter.focus].wheelPosition
                    sensorData.wheelString = myitem[adapter.focus].wheelString
                    myitem[adapter.focus] = sensorData
                    adapter.focus = -1
                    for (i in 0 until myitem.size) {
                        if (myitem[i].id.isEmpty()) {
                            adapter.focus = i
                            break
                        }
                    }
                } else {
                    handler.post {
                        JzActivity.getControlInstance().showDiaLog(
                            R.layout.data_loading_false,
                            true,
                            false,
                            "data_loading_false"
                        )
                    }
                }
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog("data_loading")
                    adapter.notifyDataSetChanged()
                    OgPublic.getInstance.playBeepSoundAndVibrate()
                }

            }
        }.start()
    }

    override fun onKeyScan() {
        super.onKeyScan()
        if (adapter.focus == -1) {
            return
        }
        PublicBean.scanSensor(object : PublicBean.scanBack {
            override fun getSensor(id: String) {
                for (i in 0 until myitem.size) {
                    if (myitem[i].id == id) {
                        myitem[i] = SensorData(myitem[i].wheelPosition, myitem[i].wheelString)
                        break
                    }
                }
                val sensorData=SensorData()
                sensorData.id=id
                sensorData.success=true
                sensorData.wheelPosition = myitem[adapter.focus].wheelPosition
                sensorData.wheelString = myitem[adapter.focus].wheelString
                myitem[adapter.focus] = sensorData
                adapter.focus = -1
                for (i in 0 until myitem.size) {
                    if (myitem[i].id.isEmpty()) {
                        adapter.focus = i
                        break
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}
