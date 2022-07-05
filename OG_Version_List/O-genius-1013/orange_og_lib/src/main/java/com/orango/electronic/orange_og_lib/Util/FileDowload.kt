package com.orango.electronic.orange_og_lib.Util


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.jzutil.getWebResource
import com.orango.electronic.jzutil.storeObject
import com.orango.electronic.orange_og_lib.Callback.Donload_C
import com.orango.electronic.orange_og_lib.Callback.Update_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import com.orango.electronic.orange_og_lib.beans.storeLocal

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipInputStream

object FileDowload {
    val serverRout: String
        get() {
            if (JzActivity.getControlInstance().getPro("toBento2", false)) {
                return "http://bento2.orange-electronic.com"
            }
            when (PublicBean.lastCountry) {
                "Taiwan" -> {
                    return "http://bento3.orange-electronic.com"
                }
            }
            return "http://bento2.orange-electronic.com"
        }

    //國家
    val country: String get() = JzActivity.getControlInstance().getPro("counrty", "EU")

    //本地版本
    lateinit var localData: FileJsonVersion
    lateinit var onlineDate: FileJsonVersion

    //Progress
    lateinit var caller: Update_C

    //所有檔案數量
    var allCount = 0
    var progress = 0
        set(value) {
            TaskHandler.newInstance.runTaskDelay("UpdateProgress", 0.5, runner {
                if (progress * 100 / allCount > 95) {
                    caller.Updateing(95)
                } else {
                    caller.Updateing(progress * 100 / allCount)
                }
            })
            field = value
        }

    //判斷是否需要初次加載
    fun needInit(): Boolean {
        val data = "DataListVersion".getObject<FileJsonVersion>()
        if (data == null) {
            return true
        } else {
            val item = data as FileJsonVersion
            return item.lanVersion == "no" || item.mmyVersion == "no" || item.s18List.isEmpty() || item.s19List.isEmpty() || item.obdList.isEmpty()
        }
    }

    //判斷是否需要更新
    fun needUpdate(): Boolean {
        val data = "DataListVersion".getObject<FileJsonVersion>()
        val dataOnline = "OnlineDataVersion".getObject<FileJsonVersion>()
        if (data == null || dataOnline == null) {
            return true
        } else {
            return "${OgPublic.versionCodes}.apk" != dataOnline.apkVersion || data.lanVersion != dataOnline.lanVersion || "${OgPublic.MCU_NUMBER}.x2" != dataOnline.mcuVerion || data.mmyVersion != dataOnline.mmyVersion || data.s18List != dataOnline.s18List || data.s19List != dataOnline.s19List || data.obdList != dataOnline.obdList || data.obdList2 != dataOnline.obdList2
        }
    }

    //清除資料加載
    fun clearInit() {
        FileJsonVersion("EU").storeObject("DataListVersion")
    }

    //檢查新版本
    fun ckeckNewVersion(): Boolean {
        val rfid = JzActivity.getControlInstance().getPro("RFID", false)
        val data = "${serverRout}/getVersion?country=$country&type=OG&beta=${if (OgPublic.beta) "true" else "false"}&autoRead=true&RFID=${rfid}".getWebResource(10 * 1000)
        Log.e("ckeckNewVersion", "$data")
        return if (data != null) {
            val map: FileJsonVersion =
                Gson().fromJson(data, object : TypeToken<FileJsonVersion>() {}.type)
            Log.e("ckeckNewVersion", "$map")
            map.storeObject("OnlineDataVersion")
            true
        } else {
            false
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
            localData = tempData
            onlineDate = "OnlineDataVersion".getObject<FileJsonVersion>()!!
            Log.e("onLineObdData", onlineDate.obdList2.toString())
            if (localData.obdList2 == null) {
                localData.obdList2 = mutableMapOf()
            }
            if (onlineDate.obdList2 == null) {
                onlineDate.obdList2 = mutableMapOf()
            }
            allCount =
                (4 + onlineDate.obdList.size + onlineDate.s19List.size + onlineDate.s18List.size + onlineDate.obdList2.size) + 28
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
            if (!downAllS18(caller)) {
                caller.Finish(false)
                return
            }
            if (!downMuc(activity)) {
                caller.Finish(false)
                return
            }
            if (!downAllObd(activity, caller)) {
                caller.Finish(false)
                return
            }
            if (!downAllObdII(activity, caller)) {
                caller.Finish(false)
                return
            }
            if (!Downloadapk(caller)) {
                caller.Finish(false)
                return
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


    //下載所有obd檔案
    fun downAllObd(activity: Activity, caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "DownAllS18")
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
            val result = FileDonload(
                JzActivity.getControlInstance()
                    .getRootActivity().applicationContext.filesDir.path + "/" + name + ".srec",
                if (OgPublic.beta)
                    "${serverRout}/Orange%20Cloud/Beta/Drive/OBD%20DONGLE/$name/${onlineDate.obdList[name]}"
                else
                    "${serverRout}/Orange%20Cloud/Drive/OBD%20DONGLE/$name/${onlineDate.obdList[name]}",
                30
            ) { }
            if (result) {
                localData.obdList[name] = onlineDate.obdList[name]!!
                localData.storeLocal()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //下載所有obdII檔案
    fun downAllObdII(activity: Activity, caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "downAllObdII")
            var i = 0;
            for (a in onlineDate.obdList2) {
                val res = donloadObdII(a.key)
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
    fun donloadObdII(name: String): Boolean {
        try {
            if (onlineDate.obdList2[name] == localData.obdList2[name]) {
                return true
            }
            val result = FileDonload(
                JzActivity.getControlInstance()
                    .getRootActivity().applicationContext.filesDir.path + "/" + name + ".srec",
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Drive/OBD%20DONGLE2/$name/${onlineDate.obdList2[name]}" else "${serverRout}/Orange%20Cloud/Drive/OBD%20DONGLE2/$name/${onlineDate.obdList2[name]}",
                30
            ) { }
            if (result) {
                localData.obdList2[name] = onlineDate.obdList2[name]!!
                localData.storeLocal()
            }
            return result
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
                val res = donloads19(
                    a.key
                )
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
            val result = FileDonload(
                "/sdcard/files19/$name.s19",
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Database/SensorCode/OG%20SIII/$name/${onlineDate.s19List[name]}" else "${serverRout}/Orange%20Cloud/Database/SensorCode/OG%20SIII/$name/${onlineDate.s19List[name]}",
                30, Donload_C { })
            if (result) {
                localData.s19List[name] = onlineDate.s19List[name]!!
                localData.storeLocal()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //下載所有s18檔案
    fun downAllS18(caller: Update_C): Boolean {
        try {
            Log.e("FileDowload", "DownAllS18")
            var i = 0;
            for (a in onlineDate.s18List) {
                val res = donloads18(
                    a.key
                )
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

    //下載s18檔案
    fun donloads18(name: String): Boolean {
        try {
            if (localData.s18List[name] == onlineDate.s18List[name]) {
                return true
            }
            val result = FileDonload(
                "/sdcard/files18/$name.s18",
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Database/SensorCode/SII/$name/${onlineDate.s18List[name]}" else "${serverRout}/Orange%20Cloud/Database/SensorCode/SII/$name/${onlineDate.s18List[name]}",
                30, Donload_C { })
            if (result) {
                localData.s18List[name] = onlineDate.s18List[name]!!
                localData.storeLocal()
            }
            return result
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
            if (onlineDate.mcuVerion == "${OgPublic.MCU_NUMBER}.x2") {
            return true
            }
            val rfid = JzActivity.getControlInstance().getPro("RFID", false)
            val result = if (rfid) FileDonload(
                activity.applicationContext.filesDir.path + "/update.x2",
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Drive/OG/RFID_FW/${onlineDate.mcuVerion}" else "${serverRout}/Orange%20Cloud/Drive/OG/RFID_FW/${onlineDate.mcuVerion}",
                30
            ) {

            } else FileDonload(
                activity.applicationContext.filesDir.path + "/update.x2",
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Drive/OG/Firmware/${onlineDate.mcuVerion}" else "${serverRout}/Orange%20Cloud/Drive/OG/Firmware/${onlineDate.mcuVerion}",
                30
            ) {

            }
            if (result) {
                Cmd_Og.reboot()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //下載apk
    fun Downloadapk(caller: Update_C): Boolean {
        try {
            var tempprogress = progress
            Log.e("FileDowload", "Downloadapk")
            if ("${OgPublic.versionCodes}.apk" == onlineDate.apkVersion) {
                return true
            }
            val link =
                if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Drive/OG/APP%20Software/${onlineDate.apkVersion}" else "${serverRout}/Orange%20Cloud/Drive/OG/APP%20Software/${onlineDate.apkVersion}"
            val result = FileDonload(
                "/sdcard/update/update.apk",
                link,
                1200
            ) { progress ->
                this.progress = tempprogress + progress
            }
            if (result) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setDataAndType(
                    Uri.fromFile(File("/sdcard/update/update.apk")),
                    "application/vnd.android.package-archive"
                )//image/*
                JzActivity.getControlInstance().getRootActivity()
                    .startActivity(intent)//此处可能会产生异常（比如说你的MIME类型是打开视频，但是你手机里面没装视频播放器，就会报错）
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            Log.e("apkdown", "下載完成")
            return result
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
            val activity = OgPublic.getInstance
            activity.itemDAO.close()
            return if (FileDonload(
                    "/sdcard/update/mmy.db",
                    if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Database/MMY/$country/${onlineDate.mmyVersion}" else "${serverRout}/Orange%20Cloud/Database/MMY/$country/${onlineDate.mmyVersion}",
                    1200
                ) {}
            ) {
                val result = activity.itemDAO.dbinit(File("/sdcard/update/mmy.db").inputStream())
                Log.e("doloadmmy->", "" + File("/sdcard/update/mmy.db").inputStream().readBytes().size)
                activity.itemDAO.create()
                if (result) {
                    localData.mmyVersion = onlineDate.mmyVersion
                    localData.storeLocal()
                }
                result
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //下載語言
    fun doloadLan(): Boolean {
        try {
            progress++
            if (localData.lanVersion == onlineDate.lanVersion) {
                return true
            }
            val activity = OgPublic.getInstance
            activity.onlinelanDB.close()
            return if (FileDonload(
                    "/sdcard/update/language.db",
                    if (OgPublic.beta) "${serverRout}/Orange%20Cloud/Beta/Language/${onlineDate.lanVersion}" else "${serverRout}/Orange%20Cloud/Language/${onlineDate.lanVersion}",
                    1200
                ) {}
            ) {
                val result = activity.onlinelanDB.dbinit(File("/sdcard/update/language.db").inputStream())
                activity.onlinelanDB.create()
                if (result) {
                    localData.lanVersion = onlineDate.lanVersion
                    localData.storeLocal()
                }
                result
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun FileDonload(path: String, url: String, timeout: Int, caller: Donload_C): Boolean {
        try {
            Log.d("path", path)
            // UrlRequest.ignoreSsl()
            val conn = URL("$url?zip=true").openConnection() as HttpURLConnection
            conn.connectTimeout = 1000 * timeout
            val `is` = conn.inputStream
            val fos = FileOutputStream(File("$path.zip"))
            val bufferSize = 8192
            var prread = 0.0
            val buf = ByteArray(bufferSize)
            while (true) {
                val read = `is`.read(buf)
                prread += read.toDouble() / 1024 / 1024
                caller.Updateing(prread.toInt())
                if (read == -1) {
                    break
                }
                fos.write(buf, 0, read)
            }
            `is`.close()
            fos.close()
            val buffer = ByteArray(1024)
            val zis = ZipInputStream(FileInputStream(File("$path.zip")))
            //獲取壓縮包中的entry，並將其解壓
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                val newFile = File(path)
                Log.e("newFile", newFile.name)
                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                Log.e("newFileSize", "${newFile.name}->" + newFile.readBytes().size)
                //解壓完成一個entry，再解壓下一個
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("錯誤", e.message.toString())
            return false
        }

    }
}
