package com.orango.electronic.orange_og_lib

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.telephony.TelephonyManager
import com.example.jzonlinedata.OnlineData
import com.example.jzonlinedata.getOnlineData
import com.example.jztaskhandler.TaskHandler
import com.jzsql.lib.mmySql.JzSqlHelper
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.FileDowload.ckeckNewVersion
import com.orango.electronic.orange_og_lib.Util.FileDowload.needInit
import com.orango.electronic.orange_og_lib.Util.Systool.setNaVaGation
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import java.io.File
import android.os.Looper
import android.util.Log
import com.example.jzlifttool.LifeTimer
import com.orango.electronic.jzutil.*
import com.orango.electronic.orange_og_lib.Util.*
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.beans.Md_Tire_Hotel
import java.lang.Exception
import java.util.*


class OgPublic : HomeWatcher.OnHomePressedListener {
    override fun onHomePressed() {
        Cmd_Og.cancel = true
        BluetoothAdapter.getDefaultAdapter().disable()
        JzActivity.getControlInstance().closeDiaLog()
        JzActivity.getControlInstance().goMenu()
    }

    override fun onHomeLongPressed() {
        Cmd_Og.cancel = true
        JzActivity.getControlInstance().closeDiaLog()
        JzActivity.getControlInstance().goMenu()
    }

    var play = VibMediaUtil(JzActivity.getControlInstance().getRootActivity())
    fun playBeepSoundAndVibrate() {
        play.playBeepSoundAndVibrate()
    }
    enum class FwType{Normal,Rfid,NotSet}
    companion object {
        //判斷運作FW類型
        var fwType=FwType.NotSet
        var instance: OgPublic? = null
        val getInstance: OgPublic
            get() {
                if (instance == null) {
                    instance = OgPublic()
                }
                return instance!!
            }

        //-------------------------------------------------------------------------取得硬體版本-----------------------------------------------------------------------------------
        var hardWareVersion
            get() = JzActivity.getControlInstance().getPro("hardWareVersion", "no")
            set(value) {
                JzActivity.getControlInstance().setPro("hardWareVersion", value)
            }

        //-------------------------------------------------------------------------取得MCU版本-----------------------------------------------------------------------------------
        val MCU_NUMBER
            get() = JzActivity.getControlInstance().getPro("Version", "no").replace(
                ".x2",
                ""
            )

        //-------------------------------------------------------------------------取得資料庫版本-----------------------------------------------------------------------------------
        val DataVersion
            get() = JzActivity.getControlInstance().getPro(
                "mmyname",
                "MMY_EU_list_V0.5_191113"
            ).substring(12).replace(".db", "")

        //---------------------------------------------------------------------------取得APK版本------------------------------------------------------------------------------------
        val versionCodes: Int
            get() {
                //获取包管理器
                val pm = JzActivity.getControlInstance().getRootActivity().packageManager
                //获取包信息
                try {
                    val packageInfo = pm.getPackageInfo(
                        JzActivity.getControlInstance().getRootActivity().packageName,
                        0
                    )
                    //返回版本号
                    return packageInfo.versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    return 0
                }
            }

        //--------------------------------------------------------------------------Device ID-----------------------------------------------------------------------------------------
        val deviceID: String
            @SuppressLint("MissingPermission")
            get() {
                try {
                    val telephonyManager =
                        JzActivity.getControlInstance().getRootActivity()
                            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    return telephonyManager.deviceId
                }catch (e:Exception){e.printStackTrace()
                return ""
                }
            }

        //------------------------------------------------------------------------登入完成後要執行的動作---------------------------------------------------------------------------------------
        var signSuccess: signInBack? = null

        //---------------------------------------------------------------------------登入要切換的Fragid---------------------------------------------------------------------------------------
        var fragid: Int = 0

        //==================================================登入的帳密=========================================================================
        val admin get() = JzActivity.getControlInstance().getPro("admin", "nodata")
        val password get() = JzActivity.getControlInstance().getPro("password", "nodata")
        val beta get() = JzActivity.getControlInstance().getPro("beta", false)
        val autoRead get() = JzActivity.getControlInstance().getPro("autoRead", false)
    }

    val homeWatcher = HomeWatcher(JzActivity.getControlInstance().getRootActivity())
    lateinit var restartClass: Class<*>

    //------------------------------------------------------------------------多國語言資料庫------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    val languageDB: JzSqlHelper
        get() {
            if (templanDB == null) {
                templanDB =
                    JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "templanDB.db")
                templanDB!!.close()
                templanDB!!.init_ByAsset("alllan.db")
                templanDB!!.create()
                return templanDB!!
            } else {
                return templanDB!!
            }
        }
    var templanDB: JzSqlHelper? = null
    var onlinelanDB: JzSqlHelper =
        JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "onlinelanDB.db")
    val testLan: Boolean
        get() {
            return JzActivity.getControlInstance().getPro("testlan", false)
        }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    var sqlite =
        JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "updatememory")
    var itemDAO =
        JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "usb_tx_mmy.db")
    var favoriteDAO =
        JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "favorite.db")
    var tester = false
    var first = true
    fun start(act: Class<*>, test: Boolean) {
        val file = File("/sdcard/update/")
        val files19 = File("/sdcard/files19/")
        val files18 = File("/sdcard/files18/")
        while (!file.exists()) {
            file.mkdirs()
        }
        while (!files19.exists()) {
            files19.mkdirs()
        }
        while (!files18.exists()) {
            files18.mkdirs()
        }
        val context = JzActivity.getControlInstance().getRootActivity()
        tester = test
        homeWatcher.setOnHomePressedListener(this)
        homeWatcher.startWatch()
        restartClass = act
        setNaVaGation(true)
        Thread { Cmd_Og.open() }.start()
        if (!test) {
            Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 50)
            Systool.screenReceiver(context)
            if (first) {
                Systool.hookWebView()
//                LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0, 1000 * 10) {
//                    if(PublicBean.closeDisCharge){
//                        return@schedule
//                    }
//                    if (HardwareApp.getBatteryState() == 0) {
//                        JzActivity.getControlInstance().getHandler().post {
//                            JzActivity.getControlInstance().showDiaLog(
//                                R.layout.dia_ischarage,
//                                false,
//                                false,
//                                "dia_ischarage"
//                            )
//                        }
//                    } else {
//                        JzActivity.getControlInstance().getHandler().post {
//                            JzActivity.getControlInstance().closeDiaLog("dia_ischarage")
//                        }
//                    }
//                }
            }
        }
        if (first) {
            var onSendError=false
            LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0, 1000 * 60) {
                Log.e("ErrorMessageJson", "timer")
                if(!onSendError){
                    onSendError=true
                    Thread {
                        Log.e("ErrorMessageJson", "runner")
                        Looper.prepare()
                        Log.e("ErrorMessageJson", "trySend")
                        val objList = "ErrorMessageJson".listObject()
                        for (i in 0 until objList.size) {
                            if (i < objList.size - 50) {
                                objList[i].name.deleteObject("ErrorMessageJson")
                                continue
                            }
                            Log.e("ErrorMessageJson", "trySend$i")
                            val res =
                                "${serverRout}/sendSES".postRequest(
                                    10 * 1000,
                                    objList[i].json.stringToUnicode()!!
                                )
                            Log.e("ErrorMessageJson", "SendResult" + res)
                            if (res == "true") {
                                objList[i].name.deleteObject("ErrorMessageJson")
                            }
                        }
                        Log.e("ErrorMessageJson", "sendFinish")
                        onSendError=false
                        Looper.loop()
                    }.start()
                }
            }
        }
        if (first) {
            var onSendMemory=false
            LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0,
                1000 * 30
            ) {
                if(!onSendMemory){
                    onSendMemory=true
                    Thread {
                        Looper.prepare()
                        TaskHandler.newInstance.runTaskOnce("updatememory") {
                            if (!haveonline) {
                                onLineData()
                            } else {
                                if (admin.getOnlineData() == "block" || deviceID.getOnlineData() == "block") {
                                    JzActivity.getControlInstance().closeDiaLog()
                                    FileDowload.clearInit()
                                    JzActivity.getControlInstance().setPro("admin", "nodata")
                                    getInstance.resrart()
                                }
                            }
                            val data = DataUpload()
                            data.start()
                        }
                        onSendMemory=false
                        Looper.loop()
                    }.start()
                }

            }
            LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0,1000*60){
                Thread{
                    Looper.prepare()
                    try {
                        val data = DataUpload()
                        data.uploadLocation()
                    }catch (e:Exception){}
                    Looper.loop()
                }.start()
            }
            LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0,
                1000 * 60
            ) {
                Thread {
                    Looper.prepare()
                    TaskHandler.newInstance.runTaskOnce("onlineCkeckNewVersion") {
                        try{
                            if (!needInit()) {
                                ckeckNewVersion()
                            }
                        }catch (e:Exception){}
                    }
                    Looper.loop()
                }.start()
            }
            if(JzActivity.getControlInstance().getLanguage()== Locale("de")){
                LifeTimer(JzActivity.getControlInstance().getRootActivity().lifecycle).schedule(0,
                    1000 * 60
                ) {
                    try {
                        val count = Md_Tire_Hotel.getScheduleCount()
                        Log.e("getScheduleCount", "getScheduleCount$count")
                    } catch (e: Exception) {
                    }
                }
            }
        }
        first = false
    }

    fun resrart() {
        JzActivity.getControlInstance().getRootActivity().recreate()
    }

    var haveonline = false
    fun onLineData() {
        JzActivity.getControlInstance().getHandler().post {
            OnlineData.newInstance.setUP(
                JzActivity.getControlInstance().getRootActivity(),
                "${serverRout}/Orange%20Cloud/OnlineData/BlockDevice.txt",
                object :
                    com.example.jzonlinedata.callback {
                    override fun result(a: Boolean) {
                        if (a) {
                            haveonline = true
                        }
                    }
                })
        }
    }

    fun stop() {

        Cmd_Og.cancel = true
        homeWatcher.setOnHomePressedListener(null)
        // 登出廣播
        homeWatcher.stopWatch()
    }

    fun closeDB() {
        sqlite.close()
        itemDAO.close()
        favoriteDAO.close()
        languageDB.close()
    }

}

interface signInBack {
    fun result(a: Boolean)
}

