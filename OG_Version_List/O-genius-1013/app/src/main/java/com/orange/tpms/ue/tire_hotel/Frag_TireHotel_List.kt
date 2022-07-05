package com.orange.tpms.ue.tire_hotel

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.Api_Tire_Hotel
import com.orange.tpms.ExtensionUtil
import com.orange.tpms.R
import com.orange.tpms.ue.dialog.Dia_Hint
import com.orange.tpms.ue.dialog.Dia_Hotel_Nodata
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.Callback.Donload_C
import com.orango.electronic.orange_og_lib.Callback.Update_C
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.Md_Tire_Hotel
import kotlinx.android.synthetic.main.frag_motor_select.view.*
import kotlinx.android.synthetic.main.frag_tire_hotel_list.view.*
import kotlinx.android.synthetic.main.hotel_list_item.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Frag_TireHotel_List : JzFragement(R.layout.frag_tire_hotel_list) {
    var item: ArrayList<String> = ArrayList()
    var adapter = list_adapter()
    var canRun = true
    var test = false
    var setDate: Date = Date()
        set(value) {
            field = value
            item.clear()
            rootview.animation_view3.visibility = View.VISIBLE
            rootview.textView19.text = SimpleDateFormat("yyyy/MM/dd").format(value)
            dataLoading()
        }

    override fun viewInit() {
        rootview.fixLanguage()

        refresh = true
        item.clear()
        rootview.recy.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
        rootview.recy.adapter = adapter
        setDate = Date()
        rootview.backto.text = "<"
        rootview.nextto.text = ">"
        rootview.backto.setOnClickListener {
            setDate = setDate.calDay(-1)
        }
        rootview.nextto.setOnClickListener {
            setDate = setDate.calDay(+1)
        }
        if(Api_Tire_Hotel.requestType=="00"){
            checkAppVersion()
        }else{
            rootview.totopm.visibility=View.GONE
        }
        rootview.totopm.setOnClickListener {
            if (ExtensionUtil.appInstalledOrNot("de.topm.mobile2.android") && JzActivity.getControlInstance()
                    .getPro("localTopM", "null") == JzActivity.getControlInstance()
                    .getPro("TopMversion", "")
            ) {
                JzActivity.getControlInstance().showDiaLog(true, false, object :
                    SetupDialog(R.layout.tire_hotel_goback_hint) {
                    override fun dismess() {
                        val packageManager = activity!!.packageManager
                        var intent: Intent? = Intent()
                        intent = packageManager.getLaunchIntentForPackage("de.topm.mobile2.android")
                        startActivity(intent)
                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        JzActivity.getControlInstance().closeDiaLog("tire_hotel_goback_hint")
                        return true
                    }

                    override fun setup(rootview: Dialog) {
                    }
                }, "tire_hotel_goback_hint")
            } else {
                JzActivity.getControlInstance()
                    .showDiaLog(false, true, Dia_UpateIng(), "Dia_UpateIng")
            }
        }
    }


    fun dataLoading() {
        Thread {
            if (!canRun) {
                return@Thread
            }
            val data = Md_Tire_Hotel.getSchedule(setDate)
            if (data != null) {
                if (!canRun) {
                    return@Thread
                }
                handler.post {
                    rootview.animation_view3.visibility = View.GONE
                    item = Gson().fromJson(data, object : TypeToken<ArrayList<String>>(){}.type)
                    if (item.size == 0) {
                        if (JzActivity.getControlInstance().getNowPageTag() == "Frag_TireHotel_List") {
                            if(Api_Tire_Hotel.requestType=="00"){
                                JzActivity.getControlInstance()
                                    .showDiaLog(false, false, Dia_Hotel_Nodata(), "Dia_Hotel_Nodata")
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            } else {
                dataLoading()
            }
        }.start()
    }

    inner class SensorData(
        var plateNumber: String,
        var sensorID: String,
        var pressure: String,
        var temp: String,
        var wheelPosition: String,
        var season: String,
        var depth: String,
        var bat: String
    )

    inner class list_adapter : JzAdapter(R.layout.hotel_list_item) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val inItem: MutableMap<String, Any> = Gson().fromJson(
                item[position],
                object : TypeToken<MutableMap<String, Any>>() {}.type
            )
            holder.mView.name.text = (inItem["Booking_Information"] as MutableMap<String, String>)["name"]
            holder.mView.plate.text = (inItem["Booking_Information"] as MutableMap<String, String>)["plate"]
            when (inItem["defaultSeason"]) {
                "winter" -> {
                    holder.mView.store.text =
                        (inItem["Winter_Tire"] as MutableMap<String, String>)["storageDate"]
                }
                "summer" -> {
                    holder.mView.store.text =
                        (inItem["Summer_Tire"] as MutableMap<String, String>)["storageDate"]
                }
                "allSeason" -> {
                    holder.mView.store.text =
                        (inItem["ALL_Tire"] as MutableMap<String, String>)["storageDate"]
                }
            }
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inItem["appoint"].toString())

            holder.mView.textView21.text = (SimpleDateFormat("HH:mm").format(date))

            holder.mView.setOnClickListener {
                PublicBean.selectMmy.make =
                    (inItem["Vehicle_Information"] as MutableMap<String, String>)["Maker"]!!
                PublicBean.selectMmy.model =
                    (inItem["Vehicle_Information"] as MutableMap<String, String>)["Model"]!!
                PublicBean.selectMmy.year =
                    (inItem["Vehicle_Information"] as MutableMap<String, String>)["Year"]!!
                Frag_Tire_Hotel_List_Detail.item = Gson().fromJson(
                    item[position],
                    object : TypeToken<MutableMap<String, Any>>() {}.type
                )
                JzActivity.getControlInstance().changePage(
                    Frag_Tire_Hotel_List_Detail(),
                    "Frag_Tire_Hotel_List_Detail",
                    true
                )
            }
            holder.mView.fixLanguage()
        }

        override fun sizeInit(): Int {
            return item.size
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        canRun = false
    }

    //下載TireHotel apk
    fun downLoadapk(caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "Downloadapk")
            val results = "${serverRout}/SelectMysql".postRequest(
                20 * 1000,
                "SELECT * FROM orange_userdata.fileversion where name='topM'"
            )
            var data: ArrayList<MutableMap<String, Any>>? = null
            if (results != null) {
                Log.e("topMVersion", results)
                data = Gson().fromJson(results,
                    object : TypeToken<ArrayList<MutableMap<String, Any>>>() {}.type)
                Log.e("topMVersion", data!![0]["version"].toString())
            } else {
                Log.e("topMVersion", "getNull")
                caller.Finish(false)
                return false
            }
            JzActivity.getControlInstance().setPro("TopMversion", data[0]["version"].toString())
            val link = data[0]["rout"].toString()
            val result = FileDowload.FileDonload(
                "/sdcard/update/topM.apk",
                link,
                1200, Donload_C { progress ->
                    caller.Updateing(progress)
                })
            if (result) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setDataAndType(
                    Uri.fromFile(File("/sdcard/update/topM.apk")),
                    "application/vnd.android.package-archive"
                )//image/*
                JzActivity.getControlInstance().getRootActivity()
                    .startActivity(intent)
            }
            JzActivity.getControlInstance().setPro("localTopM", data[0]["version"].toString())
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    //
    /**
     * 下載Dialog
     * */
    inner class Dia_UpateIng : SetupDialog(R.layout.update_dialog) {
        override fun dismess() {

        }

        override fun keyevent(event: KeyEvent): Boolean {
            return false
        }

        override fun setup(rootview: Dialog) {
            Thread {
                val result = downLoadapk(object : Update_C {
                    override fun Updateing(progress: Int) {
                        handler.post {
                            TaskHandler.newInstance.runTaskDelay("UpdateProgress", 0.5, runner {
                                handler.post {
                                    rootview.findViewById<TextView>(com.orango.electronic.orange_og_lib.R.id.tit).text =
                                        """${"jz.265".getFix()}..$progress%"""
                                }
                            })
                        }
                    }

                    override fun Finish(a: Boolean) {

                    }
                })
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog("Dia_UpateIng")
                    if (!result) {
                        JzActivity.getControlInstance()
                            .showDiaLog(true, false, Dia_Hint("jz.386".getFix()), "Dia_Hint")

                    }
                }
            }.start()
        }

    }

}

fun Date.calDay(a: Int): Date {
    val rightNow = Calendar.getInstance()
    rightNow.time = this
    rightNow.add(Calendar.DAY_OF_YEAR, a)
    return rightNow.time
}

/**
 * 判斷tireHotel是否已下載
 * */
fun checkAppVersion() {
    Thread {
        val result = "${serverRout}/SelectMysql".postRequest(
            60 * 1000,
            "SELECT * FROM orange_userdata.fileversion where name='topM'"
        )
        if (result != null) {
            Log.e("topMVersion", result)
            val data: ArrayList<MutableMap<String, Any>> = Gson().fromJson(result,
                object : TypeToken<ArrayList<MutableMap<String, Any>>>() {}.type)
            JzActivity.getControlInstance().setPro("TopMversion", data[0]["version"].toString())
            Log.e("topMVersion", data[0]["version"].toString())
        } else {
            Log.e("topMVersion", "getNull")
        }
    }.start()


}
