package com.orange.og_lite

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.webkit.PermissionRequest
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.callback
import com.example.jztaskhandler.runner
import com.jianzhi.jzblehelper.BleHelper
import com.jianzhi.jzblehelper.FormatConvert
import com.jianzhi.jzblehelper.callback.BleCallBack
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.jianzhi.jzblehelper.models.BleBinary
import com.onsemi.ble.UpdateControllerListener
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.permission_C
import com.orange.jzchi.jzframework.onActivityResultCallback
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Frag.Frag_Function_Check_Sensor_Read
import com.orange.og_lite.Frag.Frag_Function_OBD_ID_copy
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.Util.BleFw_Update
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.adapter.Ad_ScanBle
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.BootloaderState
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.PublicBeans.Companion.manager
import kotlinx.android.synthetic.main.fragment_frag__function__obd__id_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__show__read.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BleManager(var activity: Activity) : BleCallBack {
    companion object{
        var toggleReadSensor=true
    }
    override fun onDisconnect() {}

    val connectLAST get() = JzActivity.getControlInstance().getPro("lastble","nodata")
        set(value) {
            JzActivity.getControlInstance().setPro("lastble",value)
        }
    var adapter:  Ad_ScanBle
    var devicelist = ArrayList<BluetoothDevice>()
    var handler= Handler()
    var Ble_Helper: BleHelper
    init {
        Ble_Helper = BleHelper( activity,this)
        adapter=Ad_ScanBle(devicelist,this)

    }

    override fun onConnectFalse() {
        OgCommand.canSend=false
        //當ble連線失敗時觸發
        handler.post {

        }

    }
    var commandFinfish=false
    override fun onConnectSuccess() {
        manager.Ble_Helper.stopScan()
        if(Command.onProgram){
            Thread{
                Thread.sleep(2000)
                OgCommand.canSend=true
                Command.ogCommand.retry=true
            }.start()
            return
        }
        JzActivity.getControlInstance().getHandler().post {
            JzActivity.getControlInstance().closeDiaLog("scandia")
        }
        handler.post {  JzActivity.getControlInstance()
            .showDiaLog(R.layout.data_loading, false, true, "versioncheck") }
           Thread{
               val frag=JzActivity.getControlInstance().getNowPage()
               if(frag is Frag_Function_OBD_ID_copy){
                   Thread.sleep(4000)
                   for(i in Command.ogCommand.askID()){
                       handler.post {frag.insert(i)}
                   }
                   JzActivity.getControlInstance().getHandler().post { JzActivity.getControlInstance().closeDiaLog("versioncheck")}
               }else{
                   TaskHandler.newInstance.runTaskOnce("onConnectSuccess",runner {
                       Thread.sleep(2000)
                       OgCommand.canSend=true
                       Thread.sleep(50)
                       val a=Command.ogCommand.askBleVersion()&&Command.getState()&&Command.ogCommand.askVersion()
                       if(!a){
                           JzActivity.getControlInstance().getHandler().post {
                               JzActivity.getControlInstance().closeDiaLog("versioncheck")
                               if(!a){
                                   commandFinfish=false
                                   Ble_Helper.disconnect()
                               }
                           }
                           return@runner
                       }
                       if(PublicBeans.DongleState==BootloaderState.Obd_App){
                           Thread.sleep(50)
                           Command.goState(BootloaderState.Og_App)
                       }
                       Thread.sleep(200)
                       Command.readSN()
                       Log.e("SN",PublicBeans.SN)
                       Thread.sleep(200)
                       Command.setClose(JzActivity.getControlInstance().getPro("time",300))
                       Thread.sleep(200)
                       Command.getBattery()
                       Thread.sleep(200)
                       Command.ueedUpdateBootloader()
                       Thread.sleep(200)
                       JzActivity.getControlInstance().getHandler().post {
                           commandFinfish=true
                           JzActivity.getControlInstance().closeDiaLog("versioncheck")
                           (JzActivity.getControlInstance().getRootActivity() as MainActivity).checkVersion()
                       }
                   })
               }
           }.start()
    }

    override fun onConnecting() {
        //當ble開始連線時觸發  0.02
        TaskHandler.newInstance.runTaskDelay("onConnecting",0.02, runner {
            Log.d("JzBleMessage", "藍芽正在連線中")
        })

    }

    override fun needGPS() {

    }
    var lastRx=""
    override fun rx(a: BleBinary) {
        if(lastRx==a.readHEX()){return}

        if(a.readHEX().contains("F5EE0000FF")&&Command.onProgram){
            Command.ogCommand.cancel=true
            handler.post {  JzActivity.getControlInstance()
                .showDiaLog(R.layout.replace_battery, true, false, "replace_battery")}
            return
        }
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        val dateStr = sdf.format(Date())
        lastRx=a.readHEX()
        OgCommand.tx_memory.append("RX:\t").append("$dateStr"+"\t").append(lastRx).append("\n")
        if(a.readHEX()=="F500000304F20A"||a.readHEX()=="0AF700030000F5"){
            if(!toggleReadSensor){return}
            val frag=JzActivity.getControlInstance().getNowPage()
            handler.post {
                if(frag is Frag_Function_OBD_ID_copy){
                    frag.rootview.sending_data.performClick()
                }else if(frag is Frag_Function_Program){
                    frag.rootview.sending_data2.performClick()
                }else if(frag is Frag_Function_Check_Sensor_Read){
                    frag.rootview.read.performClick()
                }
            }
            Log.d("JzBleMessage", "收到trigger${a.readHEX()}")
        }else{
            Command.rx += a.readHEX()
        }
        Log.d("JzBleMessage", "收到藍牙消息${Command.rx}")
    }

    override fun requestPermission(permission: ArrayList<String>) {
        //當藍牙權限不足時觸發

//        for (i in permission) {
//            Log.e("JzBleMessage", "權限不足請先請求權限${i}")
//        }
        JzActivity.getControlInstance().permissionRequest(permission.toTypedArray(),  object : permission_C{
            override fun requestFalse(a: String?) {
                Log.e("JzBleMessage_False", a)
                JzActivity.getControlInstance().closeDiaLog()
                JzActivity.getControlInstance().toast("使用藍芽需要定位權限")
            }

            override fun requestSuccess(a: String?) {
                Log.e("JzBleMessage_Success", a)

            }
        },10)
    }

    override fun scanBack(device: BluetoothDevice, scanRecord: BleBinary) {
        if(Ble_Helper.isConnect()){return}
        if(Command.onProgram ){
            if(!Ble_Helper.isConnect()){
                if(connectLAST==device.address){
                    Ble_Helper.connect(device.address,5, ConnectResult {
                        if(it){
                            Ble_Helper.stopScan()
                        }
                    })
                }
            }
            return
        }
        //當掃描到新裝置時觸發
        if (!devicelist.contains(device) && device.name!=null && (device.name.contains("OG_LITE") || device.name.contains("OG_Lite")||device.name.contains("OGLite"))) {
            devicelist.add(device)
            adapter.notifyDataSetChanged()
        }
        if(device.name=="ON FOTA RSL10"){
            Log.d("JzBleMessage", "updateFota")
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
                JzActivity.getControlInstance()
                    .getRootActivity().assets.open("Base.fota").readBytes() )
        }else{
            if(connectLAST==device.address){
                TaskHandler.newInstance.runTaskDelay("connect",5.0,
                    runner {
                        Log.d("JzBleMessage", "connect:${device.name}/地址:${device.address}")
                        handler.post {  JzActivity.getControlInstance()
                            .showDiaLog(R.layout.data_loading, false, true, "scandia") }
                        Ble_Helper.connect(device.address,5, ConnectResult {
                            if(it){
                                Ble_Helper.stopScan()
                            }
                        })
                    })
            }
        }
        Log.d("JzBleMessage", "掃描到裝置:名稱${device.name}/地址:${device.address}")
        //當獲取到device.address即可儲存下來，藍牙連線時會使用到
    }

    override fun tx(b: BleBinary) {
        OgCommand.waitTx=false
        lastRx=""
        TaskHandler.newInstance.runTaskDelay("tx",0.02, runner {
            Command.rx=""
            Log.d("JzBleMessage", "傳送藍牙消息${b.readHEX()}")
        })
    }


}
