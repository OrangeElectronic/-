package com.orange.tpms.ue.activity

import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson
import com.jianzhi.customer_service.adapter.Qustion
import com.jianzhi.customer_service.frag.Frag_UserMessage
import com.orange.jzchi.jzframework.DiaClass
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.BleManager
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.app.TPMSApp
import com.orange.tpms.service.KeyEventDefine
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orange.tpms.ue.dialog.Dia_check_update
import com.orange.tpms.ue.kt_frag.*
import com.orange.tpms.utils.BleCommand
import com.orange.tpms.utils.ObdCommand
import com.orango.electronic.orange_og_lib.Callback.Scan_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Dialog.Dia_Check_Data
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.OgPublic.Companion.beta
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.*
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.activity_kt.view.*
import kotlinx.android.synthetic.main.fragment_frag__program__detail.view.*
import java.util.*


class KtActivity : JzActivity(), Scan_C {
    lateinit var bleManager: BleManager
    var BleCommand = BleCommand()
    var ObdCommand = ObdCommand()
    var Fragnumber = 0

    override fun savedInstanceAble(): Boolean {
        return false
    }

    override fun keyEventListener(event: KeyEvent): Boolean {
        return KeyEventDefine.keyEvent(event) ?: keyEventLinstener.keyEventListener(event, this)
    }


    override fun viewInit(rootview: View) {

        if(!TPMSApp.TestMode){
            Systool.openGPS(false)
        }
        Thread {
            Thread.sleep(5000)
            handler.post { setKeyguardEnable(false) }
        }.start()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        OgPublic.fragid = R.id.frage
        bleManager = BleManager(this)
        bleManager.BleHelper.closeBle()
        Thread {
            try {
                while (!getControlInstance().isFrontDesk()) {
                    Thread.sleep(100)
                }
                handler.post { getControlInstance().setHome(kt_splash(), "kt_splash") }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }.start()
        //適應舊版語言選擇
        when (getControlInstance().getPro("Lan", "nodata")) {
            "en" -> {
                getControlInstance().setLanguage(Locale("en"))
                getControlInstance().setPro("Lan", "nodata")
            }
            "zh" -> {
                getControlInstance().setLanguage(Locale("zh-rCN"))
                getControlInstance().setPro("Lan", "nodata")
            }
            "tw" -> {
                getControlInstance().setLanguage(Locale("tw"))
                getControlInstance().setPro("Lan", "nodata")
            }
            "it" -> {
                getControlInstance().setLanguage(Locale("it"))
                getControlInstance().setPro("Lan", "nodata")
            }
            "de" -> {
                getControlInstance().setLanguage(Locale("de"))
                getControlInstance().setPro("Lan", "nodata")
            }
        }
        SensorData.call = object : SensorData.callback {
            override fun run() {
                val frag = getControlInstance().getNowPage()
                if (frag is Frag_New_ProgramDetail) {
                    Log.e("initObserver", "haveInit")
                    for (i in frag.sensordara) {
                        Log.e(
                            "initObserver",
                            "sensor:${i.id} | ${i.programResult == SensorData.STATE_NORMAL}"
                        )
                    }
                    if (frag.sensordara.filter {
                            it.id.isNotEmpty() && it.programResult == SensorData.STATE_NORMAL
                        }.size != PublicBean.ProgramNumber) {
                        frag.rootview.bt_program.text = "jz.231".getFix()
                    } else {
                        frag.rootview.bt_program.text =
                            frag.rootview.resources!!.jzString(com.orango.electronic.orange_og_lib.R.string.app_program)
                    }

                }
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("觸控", "$event")
        return super.onTouchEvent(event)
    }

    override fun GetScan(a: String?) {
        if (Fraging != null) {
            (Fraging as RootFragement).ScanContent(a!!)
        }
    }

    override fun changePageListener(tag: String, frag: androidx.fragment.app.Fragment) {
        Cmd_Og.cancel=true
        val nowfrag = getControlInstance().getNowPage()
        if (nowfrag is Frag_UserMessage) {
            Thread {
                while (!nowfrag.haveRootView()) {
                }
                getControlInstance().getHandler().post { nowfrag.rootview.fixLanguage() }
            }.start()
            nowfrag.it.clear()
            nowfrag.it.add(Qustion("ble", "jz.456".getFix(), R.mipmap.app_btn_bluetooth_n))
            nowfrag.it.add(Qustion("Update", "jz.153".getFix(), R.mipmap.app_btn_updata_n))
            nowfrag.it.add(
                Qustion(
                    "Check sensor",
                    "jz.10".getFix(),
                    R.mipmap.app_btn_cheack_sensor_n
                )
            )
            nowfrag.it.add(Qustion("Program", "jz.149".getFix(), R.mipmap.app_btn_program_n))
            nowfrag.it.add(Qustion("ID copy", "jz.13".getFix(), R.mipmap.app_btn_id_copy_n))
            nowfrag.it.add(Qustion("OBD relearn", "jz.15".getFix(), R.mipmap.app_btn_id_copy_n))
            nowfrag.it.add(
                Qustion(
                    "ID copy by OBD",
                    "jz.401".getFix(),
                    R.mipmap.app_btn_id_copy_by_obd_n
                )
            )
            nowfrag.it.add(Qustion("USB PAD", "USB PAD".getFix(), R.mipmap.app_btn_usb_pad_n))
            nowfrag.it.add(Qustion("more", "jz.457".getFix(), 0))
        }

        if (tag == "Frag_home") {
            JzActivity.getControlInstance().closeDiaLog()
            checkVersion()
        }
        val mainfrag = getControlInstance().getActionBar()
        if (mainfrag != null && mainfrag is Frag_Manager) {
            if ((PublicBean.position == PublicBean.OBD_RELEARM || PublicBean.position == PublicBean.ID_COPY_OBD) && bleManager.BleHelper.isConnect() && BleManager.connectBlename.contains(
                    "OBD"
                )
            ) {
                mainfrag.rootview.logout.visibility = View.VISIBLE
                mainfrag.rootview.logout.setImageResource(R.mipmap.icon_obdii)
            } else if ((PublicBean.position == PublicBean.PAD_PROGRAM || PublicBean.position == PublicBean.PAD_COPY) && bleManager.BleHelper.isConnect() && BleManager.connectBlename.contains(
                    "PAD"
                )
            ) {
                mainfrag.rootview.logout.visibility = View.VISIBLE
                mainfrag.rootview.logout.setImageResource(R.mipmap.icon_link_pad)
            } else {
                mainfrag.rootview.logout.visibility = View.GONE
            }
            Log.e("switch", tag)
            Log.e("switch", "count:" + supportFragmentManager.backStackEntryCount)
            mainfrag.back.setImageResource(R.mipmap.back)
            if (beta) {
                mainfrag.titlebar.setBackgroundResource(R.color.material_deep_teal_200)
            } else {
                mainfrag.titlebar.setBackgroundResource(R.color.color_orange)
            }
            mainfrag.back.setOnClickListener { getControlInstance().goBack() }
            if (supportFragmentManager.backStackEntryCount != 0) {
                mainfrag.back.setImageResource(R.mipmap.back)
                mainfrag.back.visibility = View.VISIBLE
                mainfrag.back.setOnClickListener { getControlInstance().goBack() }
            } else {
                mainfrag.back.visibility = View.GONE
            }
            Cmd_Og.NowTag = "${Fragnumber++}"
            if (Fragnumber > 100) {
                Fragnumber = 0
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            mainfrag.rootview.customer.visibility = View.GONE
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getControlInstance().toggleActionBar(true)
            when (tag) {
                "Dia_Check_Data" -> {
                    getControlInstance().toggleActionBar(false)
                }
                "Frag_UserMessage" -> {
                    getControlInstance().toggleActionBar(false)
                }
                "Frag_Reset_Ps" -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mainfrag.tit.text = "jz.341".getFix()
                }
                "Frag_Register" -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mainfrag.tit.text = "jz.6".getFix()
                }
                "Frag_Sign_in" -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mainfrag.tit.text = "jz.75".getFix()
                }
                "Frag_Wifi" -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mainfrag.tit.text = "jz.40".getFix()
                }
                "PrivaryPolicy" -> {
                    mainfrag.tit.text = "jz.71".getFix()
                }
                "Frag_Languages" -> {
                    mainfrag.tit.text = "jz.152".getFix()
                }
                "Frag_home" -> {
                    mainfrag.rootview.logout.visibility = View.GONE
                    mainfrag.rootview.customer.visibility = View.VISIBLE
                    mainfrag.tit.text = "O-Genius"
                }
                "Frag_CheckSensor" -> {
                    mainfrag.tit.text = "jz.404".getFix()
                }
                "Frag_Check_Sensor_Read" -> {
                    mainfrag.tit.text =  "jz.404".getFix()
                }
                "Frag_Check_Location" -> {
                    mainfrag.tit.text = resources!!.jzString(R.string.app_home_check_sensor)
                }
                "Frag_Program_Sensor" -> {
                    mainfrag.tit.text = "jz.12".getFix()
                }
                "Frag_Id_Copy" -> {
                    mainfrag.tit.text = resources!!.jzString(R.string.app_home_id_copy)
                }
                "Frag_Relearm" -> {
                    mainfrag.tit.text = resources!!.jzString(R.string.Relearn_Procedure)
                }
                "Frag_Pad_IdCopy" -> {
                    mainfrag.tit.text =
                        "jz.403".getFix()
                }
                "Frag_Pad_Program" -> {
                    mainfrag.tit.text =
                        "jz.402".getFix()
                }
                "Frag_WebView" -> {
                    mainfrag.tit.text =
                        JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.Online_shopping)
                }
                "Frag_Obd" -> {
                    when (PublicBean.position) {
                        PublicBean.OBD_RELEARM -> {
                            mainfrag.tit.text =
                                resources.jzString(R.string.app_home_obdii_relearna)
                        }
                        PublicBean.ID_COPY_OBD -> {
                            mainfrag.tit.text = JzActivity.getControlInstance()
                                .getRootActivity().resources.jzString(R.string.idcopyobd2)
                        }
                    }
                }
                "Frag_Setting" -> {
                    mainfrag.tit.text = resources!!.jzString(R.string.app_setting)
                }
            }
        } else {
            if (tag == "kt_splash") {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    override fun dialogLinstener(dialog: DiaClass, tag: String) {
        val view = dialog.dialog.window!!.decorView
        view.fixLanguage()
    }

    override fun onDestroy() {
        super.onDestroy()
        OgPublic.getInstance.closeDB()
        OgPublic.getInstance.stop()
        OgPublic.instance = null
    }

    override fun onResume() {
        super.onResume()
        Thread{
            Thread.sleep(3000)
            handler.post { OgPublic.getInstance.start(KtActivity::class.java, TPMSApp.TestMode) }
        }.start()
    }

    //屏蔽螢幕鎖功能
    private fun setKeyguardEnable(enable: Boolean) {
        //disable
        if (!enable) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
            return
        }
        //enable
        window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }

    //更新檢查判斷
    fun checkVersion() {
        Log.e("localmcu", OgPublic.MCU_NUMBER)
        Log.e("localapk", "" + OgPublic.versionCodes)
        Log.e("Firebasetitle", "" + getControlInstance().getPro("Firebasetitle", "nodata"))
        val model = Dia_Check_Data.getUSer_InfoMation()
        if (FileDowload.needInit()) {
            getControlInstance()
                .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        } else {
            if (FileDowload.needUpdate()) {
                getControlInstance()
                    .showDiaLog(false, false, Dia_check_update(), "Dia_check_update")
            } else {
                if (model != null) {
                    Log.e("userinfo", "" + Gson().toJson(model))
                    if (model.countrySelect == "NA") {
                        getControlInstance().setHome(Frag_Select_Area(model), "Frag_Select_Area")
                    } else if (model.storeType == "NA" || model.company == "NA" || model.firstNmae == "NA" || model.lastName == "NA"
                        || model.telPhone == "NA" || model.area == "NA" || model.state == "NA" || model.city == "NA" || model.street == "NA" || model.areaNO == "NA" || model.country == "NA"
                    ) {
                        getControlInstance().setHome(Dia_Check_Data(model), "Dia_Check_Data")
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        OgPublic.getInstance.stop()

    }
}
