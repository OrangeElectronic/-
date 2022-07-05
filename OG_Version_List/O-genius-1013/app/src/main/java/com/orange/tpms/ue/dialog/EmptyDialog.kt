package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.RequestFun
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.data_loading_cancel.*
import kotlinx.android.synthetic.main.provide_email.*
import kotlinx.android.synthetic.main.reduce_start.*
import kotlinx.android.synthetic.main.reduce_start.pass
import kotlinx.android.synthetic.main.reduce_start.textView69
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EmptyDialog(var id: Int) {
    fun show() {
        JzActivity.getControlInstance().showDiaLog(false, true, object : SetupDialog(id) {
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                if (event.keyCode == 4) {
                    Cmd_Og.cancel = true
                }
                return false
            }

            override fun setup(rootview: Dialog) {
            }
        }, "data_loading")
    }

}

object DialogDefine {
    //需要洩壓
    fun showNeedReduceDia(image:Int,text:String) {
        JzActivity.getControlInstance().showDiaLog(false, true, object : SetupDialog(R.layout.data_loading_cancel) {
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                if (event.keyCode == 4) {
                    Cmd_Og.cancel = true
                }
                return false
            }

            override fun setup(rootview: Dialog) {
                rootview.textView69.setOnClickListener { Cmd_Og.cancel = true }
                rootview.pass.text = text
                rootview.imageView48.setImageResource(image)
            }
        }, "NeedReduceDia")
    }

    //洩壓開始
    fun startReduce(callback: () -> Unit,image:Int,text:String) {
        JzActivity.getControlInstance().showDiaLog(false, true, object : SetupDialog(R.layout.reduce_start) {
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                return true
            }

            override fun setup(rootview: Dialog) {
                rootview.textView69.setOnClickListener {
                    JzActivity.getControlInstance().closeDiaLog()
                }
                rootview.textView70.setOnClickListener {
                    JzActivity.getControlInstance().closeDiaLog()
                    callback()
                }
                rootview.imageViewPlave.setImageResource(image)
                rootview.pass.text = text
            }
        }, "reduce_start")
    }
    //寄資料到客戶手上
    fun sendDataToCustomer(sensorLists:ArrayList<SensorData>){
        JzActivity.getControlInstance().showDiaLog(false, false, object : SetupDialog(R.layout.provide_email) {
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                return true
            }

            override fun setup(rootview: Dialog) {
                val text=JzActivity.getControlInstance().getPro("emailsend",if(OgPublic.admin.contains("@")) OgPublic.admin else "")
                rootview.eemail.setText(text)
                rootview.textView73.setOnClickListener {
                    EmptyDialog(R.layout.data_loading).show()
                    JzActivity.getControlInstance().setPro("emailsend",rootview.eemail.text.toString())
                    Thread{
                        val sensorList:ArrayList<MutableMap<String,Any>> = arrayListOf()
                        sensorLists.map {
                            val sensorData:MutableMap<String,Any> = mutableMapOf()
                            sensorData["id"]=it.id
                            sensorData["tem"]=it.c
                            sensorData["kpa"]=it.kpa
                            sensorList.add(sensorData)
                        }
                        val data:MutableMap<String,Any> = mutableMapOf()
                        data["sensorList"]=sensorList
                        data["temUnit"]=SensorData.getTem().replace("°","")
                        data["account"]=PublicBean.admin
                        data["mail"]=rootview.eemail.text.toString()
                        data["preUnit"]=SensorData.getPre().replace(":","")
                        data["exFun"]="Program"
                        data["make"]= PublicBean.selectMmy.make
                        data["model"]= PublicBean.selectMmy.model
                        data["year"]= PublicBean.selectMmy.year
                        data["time"]=SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
                        val result=RequestFun("PublicLogic","sendSensorData").request(data)
                        JzActivity.getControlInstance().getHandler().post {
                            JzActivity.getControlInstance().closeDiaLog("data_loading")
                            if(result==null||(result["result"]==false)){
                                JzActivity.getControlInstance().showDiaLog(
                                    R.layout.send_mail_false,
                                    true,
                                    false,
                                    "data_loading_false"
                                )
                            }else{
                                JzActivity.getControlInstance().closeDiaLog()
                            }
                        }
                    }.start()
                }

                rootview.textView72.setOnClickListener {
                    JzActivity.getControlInstance().closeDiaLog()
                }

            }
        }, "provide_email")
    }
}

