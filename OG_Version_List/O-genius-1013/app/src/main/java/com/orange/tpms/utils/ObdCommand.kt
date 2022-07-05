package com.orange.tpms.utils

import android.app.Dialog
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.model.ID_Beans
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.Callback.Obd_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.StringHexToByte
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.cancel
import com.orango.electronic.orange_og_lib.MysqDatabase
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.xor


class ObdCommand {
    val act get () = (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
    var AppVersion = ""
    //要做燒錄的ＯＢＤ file名稱
    var obdFile = "OBDB_APP_TO001_191030"
    //自動設定checkbyteF5020005000000F20A
    fun addcheckbyte(com: String): String {
        val a = StringHexToByte(com)
        var checkbyte = a[0]
        for (i in 1 until a.size - 2) {
            checkbyte = checkbyte xor a[i]
        }
        a[a.size - 2] = checkbyte
        return bytesToHex(a)
    }

    //握手
    fun HandShake(): Boolean {
        try {
            val a = "0A0000030000F5"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            act.BleHelper.writeHex(addcheckbyte(a),act.BleHelper.RXchannel,act.BleHelper.TXchannel)
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 3) {
                    return false
                }
                if (act.BleHelper.RxData.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    //判斷是否為二代OBD
    var isOBDII=false


    fun GetId(count: String): ID_Beans {
        val id = ID_Beans()
        try {
            val a = "60BF0001" + count + "FF0A"
            act.BleHelper.writeBytes(GetXOR(a),act.BleHelper.RXchannel,act.BleHelper.TXchannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            cancel=false
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 5) {
                    fal++
                    past = sdf.parse(sdf.format(Date()))
                    act.BleHelper.writeBytes(GetXOR(a), act.BleHelper.RXchannel,act.BleHelper.TXchannel)
                }
                if ((fal == 12) || cancel) {
                    id.success = false
                    return id
                }
                if (act.BleHelper.RxData.length == 52) {
                    id.success = true
                    id.LF = act.BleHelper.RxData.substring(8, 16)
                    id.RF = act.BleHelper.RxData.substring(16, 24)
                    id.RR = act.BleHelper.RxData.substring(24, 32)
                    id.LR = act.BleHelper.RxData.substring(32, 40)
                    id.SP = act.BleHelper.RxData.substring(40, 48)
                    if (id.LF == "FFFFFFFF" && id.RF == "FFFFFFFF" && id.LR == "FFFFFFFF" && id.RR == "FFFFFFFF" && id.SP == "FFFFFFFF") {
                        id.success = false
                    }
                    MysqDatabase.insertOBD(Cmd_Og.tx_memory.toString(),"ReadSuccess")
                    return id
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return id
        }

    }

    //Reboot
    fun Reboot(): Boolean {
        try {
            val a = "0A0D00030000F5"
            act.BleHelper.writeHex(addcheckbyte(a),act.RxChannel,act.TxChannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 7) {
                    return false
                }
                if (act.BleHelper.RxData.equals("F501000300F70A")) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun WriteVersion(version:String): Boolean {
        try {
            val command =
                GetXOR("0ACA0015DDFFF5".replace("DD", bytesToHex(version.replace(".srec", "").toByteArray())))
            act.BleHelper.writeBytes(command,act.RxChannel,act.TxChannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 1) {
                    if (fal == 1) {
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    act.BleHelper.writeBytes(command,act.RxChannel,act.TxChannel)
                    fal++
                }
                if (act.BleHelper.RxData.length == 14) {
                    Log.d("BLEDATA", "寫入版本")
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GoBootloader(): Boolean {
        try {
            act.BleHelper.writeBytes(GetXOR("0ACD010100FFF5"),act.RxChannel,act.TxChannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 10) {
                    if (fal == 1) {
                        return false
                    }
                    past = sdf.parse(sdf.format(Date()))
                    act.BleHelper.writeBytes(GetXOR("0ACD010100FFF5"),act.RxChannel,act.TxChannel)
                    fal++
                }
                if (act.BleHelper.RxData.contains("F5CD010100CD0A01000300F70A")) {
                    Log.d("BLEDATA", "進入燒錄")
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    // 燒寫&amp;驗證Flash
    fun WriteFlash(context: Context, Ind: Int): Boolean {
        try {
            val fo = FileInputStream(context.applicationContext.filesDir.path + "/" +obdFile+".srec")
            val fr = InputStreamReader(fo)
            val br = BufferedReader(fr)
            val sb = StringBuilder()
            while (br.ready()) {
                var s: String? = br.readLine()
                if (s != null) {
                    s = s.replace("null", "")
                    sb.append(s)
                }
            }
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
                    b = b - 255
                }
                val result = StringBuffer(Integer.toHexString(b))
                while (result.length < 2) {
                    result.insert(0, "0")
                }
                val cont = result.toString().toUpperCase()
                if (i == Long - 1) {
                    Log.d("write", "以跑完$i")
                    val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                    val length = sb.substring(i * Ind, sb.length).toByteArray().size + 3
                    act.BleHelper.writeHex(Convvvert(data, Integer.toHexString(length), cont), act.RxChannel,act.TxChannel)
                    JzActivity.getControlInstance().getRootActivity().handler.post {
                        JzActivity.getControlInstance().showDiaLog(false, true,object:SetupDialog(R.layout.normal_dialog){
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                                return false
                            }

                            override fun setup(rootview: Dialog) {
                                val text:TextView = rootview.findViewById(R.id.tit)
                                text.text=(JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.wait) + 100 + "%")
                            }

                        },"normal_dialog")
                    }
                    return true
                } else {
                    val data = bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                    Log.d("行數", "" + i)
                    val length = sb.substring(i * Ind, i * Ind + Ind).toByteArray().size + 3
                    if (!check(Convvvert(data, Integer.toHexString(length), cont))) {
                        return false
                    }
                    val finalLong = Long
                    JzActivity.getControlInstance().getRootActivity().handler.post {
                        JzActivity.getControlInstance().showDiaLog(false, true, object :SetupDialog(R.layout.normal_dialog){
                            override fun dismess() {

                            }

                            override fun keyevent(event: KeyEvent): Boolean {
                               return false
                            }

                            override fun setup(rootview: Dialog) {
                                val text:TextView = rootview.findViewById(R.id.tit)
                                text.text=(JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.wait) + i * 100 / finalLong + "%")
                            }
                        },"normal_dialog")
                    }
                }
            }
            fr.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //設定tireid
    fun setTireId(Id: ArrayList<String>, caller: Obd_C) {
        try {
            Log.e("setTireId","comein")
            cancel=false
            val tmpsend = ArrayList<String>()
            tmpsend.add("60A200FFFFFFFFC20A")
            var i = 0
            Log.e("setTireId","addcheckbyte$Id")
            val position= arrayOf("4","1","2","3","5")
            for (id in Id) {
                Log.e("setTireId","id${AddEmpty(id)}")
                tmpsend.add(addcheckbyte("60A20XidFF0A".replace("id", AddEmpty(id)).replace("X", position[i])))
                i++
            }
            tmpsend.add("60A2FFFFFFFFFF3D0A")
            for (a in tmpsend) {
                act.BleHelper.writeHex(a,act.RxChannel,act.TxChannel)
                Thread.sleep(50)
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            Log.e("setTireId","todate")
            var fal=0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 5) {
                    past=sdf.parse(sdf.format(Date()))
                    fal+=1
                    for (a in tmpsend) {
                        act.BleHelper.writeHex(a,act.RxChannel,act.TxChannel)
                        Thread.sleep(50)
                    }
                    if(fal==12||(cancel)){
                        caller.result_C(false)
                        break
                    }
                }
                if (act.BleHelper.RxData.length>=18) {
                    caller.result_C(true)
                    break
                }
                Thread.sleep(100)
            }
            Log.e("setTireId","toend")
        } catch (e: Exception) {
            e.printStackTrace()
            caller.result_C(false)
            Log.e("setTireId","toerror")
        }

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
        act.BleHelper.writeHex(addcheckbyte(data),act.RxChannel,act.TxChannel)
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (fal < 5) {
                //                Thread.currentThread().sleep(20);
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 1) {
                    past = sdf.parse(sdf.format(Date()))
                    act.BleHelper.writeHex(addcheckbyte(data), act.BleHelper.RXchannel,act.BleHelper.TXchannel)
                    fal++
                }
                if (act.BleHelper.RxData.length >= 16) {
                    //                    Thread.currentThread().sleep(30);
                    return true
                }
                Thread.sleep(100)
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun AskVersion(): Boolean {
        try {
            act.BleHelper.writeBytes(GetXOR("0ACF000100FFF5"),act.RxChannel,act.TxChannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            val past = sdf.parse(sdf.format(Date()))
            val fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (time > 2) {
                    return false
                }
                if (act.BleHelper.RxData.length == 54) {
                    AppVersion = act.BleHelper.RxData.substring(8, 50)
                    Log.d("BLEDATA", "版本號:$AppVersion")
                    isOBDII=false
                    return true
                }else if(act.BleHelper.RxData.length == 56){
                    AppVersion = act.BleHelper.RxData.substring(8, 52)
                    Log.d("BLEDATA", "版本號:$AppVersion")
                    isOBDII=true
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun GoApp(): Boolean {
        try {
            act.BleHelper.writeBytes(GetXOR("0ACD000100FFF5"),act.RxChannel,act.TxChannel)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
            var past = sdf.parse(sdf.format(Date()))
            var fal = 0
            while (true) {
                val now = sdf.parse(sdf.format(Date()))
                val time = getDatePoor(now, past)
                if (fal == 3) {
                    return false
                }
                if (time > 1) {
                    past = sdf.parse(sdf.format(Date()))
                    act.BleHelper.writeBytes(GetXOR("0ACD000100FFF5"),act.RxChannel,act.TxChannel)
                    fal++
                }
                if (act.BleHelper.RxData.length >= 14) {
                    Log.d("BLEDATA", "進入app")
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    companion object {
        var WRITE_SUCCESS = "F502000300F40A"
        var Program_Flash_Fail = "F502000302F60A"
        var VERIFY_FAIL = "F502000303F70A"
        fun setScreenSleepTime(millisecond: Int, context: Context) {
            try {
                Settings.System.putInt(
                    context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT,
                    millisecond
                )
            } catch (localException: Exception) {
                localException.printStackTrace()
            }

        }

        fun AddEmpty(a: String): String {
            var temp=a
            while (temp.length<8){
                temp="0$temp"
            }
            return temp
        }

        private fun bytesToHex(hashInBytes: ByteArray): String {
            val sb = StringBuilder()
            for (b in hashInBytes) {
                sb.append(String.format("%02X", b))
            }
            return sb.toString()
        }

        //System.out.println(strDate);
        val dateTime: String
            get() {

                val sdFormat = SimpleDateFormat("HH:mm:ss:SSS")

                val date = Date()

                return sdFormat.format(date)

            }

        fun getDatePoor(endDate: Date, nowDate: Date): Double {
            val diff = endDate.time - nowDate.time
            val sec = diff / 1000
            return sec.toDouble()
        }

        fun GetXOR(a: String): ByteArray {
            val command = StringHexToByte(a)
            var xor:Byte = 0
            for (i in 0 until command.size - 2) {
                xor = xor xor command[i]
            }
            command[command.size - 2] = xor.toByte()
            return command
        }
    }
}
