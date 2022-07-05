package com.orange.og_lite.beans

import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.util.Log
import androidx.fragment.app.Fragment
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.BleManager
import com.orange.og_lite.Frag.*
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.connectBack
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.jzutil.toHex
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList

enum class BootloaderState {
    Bootloader, Og_App, Obd_App
}

class PublicBeans {
    interface sensorBack {
        fun callback(content: String)
    }

    companion object {
        //---------------------------------------------------------------------->OG用<-----------------------------------------------------------------------------------------------------------------------
        var programNumber = 0
        var Update = false
        var Program_position = -1
        //---------------------------------------------------------------------->OBD用<-----------------------------------------------------------------------------------------------------------------------

        var DongleState = BootloaderState.Bootloader
        var position = 0;
        val OBD複製 = 1;
        val OBD學碼 = 2;
        val 燒錄 = 3;
        val 複製ID = 4;
        val 讀取 = 5;
        val 線上訂購 = 6
        val 原廠學碼步驟 = 7
        var selectWay = 0
        var SCAN = 1
        var READ = 2
        var KEY_IN = 3
        var make = ""
        var model = ""
        var OBDtool = true
        var DialogKeyboard = true
        var DialogKeytext = ""
        var beta: Boolean
            get() {
                return JzActivity.getControlInstance().getPro("beta", false)
            }
            set(value) {
                JzActivity.getControlInstance().setPro("beta", value)
            }
        val manager: BleManager
            get() {
                return (JzActivity.getControlInstance().getRootActivity() as MainActivity).manager
            }
        val admin: String
            get() {
                return JzActivity.getControlInstance().getPro("admin", "nodata")
            }
        val password: String
            get() {
                return JzActivity.getControlInstance().getPro("password", "nodata")
            }

        var year = ""
        var serial = ""
        var obdappversion = ""
        //-------------------------------------------------------------------------上次的COPY或Program紀錄-----------------------------------------------------------------------------------
        var lastFunction: ObdBeans = ObdBeans()
        //-------------------------------------------------------------------------取得MCU版本-----------------------------------------------------------------------------------
        val MCU_NUMBER
            get() = JzActivity.getControlInstance().getPro("Version", "ff")
        //-------------------------------------------------------------------------取得Bootloader版本-----------------------------------------------------------------------------------
        val bootloaderVersion
            get() = JzActivity.getControlInstance().getPro("bootloaderVersion", "10")
        //-------------------------------------------------------------------------取得硬體版本-----------------------------------------------------------------------------------
        var hardWareVersion
            get() = JzActivity.getControlInstance().getPro("hardWareVersion", "20190102")
        set(value) {
            JzActivity.getControlInstance().setPro("hardWareVersion",value)
        }
        //-------------------------------------------------------------------------取得資料庫版本-----------------------------------------------------------------------------------
        val DataVersion
            get() = JzActivity.getControlInstance().getPro(
                "mmyname",
                "MMY_EU_list_V0.5_191113"
            ).substring(12).replace(".db", "");
        //-------------------------------------------------------------------------取得SN-----------------------------------------------------------------------------------
        val SN: String
            get() {
                val memory = JzActivity.getControlInstance().getPro(
                    "SN",
                    "nodata"
                )
                if (memory == "nodata" || memory == "FFFFFFFFFFFF") {
                    return "test"
                } else {
                    return memory
                }
            }
        //-------------------------------------------------------------------------取得藍芽版本-----------------------------------------------------------------------------------
        var bleVersion: String
            get() {
                return String(
                    JzActivity.getControlInstance().getPro(
                        "bleVersion",
                        "nodata".toByteArray().toHex()
                    ).hexToByte()
                )
            }
            set(value) {
                JzActivity.getControlInstance().setPro("bleVersion", value)
            }
        //---------------------------------------------------------------------------取得APK版本------------------------------------------------------------------------------------
        val versionCodes: Int
            get() {
                //获取包管理器
                val pm = JzActivity.getControlInstance().getRootActivity().getPackageManager()
                //获取包信息
                try {
                    val packageInfo = pm.getPackageInfo(
                        JzActivity.getControlInstance().getRootActivity().getPackageName(),
                        0
                    )
                    //返回版本号
                    return packageInfo.versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    return 0
                }
            }
        //---------------------------------------------------------------------->公用<---------------------------------------------------------------------------------------------------------
        var readable = arrayOf(false, false, false, false, false)

        //取得OBD燒錄檔
        fun getOBD2(): String? {
            var data: String = ""
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            act.item.query(
                "select `OBD2` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD2` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
//            val input = JzActivity.getControlInstance().getRootActivity().assets.open("OBDB_APP_TO001_200728.srec")
//            val reader = BufferedReader(InputStreamReader(input, "utf-8"))
//            var line: String? = reader.readLine()
//            val strBuf = StringBuffer()
//            while (line  != null) {
//                strBuf.append(line)
//                line = reader.readLine()
//            }
//            return strBuf.toString()
//            Log.e("obd", "data->$data")
            return if(data.length>1) getFile(data.substring(0,data.length-1)+"L") else null
        }

        //取得OBD名稱
        fun getOBDName(): String? {
            var data: String = ""
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            act.item.query(
                "select `OBD2` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD2` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
            return data
        }

        //取得現有OBD版本號
        fun getOBDVersion(): String? {
            var data: String = ""
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            act.item.query(
                "select `OBD2` from `Summary table` where Make='$make' and Model='$model' and year='$year' and `OBD2` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
            return FileJsonVersion.getLocal().obdList[data.substring(0,data.length-1)+"L"]?.replace(".srec", "")
        }

        //取得OGMcuVersion
        fun getOGMcuVersion(): String? {
            return "OBDB_APP_CR001_191216.srec".replace(".srec", "")
        }

        //取輪胎數量
        fun wheelCount(): Int {
            val act = JzActivity.getControlInstance().getRootActivity() as MainActivity
            var wheelcount = 4
            act.item.query(
                "select  `Wheel_Count`  from `Summary table` where Make='$make' and Model='$model' and year='$year' limit 0,1",
                Sql_Result {
                    //Callback回調，會迴圈跑到所有資料載入完
                    val result1 = it.getString(0)
                    wheelcount = result1.toInt()
                });
            return wheelcount
        }

        //取得sensorid
        fun getSensor(callback: sensorBack) {
            JzActivity.getControlInstance().changePage(
                Frag_Function_QrcodeScanner(
                    object :
                        scanback {
                        override fun callback(content: String) {
                            if (!content.contains(":") && !content.contains("*")) {
                                if (content != "nofound") {
                                    JzActivity.getControlInstance()
                                        .toast(R.string.app_invalid_sensor_qrcode)
                                } else {
                                    JzActivity.getControlInstance()
                                        .toast(R.string.app_scan_code_timeout)
                                }
                                return
                            }
                            var sensorid = ""
                            if (content.contains("**")) {
                                JzActivity.getControlInstance()
                                    .toast(R.string.app_invalid_sensor_qrcode)
                            } else {
                                if (content.split(":", "*").size >= 3) {
                                    sensorid = content.split(":", "*")[1]
                                }
                            }
                            if (sensorid != "") {
                                callback.callback(sensorid)
                            }
                        }
                    },
                    1
                ), "Frag_Function_QrcodeScanner", true
            )
        }

        //取得OgMcu
        fun getOgMcu(): String? {
            val input =
                JzActivity.getControlInstance().getRootActivity().assets.open("OG_LITE_APP.srec")
            val reader = BufferedReader(InputStreamReader(input, "utf-8"))
            var line: String? = reader.readLine()
            val strBuf = StringBuffer()
            while (line != null) {
                strBuf.append(line)
                line = reader.readLine()
            }
            return strBuf.toString()
        }

        //取得file()
        fun getFile(a: String): String? {
            var data: String? = null
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.query("select data from file where ditectfit='$a'", Sql_Result {
                data = it.getString(0)
            })
            return data
        }

        //插入file()
        fun insertFile(a: String, b: String) {
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.exsql("insert or replace into file (ditectfit,data) values ('$a','$b')")

        }

        //取得S19
        fun getS19(): String {
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            var name = ""
            item.query("select  `Direct Fit` from `Summary table` " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1",
                Sql_Result {
                    name = it.getString(0)
                }
            )
            return name
        }
        //取得SensorModel
        fun getSencsorModel(): String {
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            var name = "SP201"
            val result = item.query(
                "select `Sensor` from `Summary table` where  `Direct Fit`='${getS19()}'",
                Sql_Result {
                    name = it.getString(0)
                })
            Log.e("getSencsorModel", name)
            return name
        }
        //取得getOePart
        fun getOePart(): String {
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            var name = ""
            item.query(
                "select `OE Part Num` from `Summary table`   where `Direct Fit`='${getS19()}' limit 0,1",
                Sql_Result {
                    name = it.getString(0)
                })
            return name
        }

        //取得id數量
        fun getIdcount(): Int {
            var int = 8
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `ID_Count` from `Summary table` where `Direct Fit`='${getS19()}' limit 0,1",
                Sql_Result {
                    int = it.getString(0).toInt()
                })
            return int
        }

        //取得SencsorModel
        fun SencsorModel(): String {
            var a = "SP201"
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `Sensor` from `Summary table` where  `Direct Fit`='${getS19()}'",
                Sql_Result {
                    a = it.getString(0)
                })
            return a
        }

        //取得Lf
        fun getLf(): String? {
            var a = "0"
            val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            val result = item.query(
                "select `Lf` from `Summary table` where  `Direct Fit`='${getS19()}'",
                Sql_Result {
                    a = it.getString(0)
                })
            return a
        }

        //取得S19File
        fun getS19File(): String? {
            var data: String = ""
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item_File
            act.query("select data from file where ditectfit='${getS19()}'", Sql_Result {
                data = it.getString(0)
            })
            return data
        }

        //取得Lf_Power
        fun getLfPower(): String {
            var data: String = "00"
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            act.query("select  `LF Power` from `Summary table` " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)
                })
            return data
        }

        //取得HEX
        fun getHEX(): String {
            var data: String = "00"
            val act = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
            act.query("select  `OBD Product No. (hex)` from `Summary table`  " +
                    "where Make='$make' and Model='$model' and year='$year'  and  `Direct Fit` not in('NA') limit 0,1",
                Sql_Result {
                    data = it.getString(0)

                })
            if (data.length == 4) {
                data = data.substring(2, 4)
            } else {
                return "00"
            }
            return data
        }


        fun changeSwitch() {
            when (position) {
                OBD複製 -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(),
                        "Frag_Function_ID_Copy_Selection", true
                    )

                }
                OBD學碼 -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(),
                        "Frag_Function_ID_Copy_Selection", true
                    )
                }
                燒錄 -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_Program_Sensor_Quantity(),
                        "Frag_Function_Program_Sensor_Quantity", true
                    )
                }
                複製ID -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(),
                        "Frag_Function_ID_Copy_Selection", true
                    )
                }
                讀取 -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_Check_Sensor_Read(),
                        "Frag_Function_Check_Sensor_Read", true
                    )
                }
                原廠學碼步驟 -> {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_Relearn_Procedure(
                            1
                        ), "Frag_Function_Relearn_Procedure", true
                    )
                }

            }
        }

        //取得學馬步驟類型
        fun getRelearmMode(): String {
            try {
                val item = (JzActivity.getControlInstance().getRootActivity() as MainActivity).item
                var name = ""
                item.query(
                    "select `OGL Auto` from `Summary table`   where  Make='$make' and Model='$model' and year='$year'  limit 0,1",
                    Sql_Result {
                        name = it.getString(0)
                    })
                Log.e("PublicBeanRelearmMode", "->$name")
                return name
            } catch (e: Exception) {
                e.printStackTrace()
                return "0"
            }
        }


//        val manager= JzActivity.getControlInstance().getRootActivity() as BleManager
//        fun GoScanner(change: Fragment, frag:Int, tag:String, goback: Boolean)
//        {
//            if (!manager.Ble_Helper.isConnect() || !manager.Ble_Helper.bleadapter.isEnabled){
//                JzActivity.getControlInstance().showDiaLog(R.layout.activity_scan_ble, false, false,
//                    Da_Scan_ble(object : connectBack {
//                        override fun connec() {
//                            JzActivity.getControlInstance().changePage(change!!,frag,tag,goback)
//                        }
//                    }), "Da_Scan_ble")
//                manager.Ble_Helper.openBle()
//                manager.Ble_Helper.startScan()
//            }else{
//                JzActivity.getControlInstance().changePage(change!!,frag,tag,goback)
//            }
//        }
    }
}