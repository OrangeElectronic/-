package com.orange.tpms.ue.kt_frag


import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jianzhi.glitter.GlitterActivity
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.Frag.Frag_Edit_StartDate
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.utils.WifiUtils
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__setting.view.*
import kotlinx.android.synthetic.main.reset.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Setting : RootFragement(R.layout.fragment_frag__setting) {
    override fun viewInit() {
        rootview.fixLanguage()
        btn.add(rootview.bt_favorite)
        btn.add(rootview.bt_wifi)
        btn.add(rootview.bt_ble)
        btn.add(rootview.bt_unit)
        btn.add(rootview.bt_auto_lock)
        btn.add(rootview.bt_lan)
        btn.add(rootview.bt_sounds)
        btn.add(rootview.bt_information)
        btn.add(rootview.bt_reset)
        btn.add(rootview.bt_update)
        btn.add(rootview.bt_policy)
        btn.add(rootview.bt_enginer)
        btn.add(rootview.bt_time)
        Ttn.add(rootview.tv_my_favourite)
        Ttn.add(rootview.tv_wifi)
        Ttn.add(rootview.tv_blue_bud)
        Ttn.add(rootview.tv_unit)
        Ttn.add(rootview.tv_auto_lock)
        Ttn.add(rootview.tv_language)
        Ttn.add(rootview.tv_sounds)
        Ttn.add(rootview.tv_information)
        Ttn.add(rootview.tv_system_reset)
        Ttn.add(rootview.tv_system_update)
        Ttn.add(rootview.tv_privacy_policy)
        Ttn.add(rootview.tv_enginer)
        Ttn.add(rootview.timetext)
//        Ttn.add(rootview.reset_password)
        Ttn.add(rootview.logOuttext)
        val connetedWifi = WifiUtils.getInstance(activity).connectedSSID
        rootview.tv_conneted_wifi.text = connetedWifi
        rootview.bt_time.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Edit_TimeZone(), "Frag_Edit_TimeZone", true)
        }
        rootview.bt_enginer.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Enginer(), "Frag_Enginer", true)
        }
        rootview.bt_update.setOnClickListener {
            PublicBean.Update = false
            JzActivity.getControlInstance().changePage(Frag_Update(), "Frag_Update", true)
        }
        rootview.bt_favorite.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_SettingFavorite(), "Frag_SettingFavorite", true)
        }
        rootview.bt_wifi.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Setting_Wifi(), "Frag_Setting_Wifi", true)
        }
        rootview.bt_lan.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Setting_Lan(), "Frag_Setting_Lan", true)
        }
        rootview.bt_unit.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Setting_Unit(), "Frag_Setting_Unit", true)
        }
        rootview.bt_auto_lock.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Auto_Lock(), "Frag_Auto_Lock", true)
        }
        rootview.bt_sounds.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Sounds(), "Frag_Sounds", true)
        }
        rootview.bt_information.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Information(), "Frag_Information", true)
        }
        rootview.bt_policy.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Policy(), "Frag_Policy", true)
        }
//        rootview.bt_reset_Password.setOnClickListener {
//            JzActivity.getControlInstance().changePage()
//        }
        rootview.bt_ble.setOnClickListener {
            JzActivity.getControlInstance()
                .showDiaLog(true, false, object : SetupDialog(R.layout.bledialog) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return true
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.findViewById<TextView>(R.id.no).setOnClickListener {
                            adapter.disable()
                            this@Frag_Setting.rootview.bleconnect.text =
                                resources!!.jzString(R.string.app_blue_bud_close)
                            JzActivity.getControlInstance().closeDiaLog()
                        }
                        rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
                            adapter.enable()
                            this@Frag_Setting.rootview.bleconnect.text =
                                resources!!.jzString(R.string.app_blue_bud_open)
                            JzActivity.getControlInstance().closeDiaLog()
                        }
                    }

                }, "bledialog")

        }
        rootview.bt_reset.setOnClickListener {
            JzActivity.getControlInstance()
                .showDiaLog(true, false, object : SetupDialog(R.layout.reset) {
                    override fun dismess() {

                    }
                    override fun keyevent(event: KeyEvent): Boolean {
                        return true
                    }
                    override fun setup(rootview: Dialog) {
                        rootview.findViewById<TextView>(R.id.no).setOnClickListener {
                            JzActivity.getControlInstance().closeDiaLog()
                        }
                        rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
                            try {
                                arrayOf(GlitterActivity.instance().getExternalFilesDir("waitUpload")!!.absolutePath,GlitterActivity.instance().getExternalFilesDir("upload")!!.absolutePath).map {
                                    if( File(it).exists()){
                                        for( a in  File(it).listFiles()){
                                           a.delete()
                                        }
                                    }
                                }
                            }catch (e:Exception){e.printStackTrace()}
                            JzActivity.getControlInstance().closeDiaLog()
                            FileDowload.clearInit()
                            JzActivity.getControlInstance().setPro("admin", "nodata")
                            OgPublic.getInstance.resrart()
                        }
                    }
                }, "reset")
        }
        rootview.bt_signout.setOnClickListener {
            JzActivity.getControlInstance()
                .showDiaLog(true, false, object : SetupDialog(R.layout.reset) {
                    override fun dismess() {

                    }
                    override fun keyevent(event: KeyEvent): Boolean {
                        return true
                    }
                    override fun setup(rootview: Dialog) {
                        rootview.tit.text = "jz.111".getFix()+"?"
                        rootview.findViewById<TextView>(R.id.yes).text="jz.113".getFix()
                        rootview.findViewById<TextView>(R.id.no).setOnClickListener {
                            JzActivity.getControlInstance().closeDiaLog()
                        }
                        rootview.findViewById<TextView>(R.id.yes).setOnClickListener {
                            try {
                                arrayOf(GlitterActivity.instance().getExternalFilesDir("waitUpload")!!.absolutePath,GlitterActivity.instance().getExternalFilesDir("upload")!!.absolutePath).map {
                                    if( File(it).exists()){
                                        for( a in  File(it).listFiles()){
                                            a.delete()
                                        }
                                    }
                                }
                            }catch (e:Exception){e.printStackTrace()}
                            JzActivity.getControlInstance().closeDiaLog()
                            JzActivity.getControlInstance().setPro("admin", "nodata")
                            OgPublic.getInstance.resrart()
                        }
                    }
                }, "reset")
        }
        BleUpdate()
        JzActivity.getControlInstance().getRootActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    val adapter = BluetoothAdapter.getDefaultAdapter()
    var btn = ArrayList<View>()
    var Ttn = ArrayList<TextView>()


    override fun enter() {
        for (i in 0 until Ttn.size) {
            if (Ttn[i].isFocused) {
                btn[i].performClick()
            }
        }
    }

    fun BleUpdate() {
        val originalBluetooth = adapter != null && adapter.isEnabled
        if (originalBluetooth) {
            rootview.bleconnect.text = resources!!.jzString(R.string.app_blue_bud_open)
        } else {
            rootview.bleconnect.text = resources!!.jzString(R.string.app_blue_bud_close)
        }
    }
}
