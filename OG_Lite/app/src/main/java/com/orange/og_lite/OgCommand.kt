package com.orange.og_lite

import android.app.Activity
import android.util.Log
import com.example.jztaskhandler.TaskHandler
import com.jianzhi.jzblehelper.BleHelper
import com.jianzhi.jzblehelper.FormatConvert
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.Command.Companion.addcheckbyte
import com.orange.og_lite.Command.Companion.clock
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.JavaUtil.getBit
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.BootloaderState
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SensorData
import com.orange.og_lite.callback.*
import com.orango.electronic.jzutil.hexToByte
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.and
import kotlin.experimental.xor

class OgCommand {
    var NowTag = ""
    var SendTag = ""
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"

    companion object {
        var tx_memory = StringBuffer()
        var waitTx=false
        var canSend=true
    }

    var retry = false
    var cancel: Boolean = false
    fun sendHEX() {
        try {
            var lf = PublicBeans.getLfPower()
            val hex = PublicBeans.getHEX()
            if (lf!!.length < 2) {
                lf = "0${lf}"
            }
            Send("0AED000504${lf}${hex}00F5")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun    Send(a: String) {
        try {

            if(!canSend){return}
            Log.e("嘗試傳送","$a")
            retry=false
            Command.rx = ""
            cancel = false
            waitTx=true
            SendTag = NowTag
            val data = GetCrc(a.toUpperCase())
            var dateStr = ""
            val sdf = SimpleDateFormat("HH:mm:ss:SSS")
            dateStr = sdf.format(Date())
            tx_memory.append("TX:\t").append(dateStr + "\t").append("${FormatConvert.bytesToHex(data)}  ").append("\n")
            if (tx_memory.toString().length > 40000) {
                tx_memory =
                    StringBuffer().append(
                        tx_memory.toString().substring(tx_memory.toString().length - 40000)
                    )
            }
            if (!(JzActivity.getControlInstance()
                    .getRootActivity() as MainActivity).manager.Ble_Helper.isConnect()
            ) {
                Command.handler.post {
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                }
                return
            }
            if(!(JzActivity.getControlInstance()
                    .getRootActivity() as MainActivity).manager.Ble_Helper.writeBytes(
                    data,
                    rxUUID,
                    TXUUID
                )){
                (JzActivity.getControlInstance()
                    .getRootActivity() as MainActivity).manager.Ble_Helper.disconnect()
                Thread.sleep(100)
                (JzActivity.getControlInstance()
                    .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                return
            }
            val clock= TaskHandler.newInstance.clock()
            while (OgCommand.waitTx){
                if(clock.stop()>3){
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.disconnect()
                    Thread.sleep(100)
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.startScan()
                    break
                }
                Thread.sleep(100)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun GetCrcString(a: String): String {
        val command = StringHexToByte(a)
        var xor: Byte = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i]
        }
        command[command.size - 2] = xor.toByte()
        return bytesToHex(command)
    }

    //取得版本號
    fun askVersion(): Boolean {
        try {
            val a = ("0ADC000100FFF5")
            Send((a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 2) {
                    Send((a))
                    clock.Zeroing()
                    fal += 1
                }
                if (fal == 3) {
                    return false
                }
                if (Command.rx.length == 54) {
                    JzActivity.getControlInstance().setPro("Version", Command.rx.substring(8, 50))
                    MainActivity.initNcuVersion = true
                    Log.d("BLECommand.rx", "版本號:" + Command.rx.substring(8, 50))
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            return false
        }
    }

    //取得BLE版本號
    fun askBleVersion(): Boolean {
        try {
//            Command.goState(BootloaderState.Bootloader)
            if (Util_FileDowload.oldVersion) {
                return true
            }
            val a = ("r")
            (JzActivity.getControlInstance()
                .getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(a, rxUUID, TXUUID)
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 2) {
                    (JzActivity.getControlInstance()
                        .getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(
                        a,
                        rxUUID,
                        TXUUID
                    )
                    clock.Zeroing()
                    fal += 1
                }
                if (fal == 3) {
                    return false
                }

                if (Command.rx.length >= 10) {
                    JzActivity.getControlInstance()
                        .setPro("BleVersion", Command.rx.substring(8, 56))
                    MainActivity.initNcuVersion = true
                    PublicBeans.bleVersion = Command.rx.substring(6, 56)
                    Log.d(
                        "BLECommand.rx",
                        "藍芽版本號:" + String(Command.rx.substring(8, 56).hexToByte())
                    )
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            return false
        }
    }

    //取得斷線後ID
    fun askID(): ArrayList<String> {
        val allid = ArrayList<String>()
        try {
            Command.goState(BootloaderState.Og_App)
            val a = ("0ADE000300D7F5")
            Send((a))
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return allid
                }
                if (Command.rx.length >= 20) {
                    val id = Command.rx.substring(10, Command.rx.length - 4)
                    for (i in 0 until id.length / 8) {
                        allid.add(id.substring(0 * i, 0 * i + 8))
                    }
                    Log.e("askID", id)
                    return allid
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            return allid
        }
    }

    //寫入版本號
    fun writeVersion(): Boolean {
        try {
            val a = (
                    "0ADA0015DDFFF5".replace(
                        "DD",
                        FormatConvert.bytesToHex(
                            if (Util_FileDowload.oldVersion) "OGLE_OG_HWV100_SWV102".toByteArray() else JzActivity.getControlInstance()
                                .getPro("mcuinit", "").toByteArray()
                        )
                    ))
            Send(a)
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 1) {
                    fal++
                    if (fal == 1) {
                        return false
                    }
                    clock.Zeroing()
                    Send((a))
                }

                if (Command.rx.length == 14) {
                    JzActivity.getControlInstance().setPro(
                        "Version",
                        FormatConvert.bytesToHex(
                            if (Util_FileDowload.oldVersion) "OGLE_OG_HWV100_SWV102".toByteArray() else JzActivity.getControlInstance()
                                .getPro("mcuinit", "").toByteArray()
                        )
                    )
                    Log.d("BLECommand.rx", "寫入版本");
                    return Command.goState(BootloaderState.Og_App);
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //跳轉至燒錄OG
    fun goPrOG(): Boolean {
        try {
            if (!Util_FileDowload.oldVersion) {
                Command.ogCommand.Send("0A0D00030000F5")
                Thread.sleep(200)
            }
            val a = ("0A8D00030000F5")
            clock.Zeroing()
            Send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return true
                }
                if (Command.rx.length >= 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //跳轉至燒錄Bootloader
    fun goPrBoot(): Boolean {
        try {
            if (!Util_FileDowload.oldVersion) {
                Command.goState(BootloaderState.Og_App)
                Thread.sleep(200)
            }
            val a = ("0A8D00030200F5")
            clock.Zeroing()
            Send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return true
                }
                if (Command.rx.length >= 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun goPrBootloader(): Boolean {
        try {
            val a = ("0AED010100FFF5")
            Send((a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 10) {
                    fal++
                    if (fal == 1) {
                        return false
                    }
                    clock.Zeroing()
                    Send((a))
                }
                if (Command.rx.length >= 14) {
                    Log.d("BLECommand.rx", "進入燒錄");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //
    fun GetCrc(a: String): ByteArray {
        val command = StringHexToByte(a)
        var xor: Byte = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i]
        }
        command[command.size - 2] = xor.toByte()
        return command
    }

    fun StringHexToByte(cs: CharSequence): ByteArray {
        val bytes = ByteArray(cs.length / 2)
        for (i in 0 until cs.length / 2)
            bytes[i] = Integer.parseInt(cs.toString().substring(2 * i, 2 * i + 2), 16).toByte()
        return bytes
    }

    fun bytesToHex(hashInBytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }


    fun byte2ToINT(bytes: ByteArray): Int {
        val high = bytes[0].toInt()
        val low = bytes[1].toInt()
        return high shl 8 and 0xFF00 or (low and 0xFF)
    }

    //    public static Read
    fun GetPrId(): ArrayList<SensorData> {
        val array = ArrayList<SensorData>()
        try {
            val replace =
                "0A 10 000E 01 02 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace(
                    "HEX",
                    PublicBeans.getHEX()
                ).replace(" ", "").replace("LF", PublicBeans.getLfPower())
            Send(replace)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 1
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (retry) {
                    fal += 1
                    if (fal == 3) {
                        return array
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(replace)
                }
                if (Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A") || time > 20) {
                    fal += 1
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "5")
                        return array
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(replace)
                    JzActivity.getControlInstance().toast("第幾次$fal")
                }

                if (cancel) {
                    return array
                }
                if (Command.rx.length >= 36) {
                    val data = SensorData()
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.id = (Command.rx.substring(8, 16))
                    data.idcount = (idcount)
                    data.bat =
                        (getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa = byte2ToINT(StringHexToByte(Command.rx.substring(22, 26))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c = (bytes[1] - bytes[0])
                    data.vol = (22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.success = (true)
                    data.有無胎溫 =
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            0,
                            1
                        ) == "1"
                    data.有無電壓 =
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            1,
                            2
                        ) == "1"
                    data.有無電池 =
                        getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                            2,
                            3
                        ) == "1"
                    array.add(data)
                    if (array.size == PublicBeans.programNumber) {
                        return array
                    } else {
                        if (Command.rx.length > 36) {
                            Command.rx = Command.rx.substring(36)
                        } else {
                            Command.rx = ""
                        }
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return array
        }
    }

    //    public static Read
    fun GetPr(Lf: String, count: Int, hex: String): ArrayList<SensorData> {
        val response = ArrayList<SensorData>()
        try {
            var co = Integer.toHexString(count)
            while (co.length < 2) {
                co = "0$co"
            }
            Send(
                "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 00 00 39 F5".replace(
                    " ",
                    ""
                ).replace("LF", Lf).replace("count", co).replace("hex", hex)
            )
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (retry) {
                    fal += 1
                    if (fal == 2 ) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "1")
                        return response
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(
                        "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 00 00 39 F5".replace(
                            " ",
                            ""
                        ).replace("LF", Lf).replace("count", co).replace("hex", hex)
                    )
                }
                if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                        "F51C000302000A"
                    )
                ) {
                    fal += 1
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "1")
                        return response
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(
                        "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 00 00 39 F5".replace(
                            " ",
                            ""
                        ).replace("LF", Lf).replace("count", co).replace("hex", hex)
                    )
                }

                if (cancel) {
                    return response
                }
                if (Command.rx.length >= 36) {
                    val data = SensorData()
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.idcount = (idcount)
                    data.id = Command.rx.substring(8, 16);
                    data.bat =
                        (getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa = (byte2ToINT(StringHexToByte(Command.rx.substring(22, 26)))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c = (bytes[1] - bytes[0])
                    data.vol = (22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.有無胎溫 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                0,
                                1
                            ) == "1"
                            )
                    data.有無電壓 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                1,
                                2
                            ) == "1"
                            )
                    data.有無電池 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                2,
                                3
                            ) == "1"
                            )
                    data.success = (true)
                    response.add(data)
                    Command.rx = Command.rx.substring(36)
                    if (response.size == count) {
                        return response
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return response
        }
    }

    fun GetId(): SensorData {
        val data = SensorData()
        try {
            var sendData="0A 10 000E 01 00 LF HEX 00 00 00 00 00 00 00 00 39 F5".replace(
                "HEX",
                PublicBeans.getHEX()
            ).replace(" ", "").replace("LF", PublicBeans.getLfPower())
            Send(sendData)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (retry) {
                    fal += 1
                    if (fal == 3) {
                        data.success = (false)
                        return data
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(sendData)
                }
                if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                        "F51C000302000A"
                    ) || Command.rx == "F51C000301EB0A"
                ) {
                    fal += 1
                    if (fal == 3) {
                        data.success = (false)
                        if (time > 15) {
                            ReOpen()
                            MysqDatabase.insertTrigger(tx_memory.toString(), "noResponse", "NA")
                        } else {
                            MysqDatabase.insertTrigger(tx_memory.toString(), "timeout", "NA")
                        }
                        return data
                    } else {
                        past = sdf.parse(sdf.format(Date()))
                        Send(sendData)
                    }
                }

                if (cancel) {
                    data.success = (false)
                    return data
                }
                if (Command.rx.length >= 36) {
                    val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                    data.idcount = (idcount)
                    data.id = (Command.rx.substring(16 - idcount, 16))
                    //                    data.id=Command.rx.substring(8, 16);
                    data.bat =
                        (getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(3, 4))
                    data.kpa = (byte2ToINT(StringHexToByte(Command.rx.substring(22, 26)))).toFloat()
                    val bytes = StringHexToByte(Command.rx.substring(18, 22))
                    data.c = (bytes[1] - bytes[0])
                    data.vol = (22 + (StringHexToByte(Command.rx.substring(26, 28))[0] and 0x0F))
                    data.有無胎溫 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                0,
                                1
                            ) == "1"
                            )
                    data.有無電壓 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                1,
                                2
                            ) == "1"
                            )
                    data.有無電池 = (
                            getBit(StringHexToByte(Command.rx.substring(28, 30))[0]).substring(
                                2,
                                3
                            ) == "1"
                            )
                    data.success = (true)
                    MysqDatabase.insertTrigger(
                        tx_memory.toString(),
                        "success",
                        data.id,
                        if (data.有無胎溫) "" + ((bytes[1].toInt() and 0xFF) - (bytes[0].toInt() and 0xFF)) else "NA",
                        "" + byte2ToINT(StringHexToByte(Command.rx.substring(22, 26))).toFloat(),
                        if (data.有無電池) data.bat else "NA"
                    )
                    return data
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            data.success = (false)
            return data
        }
    }

    lateinit var P_Callback: Program_C
    fun Program(
        count: String,
        caller: Program_C,
        sensor: ArrayList<String>
    ) {
        try {
            PublicBeans.lastFunction = ObdBeans()
            for (i in sensor) {
                PublicBeans.lastFunction.OldSemsor.add(i)
                PublicBeans.lastFunction.NewSensor.add(i)
            }
            P_Callback = caller
            if (SendTrigerInfo(sensor) && ProgramFirst(
                    PublicBeans.getLfPower(),
                    PublicBeans.getHEX(),
                    count,
                    PublicBeans.getS19File().toString()
                )
            ) {
                caller.Program_Finish(ProgramCheck(spilt))
            } else {
                caller.Program_Finish(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Program_Finish(false)
        }

    }

    fun SendTrigerInfo(sensor: ArrayList<String>): Boolean {
        try {
            for (i in sensor.indices) {
                var position = Integer.toHexString(i + 1)
                while (position.length < 2) {
                    position = "0$position"
                }
                var id = sensor[i]
                while (id.length < 8) {
                    id = "0$id"
                }
                val sendData= "0A 15 00 0E position ID 00 00 00 00 00 00 00 18 F5".replace(
                    "position",
                    position
                ).replace("ID", id).replace(" ", "")
                Send(sendData)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                var past = sdf.parse(sdf.format(Date()))
                var fal=0
                do {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now, past)
                    if(retry){
                        fal+=1
                        if(fal==3){
                            return false
                        }
                        past = sdf.parse(sdf.format(Date()))
                        Send(sendData)
                    }
                    if (time > 20 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                            "F51C000302000A"
                        )) {
                        if(Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                                "F51C000302000A"
                            )){
                            return false
                        }
                        fal+=1
                        if(fal==3){
                            return false
                        }
                        past = sdf.parse(sdf.format(Date()))
                        Send(sendData)
                    }
                    if(cancel){
                        return false
                    }

                } while (Command.rx.length != 36)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    var spilt: String = ""
    internal var ScanCount = 0
    fun ProgramFirst(Lf: String, Hex: String, count: String, data: String): Boolean {
        var Hex = Hex
        var count = count
        try {
            0
            while (count.length < 2) {
                count = "0$count"
            }
            while (Hex.length < 2) {
                Hex = "0$Hex"
            }
            val B8 = data.substring(14, 16)
            val B9 = data.substring(16, 18)
            val B12 = data.substring(22, 24)
            val B13 = data.substring(24, 26)
            val Data =
                "0A 10 00 0E  02 CT  Lf Hex 8b 9b 12b 13b 00 00 00 00 ff f5".replace("CT", count)
                    .replace("Lf", Lf).replace("Hex", Hex)
                    .replace("8b", B8).replace("9b", B9).replace("12b", B12).replace("13b", B13)
                    .replace(" ", "")
            Send(Data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if(retry){
                    fal += 1
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "Disconnect")
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(Data)
                }
                if (Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString("F51C000302000A")||time > 20) {
                    fal += 1
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "2")
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(Data)
                    JzActivity.getControlInstance().toast("第幾次$fal")
                }
                if ( cancel) {
                    return false
                }
                if (Command.rx.length >= 36) {
                    ScanCount = Integer.parseInt(Command.rx.substring(9, 10))
                    spilt = if (Command.rx.substring(10, 12) == "04") data.substring(
                        0,
                        2048 * 2
                    ) else data.substring(0, 6144 * 2)
                    return WriteFlash(spilt)
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun ReOpen() {
        try {
            Log.e("DATA:", "逾時")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun WriteFlash(data: String): Boolean {
        try {
            val count = if (data.length % 400 == 0) data.length / 400 else data.length / 400 + 1
            for (i in 0 until count) {
                if (i == count - 1) {
                    P_Callback.Program_Progress(100)
                    if (!CheckData(data.substring(400 * i), Integer.toHexString(i + 1))) {
                        return false
                    }
                } else {
                    P_Callback.Program_Progress(i * 100 / count)
                    if (!CheckData(
                            data.substring(400 * i, 400 * i + 400),
                            Integer.toHexString(i + 1)
                        )
                    ) {
                        return false
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun CheckData(data: String, place: String): Boolean {
        var place = place
        try {
            while (place.length < 2) {
                place = "0$place"
            }
            val Long =
                if (data.length == 400) "00CB" else "00" + Integer.toHexString(data.length / 2 + 3)
            val command = "0A 13 LONG DATA PLACE FF F5".replace(" ", "").replace("LONG", Long)
                .replace("DATA", data).replace("PLACE", place)
            Send(command)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if(retry){
                    fal += 1
                    if(fal==3){
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(command)
                }
                if (time > 6 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                        "F51C000302000A"
                    )
                ) {
                    fal += 1
                    if (fal == 3) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "3")
                        ReOpen()
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(command)
                }
                if(cancel){
                    return false
                }
                if (Command.rx.length >= 36) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun ProgramCheck(data: String): Boolean {
        try {
            val cheeckData="0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", "")
            Send(cheeckData)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var fal = 0
            var past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if(retry){
                    fal+=1
                    if(fal==3){
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(cheeckData)
                }
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                        "F51C000302000A"
                    )) {
                    fal+=1
                    if(fal==3){
                        MysqDatabase.InsertMemory(tx_memory.toString(), "4")
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(cheeckData)
                }
                if(cancel){
                    return false
                }
                if (Command.rx.length >= 36 && Command.rx.contains("F513000E00")) {
                    val check = Command.rx.substring(12, 20)
                    if (check == "7FFFFFFF" || check == "000007FF") {
                        Log.e("燒錄", "燒錄成功")
                        Thread.sleep(5000)
                        return true
                    } else {
                        fal += 1
                        if (fal == 4) {
                            return false
                        }
                        if (!RePr(getBit(check).substring(1), data)) {
                            return false
                        }
                        past = sdf.parse(sdf.format(Date()))
                    }
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun RePr(b: String?, data: String): Boolean {
        var b = b
        b = reverseBySort(b)
        Log.d("DATA::", "失敗" + b!!)
        val count = if (data.length % 400 == 0) data.length / 400 else data.length / 400 + 1
        for (i in 0 until count) {
            P_Callback.Program_Progress(i * 100 / count)
            if (b[i].toString() != "1") {
                if (i == count - 1) {
                    if (!CheckData(data.substring(400 * i), Integer.toHexString(i + 1))) {
                        return false
                    }
                } else {
                    if (!CheckData(
                            data.substring(400 * i, 400 * i + 400),
                            Integer.toHexString(i + 1)
                        )
                    ) {
                        return false
                    }
                }
            }
        }
        Send("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", ""))
        return true
    }



    //跳轉Bootloader
    fun goBootloader(): Boolean {
        try {
            val a = ("0ADD010100FFF5")
            Send((a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 10) {
                    fal++
                    if (fal == 1) {
                        return false
                    }
                    clock.Zeroing()
                    Send((a))
                }
                if (Command.rx.contains("F5DD010100DD0AF501000300F70A") || Command.rx.contains("F5DD010100280AF501000300F70A")) {
                    Log.d("BLECommand.rx", "進入燒錄");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun 叫一下() {
        try {
            Thread.sleep(200)
            Send("0AE200030000F5")
            Thread.sleep(200)
            Send("0AE000030000F5")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    //0A02002B003A453241323D313E3631343C303B3F3B303F3B42373637343B303F3B3F3032413D3E38333E34464428F5
    fun WriteBootloader(Ind: Int, caller: Update_C) {
        try {
            askVersion()
            Thread.sleep(500)
            if (if (Util_FileDowload.oldVersion) "OGLE_OG_HWV100_SWV102.srec".contains(
                    String(
                        PublicBeans.MCU_NUMBER.hexToByte()
                    )
                ) else Util_FileDowload.onlineDate.mcuVerion.contains(
                    String(
                        PublicBeans.MCU_NUMBER.hexToByte()
                    )
                )
            ) {
                caller.Finish(Command.goState(BootloaderState.Og_App))
                return
            } else {
                if (goPrOG() && goBootloader()) {
                    if (Util_FileDowload.oldVersion) {
                        val sb = PublicBeans.getFile("mcu")!!
                        var Long = 0
                        if (sb.length % Ind == 0) {
                            Long = sb.length / Ind
                        } else {
                            Long = sb.length / Ind + 1
                        }
                        Log.d("總行數", "" + Long)
                        for (i in 0 until Long) {
                            if (i == Long - 1) {
                                Log.d("", "" + i)
                                val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                                val length = Ind + 2
                                check(Convvvert2(data, Integer.toHexString(length)))
                                caller.Updateing(100)
                                Thread.sleep(1000)
                                caller.Finish(writeVersion())
                                return
                            } else {
                                val data =
                                    bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                                Log.d("行數", "" + i)
                                val length = Ind + 2
                                caller.Updateing(i * 100 / Long)
                                if (!check(Convvvert2(data, Integer.toHexString(length)))) {
                                    caller.Finish(false)
                                    return
                                }
                            }
                        }
                    } else {
                        val sb = PublicBeans.getFile("mcu")!!
                        var Long = 0
                        if (sb.length % Ind == 0) {
                            Long = sb.length / Ind
                        } else {
                            Long = sb.length / Ind + 1
                        }
                        Log.d("總行數", "" + Long)
                        for (i in 0 until Long) {
                            var b = i
                            if (b >= 255) {
                                b -= 255
                            }
                            val result = StringBuffer(Integer.toHexString(b))
                            while (result.length < 2) {
                                result.insert(0, "0")
                            }
                            val cont = result.toString().toUpperCase()
                            if (i == Long - 1) {
                                Log.d("", "" + i)
                                val data =
                                    bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                                val length = sb.substring(i * Ind, sb.length).length + 3
                                check(Convvvert(data, Integer.toHexString(length), cont))
                                caller.Updateing(100)
                                Thread.sleep(1000)
                                caller.Finish(writeVersion())
                                return
                            } else {
                                val data =
                                    bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                                Log.d("行數", "" + i)
                                val length = sb.substring(i * Ind, i * Ind + Ind).length + 3
                                caller.Updateing(i * 100 / Long)
                                if (!check(Convvvert(data, Integer.toHexString(length), cont))) {
                                    caller.Finish(false)
                                    return
                                }
                            }
                        }
                    }
                } else {
                    caller.Finish(false)
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Finish(false)
        }

    }

    fun WriteRootBootloader(Ind: Int, caller: Update_C) {
        try {
            Log.d("Bootlader", "開使更新")
            if (goPrBoot() && goPrBootloader()) {
                val sb = PublicBeans.getFile("bootloader")!!
                var Long = 0
                if (sb.length % Ind == 0) {
                    Long = sb.length / Ind
                } else {
                    Long = sb.length / Ind + 1
                }
                Log.d("總行數", "" + Long)
                for (i in 0 until Long) {
                    var b = i
                    if (b >= 255) {
                        b -= 255
                    }
                    val result = StringBuffer(Integer.toHexString(b))
                    while (result.length < 2) {
                        result.insert(0, "0")
                    }
                    val cont = result.toString().toUpperCase()
                    if (i == Long - 1) {
                        Log.d("", "" + i)
                        val data =
                            bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                        val length = sb.substring(i * Ind, sb.length).length + 3
                        check(Convvvert(data, Integer.toHexString(length), cont))
                        caller.Updateing(100)
                        Thread.sleep(1000)
                        caller.Finish(true)
                        return
                    } else {
                        val data =
                            bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                        Log.d("行數", "" + i)
                        val length = sb.substring(i * Ind, i * Ind + Ind).length + 3
                        caller.Updateing(i * 100 / Long)
                        if (!check(Convvvert(data, Integer.toHexString(length), cont))) {
                            caller.Finish(false)
                            return
                        }
                    }
                }
            } else {
                caller.Finish(false)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Finish(false)
        }
    }

    fun Convvvert2(data: String, length: String): String {
        var length = length
        var command = "0A02LX00F5"
        while (length.length < 4) {
            length = "0$length"
        }
        command = command.replace("L", length).replace("X", data)
        return command
    }

    fun Convvvert(data: String, length: String, line: String): String {
        var length = length
        var line = line
        var command = "0A02LHX00F5"
        while (length.length < 4) {
            length = "0$length"
        }
        if (line == "F5") {
            line = "00"
        }
        if (line.length > 2) {
            line = "00"
        }
        command = addcheckbyte(command.replace("L", length).replace("X", data).replace("H", line))
        return command
    }

    fun check(data: String): Boolean {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            Send(data)
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if(retry){
                    fal+=1
                    if(fal==3){
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(data)
                }
                if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                        "F51C000302000A"
                    )) {
                    fal+=1
                    if(fal==3){
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(data)
                }
                if(cancel){
                    return false
                }
                if (Command.rx.length >= 14 && Command.rx == GetCrcString("F502000300F40A") || Command.rx.contains(
                        "F50B000301F70A"
                    )
                ) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun IdCopy(caller: Copy_C, _long: Int, obd: ObdBeans) {
        var hex = PublicBeans.getHEX()
        try {
            while (hex.length < 2) {
                hex = "0$hex"
            }
            for (i in 0 until obd.wheelPosition.size) {
                var Original_ID = obd.OldSemsor[i]
                while (Original_ID.length < 8) {
                    Original_ID = "0$Original_ID"
                }
                var New_ID = obd.NewSensor[i]
                while (New_ID.length < 8) {
                    New_ID = "0$New_ID"
                }
                val data =
                    "0A 11 00 0E Original_ID Original_Long New_ID New_Long hex 00 ff f5".replace(
                        " ",
                        ""
                    ).replace(
                        "Original_Long",
                        "0$_long"
                    )
                        .replace("New_Long", "0$_long").replace("Original_ID", Original_ID)
                        .replace("New_ID", New_ID).replace("hex", hex)
                Log.e("DATA:", "Prepare:$data")
                Send(data)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val fal = 0
                val past = sdf.parse(sdf.format(Date()))
                while (true) {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now, past)
                    if (time > 15 || Command.rx == GetCrcString("F51C000301000A") || Command.rx == GetCrcString(
                            "F51C000302000A"
                        )
                    ) {
                        if (time > 15) {
                            ReOpen()
                            caller.Copy_Finish(false)
                            return
                        }
                        if (SendTag == NowTag) {
                            obd.state[i] = ObdBeans.PROGRAM_FALSE
                            caller.Copy_Next(false, i)
                        }
                        break
                    }
                    if(cancel){
                        caller.Copy_Finish(false)
                        return
                    }
                    if (Command.rx.length >= 36) {
                        val idcount = Integer.parseInt(Command.rx.substring(17, 18))
                        if (Command.rx.contains(obd.OldSemsor[i].substring(8 - idcount))) {
                            obd.state[i] = ObdBeans.PROGRAM_SUCCESS
                            caller.Copy_Next(true, i)
                        } else {
                            obd.state[i] = ObdBeans.PROGRAM_FALSE
                            caller.Copy_Next(false, i)

                        }
                        break
                    }
                    Thread.sleep(100)
                }
                Thread.sleep(1000)
            }
            if (SendTag == NowTag) {
                caller.Copy_Finish(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Copy_Finish(false)
        }

    }

    fun reverseBySort(str: String?): String? {
        if (str == null || str.length == 1) {
            return null
        }
        val sb = StringBuffer()
        for (i in str.length - 1 downTo 0) {
            sb.append(str[i])//使用StringBuffer從右往左拼接字元
        }
        return sb.toString()
    }

    fun getDatePoor(endDate: Date, nowDate: Date): Double {
        val diff = endDate.time - nowDate.time
        val sec = diff / 1000
        return (if (SendTag == NowTag) sec else 16).toDouble()
    }
}