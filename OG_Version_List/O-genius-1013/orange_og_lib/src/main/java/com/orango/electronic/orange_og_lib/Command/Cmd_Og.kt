package com.orango.electronic.orange_og_lib.Command

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import com.example.jztaskhandler.JzClock
import com.example.jztaskhandler.TaskHandler
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.orange_og_lib.Callback.*
import com.orango.electronic.orange_og_lib.Dialog.single_program
import com.orango.electronic.orange_og_lib.MysqDatabase
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.SensorData
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

object Cmd_Og {

    var Rx = ""
    var NowTag = ""
    var SendTag = ""
    val handler = Handler(Looper.getMainLooper())

    //當前燒錄紀錄
    var tx_memory = StringBuffer()

    //當用戶取消當前動作時觸發
    var cancel: Boolean = false
        set(value) {
            field = value
            if (field) {
                Thread {
                    //通知模組進行中斷指令
                    Send("0A0E00030000F5")
                }.start()
            }
        }
    var sensorData = ArrayList<SensorData>()

    //硬體版本號
    val hardVersion: Boolean
        get() {
            try {
                Send("0A 0C 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 08 F5".replace(" ", ""))
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val past = sdf.parse(sdf.format(Date()))
                while (true) {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now!!, past!!)
                    if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel!!) {
                        if (time > 20) {
                            ReOpen()
                        }
                        return false
                    }
                    if (Rx.length >= 36) {
                        OgPublic.hardWareVersion = Rx.substring(8, 16)
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

    var nextStep = "0"
    lateinit var P_Callback: Program_C
    var sb = StringBuilder()
    lateinit var spilt: String
    internal var ScanCount = 0

    //判斷有無傳送成功的Toggle
    var isSendToggle = false
    fun sendData(dat: String) {
        Rx = ""
        if (!arrayOf("0A0E00030000F5").contains(dat)) {
            cancel = false
        }
        SendTag = NowTag
        val data = GetCrc(dat.toUpperCase())
        var dateStr = ""
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        dateStr = sdf.format(Date())
        tx_memory.append("TX:\t").append(dateStr).append(HardwareApp.bytesToHexString(data))
            .append("\n")
        if (tx_memory.toString().length > 40000) {
            tx_memory =
                StringBuffer().append(tx_memory.toString().substring(tx_memory.toString().length - 40000))
        }
        HardwareApp.send(byteArrayOf(0x1B, 0x23, 0x23, 0x55, 0x54, 0x54, 0x32))
        HardwareApp.send(byteArrayOf(data.size.toByte()))
        HardwareApp.send(data)
    }

    fun Send(a: String) {
        Log.e("trySend", a)
        isSendToggle = false
        val clock = JzClock()
        var fal = 0
        sendData(a)
        while (!isSendToggle && (a.substring(0, 4)
                .toUpperCase() != "0A02") && (a != "0A0000030000F5") && (a != "0A0C000E00000000000000000000000008F5")
        ) {
            if (clock.stop() > 0.2) {
                fal++
                clock.Zeroing()
                if (fal == 3) {
                    break
                } else {
                    sendData(a)
                }
            }
            Thread.sleep(10)
        }
    }

    //開啟TPMS功能
    fun openTpms() {
        if(OgPublic.fwType==OgPublic.FwType.Normal){return}
        HardwareApp.getInstance().open5V(true)
        Thread.sleep(200)
        HardwareApp.getInstance().open9V(true)
        Thread.sleep(200)
        HardwareApp.getInstance().openPB5(true)
        Thread.sleep(200)
        HardwareApp.getInstance().setGpio1V(true)
        Thread.sleep(200)
        OgPublic.fwType=OgPublic.FwType.Normal
    }

    fun GetCrcString(a: String): String {
        val command = StringHexToByte(a)
        var xor = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i].toInt()
        }
        command[command.size - 2] = xor.toByte()
        return bytesToHex(command)
    }

    fun GetCrc(a: String): ByteArray {
        val command = StringHexToByte(a)
        var xor = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i].toInt()
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

    fun getBit(a: String): String {
        val data = StringHexToByte(a)
        val sb = StringBuffer()
        for (i in data.indices) {
            sb.append(data[i].toInt() shr 7 and 0x1)
            sb.append(data[i].toInt() shr 6 and 0x1)
            sb.append(data[i].toInt() shr 5 and 0x1)
            sb.append(data[i].toInt() shr 4 and 0x1)
            sb.append(data[i].toInt() shr 3 and 0x1)
            sb.append(data[i].toInt() shr 2 and 0x1)
            sb.append(data[i].toInt() shr 1 and 0x1)
            sb.append(data[i].toInt() shr 0 and 0x1)
        }

        return sb.toString()
    }

    fun getBit(by: Byte): String {
        val sb = StringBuffer()
        sb.append(by.toInt() shr 7 and 0x1)
        sb.append(by.toInt() shr 6 and 0x1)
        sb.append(by.toInt() shr 5 and 0x1)
        sb.append(by.toInt() shr 4 and 0x1)
        sb.append(by.toInt() shr 3 and 0x1)
        sb.append(by.toInt() shr 2 and 0x1)
        sb.append(by.toInt() shr 1 and 0x1)
        sb.append(by.toInt() shr 0 and 0x1)
        return sb.toString()
    }

    fun byte2ToINT(bytes: ByteArray): Int {
        val high = bytes[0].toInt()
        val low = bytes[1].toInt()
        return high shl 8 and 0xFF00 or (low and 0xFF)
    }

    //多顆read
    fun GetPrId(hex: String, Lf: String): ArrayList<SensorData> {
        val array = ArrayList<SensorData>()
        try {
            val replace = "0A 10 000E 01 02 LF HEX 00 00 00 00 00 00 00 00 39 F5"
                .replace("HEX", hex)
                .replace(" ", "")
                .replace("LF", Lf)
            Send(replace)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 1
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A")) {
                    if (fal == 3) {
                        when (PublicBean.position) {
                            PublicBean.燒錄傳感器 -> {
                                MysqDatabase.InsertMemory(
                                    tx_memory.toString(),
                                    "5-${PublicBean.ProgramNumber}-${array.size}",
                                    sensorData
                                )
                            }
                            PublicBean.複製傳感器, PublicBean.ID_COPY_OBD -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "6_noResponse")
                            }
                        }
                        return array
                    }
                    past = sdf.parse(sdf.format(Date()))
                    Send(replace)
                    fal += 1
                }
                if (time > 20 || cancel) {
                    if (time > 20) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "5_noResponse", sensorData)
                        ReOpen()
                    } else {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "11", sensorData)
                    }
                    return array
                }
                if (Rx.length >= 36) {
                    val data = SensorData()
                    val idcount = Integer.parseInt(Rx.substring(17, 18))
                    data.id = Rx.substring(8, 16)
                    data.idcount = idcount
                    data.bat = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4)
                    data.kpa = byte2ToINT(StringHexToByte(Rx.substring(22, 26))).toFloat()
                    val bytes = StringHexToByte(Rx.substring(18, 22))
                    data.c = bytes[1] - bytes[0]
                    data.vol = (22 + (StringHexToByte(Rx.substring(26, 28))[0] and 0x0F)).toFloat()
                    data.有無胎溫 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(0, 1) == "1"
                    data.有無電壓 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(1, 2) == "1"
                    data.有無電池 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(2, 3) == "1"
                    data.success = true
                    array.add(data)
                    if (array.size == ScanCount) {
                        return array
                    } else {
                        if (Rx.length > 36) {
                            Rx = Rx.substring(36)
                        } else {
                            Rx = ""
                        }
                    }
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return array
        }
    }

    class GetPrInfoMation(var sensorData: ArrayList<SensorData> = ArrayList<SensorData>(), var type: String = "success")

    fun GetPr(Lf: String, count: Int, hex: String, needWait: Boolean = true): GetPrInfoMation {
        val response = GetPrInfoMation()
        try {
            var co = Integer.toHexString(count)
            while (co.length < 2) {
                co = "0$co"
            }
            Send(
                "0A 10 000E 01 00 LF hex 00 00 00 00 count 00 ${if (PublicBean.getSencsorModel() == "SP201") "00" else "01"} 00 39 F5".replace(
                    " ", ""
                ).replace("LF", Lf).replace("count", co).replace("hex", hex)
            )
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel) {
                    if (time > 20) {
                        ReOpen()
                        response.type = "noResponse"
                    } else if (cancel) {
                        response.type = "cancel"
                    } else {
                        when (PublicBean.position) {
                            PublicBean.燒錄傳感器 -> {
                                response.type = "1-${PublicBean.ProgramNumber}-${response.sensorData.size}"
                            }
                            PublicBean.複製傳感器 -> {
                                response.type = "7-1-${response.sensorData.size}"
                            }
                        }
                    }
                    return response
                }
                if (Rx.length >= 36) {
                    when (PublicBean.position) {
                        PublicBean.燒錄傳感器 -> {
                            response.type = "1-${PublicBean.ProgramNumber}-${PublicBean.ProgramNumber}"
                        }
                        PublicBean.複製傳感器 -> {
                            response.type = "7-1-1"
                        }
                    }
                    val clock = TaskHandler.newInstance.clock()
                    val data = SensorData()
                    val condition = Rx.substring(16, 17).toUpperCase()
                    if (condition == "2") {
                        Log.e("2代燒錄", Rx.substring(8, 16))
                        data.senesorType = "S2"
                    } else {
                        Log.e("3代燒錄", Rx.substring(8, 16))
                        data.senesorType = "S3"
                    }
                    val idcount = PublicBean.getIdcount()
                    data.idcount = idcount
                    data.id = Rx.substring(8, 16)
                    data.bat = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4)
                    data.kpa = byte2ToINT(StringHexToByte(Rx.substring(22, 26))).toFloat()
                    val bytes = StringHexToByte(Rx.substring(18, 22))
                    data.c = bytes[1] - bytes[0]
                    data.vol = (22 + (StringHexToByte(Rx.substring(26, 28))[0] and 0x0F)).toFloat()
                    data.有無胎溫 = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(0, 1) == "1"
                    data.有無電壓 = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(1, 2) == "1"
                    data.有無電池 = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(2, 3) == "1"
                    data.success = true
                    response.sensorData.add(data)
                    Rx = Rx.substring(36)
                    if (response.sensorData.size == count) {
                        while (clock.stop() < 6 && needWait) {
                            Thread.sleep(30)
                        }
                        return response
                    }
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return response
        }
    }

    //
    fun getPrIdAuto(): ArrayList<SensorData> {
        val array = ArrayList<SensorData>()
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 7 || cancel) {
                    if (cancel) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "11", sensorData)
                    }
                    return array
                }
                if (Rx.length >= 36) {
                    if (Rx.substring(0, 4) != "F520") {
                        Rx = Rx.substring(36)
                    } else {
                        val data = SensorData()
                        val idcount = Integer.parseInt(Rx.substring(17, 18))
                        data.id = Rx.substring(8, 16)
                        data.idcount = idcount
                        data.bat = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4)
                        data.kpa = byte2ToINT(StringHexToByte(Rx.substring(22, 26))).toFloat()
                        val bytes = StringHexToByte(Rx.substring(18, 22))
                        data.c = bytes[1] - bytes[0]
                        data.vol = (22 + (StringHexToByte(Rx.substring(26, 28))[0] and 0x0F)).toFloat()
                        data.有無胎溫 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(0, 1) == "1"
                        data.有無電壓 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(1, 2) == "1"
                        data.有無電池 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(2, 3) == "1"
                        data.success = true
                        array.add(data)
                        if (array.size == ScanCount) {
                            return array
                        } else {
                            if (Rx.length > 36) {
                                Rx = Rx.substring(36)
                            } else {
                                Rx = ""
                            }
                        }
                    }
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return array
        }
    }

    //單顆讀取
    fun GetId(
        hex: String,
        Lf: String,
        timeOut: Int = 15,
        oeRead: String = "00",
        reduceExe: () -> Unit = {}
    ): SensorData {
        val data = SensorData()
        tx_memory = StringBuffer()
        try {
            val index = arrayOf("02", "03").indexOf(oeRead)
            Send(
                "0A 10 000E 01 00 LF HEX 00 ${PublicBean.getOERead()} 00 00 00 ${PublicBean.getLFProtocol()} 00 00 39 F5".replace(
                    "HEX",
                    hex
                ).replace(" ", "").replace("LF", Lf)
            )
            val clock = JzClock()
            while (true) {
                val time = clock.stop()
                if (time > timeOut || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel || Rx == "F511000300E70A") {
                    if (index != -1) {
                        if (Rx.contains("F51C")) {
                            Rx = ""
                            reduceExe()
                        } else {
                            MysqDatabase.insertTrigger(tx_memory.toString(), "11-4", "NA")
                            data.success = false
                            data.cancel = true
                            return data
                        }
                    } else {
                        data.success = false
                        if (time > timeOut) {
                            ReOpen()
                            when (PublicBean.position) {
                                PublicBean.複製傳感器, PublicBean.ID_COPY_OBD -> {
                                    MysqDatabase.insertCopyResult(tx_memory.toString(), "6_noResponse")
                                }
                                PublicBean.OBD_RELEARM -> {
                                }
                                else -> {
                                    MysqDatabase.insertTrigger(tx_memory.toString(), "noResponse", "NA")
                                }
                            }
                        } else if (cancel) {
                            data.cancel = true
                            when (PublicBean.position) {
                                PublicBean.複製傳感器, PublicBean.ID_COPY_OBD -> {
                                    MysqDatabase.insertCopyResult(tx_memory.toString(), "11")
                                }
                                PublicBean.OBD_RELEARM -> {
                                }
                                else -> {
                                    MysqDatabase.insertTrigger(tx_memory.toString(), "11", "NA")
                                }
                            }
                        } else {
                            when (PublicBean.position) {
                                PublicBean.複製傳感器, PublicBean.ID_COPY_OBD -> {
                                    MysqDatabase.insertCopyResult(tx_memory.toString(), "6-1-0")
                                }
                                PublicBean.OBD_RELEARM -> {
                                }
                                else -> {
                                    MysqDatabase.insertTrigger(tx_memory.toString(), "11-1", "NA")
                                }
                            }
                        }
                        return data
                    }
                }
                if (Rx.length >= 36) {
                    when (PublicBean.position) {
                        PublicBean.複製傳感器, PublicBean.ID_COPY_OBD -> {
                            MysqDatabase.insertCopyResult(tx_memory.toString(), "6-1-1")
                        }
                    }
                    val idcount = Integer.parseInt(Rx.substring(17, 18))
                    data.idcount = idcount
                    data.id = Rx.substring(16 - idcount, 16)
                    data.bat = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4)
                    data.kpa = byte2ToINT(StringHexToByte(Rx.substring(22, 26))).toFloat()
                    val bytes = StringHexToByte(Rx.substring(18, 22))
                    data.c = ((bytes[1].toInt() and 0xFF) - (bytes[0].toInt() and 0xFF))
                    data.vol = (22 + (StringHexToByte(Rx.substring(26, 28))[0] and 0x0F)).toFloat()
                    data.有無胎溫 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(0, 1) == "1"
                    data.有無電壓 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(1, 2) == "1"
                    data.有無電池 =
                        getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(2, 3) == "1"
                    data.success = true
                    when (PublicBean.position) {
                        PublicBean.複製傳感器, PublicBean.ID_COPY_OBD, PublicBean.OBD_RELEARM -> {
                        }
                        else -> {
                            if (data.kpa > 100) {
                                MysqDatabase.insertTrigger(
                                    tx_memory.toString(),
                                    if ((index != -1) && time > 15) "11-2-1" else "11-2",
                                    data.id,
                                    "" + data.c,
                                    "" + data.kpa,
                                    data.bat,
                                    exTime = time.toString()
                                )
                            } else {
                                MysqDatabase.insertTrigger(
                                    tx_memory.toString(),
                                    if ((index != -1) && time > 15) "11-3-1" else "11-3",
                                    data.id,
                                    "" + data.c,
                                    "" + data.kpa,
                                    data.bat,
                                    exTime = time.toString()
                                )
                            }
                        }
                    }
                    return data
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            data.success = false
            return data
        }
    }

    //RfiDRead
    fun rfidRead(callback: (sensorData: SensorData?) -> Unit) {
        Thread {
            try {
                sendData("0a 10 00 0E 01 00 14 B3 00 00 00 00 00 13 00 00 a1 f5".replace(" ", ""))
                val clock = JzClock()
                while (true) {
                    if (clock.stop() > 15) {
                        handler.post { callback(null) }
                        break
                    }
                    if(Rx.length==70){
                        val data=SensorData()
                        data.idcount = Integer.parseInt(Rx.substring(17, 18))
                        data.id = Rx.substring(16-data.idcount , 16)
                        data.rfIdData=Rx.substring(18,42)
                        data.storageData=Rx.substring(42,66)
                        data.success = true
                        handler.post {
                            callback(data)
                        }
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {  callback(null) }
            }
        }.start()
    }
    //writeRfSensor
    fun writeRfSensor(data1:String,data2:String,callback: (sensorData: Boolean) -> Unit) {
        Thread {
            Thread.sleep(2000)
            try {
                val to12Byte:(a:String)->String={
                    var a=it
                    while (a.length<24){a="0$a"}
                    a
                }
                sendData("0a 40 00 1F 00 33 F4 90 06 ${to12Byte(data1)} ${to12Byte(data2)} 04 f5".replace(" ", ""))
                val clock = JzClock()
                while (true) {
                    if (clock.stop() > 15) {
                        handler.post { callback(false) }
                        break
                    }
                    if(Rx.length==70){
                        handler.post {
                            callback(true)
                        }
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {  callback(false) }
            }
        }.start()
    }

    fun Program(
        Lf: String,
        Hex: String,
        count: String,
        s19: String,
        caller: Program_C,
        sensorData: ArrayList<SensorData>
    ) {
        try {
            this.sensorData = sensorData
            var result = true
            if (!PublicBean.getSIISensor()) {
                sensorData.map { it.programResult = SensorData.STATE_FAILED }
            }
            if (!SendTrigerInfo2(sensorData)) {
                caller.Program_Finish(false)
                return
            }
            val a = ProgramFirst(Lf, Hex, count, PublicBean.getS3Code().toString(), sensorData)
            if (a != 2 && a != 3) {
                caller.prograam_NotProceed(a)
                return
            }
            if (a == 2 && !PublicBean.getSIISensor()) {
                caller.prograam_NotProceed(-4)
                return
            }

            val s2 = sensorData.filter {
                it.senesorType == "S2" && it.programResult != SensorData.STATE_SUCCESS
            }
            val s3 = sensorData.filter {
                it.senesorType == "S3" && it.programResult != SensorData.STATE_SUCCESS
            }
            if (s2.isNotEmpty()) {
                PublicBean.SensorType = "S2"
                P_Callback = caller
                Log.e("s19:", s19)
                sb = PublicBean.getS2Code()
                when (a) {
                    -1 -> {
                        result = false
                        return
                    }
                    0, 1 -> {
                        JzActivity.getControlInstance().getHandler().post {
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance()
                                .showDiaLog(true, false, single_program(a), "single_program")
                        }
                        return
                    }
                }

                result = if (sendErase(true) && WriteFlash(spilt, true) && result) {
                    ProgramCheck(spilt, true)
                } else {
                    false
                }
            }
            if (s3.isNotEmpty()) {
                PublicBean.SensorType = "S3"
                P_Callback = caller
                Log.e("s19:", s19)
                sb = PublicBean.getS3Code()
                Log.e("ProgramSensor", "$a")
                when (a) {
                    -1 -> {
                        result = false
                        return
                    }

                    0, 1 -> {
                        JzActivity.getControlInstance().getHandler().post {
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance()
                                .showDiaLog(true, false, single_program(a), "single_program")
                        }
                        return
                    }
                }
                if (sendErase() && WriteFlash(spilt) && result) {
                    result = ProgramCheck(spilt, false)
                } else {
                    result = false
                }
            }
            caller.Program_Finish(result)
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Program_Finish(false)
        }
    }

    // -1是失敗
    //0  是包含201
    //1  是包含202
    //2  是請繼續
    fun SendTrigerInfo(sensor: ArrayList<String>): Boolean {
        try {
            Log.e("ProgramProgress", "SendTrigerInfo")
            for (i in sensor.indices) {
                var position = Integer.toHexString(i + 1)
                while (position.length < 2) {
                    position = "0$position"
                }
                var id = sensor[i]
                while (id.length < 8) {
                    id = "0$id"
                }
                Send(
                    "0A 15 00 0E position ID 00 00 00 00 00 00 00 18 F5".replace(
                        "position",
                        position
                    ).replace("ID", id).replace(" ", "")
                )
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val past = sdf.parse(sdf.format(Date()))
                do {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now!!, past!!)
                    if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel!!) {
                        if (time > 20) {
                            ReOpen()
                            return false
                        }
                    }
                } while (Rx.length != 36)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun SendTrigerInfo2(sensor: List<SensorData>): Boolean {
        try {
            Log.e("ProgramProgress", "SendTrigerInfo2")
            for (i in sensor.indices) {
                var position = Integer.toHexString(i + 1)
                while (position.length < 2) {
                    position = "0$position"
                }
                var id = sensor[i].id
                while (id.length < 8) {
                    id = "0$id"
                }
                Send(
                    "0A 15 00 0E position ID 00 00 00 00 00 00 00 18 F5".replace(
                        "position",
                        position
                    ).replace("ID", id).replace(" ", "")
                )
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val past = sdf.parse(sdf.format(Date()))
                do {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now!!, past!!)
                    if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel!!) {
                        if (time > 20) {
                            ReOpen()
                            return false
                        }
                    }
                } while (Rx.length != 36)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun sendErase(isS2: Boolean = false): Boolean {
        Log.e("ProgramProgress", "sendErase")
        try {
            Send("0A16000E0" + nextStep + "000000000000000000000012F5 ".trim { it <= ' ' })
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel!!) {
                    if (time > 20) {
                        ReOpen()
                    }
                    return false
                }
                if (Rx.length == 36) {
                    ScanCount = Integer.parseInt(Rx.substring(9, 10))
                    if (!isS2) {
                        spilt = if (Rx.substring(10, 12) == "04") sb.toString().substring(
                            0,
                            2048 * 2
                        ) else sb.toString().substring(0, 6144 * 2)
                    } else {
                        spilt = sb.toString()
                    }
                    Thread.sleep(50)
                    return true
                }
            }
        } catch (r: Exception) {
            r.printStackTrace()
            return false
        }

    }

    fun ProgramFirst(Lf: String, Hex: String, count: String, data: String, sensorData: ArrayList<SensorData>): Int {
        var Hex = Hex
        var count = count
        try {
            var sensorMode = PublicBean.getSencsorModel()
            val erase = ArrayList<String>()
            val sensorType = ArrayList<String>()
            if (sensorMode == "SP201") {
                sensorMode = "0"
            } else {
                sensorMode = "1"
            }
            while (count.length < 2) {
                count = "0$count"
            }
            while (Hex.length < 2) {
                Hex = "0$Hex"
            }
            val B8 = if (data.isEmpty()) "00" else data.substring(14, 16)
            val B9 = if (data.isEmpty()) "00" else data.substring(16, 18)
            val B12 = if (data.isEmpty()) "00" else data.substring(22, 24)
            val B13 = if (data.isEmpty()) "00" else data.substring(24, 26)
            val Data =
                "0A 10 00 0E  02 CT  Lf Hex 8b 9b 12b 13b 00 00 ${if (sensorMode == "0") "00" else "01"} 00 ff f5"
                    .replace("CT", count).replace("Lf", Lf)
                    .replace("Hex", Hex).replace("8b", B8)
                    .replace("9b", B9).replace("12b", B12)
                    .replace("13b", B13).replace(" ", "")
            Send(Data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 2
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel) {
                    if (fal == 3) {
                        when (PublicBean.position) {
                            PublicBean.燒錄傳感器 -> {
                                MysqDatabase.InsertMemory(
                                    tx_memory.toString(),
                                    "2-${PublicBean.ProgramNumber}-${Rx.length / 36}",
                                    sensorData
                                )
                            }
                            PublicBean.複製傳感器 -> {
                                MysqDatabase.insertCopyResult(
                                    tx_memory.toString(),
                                    "8-${PublicBean.ProgramNumber}-${Rx.length / 36}"
                                )
                            }
                        }
                        return -1
                    }
                    SendTrigerInfo2(sensorData)
                    past = sdf.parse(sdf.format(Date()))
                    Send(Data)
                    fal += 1
                }

                if (time > 20 || cancel) {
                    if (time > 20) {
                        ReOpen()
                        when (PublicBean.position) {
                            PublicBean.燒錄傳感器 -> {
                                MysqDatabase.InsertMemory(tx_memory.toString(), "2_noResponse", sensorData)
                            }
                            PublicBean.複製傳感器 -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "8_noResponse")
                            }
                        }
                    } else {
                        when (PublicBean.position) {
                            PublicBean.燒錄傳感器 -> {
                                MysqDatabase.InsertMemory(tx_memory.toString(), "11", sensorData)
                            }
                            PublicBean.複製傳感器 -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "11")
                            }
                        }
                    }
                    return -1
                }

                if (Rx.length >= 36 * PublicBean.ProgramNumber) {
                    for (i in 0 until PublicBean.ProgramNumber) {
                        val condition = Rx.substring(36 * i + 16, 36 * i + 17).toUpperCase()
                        val seType = Rx.substring(36 * i + 17, 36 * i + 18).toUpperCase()
                        Rx.substring(36 * i + 16, 36 * i + 17).toUpperCase()
                        Log.e(
                            "condition",
                            sensorMode + ":" + Rx.substring(36 * i + 16, 36 * i + 17).toUpperCase()
                        )
                        if (condition != "A" && condition != sensorMode) {
                            return Integer.parseInt(sensorMode)
                        }
                        erase.add(condition)
                        sensorType.add(seType)
                    }
                    break
                }
                Thread.sleep(30)
            }

            if (!erase.contains("1")) {
                nextStep = "0"
            } else if (erase.contains("1") && !erase.contains("0") && !erase.contains("A")) {
                nextStep = "1"
            } else if (erase.contains("1") && erase.contains("A") && !erase.contains("0")) {
                nextStep = "A"
            }
            val s2size = sensorType.filter { it == "0" }.size
            val s3size = sensorType.filter { it != "0" }.size
            if (s2size > 1) {
                return -2
            } else if (s2size == 1 && s2size != sensorType.size) {
                return -3
            } else if (s2size == 1 && s2size == sensorType.size) {
                for (i in sensorData) {
                    i.senesorType = "S2"
                }
                return 2
            } else {
                for (i in sensorData) {
                    i.senesorType = "S3"
                }
                return 3
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }

    }

    fun close() {
        try {
            Log.e("DATA:", "重新上電")
            HardwareApp.getInstance().open5V(false)
            Thread.sleep(200)
            HardwareApp.getInstance().open9V(false)
            Thread.sleep(200)
            HardwareApp.getInstance().openPB5(false)
            Thread.sleep(200)
            HardwareApp.getInstance().setGpio1V(false)
            Thread.sleep(200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun open() {
        try {
            HardwareApp.getInstance().open5V(true)
            Thread.sleep(200)
            HardwareApp.getInstance().open9V(true)
            Thread.sleep(200)
            HardwareApp.getInstance().openPB5(true)
            Thread.sleep(200)
            HardwareApp.getInstance().setGpio1V(true)
            Thread.sleep(200)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ReOpen() {
        try {
            OgPublic.fwType=OgPublic.FwType.NotSet
            close()
            open()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun WriteFlash(data: String, s2: Boolean = false): Boolean {
        Log.e("ProgramProgress", "WriteFlash")
        try {
            val count =
                if (data.length % (if (s2) 100 else 400) == 0) data.length / (if (s2) 100 else 400) else data.length / (if (s2) 100 else 400) + 1
            for (i in 0 until count) {
                if (i == count - 1) {
                    P_Callback.Program_Progress(95)
                    if (!CheckData(data.substring((if (s2) 100 else 400) * i), Integer.toHexString(i + 1))) {
                        return false
                    }
                } else {
                    P_Callback.Program_Progress(i * 100 / count)
                    if (!CheckData(
                            data.substring(
                                (if (s2) 100 else 400) * i,
                                (if (s2) 100 else 400) * i + (if (s2) 100 else 400)
                            ),
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
            val Long = "00" + Integer.toHexString(data.length / 2 + 3)
            val command = "0A 13 LONG DATA PLACE FF F5".replace(" ", "").replace("LONG", Long)
                .replace("DATA", data).replace("PLACE", place)
            Send(command)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 6) {
                    fal += 1
                    if (fal == 3) {
                        if (PublicBean.position == PublicBean.燒錄傳感器) {
                            MysqDatabase.InsertMemory(tx_memory.toString(), "3", sensorData)
                        } else {
                            MysqDatabase.insertCopyResult(tx_memory.toString(), "3")
                        }

                        ReOpen()
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                }
                if (cancel) {
                    if (PublicBean.position == PublicBean.燒錄傳感器) {
                        MysqDatabase.InsertMemory(tx_memory.toString(), "11", sensorData)
                    } else {
                        MysqDatabase.insertCopyResult(tx_memory.toString(), "11")
                    }
                    return false
                }
                if (Rx.length >= 36) {
                    return true
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun ProgramCheck(data: String, s2: Boolean): Boolean {
        Log.e("ProgramProgress", "ProgramCheck")
        try {
            Send("0A 14 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 ff f5".replace(" ", ""))
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var fal = 0
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 60 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || fal == 10 || cancel) {
                    if (PublicBean.position == PublicBean.燒錄傳感器) {
                        when {
                            time > 15 -> {
                                MysqDatabase.InsertMemory(tx_memory.toString(), "4_noResponse", sensorData)
                                ReOpen()
                            }
                            cancel -> {
                                MysqDatabase.InsertMemory(tx_memory.toString(), "11", sensorData)
                            }
                            else -> {
                                MysqDatabase.InsertMemory(tx_memory.toString(), "4", sensorData)
                            }
                        }
                        return false
                    } else {
                        when {
                            time > 15 -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "4_noResponse")
                                ReOpen()
                            }
                            cancel -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "11")
                            }
                            else -> {
                                MysqDatabase.insertCopyResult(tx_memory.toString(), "4")
                            }
                        }
                        return false
                    }

                }
                if (Rx.length >= 36 && Rx.contains("F513000E00")) {
                    val check = Rx.substring(12, 20)
                    if (check == "7FFFFFFF" || check == "000007FF" || Rx.contains("F513000E0003")) {
                        return true
                    } else {
                        Thread.sleep(500)
                        if (!s2) {
                            if (!RePr(getBit(check).substring(1), data)) {
                                return false
                            }
                            fal++
                        } else {
                            return false
                        }
                    }
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun RePr(b: String, data: String): Boolean {
        var b = b
        b = reverseBySort(b)!!
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

    fun reboot(): Boolean {
        try {
            val data = "0A0D00030000F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 20 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A")) {
                    if (time > 20) {
                        ReOpen()
                    }
                    return false
                }
                if (Rx.length == 14) {
                    return true
                }
                Thread.sleep(1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetVerion(caller: Version_C) {
        try {
            val data = "0A0A000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 15 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A")) {
                    if (time > 15) {
                        ReOpen()
                    }
                    caller.version("", false)
                    return
                }
                if (Rx.length >= 36) {
                    caller.version(Rx.substring(8, 16), true)
                    return
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun WriteBootloader(act: JzActivity, Ind: Int, filename: String, caller: Update_C) {
        try {
            val fr =
                InputStreamReader(if (filename == "no") act.assets.open("update.x2") else FileInputStream(act.applicationContext.filesDir.path + "/update.x2"))
            val br = BufferedReader(fr)
            val sb = StringBuilder()
            while (br.ready()) {
                var s = br.readLine()
                s = s.replace("null", "")
                sb.append(s)
            }
            var Long = 0
            if (sb.length % Ind == 0) {
                Long = sb.length / Ind
            } else {
                Long = sb.length / Ind + 1
            }
            Log.d("總行數", "" + Long)
            for (i in 0 until Long) {
                if (i == Long - 1) {
                    Log.d("行數", "" + i)
                    val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                    val length = Ind + 2
                    check(Convvvert(data, Integer.toHexString(length)))
                    caller.Updateing(100)
                    caller.Finish(true)
                    return
                } else {
                    val data = bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                    Log.d("行數", "" + i)
                    val length = Ind + 2
                    caller.Updateing(i * 100 / Long)
                    if (!check(Convvvert(data, Integer.toHexString(length)))) {
                        caller.Finish(false)
                        return
                    }
                }
            }
            fr.close()
            caller.Finish(true)
        } catch (e: Exception) {
            e.printStackTrace()
            caller.Finish(false)
        }

    }

    fun Convvvert(data: String, length: String): String {
        var length = length
        var command = "0A02LX00F5"
        while (length.length < 4) {
            length = "0$length"
        }
        command = command.replace("L", length).replace("X", data)
        return command
    }

    fun check(data: String): Boolean {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            Send(data)
            while (fal < 5) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (cancel) {
                    return false
                }
                if (time > 2) {
                    past = sdf.parse(sdf.format(Date()))
                    Send(data)
                    fal++
                }
                if (Rx.length >= 14 && Rx == GetCrcString("F502000300F40A") || Rx == GetCrcString("F50B000301F70A") || Rx == "F50B000301F70A") {
                    return true
                }
                Thread.sleep(30)
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GetHard() {
        try {
            val data = "0A0C000EFFFFFFFFFFFFFFFFFFFFFFFF00F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 15 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A")) {
                    if (time > 15) {
                        ReOpen()
                    }
                    return
                }
                if (Rx.length >= 14) {
                    //                    if(Rx.contains(GetCrcString("F500000302F40A"))){caller.result(2);}
                    //                    if(Rx.contains(GetCrcString("F500000301F40A"))){caller.result(1);}
                    return
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //        caller.result(-1);
        }

    }

    fun HandShake(caller: Hanshake_C) {
        try {
            val data = "0A0000030000F5"
            Send(data)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now!!, past!!)
                if (time > 8 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A")) {
                    if (time > 8) {
                        ReOpen()
                    }
                    caller.result(-1)
                    return
                }
                if (Rx.length >= 14) {
                    if (Rx.contains(GetCrcString("F500000302F40A"))) {
                        caller.result(2)
                        return
                    }
                    if (Rx.contains(GetCrcString("F500000301F40A"))) {
                        caller.result(1)
                        return
                    }
                    if (Rx.contains(GetCrcString("F501000300F70A"))) {
                        caller.result(1)
                        return
                    }
                    caller.result(-1)
                    return
                }
                Thread.sleep(30)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            caller.result(-1)
        }
    }

    fun IdCoopy(originId: ArrayList<String>, newID: ArrayList<String>, result: (id: ArrayList<SensorData>) -> Unit) {
        val _long = PublicBean.getIdcount()
        var hex = PublicBean.getHEX()
        while (hex.length < 2) {
            hex = "0$hex"
        }
        try {
            val succes = ArrayList<SensorData>()
            for (i in 0 until originId.size) {
                var Original_ID = originId[i]
                while (Original_ID.length < 8) {
                    Original_ID = "0$Original_ID"
                }
                var New_ID = newID[i]
                while (New_ID.length < 8) {
                    New_ID = "0$New_ID"
                }
                val data = "0A 11 00 0E Original_ID Original_Long New_ID New_Long hex 00 ff f5".replace(" ", "")
                    .replace("Original_Long", "0$_long")
                    .replace("New_Long", "0$_long")
                    .replace("Original_ID", Original_ID)
                    .replace("New_ID", New_ID).replace("hex", hex)
                Log.e("DATA:", "Prepare:$data")
                Send(data)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val fal = 0
                val past = sdf.parse(sdf.format(Date()))
                while (true) {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now!!, past!!)
                    if (time > 15 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel) {
                        if (time > 15) {
                            ReOpen()
                        }
                        break
                    }
                    if (Rx.length >= 36) {
                        val data = SensorData()
                        val idcount = Integer.parseInt(Rx.substring(17, 18))
                        data.id = Rx.substring(8, 16)
                        data.idcount = idcount
                        data.bat = getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(3, 4)
                        data.kpa = byte2ToINT(StringHexToByte(Rx.substring(22, 26))).toFloat()
                        val bytes = StringHexToByte(Rx.substring(18, 22))
                        data.c = bytes[1] - bytes[0]
                        data.vol = (22 + (StringHexToByte(Rx.substring(26, 28))[0] and 0x0F)).toFloat()
                        data.有無胎溫 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(0, 1) == "1"
                        data.有無電壓 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(1, 2) == "1"
                        data.有無電池 =
                            getBit(StringHexToByte(Rx.substring(28, 30))[0]).substring(2, 3) == "1"
                        data.success = true
                        if (arrayOf("HOSTKA03", "HOSWA306").indexOf(PublicBean.getS19()) != -1) {
                            data.id = data.id.substring(6, 8) + data.id.substring(0, 6)
                        }
                        succes.add(data)
                        break
                    }
                    Thread.sleep(30)
                }
                Thread.sleep(1000)
            }
            result(succes)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun IdCopy(caller: Copy_C, hex: String, _long: Int) {
        var hex = hex
        try {
            PublicBean.CopySuccess = ArrayList()
            PublicBean.CopySuccess.add(false)
            PublicBean.CopySuccess.add(false)
            PublicBean.CopySuccess.add(false)
            PublicBean.CopySuccess.add(false)
            PublicBean.CopySuccess.add(false)
            while (hex.length < 2) {
                hex = "0$hex"
            }
            for (i in 0 until PublicBean.SensorList.size) {
                var Original_ID = PublicBean.SensorList[i].id
                while (Original_ID.length < 8) {
                    Original_ID = "0$Original_ID"
                }
                var New_ID = PublicBean.NewSensorList[i].id
                while (New_ID.length < 8) {
                    New_ID = "0$New_ID"
                }
                val data =
                    "0A 11 00 0E Original_ID Original_Long New_ID New_Long hex 00 ff f5".replace(
                        " ",
                        ""
                    ).replace("Original_Long", "0$_long")
                        .replace("New_Long", "0$_long").replace("Original_ID", Original_ID)
                        .replace("New_ID", New_ID).replace("hex", hex)
                Log.e("DATA:", "Prepare:$data")
                Send(data)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                val fal = 0
                val past = sdf.parse(sdf.format(Date()))
                while (true) {
                    val now = sdf.parse(sdf.format(Date()))
                    val time = getDatePoor(now!!, past!!)
                    if (time > 15 || Rx.contains("F51C000301") || Rx == GetCrcString("F51C000302000A") || cancel) {
                        if (time > 15) {
                            ReOpen()
                            caller.Copy_Finish(false)
                            return
                        }
                        if (SendTag == NowTag) {
                            caller.Copy_Next(false, i)
                        }
                        break
                    }
                    if (Rx.length >= 36) {
                        val idcount = Integer.parseInt(Rx.substring(17, 18))
                        var tempid = PublicBean.SensorList[i].id
                        while (tempid.length < 8) {
                            tempid = "0$tempid"
                        }
                        if (Rx.contains(tempid.substring(8 - idcount).toUpperCase())) {
                            if (SendTag == NowTag) {
                                PublicBean.CopySuccess[i] = true
                                caller.Copy_Next(true, i)
                            }
                        } else {
                            if (SendTag == NowTag) {
                                PublicBean.CopySuccess[i] = false
                                caller.Copy_Next(false, i)
                            }
                        }
                        break
                    }
                    Thread.sleep(30)
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
