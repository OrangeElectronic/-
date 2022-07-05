package com.orange.tpms.app

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.core.net.toUri
import com.example.jzpictureselector.Util_Glide_Egine
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.jzcrashhandler.CrashHandle
import com.orange.ble_plugin.Glitter_BLE
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.GlitterInterFace
import com.orange.tpms.R
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.utils.FileUtil.Companion.getBitmapFormUri
import com.orango.electronic.jzutil.JzUtil
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * 进程唯一的Application
 * Created by haide.yin() on 2019/3/26 14:35.
 */
class TPMSApp : Application() {
    var handler = Handler()
    override fun onCreate() {
        super.onCreate()
        // 硬件初始化
        HardwareApp.getInstance()
            .setApplication(this)       //设置Application
            .setEnableHareware(true)   //是否开启硬件功能，可用于关闭调试UI
            .onCreate()
        JzUtil.setUp(applicationContext)
        //最后统一调用初始化硬件
        if (TestMode) {
            CrashHandle.newInstance(this, KtActivity::class.java).setUP(CrashHandle.SHOW_CRASH_MESSAGE)
        } else {
            CrashHandle.newInstance(this, KtActivity::class.java).setUP(CrashHandle.UPLOAD_CRASH_MESSAGE)
        }
        //＊＊*--------------添加Glitter底層暴露方法--------------*＊＊//
        //跨平台開發套件
        Glitter_BLE(applicationContext, arrayOf("Calipers")).create()
        //多國語言取得
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getLan)
        //MMY取得
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getMmy)
        //ReadSensor
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.readSensor)
        //OG叫一下
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.playBeet)
        //隱藏NaviGation
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.hideNavagation)
        //隱藏輸入匡
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.hideKeyBoard)
        //OG Scan讀取Sensor
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.scanSensor)
        //GetPro(Int)
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getInt)
        //多顆讀取
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.readMt)
        //開始燒錄
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.program)
        //取消操作
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.cancel)
        //取的OG帳號
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.ogAccount)
        //掃描
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.scanText)
        //取得Web路徑
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.webRout)
        //取得壓力單位
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getPressureUnit)
        //取得溫度單位
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getTemUnit)
        /***＊＊*＊＊*＊＊*＊＊*＊＊*＊序列化處理框架*＊*＊＊*＊＊*＊＊*＊＊*＊＊****/
        //寫入檔案
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.writeFile)
        //讀取檔案
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.readFile)
        //刪除檔案
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.deleteFile)
        //列出檔案
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.listFile)
        //移除檔案路徑
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.deleteRout)
        //列出此路徑所有路徑名稱和路經
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.listFileRout)
        //ID COPY
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.idCopy)
        //Get MmyInterFace
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getMmyInterFace)
        //getHsn
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.getHsn)
        //toast
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.toast)
        //readRFID
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.readRFID)
        //writeRFID
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.writeRFID)
        //openRFID
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.openRFID)
        //readRfSensor
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.readRfSensor)
        //writeRfSensor
        GlitterActivity.addJavacScriptInterFace(GlitterInterFace.writeRfSensor)
        /**＊＊*＊＊*＊＊*＊＊*＊＊＊*＊＊*＊＊*＊＊*＊＊*＊＊*＊＊*＊＊*＊＊**＊＊*/
        GlitterActivity.onCreateCallBack = {
            handler.post {
                GlitterActivity.instance().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                GlitterActivity.instance().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                GlitterActivity.instance().window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
            }
        }
        GlitterActivity.setImageCallBack = { fileChooserParams ->
            Log.e("fileChooserParams", fileChooserParams!!.acceptTypes[0])
            handler.post {
                val act = GlitterActivity.instance()
                var success = 0
                act.getPermission(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    object : GlitterActivity.permission_C {
                        @SuppressLint("MissingPermission")
                        override fun requestSuccess(a: String?) {
                            success += 1
                            if (success == 2) {
                                if (fileChooserParams.acceptTypes[0].contains("image")) {
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
                                } else if (fileChooserParams.acceptTypes[0].contains("video")) {
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
                                } else {
                                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                                    intent.type = "file/*"
                                    GlitterActivity.instance().startActivityForResult(intent, 22)
                                }
                            }
                        }

                        override fun requestFalse(a: String?) {
                            if (GlitterActivity.instance().uploadMessageAboveL != null) {
                                GlitterActivity.instance().uploadMessageAboveL!!.onReceiveValue(null)
                                GlitterActivity.instance().uploadMessageAboveL = null
                            }
                        }
                    })
            }

        }
        GlitterActivity.addActivityResult(object : GlitterActivity.ResultCallBack {
            override fun resultBack(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
                    val result = Matisse.obtainResult(data)
                    val results:ArrayList<Uri> = arrayListOf()
                    result.toTypedArray().map {
                        val image=getBitmapFormUri(applicationContext,it)
                        val tok = (System.currentTimeMillis() + Random().nextInt(999999999)).toString() + ""
                        val file=File(JzActivity.getControlInstance().getRootActivity().applicationContext.filesDir.path + "/" + tok + ".jpg")
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
    }

    companion object {
        var TAG = TPMSApp::class.java.simpleName
        var TestMode = true
    }

}
