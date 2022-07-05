package com.orange.tpms.ue.kt_frag


import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.Ad_Function_Check_Sensor
import com.orange.tpms.ue.dialog.DialogDefine
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orange.tpms.ue.tire_hotel.Frag_Tire_Hotel_List_Detail
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.beans.SensorData
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean.openOrClose
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.*
import kotlinx.android.synthetic.main.new_function_obd_id_copy.*

class Frag_Check_Sensor_Read(var upload: Boolean = false) : RootFragement(R.layout.fragment_frag__check__sensor__read) {
    lateinit var adapter: Ad_Function_Check_Sensor
    var myitem = ArrayList<SensorData>()
    var wheelC = PublicBean.wheelCount()
    override fun viewInit() {
        rootview.fixLanguage()
        if (openOrClose()) {
            JzActivity.getControlInstance().showDiaLog(R.layout.need_open, true, false, "need_open")
        }
        if (upload) {
            for (i in PublicBean.readable.indices) {
                if (PublicBean.readable[i]) {
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
            }
            PublicBean.ProgramNumber = myitem.size
        } else {
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
        }

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
        adapter = Ad_Function_Check_Sensor(myitem, rootview.cw_car, rootview.motorre)
        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        adapter.notifyDataSetChanged()
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

        rootview.read.setOnClickListener {
            //當為tireHotel 讀取完要跳資料上傳
            if (upload && myitem.none { it.id.isEmpty() }) {
                if (upload) {
                    Frag_Tire_Hotel_List_Detail.tpms.map { ite ->
                        myitem.map {
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
                    JzActivity.getControlInstance()
                        .changePage(Frag_Relearm_Detail(), "Frag_Relearm_Detail", true)
                }
            } else {
                if (adapter.focus == -1) {
                    return@setOnClickListener
                }
                val oeRead = PublicBean.getOERead()
                val index = arrayOf("02", "03").indexOf(oeRead)
                Thread {
                    TaskHandler.newInstance.runTaskOnce("read") {
                        //判斷結果
                        val conFigResult: (sensorData: SensorData) -> Unit = { sensorData ->
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
                                if (upload && myitem.none { it.id.isEmpty() }) {
                                    handler.post {
                                        rootview.read.setTextColor(resources.getColor(R.color.color_orange))
                                        rootview.read.text = "Hochladen"
                                    }
                                }
                            } else {
                                handler.post {
                                    if(!sensorData.cancel){
                                        JzActivity.getControlInstance().showDiaLog(R.layout.need_open, true, false, "need_open")
                                    }
                                }
                            }
                            handler.post {
                                rootview.textView27.visibility = View.GONE
                                JzActivity.getControlInstance().closeDiaLog("data_loading")
                                JzActivity.getControlInstance().closeDiaLog("NeedReduceDia")
                                adapter.notifyDataSetChanged()
                                OgPublic.getInstance.playBeepSoundAndVibrate()
                            }
                        }
                        handler.post {
                            EmptyDialog(R.layout.data_loading).show()
                        }
                        val sensorData = Cmd_Og.GetId(
                            PublicBean.getHEX(),
                            PublicBean.getLfPower(),
                            arrayOf(15, 35, 600, 600)[arrayOf("00", "01", "02", "03").indexOf(oeRead)],
                            oeRead
                        ) {
                            handler.post {
                                when (oeRead) {
                                    "02" -> {
                                        DialogDefine.showNeedReduceDia(
                                            arrayOf(
                                                R.mipmap.reduce,
                                                R.mipmap.magna
                                            )[index], arrayOf("jz.613".getFix(), "jz.615".getFix())[index]
                                        )
                                    }
                                    "03" -> {
                                        DialogDefine.showNeedReduceDia(
                                            arrayOf(
                                                R.mipmap.reduce,
                                                R.mipmap.magna
                                            )[index], arrayOf("jz.613".getFix(), "jz.615".getFix())[index]
                                        )
                                    }
                                    else -> {
                                        EmptyDialog(R.layout.data_loading).show()
                                    }
                                }
                            }
                        }
                        handler.post { JzActivity.getControlInstance().closeDiaLog() }
                        conFigResult(sensorData)
                    }
                }.start()
            }
        }
        if (PublicBean.position == PublicBean.TireHotel_insert) {
            rootview.next.visibility = View.VISIBLE
            rootview.next.setOnClickListener {
                if (myitem.none { it.id != "" }) {
                    JzActivity.getControlInstance().toast("jz.45".getFix())
                    return@setOnClickListener
                }
                JzActivity.getControlInstance().changePage(Frag_Plate_Info("0"), "Frag_Plate_Info", true)
            }
        } else {
            rootview.next.visibility = View.GONE
        }
    }

    override fun anyKey() {
        super.anyKey()
        if (haveRootView()) {
            rootview.textView27.visibility = View.GONE
        }
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
        rootview.read.performClick()
    }

}
