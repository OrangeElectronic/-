package com.orange.og_lite

import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import com.orange.og_lite.Util.fixLanguage
import com.example.jztaskhandler.TaskHandler
import com.jianzhi.jzblehelper.FormatConvert
import com.jianzhi.jzblehelper.FormatConvert.StringHexToByte
import com.jianzhi.jzblehelper.FormatConvert.bytesToHex
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.BootloaderState
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.loading_view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.xor

class ObdCommand {
    var clock = TaskHandler.newInstance.clock()
    val handler: Handler
        get() {
            return JzActivity.getControlInstance().getRootActivity().handler
        }
    private val rxUUID = "00008D81-0000-1000-8000-00805F9B34FB"
    private val TXUUID = "00008D82-0000-1000-8000-00805F9B34FB"
    fun send(a: String) {
        var dateStr = ""
        val sdf = SimpleDateFormat("HH:mm:ss:SSS")
        dateStr = sdf.format(Date())
        OgCommand.tx_memory.append("TX:\t").append(dateStr + "\t").append(a).append("\n")
        Command.rx = ""
        (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager.Ble_Helper.writeHex(
            a,
            rxUUID,
            TXUUID
        )
    }

    //設定tireid
    fun setTire(myitem: ObdBeans): Boolean {
        try {
            val resourse = JzActivity.getControlInstance().getRootActivity().resources
            val positionArray = arrayOf(
                resourse.jzString(R.string.app_fl),
                resourse.jzString(R.string.app_fr),
                resourse.jzString(R.string.app_rr),
                resourse.jzString(R.string.app_rl),
                "SP"
            )
            val position = arrayOf("4", "1", "2", "3", "5")
            val allWrite = arrayOf("0", "0", "0", "0", "0")
            for (i in 0 until myitem.writeList.size) {
                allWrite[i] = myitem.writeList[i]
            }
            val tmpsend = ArrayList<String>()
            tmpsend.add("60A200FFFFFFFFC20A")
            for (i in 0 until PublicBeans.wheelCount()) {
                while (allWrite[i].length < 8) {
                    allWrite[i] = "0${allWrite[i]}"
                }
                tmpsend.add(
                    addcheckbyte(
                        "60A20XidFF0A".replace("id", allWrite[i]).replace(
                            "X",
                            position[i]
                        )
                    )
                )
            }
            tmpsend.add("60A2FFFFFFFFFF3D0A")
            for (a in tmpsend) {
                send(a)
                Thread.sleep(50)
            }
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 5) {
                    fal += 1
                    if (fal == 10) {
                        return false
                    } else {
                        for (a in tmpsend) {
                            send(a)
                            Thread.sleep(50)
                        }
                    }
                    clock.Zeroing()
                }
                if (Command.rx.contains("60B201FFFFFFFFD30A")) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
    }
    //讀取ID
    fun gerID(myitem: ObdBeans): Boolean {
        try {
            Command.getState()
            val a = "60BF00010" + PublicBeans.wheelCount() + "FF0A";
            send(addcheckbyte(a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 2.5) {
                    fal += 1
                    clock.Zeroing()
                    if (fal == 10) {
                        MysqDatabase.insertOBD(
                            OgCommand.tx_memory.toString(),
                            "ReadTimeout"
                        );return false
                    }
                    send(GetXOR(a))
                }
                if (Command.rx.length == 52) {
                    myitem.writeList.clear()
                    for (i in 0 until 5) {
                        val resourse = JzActivity.getControlInstance().getRootActivity().resources
                        val positionArray = arrayOf(
                            resourse.jzString(R.string.app_fl),
                            resourse.jzString(R.string.app_fr),
                            resourse.jzString(R.string.app_rr),
                            resourse.jzString(R.string.app_rl),
                            "SP"
                        )
                        myitem.writeList.add(Command.rx.substring((i + 1) * 8, (i + 2) * 8))
                        for (c in 0 until myitem.wheelPosition.size) {
                            if (myitem.wheelPosition[c] == positionArray[i]) {
                                myitem.OldSemsor[c] = Command.rx.substring((i + 1) * 8, (i + 2) * 8)
                                myitem.writeList[i]=myitem.NewSensor[c]
                            }
                        }

                    }
                    MysqDatabase.insertOBD(OgCommand.tx_memory.toString(), "ReadSuccess");
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
    }

    //載入APP資料
    fun loadObdApp(): Boolean {
        try {
            Command.ogCommand.Send("0A0D00030000F5")
            Thread.sleep(200)
            askVersion()
            Thread.sleep(500)
            Log.e("ObdVersion", bytesToHex(PublicBeans.getOBDVersion()!!.toByteArray()))
            Log.e("ObdVersion", PublicBeans.obdappversion)
            if (bytesToHex(PublicBeans.getOBDVersion()!!.toByteArray()) == PublicBeans.obdappversion) {
                return Command.goState(BootloaderState.Obd_App)
            } else {
                if (goPrObd() && goBootloader()) {
                    val a = writeFlush()
                    if (a) {
                        Thread.sleep(1000)
                        val b = writeVersion()
                        return a && b
                    } else {
                        return false
                    }

                } else {
                    return false
                }
            }
        } catch (e: java.lang.Exception) {
            return false
        }
    }

    //跳轉至燒錄OBD
    fun goPrObd(): Boolean {
        try {
            val a = addcheckbyte("0A8D00030100F5")
            clock.Zeroing()
            send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return true
                }
                if (Command.rx.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //握手指令
    fun handShake(): Boolean {
        try {
            val a = addcheckbyte("0A0000030000F5")
            clock.Zeroing()
            send(a)
            while (true) {
                if (clock.stop() > 3) {
                    return false
                }
                if (Command.rx.length == 14) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //Reboot
    fun reboot(): Boolean {
        try {
            val a = addcheckbyte("0A0D00030000F5")
            send((a))
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 3) {
                    return false
                }
                if (Command.rx == ("F501000300F70A")) {
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //取得版本號
    fun askVersion(): Boolean {
        try {
            val a = GetXOR("0ACF000100FFF5")
            send((a))
            clock.Zeroing()
            while (true) {
                if (clock.stop() > 2) {
                    return false
                }
                if (Command.rx.length == 56) {
                    PublicBeans.obdappversion = (Command.rx.substring(8, 52))
                    Log.d("BLECommand.rx", "版本號:" + PublicBeans.obdappversion)
                    return true
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //跳轉app
    fun goApp(): Boolean {
        try {
            val a = GetXOR("0ACD000100FFF5")
            send((a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 2) {
                    fal++
                    if (fal == 3) {
                        return false
                    }
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.length == 14) {
                    Log.d("BLECommand.rx", "進入app");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //寫入版本號
    fun writeVersion(): Boolean {
        try {
            val a = GetXOR(
                "0ACA0015DDFFF5".replace(
                    "DD",
                    bytesToHex(PublicBeans.getOBDVersion()!!.toByteArray())
                )
            )
            send(a)
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 1) {
                    fal++
                    if (fal == 1) {
                        return false
                    }
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.length == 14) {
                    Log.d("BLECommand.rx", "寫入版本");
                    return true;
                }
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //跳轉Bootloader
    fun goBootloader(): Boolean {
        try {

            val a = GetXOR("0ACD010100FFF5")
            send((a))
            clock.Zeroing()
            var fal = 0
            while (true) {
                if (clock.stop() > 10) {
                    fal++
                    if (fal == 1) {
                        return false
                    }
                    clock.Zeroing()
                    send((a))
                }
                if (Command.rx.contains("F5CD010100380AF501000300F70A") || Command.rx.contains("F5CD010100CD0AF501000300F70A")) {
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

    //寫入燒錄檔
    fun writeFlush(): Boolean {
        try {
            val sb = PublicBeans.getOBD2()!!
            var long = 0
            var Ind = 298
            if (sb.length % Ind == 0) {
                long = sb.length / Ind
            } else {
                long = sb.length / Ind + 1
            }
            Log.e("write總行數", "$long")
            for (i in 0 until long) {
                var b = i
                if (b >= 255) {
                    b -= 255
                }
                val result = StringBuffer(Integer.toHexString(b))
                while (result.length < 2) {
                    result.insert(0, "0")
                }
                val cont = result.toString().toUpperCase()

                if (i == long - 1) {
                    Log.d("write", "以跑完$i")
                    val data = bytesToHex(sb.substring(i * Ind, sb.length).toByteArray())
                    val length = sb.substring(i * Ind, sb.length).toByteArray().size + 3
                    send(Convvvert(data, Integer.toHexString(length), cont))
                    handler.post {
                        JzActivity.getControlInstance()
                            .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                                override fun dismess() {

                                }

                                override fun keyevent(event: KeyEvent): Boolean {
                                    return false
                                }

                                override fun setup(rootview: Dialog) {
                                    rootview.tit.text =
                                        rootview.context.resources.jzString(R.string.Data_Loading) + "..." + 100 + "%"
                                }
                            }, "loading_view")
                    }
                    return true
                } else {
                    val data = bytesToHex(sb.substring(i * Ind, i * Ind + Ind).toByteArray())
                    Log.d("行數", "" + i)
                    Log.e("data==",sb.substring(i * Ind, i * Ind + Ind)
                    val length = sb.substring(i * Ind, i * Ind + Ind).toByteArray().size + 3
                    if (!check(Convvvert(data, Integer.toHexString(length), cont))) {
                        return false
                    }
                    handler.post {
                        JzActivity.getControlInstance()
                            .showDiaLog(false, false, object : SetupDialog(R.layout.loading_view) {
                                override fun dismess() {

                                }

                                override fun keyevent(event: KeyEvent): Boolean {
                                    return false
                                }

                                override fun setup(rootview: Dialog) {
                                    rootview.tit.text =
                                        rootview.context.resources.jzString(R.string.Data_Loading) + "..." + (i * 100 / long) + "%"
                                }
                            }, "loading_view")
                    }
                }
            }
            Log.d("總行數", "" + long)
            return true;
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //確認是否燒路成功
    fun check(data: String): Boolean {
        try {
            var a = addcheckbyte(data)
            clock.Zeroing()
            var fal = 0
            send(a)
            while (true) {
                if (clock.stop() > 2) {
                    fal += 1
                    if (fal > 20) {
                        return false
                    }
                    clock.Zeroing()
                    send(a)
                }
                if (Command.rx.length >= 16) {
                    return true
                }
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace();
            return false;
        }
    }

    fun GetXOR(a: String): String {
        val command = StringHexToByte(a)
        var xor = 0
        for (i in 0 until command.size - 2) {
            xor = xor xor command[i].toInt()
        }
        command[command.size - 2] = xor.toByte()
        return bytesToHex(command)
    }

    fun addcheckbyte(com: String): String {
        val a = StringHexToByte(com)
        var checkbyte = a[0]
        for (i in 1 until a.size - 2) {
            checkbyte = checkbyte xor a[i]
        }
        a[a.size - 2] = checkbyte
        return bytesToHex(a)
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
}