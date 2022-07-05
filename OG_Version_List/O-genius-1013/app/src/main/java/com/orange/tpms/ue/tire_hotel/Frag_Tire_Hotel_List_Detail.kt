package com.orange.tpms.ue.tire_hotel

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.ue.kt_frag.Frag_Check_Sensor_Read
import com.orange.tpms.ue.kt_frag.Frag_Select_Mmy
import com.orango.electronic.jzutil.listView
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.Md_Tire_Hotel
import kotlinx.android.synthetic.main.frag_tire_hotel_list_detail.view.*
import kotlinx.android.synthetic.main.frag_tire_hotel_list_detail.view.next
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.*
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__set__languages.view.*
import kotlinx.android.synthetic.main.list_adapter.view.*
import kotlinx.android.synthetic.main.list_adapter.view.tit
import kotlinx.android.synthetic.main.program_false.*
import kotlinx.android.synthetic.main.tire_hotel_tpms.view.*
import kotlinx.android.synthetic.main.tire_hotel_tpms.view.wp
import kotlinx.android.synthetic.main.trend_tpms.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Frag_Tire_Hotel_List_Detail :
    JzFragement(R.layout.frag_tire_hotel_list_detail) {
    //選擇的內容
    companion object {
        //所有Sensor資料
        lateinit var item: MutableMap<String, Any>
        val tpms get() =     (item["TPMS"] as ArrayList<MutableMap<String,String>>).filter { it["season"]== selectTire }
        //更新資料
        fun uploadData() {
            JzActivity.getControlInstance().showDiaLog(R.layout.data_loading, false, false, "data_loading")
            Thread {
                TaskHandler.newInstance.runTaskOnce("UpdateTopMData", runner {
                    val map: MutableMap<String, String> = mutableMapOf()
                    map["request"] = "UpdateTopMData"
                    map["data"] = Gson().toJson(tpms)
                    Log.e("upload","UpdateTopMData"+Gson().toJson(tpms))
                    val data = Md_Tire_Hotel.domain.postRequest(1000 * 10, Gson().toJson(map))
                    JzActivity.getControlInstance().getHandler().post {
                        JzActivity.getControlInstance().closeDiaLog("data_loading")
                        if (data != null) {
                            val json: MutableMap<String, String> = Gson().fromJson(
                                data,
                                object : TypeToken<MutableMap<String, String>>() {}.type
                            )
                            if (json["result"] == "true") {
                                JzActivity.getControlInstance()
                                    .showDiaLog(R.layout.uploadsuccess, true, true, "uploadsuccess")
                                val frag = JzActivity.getControlInstance().getNowPage()
                                if (frag is Frag_Check_Sensor_Read) {
                                    frag.rootview.read.text = "jz.479".getFix()
                                    frag.rootview.read.setOnClickListener {
                                        JzActivity.getControlInstance()
                                            .goBack("Frag_Tire_Hotel_List_Detail")
                                    }
                                }
                            } else {
                                reupload()
                            }
                        } else {
                            reupload()
                        }
                    }
                })
            }.start()
        }

        //選擇的季胎
        var selectTire = ""

        //失敗重新上傳
        fun reupload() {
            JzActivity.getControlInstance().getHandler().post {
                JzActivity.getControlInstance()
                    .showDiaLog(false, false, object : SetupDialog(R.layout.reprogram_false) {
                        override fun dismess() {

                        }

                        override fun keyevent(event: KeyEvent): Boolean {
                            return false
                        }

                        override fun setup(rootview: Dialog) {
                            rootview.tit.text = "jz.477".getFix()
                            rootview.yes.setOnClickListener {
                                JzActivity.getControlInstance().closeDiaLog()
                                uploadData()
                            }
                            rootview.ok.setOnClickListener {
                                JzActivity.getControlInstance().closeDiaLog()
                            }
                        }
                    }, "uploadFasle")
            }
        }
    }
    val sensorIndex: String
        get() = when (selectSeason) {
            "0", "summer" -> {
                "Summer_Tire"
            }
            "1", "winter" -> {
                "Winter_Tire"
            }
            "2", "allSeason" -> {
                "ALL_Tire"
            }
            else -> {
                "Summer_Tire"
            }
        }
    //季胎選擇
    var selectSeason = item["defaultSeason"].toString()
        get() {
            when (field) {
                "0", "summer" -> {
                   return "0"
                }
                "1", "winter" -> {
                    return "1"
                }
                "2", "allSeason" -> {
                    return "2"
                }
                else->{
                    return "0"
                }
            }
        }
        set(value) {

            rootview.tb1.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_dark_blue, null)
            rootview.tb2.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_dark_blue, null)
            rootview.tb3.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_dark_blue, null)
            when (value) {
                "0", "summer" -> {
                    rootview.tb1.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_blue, null)
                }
                "1", "winter" -> {
                    rootview.tb2.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_blue, null)
                }
                "2", "allSeason" -> {
                    rootview.tb3.background = JzActivity.getControlInstance().getRootActivity().resources.getDrawable(R.drawable.stroke_blue, null)
                }
            }
            field = value
            sensorAdapter.notifyDataSetChanged()
            tireAdapter.notifyDataSetChanged()
            listAdapter.notifyDataSetChanged()
        }

    //所有的Sensor
    var sensorAdapter = sensoeList()

    //所有的輪胎
    var tireAdapter = tireList()
    //
    var listAdapter = list_adapter()
    override fun viewInit() {
        rootview.fixLanguage()
        selectSeason = item["defaultSeason"].toString()
        rootview.re.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
        rootview.re2.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
        rootview.re3.layoutManager = GridLayoutManager(context, 2)
        rootview.b1.setOnClickListener { selectSeason = "0"
            selectTire = selectSeason}
        rootview.b2.setOnClickListener { selectSeason = "1"
            selectTire = selectSeason}
        rootview.b3.setOnClickListener { selectSeason = "2"
            selectTire = selectSeason}
        rootview.re.adapter = listAdapter
        rootview.re2.adapter = sensorAdapter
        rootview.re3.adapter = tireAdapter
        if(PublicBean.getS19().isEmpty()){
            rootview.next.visibility=View.GONE
            rootview.upload.setBackgroundResource(R.mipmap.btn_rectangle )
        }
        rootview.next.setOnClickListener {
            selectTire = selectSeason
            PublicBean.selectMmy.make=(item["Vehicle_Information"] as MutableMap<String,String>)["Maker"].toString()
            PublicBean.selectMmy.model=(item["Vehicle_Information"] as MutableMap<String,String>)["Model"].toString()
            PublicBean.selectMmy.year= (item["Vehicle_Information"] as MutableMap<String,String>)["Year"].toString()
            if(PublicBean.mmyExists()){
                JzActivity.getControlInstance().changePage(Frag_Home_Selection(), "Frag_Home_Selection", true)
            }else{
                PublicBean.position=PublicBean.TireHotel
                JzActivity.getControlInstance().changePage(Frag_Select_Mmy(),"Frag_Select_Mmy",true)
            }
        }
        rootview.upload.setOnClickListener {
            uploadData()
        }
        selectTire = selectSeason
        Log.e("e-data",Gson().toJson(item))
    }

    inner class list_adapter : JzAdapter(R.layout.list_adapter) {
        var title = arrayOf(
            "Lagerort",
            "Einlagerung  am",
            "Kunde",
            "Kennzeichen",
            "Menge",
            "HSN / TSN",
            "Bereifung".getFix(),
            "RDKS Anlernart".getFix()
        )

        val it
            get() = arrayOf(
                (item[sensorIndex] as MutableMap<String,String>)["location"],
                (item[sensorIndex] as MutableMap<String,String>)["storageDate"],
                (item["Booking_Information"] as MutableMap<String, String>)["name"],
                (item["Booking_Information"] as MutableMap<String, String>)["plate"],
                (item[sensorIndex] as MutableMap<String,String>)["quantity"],
                (item["Vehicle_Information"] as MutableMap<String, String>)["HSN"] + "/" + (item["Vehicle_Information"] as MutableMap<String, String>)["TSN"],
                (item[sensorIndex] as MutableMap<String,String>)["tire_Size"],
               when(PublicBean.getRelearmMode()){
                   "0"->{
                       "Auto"
                   }
                   "1"->{
                       "OBD"
                   }
                   "2"->{
                       "ALL"
                   }
                   "3"->{
                       "LF"
                   }
                   "4"->{
                       "ban"
                   }
                   else->{
                       "ban"
                   }
               }
            )

        override fun sizeInit(): Int {
            return title.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.tit.text = title[position]
            holder.mView.r1.visibility = View.GONE
            holder.mView.r2.visibility = View.GONE
            holder.mView.r3.visibility = View.GONE
            if (it[position] is String) {
                holder.mView.r1.visibility = View.VISIBLE
                holder.mView.r1t.text = it[position] as String
            } else {
                val data = it[position] as Array<String>
                if (data.size == 3) {
                    holder.mView.r1.visibility = View.VISIBLE
                    holder.mView.r2.visibility = View.VISIBLE
                    holder.mView.r3.visibility = View.VISIBLE
                    holder.mView.r1t.text = data[0]
                    holder.mView.r2t.text = data[1]
                    holder.mView.r3t.text = data[2]
                } else {
                    holder.mView.r1.visibility = View.VISIBLE
                    holder.mView.r2.visibility = View.VISIBLE
                    holder.mView.r1t.text = data[0]
                    holder.mView.r2t.text = data[1]
                }
            }
        }

    }

    inner class sensoeList : JzAdapter(R.layout.tire_hotel_tpms) {
        val tpms get() =     (item["TPMS"] as ArrayList<MutableMap<String,String>>).filter { it["season"]== selectSeason }
        override fun sizeInit(): Int {
            return 4
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.title.visibility = if (position == 0) View.VISIBLE else View.GONE
            holder.mView.wp.text = arrayOf("VL","VR","HL","HR")[position]
            holder.mView.sensorID.text =  tpms[position]["sensorID"]
            holder.mView.pressure.text = tpms[position]["pressure"]
            holder.mView.temp.text = tpms[position]["temp"]
            holder.mView.bat.text = tpms[position]["bat"]

        }
    }

    inner class tireList : JzAdapter(R.layout.trend_tpms) {
        override fun sizeInit(): Int {
            return 4
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.spinner2.adapter = ArrayAdapter<String>(
                JzActivity.getControlInstance().getRootActivity(),
                R.layout.spinner,
                arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
            )
           if(tpms[position]["depth"]!!.contains(".")){
               tpms[position]["depth"]=tpms[position]["depth"]!!.substring(0,tpms[position]["depth"]!!.indexOf("."))
           }
            holder.mView.spinner2.setSelection(
                arrayOf("1", "2", "3", "4", "5", "6", "7", "8").indexOf(tpms[position]["depth"]),
                true
            )
            for (i in holder.mView.spinner2.listView()) {
                if (i is TextView) {
                    if (i.text == "1") {
                        i.setTextColor(resources.getColor(R.color.color_red))
                    }
                }
            }
            holder.mView.spinner2.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        pos: Int,
                        id: Long
                    ) {
                        if (tpms[position]["depth"] != arrayOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8"
                            )[pos]
                        ) {
                            val item=Frag_Tire_Hotel_List_Detail.tpms
                            item[position]["depth"]=arrayOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8"
                            )[pos]
                            notifyDataSetChanged()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            holder.mView.wp.text = arrayOf("VL","VR","HL","HR")[position].toUpperCase()
        }
    }
}