package com.orango.electronic.orange_og_lib.Command

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.jztaskhandler.JzClock
import com.example.jztaskhandler.callback
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.Rx
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.openTpms
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import java.lang.Exception
import java.util.*

object Cmd_Rfid {
    var handler = Handler(Looper.getMainLooper())
    fun sendData(data2: String) {
        Rx = ""
        val data = Cmd_Og.StringHexToByte(data2.replace(" ", ""))
        var checksum=Integer.toHexString(data.copyOfRange(1,data.size-2).sum())
        if(checksum.length==1){checksum="0$checksum"}
        checksum=checksum.substring(checksum.length-2,checksum.length)
        data[data.size-2]=checksum.hexToByte()[0]
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x55, 0x54, 0x54, 0x34))
        HardwareApp.send(byteArrayOf(data.size.toByte()))
        HardwareApp.send(data)
    }
    fun startRfid() {
        if(OgPublic.fwType==OgPublic.FwType.Rfid){return}
        //Open_5v
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x35, 0x56, 0x4F, 0x4E))
        //Close_9v
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x39, 0x56, 0x4F, 0x46));
        //Close_Gpio1
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x31, 0x4F, 0x4F, 0x46));
        //Open_Gpio2
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x32, 0x4F, 0x4F, 0x4E))
        OgPublic.fwType=OgPublic.FwType.Rfid
    }
    //讀取RFID
    fun readRfid(callback: (a: String?) -> Unit) {
        try {
            Thread {
                val command = "BB 00 27 00 03 22 FF FF 4A 7E"
                Thread.sleep(100)
                sendData(command)
                val clock = JzClock()
                while (true) {
                    Rx = Rx.replace("bb01ff000115167e".toUpperCase(), "")
                    if (Rx.length >= 48) {
                        val data = Rx.substring(16, 40)
                        sendData("BB00280000287E")
                        handler.post {
                            Log.e("DATA::","Rfid:${data}")
                            callback(data)
                        }
                        break
                    }
                    if (clock.stop() > 3) {
                        handler.post {
                            callback(null)
                        }
                        break
                    }
                    Thread.sleep(20)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }

    }
    //讀取User Area
    fun readUserArea(rfid:String,callback: (a: String?) -> Unit) {
        try {
            Thread {
                val command = "BB 00 0C 00 13 05 00 00 00 20 60 00 $rfid 9D 7E"
                Thread.sleep(100)
                sendData(command)
                val clock = JzClock()
                while (true) {
                    if(Rx.toUpperCase()=="BB010C0001000E7E"){
                        Rx = ""
                        sendData("BB 00 39 00 09 00 00 00 00 03 00 00 00 08 4D 7E")
                    }
                    if (Rx.length >= 76) {
                        val data = Rx.substring(40, 72)
                        handler.post {
                            callback(data)
                        }
                        break
                    }
                    if (clock.stop() > 3 ) {
                        handler.post {
                            callback(null)
                        }
                        break
                    }
                    if((Rx.toLowerCase().contains("bb01ff"))){
                        Rx = ""
                        sendData("BB 00 39 00 09 00 00 00 00 03 00 00 00 08 4D 7E")
                    }
                    Thread.sleep(20)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }
    //寫入RFID
    fun writeRfid(writeId:String,data: String, callback: (a: Boolean) -> Unit) {
        try {
            val data=data.replace(" ","").toUpperCase()
            existRfid(writeId){
                if(it){
                    getCrc{
                        crc->
                        if(crc==null){
                            callback(false)
                        }else{
                            Thread {
                                val command = "BB 00 49 00 19 00 00 00 00 01 00 00 00 08 $crc 30 00 $data 31 7E "
                                Thread.sleep(100)
                                sendData(command)
                                val clock = JzClock()
                                while (true) {
                                    if (Rx.length >= 46) {
                                        handler.post {
                                            Log.e("writeResult",Rx.substring(0,4))
                                            callback(Rx.substring(0,4)=="BB01")
                                        }
                                        break
                                    }
                                    if (clock.stop() > 3) {
                                        handler.post {
                                            callback(false)
                                        }
                                        break
                                    }
                                    Thread.sleep(20)
                                }
                            }.start()
                        }
                    }
                }else{
                    callback(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }
    //寫入UserArea
    fun writeUserArea(writeId:String,data: String, callback: (a: Boolean) -> Unit) {
        try {
            var data=data
            while(data.length<32){
                data="0$data"
            }
            Thread {
                val command = "BB 00 0C 00 13 05 00 00 00 20 60 00 $writeId 9D 7E"
                Thread.sleep(100)
                sendData(command)
                val clock = JzClock()
                while (true) {
                    if(Rx.toUpperCase() == "BB010C0001000E7E"){
                        Rx=""
                        sendData("BB 00 49 00 19 00 00 00 00 03 00 00 00 08 $data B3 7E")
                    }
                    if (Rx.length >= 46) {
                        handler.post {
                            callback(true)
                        }
                        break
                    }
                    if (clock.stop() > 3) {
                        handler.post {
                            callback(false)
                        }
                        break
                    }
                    Thread.sleep(20)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }
    //判斷RFID是否存在
    fun existRfid(data: String, callback: (a: Boolean) -> Unit) {
        try {
            Thread {
                val command = "BB 00 0C 00 13 01 00 00 00 20 60 00 $data 03 7E"
                Thread.sleep(100)
                sendData(command)
                val clock = JzClock()
                while (true) {
                    if (Rx.length >= 16) {
                        handler.post {
                            callback(Rx=="BB 01 0C 00 01 00 0E 7E".replace(" ",""))
                        }
                        break
                    }
                    if (clock.stop() > 10) {
                        handler.post {
                            callback(false)
                        }
                        break
                    }
                    Thread.sleep(20)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }
    //GetCrc
    fun getCrc(callback: (a: String?) -> Unit){
        try {
            Thread{
                val command="BB 00 39 00 09 00 00 00 00 01 00 00 00 08 4B 7E"
                Thread.sleep(100)
                sendData(command)
                val clock = JzClock()
                while (true) {
                    if (Rx.length >= 76) {
                        handler.post {
                            callback(Rx.substring(40,44))
                        }
                        break
                    }
                    if (clock.stop() > 10) {
                        handler.post {
                            callback(null)
                        }
                        break
                    }
                    Thread.sleep(20)
                }
            }.start()
        }catch (e:Exception){
            e.printStackTrace()
            callback(null)
        }
    }
}