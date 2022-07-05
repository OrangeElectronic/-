package com.orange.oglite_glitter

import com.jianzhi.glitter.GlitterActivity
import com.orange.oglite_glitter.Fota.FotaPeripheral
import com.orange.oglite_glitter.Fota.FotaPeripheralManager
import com.orange.oglite_glitter.Fota.FotaPeripheralManagerImpl
import com.orange.oglite_glitter.Fota.FotaPeripheralManagerListener
import com.onsemi.ble.UpdateControllerListener
import com.onsemi.protocol.update.FotaController
import com.onsemi.protocol.update.FotaFirmwareFile
import com.onsemi.protocol.update.FotaOptions


//用於更新ble分位
class BleFw_Update() {
    companion  object{
        var instane: BleFw_Update?=null
        fun getInstance(): BleFw_Update {
            if(instane ==null){
                instane = BleFw_Update()
            }
            return instane!!
        }
    }

    var PeripheralManager: FotaPeripheralManager = FotaPeripheralManagerImpl(GlitterActivity.instance())
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
            PeripheralManager.startScan()
            Thread.sleep(10000)
            var havefota=false
            for (i in PeripheralManager.peripherals()) {
                if (arrayOf("ON FOTA RSL10","CS8025v100").contains(i.name)) {
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
                } catch (e: Exception) {
                    updateController.onCompleted(-1)
                    com.onsemi.protocol.utility.Log.e("EngineerBle", "update failed" + e.message)
                } finally {
                    controller.removeListener(updateController)
                }
            }else{
                updateController.onCompleted(-1)
            }
        }.start()
    }

}