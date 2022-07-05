package com.orange.og_lite.Util


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.og_lite.Util.fixLanguage
import com.jianzhi.jzblehelper.FormatConvert
import com.jzsql.lib.mmySql.InitCaller
import com.onsemi.ble.UpdateControllerListener

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Command
import com.orange.og_lite.MainActivity
import com.orange.og_lite.OgCommand
import com.orange.og_lite.R
import com.orange.og_lite.beans.FileJsonVersion
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.PublicBeans.Companion.MCU_NUMBER
import com.orange.og_lite.beans.PublicBeans.Companion.bootloaderVersion
import com.orange.og_lite.beans.storeLocal
import com.orange.og_lite.callback.Donload_C
import com.orange.og_lite.callback.Update_C
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orango.electronic.jzutil.*
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.loading_view.*
import org.jsoup.Jsoup

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


object Util_FileDowload {
    //國家
    var country: String
        get() = JzActivity.getControlInstance().getPro("country", "EU")
        set(value) {
            JzActivity.getControlInstance().setPro("country", value)
        }

    //是否為舊版
    var oldVersion: Boolean = false

    //本地版本
    lateinit var localData: FileJsonVersion
    lateinit var onlineDate: FileJsonVersion

    //Progress
    lateinit var caller: Update_C

    //所有檔案數量
    var allCount = 0
    var progress = 0
        set(value) {
            caller.Updateing(progress * 100 / allCount)
            field = value
        }
    //判斷是否需要初次加載
    fun needInit(): Boolean {
        val data = "DataListVersion".getObject<FileJsonVersion>()
        if (data == null) {
            return true
        } else {
            val item = data as FileJsonVersion
            return item.lanVersion == "no" || item.mmyVersion == "no" || item.s18List.isEmpty() || item.s19List.isEmpty() || item.obdList.isEmpty() || item.mcuVerion.contains(
                MCU_NUMBER
            )
        }
    }

    //判斷是否需要更新
    fun needUpdate(): Boolean {
        val data = "DataListVersion".getObject<FileJsonVersion>()
        val dataOnline = "OnlineDataVersion".getObject<FileJsonVersion>()
        if (data == null || dataOnline == null) {
            return true
        } else {
            val item = data as FileJsonVersion
            val itemOnline = dataOnline as FileJsonVersion
            return item.lanVersion != itemOnline.lanVersion || item.mmyVersion != itemOnline.mmyVersion || item.s19List != itemOnline.s19List || item.obdList != itemOnline.obdList || if (oldVersion) !"OGLE_OG_HWV100_SWV102.srec".contains(
                String(MCU_NUMBER.hexToByte())
            ) else !itemOnline.mcuVerion.contains(
                String(MCU_NUMBER.hexToByte())
            ) || if (oldVersion) false else !itemOnline.bleVersion.contains(PublicBeans.bleVersion) || bootloaderVersion!="10"
        }
    }

    //清除資料加載
    fun clearInit() {
        FileJsonVersion("EU").storeObject("DataListVersion")
    }

    //    https://bento2.orange-electronic.com/getVersion?country=EU&type=OG_lite&beta=true
    //檢查新版本
    fun ckeckNewVersion(): Boolean {
        val data =
            "https://bento2.orange-electronic.com/getVersion?country=$country&type=OG_lite&beta=${if (PublicBeans.beta) "true" else "false"}".getWebResource(10 * 1000)
        Log.e("ckeckNewVersion", "$data")
        if (data != null) {
            val map: FileJsonVersion =
                Gson().fromJson(data, object : TypeToken<FileJsonVersion>() {}.type)
            Log.e("ckeckNewVersion", "$map")
            map.storeObject("OnlineDataVersion")
            return true
        } else {
            return false
        }
    }

    //檢查更新
    fun ChechUpdate(activity: Activity, caller: Update_C) {
        try {
            this.caller = caller
            Log.e("place", "開始下載")
            if (!ckeckNewVersion()) {
                caller.Finish(false)
                return
            }
            Log.e("place", "下載地區為${country}")
            JzActivity.getControlInstance()
                .setPro("updateDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
            var tempData = "DataListVersion".getObject<FileJsonVersion>()
            if (tempData == null) {
                tempData = FileJsonVersion(country)
            }
            localData = tempData as FileJsonVersion
            onlineDate = "OnlineDataVersion".getObject<FileJsonVersion>() as FileJsonVersion
            allCount =
                (4 + onlineDate.obdList.size + onlineDate.s19List.size + onlineDate.s18List.size) + 28
            progress = 0
            if (!doloadmmy()) {
                caller.Finish(false)
                return
            }
            if (!doloadLan()) {
                caller.Finish(false)
                return
            }
            if (!downAllS19(caller)) {
                caller.Finish(false)
                return
            }
            if (!downAllObd(activity, caller)) {
                caller.Finish(false)
                return
            }
            if (!downMuc(activity)) {
                caller.Finish(false)
                return
            }
            if(!downBootloader(activity)){
                caller.Finish(false)
                return
            }
            if (!oldVersion) {
                if (!downLoadBle()) {
                    caller.Finish(false)
                    return
                }
            }

            localData = onlineDate
            localData.storeLocal()
            caller.Finish(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("place", "下載失敗")
            caller.Finish(false)
        }

    }

    fun downLoadBle(): Boolean {
        //下載mcu
        try {
            Log.e("FileDowload", "DownMuc")
            progress++
            if (onlineDate.bleVersion.contains(PublicBeans.bleVersion)) {
                return true
            }
            val data = GetByte(
                if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OG_lite/Ble%20Firmware/${onlineDate.bleVersion}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OG_lite/Ble%20Firmware/${onlineDate.bleVersion}",
                1000*10
            )
            if (data == null) {
                return false
            } else {
                (JzActivity.getControlInstance().getRootActivity() as MainActivity).canConnect =
                    false
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance()
                        .showDiaLog(R.layout.data_loading, false, false, "ble_data_loading")
                    BleFw_Update(JzActivity.getControlInstance().getRootActivity()).runUpdate(
                        object : UpdateControllerListener {
                            override fun onProgressChanged(
                                progress: Int,
                                total: Int,
                                step: String
                            ) {
                            }

                            override fun onCompleted(status: Int) {
                                (JzActivity.getControlInstance()
                                    .getRootActivity() as MainActivity).canConnect = true
                                JzActivity.getControlInstance().getHandler().post {
                                    JzActivity.getControlInstance().closeDiaLog("ble_data_loading")
                                }
                                Log.e("fotaCompel", "fotaCompel $status")
                            }
                        }
                        ,
                        if (oldVersion) JzActivity.getControlInstance()
                            .getRootActivity().assets.open("OGLite_Old.fota").readBytes() else data)
                }

                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //下載所有obd檔案
    fun downAllObd(activity: Activity, caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "downAllObd")
            var i = 0;
            for (a in onlineDate.obdList) {
                val res = donloadObd(a.key)
                progress++
                i++
                if (!res) {
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //下載obd
    fun donloadObd(name: String): Boolean {
        try {
            if (onlineDate.obdList[name] == localData.obdList[name]) {
                return true
            }
            val data = GetText(
                if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OG_Lite_OBD/$name/${onlineDate.obdList[name]}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OG_Lite_OBD/$name/${onlineDate.obdList[name]}",
                1000*10
            )
            if (data != "nodata") {
                PublicBeans.insertFile(name, data)
                localData.obdList[name] = onlineDate.obdList[name]!!
                localData.storeLocal()
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //下載所有s19檔案
    fun downAllS19(caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "DownAllS19")
            var i = 0;
            for (a in onlineDate.s19List) {
                val res = donloads19(a.key)
                progress++
                i++
                if (!res) {
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //下載s19檔案
    fun donloads19(name: String): Boolean {
        try {
            if (localData.s19List[name] == onlineDate.s19List[name]) {
                return true
            }
            val data = GetText(
                if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/SensorCode/SIII/$name/${onlineDate.s19List[name]}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/SensorCode/SIII/$name/${onlineDate.s19List[name]}",
                1000*10
            )
            if (data != "nodata") {
                PublicBeans.insertFile(name, data)
                localData.s19List[name] = onlineDate.s19List[name]!!
                localData.storeLocal()
                return true
            } else {
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //下載mcu
    fun downMuc(activity: Activity): Boolean {
        try {
            Log.e("FileDowload", "DownMuc")
            progress++
            if (if (oldVersion) "OGLE_OG_HWV100_SWV102.srec".contains(String(MCU_NUMBER.hexToByte())) else onlineDate.mcuVerion.contains(
                    String(MCU_NUMBER.hexToByte())
                )
            ) {
                return true
            }

            val data = GetText(
                if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OG_lite/Firmware/${onlineDate.mcuVerion}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OG_lite/Firmware/${onlineDate.mcuVerion}",
                1000*10
            )
            if (data == "nodata") {
                return false
            } else {
                var wait = true
                var result = false
                if (oldVersion) {
                    PublicBeans.insertFile(
                        "mcu",
                        String(activity.assets.open("OGLE_OG_HWV100_SWV102.srec").readBytes())
                    )
                } else {
                    PublicBeans.insertFile("mcu", data)
                }
                JzActivity.getControlInstance()
                    .setPro("mcuinit", onlineDate.mcuVerion.replace(".hex", ""))

                Command.ogCommand.WriteBootloader(if (oldVersion) 132 else 40, object : Update_C {
                    override fun Finish(a: Boolean) {
                        result = a
                        wait = false
                        if (a) {
                            JzActivity.getControlInstance()
                                .setPro("Version", onlineDate.mcuVerion.toByteArray().toHex())
                        }
                        Command.handler.post {
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance().toast(if (a) "success" else "false")
                        }
                    }

                    override fun Updateing(progress: Int) {
                        Command.handler.post {
                            JzActivity.getControlInstance()
                                .showDiaLog(
                                    false,
                                    false,
                                    object : SetupDialog(R.layout.loading_view) {
                                        override fun dismess() {

                                        }

                                        override fun keyevent(event: KeyEvent): Boolean {
                                            return false
                                        }

                                        override fun setup(rootview: Dialog) {
                                            rootview.tit.text =
                                                rootview.context.resources.jzString(com.orange.og_lite.R.string.Updating) + "...${progress}%"
                                        }
                                    },
                                    "loading_view"
                                )
                        }
                    }
                })
                while (wait) {
                    Thread.sleep(100)
                }
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }
    //下載Bootloader
    fun downBootloader(activity: Activity): Boolean {
        try {
            Log.e("FileDowload", "downBootloader")
            progress++
            if (if (oldVersion) true else onlineDate.bootloaderVersion.contains(
                    bootloaderVersion)
            ) {
                return true
            }

            val data = GetText(
                if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Drive/OG_lite/Boot/${onlineDate.bootloaderVersion}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Drive/OG_lite/Boot/${onlineDate.bootloaderVersion}",
                10
            )
            if (data == "nodata") {
                return false
            } else {
                var wait = true
                var result = false
                PublicBeans.insertFile(
                    "bootloader",
                    data
                )
                Command.ogCommand.WriteRootBootloader(if (oldVersion) 132 else 40, object : Update_C {
                    override fun Finish(a: Boolean) {
                        result = a
                        wait = false
                        if (a) {
                            JzActivity.getControlInstance()
                                .setPro("bootloaderVersion", onlineDate.bootloaderVersion.replace(".srec",""))
                        }
                        Command.handler.post {
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance().toast(if (a) "success" else "false")
                        }
                    }

                    override fun Updateing(progress: Int) {
                        Command.handler.post {
                            JzActivity.getControlInstance()
                                .showDiaLog(
                                    false,
                                    false,
                                    object : SetupDialog(R.layout.loading_view) {
                                        override fun dismess() {

                                        }

                                        override fun keyevent(event: KeyEvent): Boolean {
                                            return false
                                        }

                                        override fun setup(rootview: Dialog) {
                                            rootview.tit.text =
                                                rootview.context.resources.jzString(com.orange.og_lite.R.string.Updating) + "...${progress}%"
                                        }
                                    },
                                    "loading_view"
                                )
                        }
                    }
                })
                while (wait) {
                    Thread.sleep(100)
                }
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }
    //下載mmy
    fun doloadmmy(): Boolean {
        try {
            progress++
            if (onlineDate.mmyVersion == localData.mmyVersion) {
                return true
            }
            val activity = JzActivity.getControlInstance().getRootActivity() as MainActivity
            activity.item.close()
            val result =
                activity.item.init_ByUrl(if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Database/MMY/$country/${onlineDate.mmyVersion}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Database/MMY/$country/${onlineDate.mmyVersion}")
            activity.item.create()
            if (result) {
                localData.mmyVersion = onlineDate.mmyVersion
                localData.storeLocal()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //下載語言
    fun doloadLan(): Boolean {
        try {
            progress++
            Log.e("FileDowload", "doloadLan")
            if (localData.lanVersion == onlineDate.lanVersion) {
                return true
            }
            val activity = JzActivity.getControlInstance().getRootActivity() as MainActivity
            activity.onlinelanDB.close()
            val result =
                activity.onlinelanDB.init_ByUrl(if (PublicBeans.beta) "https://bento2.orange-electronic.com/Orange%20Cloud/Beta/Language/${onlineDate.lanVersion}" else "https://bento2.orange-electronic.com/Orange%20Cloud/Language/${onlineDate.lanVersion}")
            activity.onlinelanDB.create()
            if (result) {
                localData.lanVersion = onlineDate.lanVersion
                JzActivity.getControlInstance().setPro("lanName", localData.lanVersion)
                localData.storeLocal()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    fun GetText(url: String, timeout: Int): String {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout * 1000
            conn.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            var line: String? = null
            val strBuf = StringBuffer()
            line = reader.readLine()
            while (line != null) {
                strBuf.append(line)
                line = reader.readLine()
            }
            return strBuf.toString()
        } catch (e: Exception) {
            Log.e("錯誤", e.message)
            e.printStackTrace()
            return "nodata"
        }

    }

    fun GetByte(url: String, timeout: Int): ByteArray? {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout * 1000
            conn.requestMethod = "GET"
            return conn.inputStream.readBytes()
        } catch (e: Exception) {
            Log.e("錯誤", "error" + e.message)
            e.printStackTrace()
            return null
        }

    }
}