package com.orange.og_lite

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.example.jzlifttool.LifeTimer
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.jianzhi.jzblehelper.FormatConvert
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.BootloaderState
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.PublicBeans.Companion.DataVersion
import com.orange.og_lite.beans.PublicBeans.Companion.MCU_NUMBER
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orange.tpms.ue.dialog.Dia_check_update
import com.orange.zhengxin.util.GpsUtil
import com.orango.electronic.jzutil.*

class MainActivity : JzActivity() {
    override fun dialogLinstener(dialog: Dialog, tag: String) {
        if (dialog.window != null) {
            val view = dialog.window!!.decorView
            view.fixLanguage()
        }
    }

    override fun savedInstanceAble(): Boolean {
        return false
    }

    companion object {
        var initNcuVersion = false
    }
    var canConnect=true
    lateinit var item: JzSqlHelper
    lateinit var item_favorite: JzSqlHelper
    lateinit var item_File: JzSqlHelper
    lateinit var sqlmemnory: JzSqlHelper
    lateinit var manager: BleManager
    var MyFavorite_Memory = com.orange.og_lite.beans.MyFavorite_Memory()

    override fun changePageListener(tag: String, frag: Fragment) {
        Page_MainActivity.toggleMenu(false)
        BleManager.toggleReadSensor=true
        JzActivity.getControlInstance().hideKeyBoard()
        Thread {
            if (frag is JzFragement) {
                while (!frag.haveRootView()) {
                }
                handler.post { frag.rootview.fixLanguage() }
            }
        }.start()
        if (getControlInstance().getActionBar() is Page_MainActivity) {
            (getControlInstance().getActionBar() as Page_MainActivity).changePageListener(
                tag,
                frag
            )
        }

        if (tag == "Frag_MainActivity_Home") {
            checkVersion()
            Thread {
                if (manager.commandFinfish) {
                    Command.getBattery()
                }
            }.start()
        } else if (tag == "Frag_Function_OBD_ID_copy") {
            Thread {
                TaskHandler.newInstance.runTaskOnce("sendID", runner {
                    if (PublicBeans.position == PublicBeans.複製ID || PublicBeans.position == PublicBeans.OBD學碼) {
                        if (Command.goState(BootloaderState.Og_App)) {
                            Command.ogCommand.sendHEX()
                        }
                    }
                })
            }.start()
        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun keyEventListener(event: KeyEvent): Boolean {
        return true
    }

    override fun viewInit(rootview: View) {
        getControlInstance().setDynamicFont(false)
        manager = BleManager(this)
        sqlmemnory = JzSqlHelper(this, "errormemory.db")
        item = JzSqlHelper(this, "ogmmy.db")
        item_favorite = JzSqlHelper(this, "favorite.db")
        item_File = JzSqlHelper(this, "file.db")
        item_File.exsql(
            "CREATE TABLE IF NOT EXISTS `file` (\n" +
                    "    ditectfit VARCHAR PRIMARY KEY,\n" +
                    "    data      VARCHAR\n" +
                    ");\n"
        )
        getControlInstance().setHome(Logo_Page(), "Logo_Page")
        LifeTimer(getControlInstance().getRootActivity().lifecycle).schedule(0, 1000, runner {
            Log.e("blehelper","isConnect->${manager.Ble_Helper.isConnect()} canConnect->${canConnect} Command.onProgram->${Command.onProgram}")
            if (::manager.isInitialized && !manager.Ble_Helper.isConnect() && canConnect ) {
                if(!Command.onProgram){
                    handler.post {
                        TaskHandler.newInstance.runTaskDelay("gps", 3.0, runner {
                            val isOpen: Boolean
                            val locationManager =
                                getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            if (!isOpen && getControlInstance().isFrontDesk()) {
                                getControlInstance().toast(resources.jzString(R.string.Get_location))
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                getControlInstance().getRootActivity().startActivity(intent)
                            }
                            getControlInstance().showDiaLog(
                                false, false,
                                Da_Scan_ble(object : connectBack {
                                    override fun connec() {

                                    }
                                }), "connectble"
                            )
                        })
                    }
                }
                if(!manager.Ble_Helper.bleadapter.isDiscovering){manager.Ble_Helper.startScan()}
            } else {
                handler.post { getControlInstance().closeDiaLog("connectble") }
            }
        })
        LifeTimer(getControlInstance().getRootActivity().lifecycle).schedule(0,
            1000 * 30,
            runner {
                Thread {
                    TaskHandler.newInstance.runTaskOnce("checkupdate", runner {
                 Util_FileDowload.ckeckNewVersion()
                    })
                }.start()
            })
        LifeTimer(getControlInstance().getRootActivity().lifecycle).schedule(0,
            1000 * 10,
            runner {
                uploadProgram()
                uploadLocation()
                uploadError()
            })

    }

    fun checkVersion() {
        Log.e("localmcu", MCU_NUMBER)
        Log.e(
            "mcuinit",
            FormatConvert.bytesToHex(getControlInstance().getPro("mcuinit", "").toByteArray())
        )
        Log.e("mmyinit", getControlInstance().getPro("mmyinit", "no"))
        Log.e("DataVersion", DataVersion)
        if (initNcuVersion&&getControlInstance().getNowPageTag()=="Frag_MainActivity_Home") {
            if (Util_FileDowload.needInit()) {
                getControlInstance()
                    .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
            } else {
                if(Util_FileDowload.needUpdate()){
                    getControlInstance()
                        .showDiaLog(false, false, Dia_check_update(), "Dia_check_update")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        item.close()
        item_favorite.close()
        item_File.close()
        sqlmemnory.close()
    }

    //上傳燒錄紀錄
    fun uploadProgram() {
        Thread {
            TaskHandler.newInstance.runTaskOnce("uploadProgram", runner {
                sqlmemnory.exsql(
                    "CREATE TABLE if not exists `updateResult` (`id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "  `sql` varchar NOT NULL\n)"
                )
                sqlmemnory.query("select * from `updateResult`", Sql_Result {
                    val sqlc = String(it.getString(1).hexToByte())
                    val a = MysqDatabase.ip.postRequest(1000*15, sqlc)
                    if (a != null) {
                        sqlmemnory.exsql(
                            "delete from `updateResult` where  id=${it.getString(
                                0
                            )}"
                        )
                    }
                })
            })
        }.start()
    }

    //上傳ip位址
    fun uploadLocation() {
        val permission = PackageManager.PERMISSION_GRANTED ===
                packageManager.checkPermission(
                    ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION
                )
        if(!permission){return}
        Thread{
            TaskHandler.newInstance.runTaskOnce("uplocation",runner{
                try {
                    if (PublicBeans.SN.isEmpty() || PublicBeans.admin.isEmpty()) {
                        return@runner
                    }
                    Log.e("gps", "uploadLocation")
                    var gps = GpsUtil().lastKnownLocation
                    if (gps != null) {
                        val lat = gps.latitude
                        val lon = gps.longitude
                        Log.e("gps", "lat:$lat lon:$lon")
                    }
                    var ip=""
                    var lat = 0.0
                    var lon = 0.0
                    var company=""
                    if (gps != null) {
                        lat = gps.latitude
                        lon = gps.longitude
                    }
                    try {
                        ip =
                            "https://ifconfig.me/".getWebResource(1000*10)!!.split("ip_address\">")[1].split("</strong>")[0]
                        company ="https://www.ez2o.com/App/Net/IP".getWebResource(1000*10)!!.split("</strong><td>")[1].split("<tr")[0]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                   var a= MysqDatabase.ip.postRequest(15*1000,"REPLACE INTO `oglitememory`.deviceinformation (lan,lon,serial,admin,place,mcuversion,apkversion,type,ip,Dbversion,harwareversion,area,beta) " +
                            "VALUES (${lat},${lon},'${PublicBeans.SN}','${PublicBeans.admin}','${company}','${String(PublicBeans.MCU_NUMBER.hexToByte())}','${PublicBeans.versionCodes}','Ogenius','$ip','${PublicBeans.DataVersion}','${PublicBeans.hardWareVersion}','${Util_FileDowload.country}',${if(PublicBeans.beta) 1 else 0})"
                    )
                    Log.e("insert","$a")
                    MysqDatabase.ip.postRequest(15*1000,"insert ignore into  `oglitememory`.`register_day` (admin,serial) values ('"+PublicBeans.admin+"','"+PublicBeans.SN+"')")
                }catch (e:Exception){e.printStackTrace()}
            })
        }.start()
    }

    //uploadErrorMessage
    fun uploadError(){
        Thread {
            TaskHandler.newInstance.runTaskOnce("ErrorMessageJson", runner {
                Looper.prepare()
                sqlmemnory.exsql("delete from ErrorMessageJson where rowid <= (select MAX(rowid-1000)) from `ErrorMessageJson`) ")
                for (i in "ErrorMessageJson".listObject(10)) {
                    val res="https://bento2.orange-electronic.com/sendSES".postRequest(10*1000, i.json.stringToUnicode()!!)
                    if (res !== null) {
                        i.name.deleteObject("ErrorMessageJson")
                    }
                }
                Looper.loop()
            })
        }.start()
    }

    //------------------------------------------------------------------------多國語言資料庫------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    val languageDB: JzSqlHelper
        get() {
            if (templanDB == null) {
                templanDB =
                    JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "templanDB.db")
                templanDB!!.close()
                templanDB!!.init_ByAsset("alllan.db")
                templanDB!!.create()
                return templanDB!!
            } else {
                return templanDB!!
            }
        }
    var templanDB: JzSqlHelper? = null
    val onlinelanDB: JzSqlHelper by lazy {
        JzSqlHelper(
            JzActivity.getControlInstance().getRootActivity(),
            "onlinelanDB.db"
        )
    }
    val testLan: Boolean
        get() {
            return getControlInstance().getPro("testlan", false)
        }
}
