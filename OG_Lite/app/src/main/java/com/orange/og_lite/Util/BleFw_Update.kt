package com.orange.og_lite.Util

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.jzsql.lib.mmySql.JzSqlHelper
import com.onsemi.ble.UpdateControllerListener
import com.onsemi.protocol.update.FotaController
import com.onsemi.protocol.update.FotaFirmwareFile
import com.onsemi.protocol.update.FotaOptions
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.BleManager
import com.orange.og_lite.Fota.FotaPeripheral
import com.orange.og_lite.Fota.FotaPeripheralManager
import com.orange.og_lite.Fota.FotaPeripheralManagerImpl
import com.orange.og_lite.Fota.FotaPeripheralManagerListener
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.jzutil.toHex
import kotlinx.android.synthetic.main.data_loading.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

//用於更新ble分位
class BleFw_Update(var context: Context) {
    companion  object{
        var instane:BleFw_Update?=null
        fun getInstance():BleFw_Update{
            if(instane==null){
                instane=BleFw_Update(JzActivity.getControlInstance().getRootActivity())
            }
            return instane!!
        }
    }
    var PeripheralManager: FotaPeripheralManager = FotaPeripheralManagerImpl(context)
    var controller: FotaController = FotaController()

    init {
        PeripheralManager.addListener(object : FotaPeripheralManagerListener {
            override fun selectedChanged(peripheral: FotaPeripheral) {}
            override fun selectedChanging(peripheral: FotaPeripheral) {}
            override fun onPeripheralsListUpdated() {
            }

            override fun onBluetoothEnabled() {
                PeripheralManager.startScan()
            }

            override fun onBluetoothDisabled() {
            }
        })
    }

    //////////////////////FOTA更新
    var firmwareFile: FotaFirmwareFile? = null
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
    /**
     * Start the update procedure
     */

     fun runUpdate(updateController: UpdateControllerListener,byteArray: ByteArray) {
        Thread{
            TaskHandler.newInstance.runTaskOnce("ble_data_loading", runner {
                JzActivity.getControlInstance().getHandler().post {  JzActivity.getControlInstance()
                    .showDiaLog(R.layout.data_loading, false, true, "ble_data_loading") }
                val act= ( JzActivity.getControlInstance().getRootActivity() as MainActivity)
                act.canConnect=false
                try {
                    (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.writeHex("0ACCCC",rxUUID,TXUUID)
                }catch (e:java.lang.Exception){e.printStackTrace()}
                PeripheralManager.startScan()
                Thread.sleep(10000)
                var havefota=false
                for (i in PeripheralManager.peripherals()) {
                    Log.e("linstenerFota", "" + i.name)
                    if (i.name == "ON FOTA RSL10") {
//                    Log.e("assetFile1",context.assets.open("OGLite_BB.fota").readBytes().toHex())
//                    Log.e("assetFile2",PublicBeans.getFile("bleinit")!!)
                        PeripheralManager.selected = i
                        firmwareFile = FotaFirmwareFile(byteArray.inputStream())
                        havefota=true
                        break
                    }
                }
                if(havefota){
                    val selected = PeripheralManager.selected
                    try {
                        val options = FotaOptions()
                        options.file = firmwareFile
                        controller.addListener(updateController)
                        selected.update(controller, options)
                        act.canConnect=true
                    } catch (e: Exception) {
                        updateController.onCompleted(-1)
                        com.onsemi.protocol.utility.Log.e("EngineerBle", "update failed" + e.message)
                        act.canConnect=true
                    } finally {
                        controller.removeListener(updateController)
                        act.canConnect=true
                    }
                }else{
                    updateController.onCompleted(-1)
                    act.canConnect=true
                }
            })

        }.start()
    }
    fun showProgress(a:Int){
        JzActivity.getControlInstance().showDiaLog(false,false, object :SetupDialog(R.layout.data_loading){
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                return false
            }

            override fun setup(rootview: Dialog) {
                rootview.pass.visibility = View.VISIBLE
                rootview.pass.text = "UpdateBle...$a%"
            }
        },"data_loading")
    }
}