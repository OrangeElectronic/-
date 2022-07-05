package com.orange.oglite_glitter

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import androidx.core.net.toUri
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JavaScriptInterFace
import com.jianzhi.glitter.plugins.GpsUtil
import com.onsemi.ble.UpdateControllerListener
import com.orange.ble_plugin.Glitter_BLE
import com.orange.oglite_glitter.FileUtil.Companion.getBitmapFormUri
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.system.exitProcess


class MyApp :Application(){
    var handler= Handler(Looper.getMainLooper())
    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true);
        Glitter_BLE(applicationContext).also { it.create() }
        GlitterActivity.setUp("file:///android_asset/appData",appName = "appData").onCreate {
            it.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        //BLE更新
        var onUpdateBle=false
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("updateBle") {
            if(onUpdateBle){
                it.responseValue["result"]=false
                it.finish()
                return@JavaScriptInterFace
            }
            val data = GetByte(it.receiveValue["rout"].toString(),1)
            if(data==null||onUpdateBle){
                it.responseValue["result"]=false
                it.finish()
                return@JavaScriptInterFace
            }
            onUpdateBle=true
            BleFw_Update.getInstance().runUpdate(
                object : UpdateControllerListener {
                    override fun onProgressChanged(
                        progress: Int,
                        total: Int,
                        step: String
                    ) {
                        onUpdateBle=false
                        Log.e("updateBle", "progress:$progress")
                    }
                    override fun onCompleted(status: Int) {
                        onUpdateBle=false
                        it.responseValue["result"]=(status!=-1)
                        it.finish()
                    }
                }, data)
        })
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("bleMessage_rx"){
            request ->
            Log.e("JzBleMessage","RX:${request.receiveValue["data"]}")
        })
        //關掉應用
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("closeAPP"){
            Log.e("closeAPP","closeAPP")
            exitProcess(0); })
        //檔案下載
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("FileDonload"){
            request ->
            Log.e("FileDonload","FileDonload${request.receiveValue["serverRout"].toString()}?zip=true")
            try {
                val conn = URL(request.receiveValue["serverRout"].toString()+"?zip=true").openConnection() as HttpURLConnection
                conn.connectTimeout =  (request.receiveValue["timeOut"] as Double).toInt()
                conn.readTimeout=  (request.receiveValue["timeOut"] as Double).toInt()
                Log.e("FileDonload","connectTimeout:${conn.connectTimeout}")
                Log.e("FileDonload","readTimeout:${conn.readTimeout}")
                val `is` = conn.inputStream
                val fileName=request.receiveValue["fileName"].toString()
                val file= File(applicationContext.filesDir, "$fileName.zip")
                Log.e("zipFile",file.name)
                if(!file.exists()){
                    if(fileName.contains("/")){
                        if(!file.parentFile.exists()){
                            file.parentFile.mkdirs();
                        }
                    }
                    file.createNewFile()
                }
                `is`.apply {
                    file.outputStream().use { fileOut ->
                        copyTo(fileOut)
                    }
                }
                `is`.close()
                val buffer = ByteArray(1024)
                val zis = ZipInputStream(FileInputStream(file))
                //獲取壓縮包中的entry，並將其解壓
                var zipEntry = zis.nextEntry
                while (zipEntry != null) {
                    val newFile = File(GlitterActivity.instance().applicationContext.filesDir, fileName)
                    Log.e("newFile",newFile.name)
                    val fos = FileOutputStream(newFile)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                    Log.e("newFileSize","${newFile.name}->"+newFile.readBytes().size)
                    //解壓完成一個entry，再解壓下一個
                    zipEntry = zis.nextEntry
                }
                zis.closeEntry()
                zis.close()
                request.responseValue["result"]=true
                request.finish()
                Log.e("don->","success")
            }catch (e:Exception){
                e.printStackTrace()
                Log.e("FileDonload","FileDonload->更新失敗${e.message}")
                request.responseValue["result"]=false
                request.finish()
            }
            Log.e("FileDonload","FileDonload")
        })
        //檔案解壓縮
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("Unzip"){
            request->
            //解壓的目標目錄
            //解壓的目標目錄
            val fileName=request.receiveValue["fileName"].toString()
            val file= File(GlitterActivity.instance().applicationContext.filesDir, fileName)
            val destDir = File(GlitterActivity.instance().applicationContext.filesDir, "unzip")
            destDir.mkdirs()
            val buffer = ByteArray(1024)
            val zis = ZipInputStream(FileInputStream(file))
            //獲取壓縮包中的entry，並將其解壓
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                val newFile: File = newFile(destDir, zipEntry)
                val fos = FileOutputStream(newFile)
                if(newFile.name==file.name){
                    Log.e("startZIP",newFile.name)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }
                //解壓完成一個entry，再解壓下一個
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()
        })
        //權限請求
        GlitterActivity.setImageCallBack = { fileChooserParams ->
            Log.e("fileChooserParams", "fileChooserParams")
            Log.e("fileChooserParams", ""+fileChooserParams!!.acceptTypes[0])
            handler.post {
                val act = GlitterActivity.instance()
                var success = 0
                act.getPermission(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    object : GlitterActivity.permission_C {
                        override fun requestSuccess(a: String) {
                            Log.e("requestSuccess","-requestSuccess-${a}")
                            success += 1
                            if (success == 2) {
                                when {
                                    fileChooserParams.acceptTypes[0].contains("image") -> {
                                        Matisse.from(GlitterActivity.instance())
                                            .choose(MimeType.ofImage())//图片类型
                                            .countable(false)//true:选中后显示数字;false:选中后显示对号
                                            .maxSelectable(49)
                                            .showSingleMediaType(true)
                                            .capture(true)//选择照片时，是否显示拍照
                                            .imageEngine(Util_Glide_Egine())
                                            .captureStrategy(
                                                CaptureStrategy(true, "PhotoPicker")
                                            )//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                            .forResult(123)
                                    }
                                    fileChooserParams.acceptTypes[0].contains("video") -> {
                                        Matisse.from(GlitterActivity.instance())
                                            .choose(MimeType.ofVideo())//图片类型
                                            .countable(false)//true:选中后显示数字;false:选中后显示对号
                                            .maxSelectable(1)
                                            .showSingleMediaType(true)
                                            .capture(true)//选择照片时，是否显示拍照
                                            .imageEngine(Util_Glide_Egine())
                                            .captureStrategy(
                                                CaptureStrategy(true, "PhotoPicker")
                                            )//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                            .forResult(123)
                                    }
                                    else -> {
                                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                                        intent.type = "file/*"
                                        GlitterActivity.instance().startActivityForResult(intent, 22)
                                    }
                                }
                            }
                        }

                        override fun requestFalse(a: String) {
                            Log.e("requestFalse","-requestFalse-${a}")
                            if (GlitterActivity.instance().uploadMessageAboveL != null) {
                                GlitterActivity.instance().uploadMessageAboveL!!.onReceiveValue(null)
                                GlitterActivity.instance().uploadMessageAboveL = null
                            }
                        }
                    })
            }
            Thread.sleep(1000)
            GlitterActivity.instance().permissionCaller.requestFalse("false_____")
        }
        GlitterActivity.addActivityResult(object : GlitterActivity.ResultCallBack {
            override fun resultBack(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
                    val result = Matisse.obtainResult(data)
                    val results:ArrayList<Uri> = arrayListOf()
                    result.toTypedArray().map {
                        val image=getBitmapFormUri(applicationContext,it)
                        val tok = (System.currentTimeMillis() + Random().nextInt(999999999)).toString() + ""
                        val file=File(applicationContext.filesDir.path + "/" + tok + ".jpg")
                        val stream = ByteArrayOutputStream()
                        image!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray: ByteArray = stream.toByteArray()
                        image.recycle()
                        file.writeBytes(byteArray)
                        results.add(file.toUri())
                    }
                    GlitterActivity.instance().uploadMessageAboveL!!.onReceiveValue(results.toTypedArray())
                    GlitterActivity.instance().uploadMessageAboveL = null
                } else {
                    if (GlitterActivity.instance().uploadMessage != null) {
                        GlitterActivity.instance().uploadMessage!!.onReceiveValue(null)
                        GlitterActivity.instance().uploadMessage = null
                    } else if (GlitterActivity.instance().uploadMessageAboveL != null) {
                        GlitterActivity.instance().uploadMessageAboveL!!.onReceiveValue(null)
                        GlitterActivity.instance().uploadMessageAboveL = null
                    }
                }
            }
        })
        //取得裝置類型
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("getDeviceType"){
            request ->
            //Get data from javascript
            val message=request.receiveValue["message"]
            //Will response i am glitter
            Log.e("message",message.toString())
            //Response value to javascript
            request.responseValue["data"]="Android"
            //Execute Callback
            request.finish()
        })
        //GPS定位
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("GpsManager_getGps"){
            request ->
            GpsUtil.instance!!.haveLocation {
                if(it=="grant"){
                    val map:MutableMap<String,Any?> = mutableMapOf()
                    val gpsutil=GpsUtil.instance
                    map["latitude"] =  gpsutil!!.lastKnownLocation?.latitude
                    map["longitude"] =  gpsutil.lastKnownLocation?.longitude
                    map["address"] =  gpsutil.address
                    request.responseValue["data"]=map
                    request.responseValue["result"]=true
                }else{
                    request.responseValue["result"]=false
                }
            }
        })
        //BLE LOG
        GlitterActivity.addJavacScriptInterFace(JavaScriptInterFace("JzBleMessage"){
            request ->
            Log.e("JzBleMessage",request.receiveValue["text"].toString())
            request.finish()
        })
        //打開Qrcode
        JavaScriptInterFace("openQrScanner"){
            Log.e("openQrScanner","openQrScanner")
            val handler=Handler(Looper.getMainLooper())
            handler.post {
                val intent = Intent(GlitterActivity.instance(), ScannerActivity::class.java)
                ScannerActivity.request =it
                GlitterActivity.instance().startActivity(intent)
            }
        }
        //取得系統版本資訊
        JavaScriptInterFace("getSystemVersion") {
            request->
            request.responseValue["version"]=getSystemVersion()
            request.responseValue["model"]=getSystemModel()
            request.responseValue["make"]=getSystemMake()
            request.finish()
        }
        //跳轉至應用商店
        JavaScriptInterFace("toAppStore"){
            handler.post {
                var marketPkg=applicationContext.packageName
                val uri = Uri.parse("market://details?id=${marketPkg}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.android.vending")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                GlitterActivity.instance().startActivity(intent)
            }
        }
    }
    fun GetByte(url: String, timeout: Int): ByteArray? {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout * 1000
            conn.readTimeout = timeout * 1000
            conn.requestMethod = "GET"
            val fos = ByteArrayOutputStream()
            val `is` = conn.inputStream
            val bufferSize = 8192
            val buf = ByteArray(bufferSize)
            while (true) {
                val read = `is`.read(buf)
                if (read == -1) {
                    break
                }
                fos.write(buf, 0, read)
            }
            `is`.close()
            return fos.toByteArray()
        } catch (e: Exception) {
            Log.e("錯誤", "error" + e.message)
            e.printStackTrace()
            return null }
    }
//在解壓目標資料夾，新建一個檔案
@Throws(IOException::class)
fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
    val destFile = File(destinationDir, zipEntry.name)
    val destDirPath = destinationDir.canonicalPath
    val destFilePath = destFile.canonicalPath
    if (!destFilePath.startsWith(destDirPath + File.separator)) { throw IOException("該解壓項在目標資料夾之外: " + zipEntry.name) }
    return destFile
}

    /**
     * 獲取當前手機系統版本號
     *
     * @return  系統版本號
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 獲取手機型號
     *
     * @return  手機型號
     */
    fun getSystemModel(): String {
        return Build.MODEL
    }
    /**
     * 獲取手機品牌
     *
     * @return  手機型號
     */
    fun getSystemMake(): String {
        return Build.BRAND;
    }
}


