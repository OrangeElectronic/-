package com.orange.tpms.ue.kt_frag


import android.app.Dialog
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.utils.ObdCommand.Companion.setScreenSleepTime
import com.orango.electronic.orange_og_lib.Callback.Hanshake_C
import com.orango.electronic.orange_og_lib.Callback.Update_C
import com.orango.electronic.orange_og_lib.Callback.Version_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.openTpms
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_splash.view.*
import java.io.File
import java.lang.Thread.sleep


/**
 * A simple [Fragment] subclass.
 *
 */
class kt_splash : JzFragement(R.layout.frag_splash), Hanshake_C, Update_C, Version_C {
    companion object{
        var isHella=false
    }
    override fun viewInit() {
        openTpms()
        Log.e("kt_splash","kt_splash")
        rootview.textView54.text = "${OgPublic.versionCodes}"
        rootview.fixLanguage()
        Thread {
            Cmd_Og.HandShake(this)
        }.start()
        if(isHella){
            rootview.backgrview.setBackgroundResource(R.mipmap.hella)
        }
        try{
            setScreenSleepTime(JzActivity.getControlInstance().getPro("sleep",180)*1000,JzActivity.getControlInstance().getRootActivity())
        }catch (e:Exception){}
    }

    fun ListenFinish() {
        Thread {
            Cmd_Og.hardVersion
            sleep(1000)
            while(!JzActivity.getControlInstance().isFrontDesk()){sleep(100)}
            handler.post {
                JzActivity.getControlInstance().setUpActionBar(Frag_Manager())
                JzActivity.getControlInstance().toggleActionBar(true)
            }
        }.start()
    }

    override fun version(a: String, result: Boolean) {
        if (result) {
            JzActivity.getControlInstance().setPro("Version", a)
            Log.e("版本號", a)
            ListenFinish()
        } else {
            Cmd_Og.HandShake(this)
        }
    }

    override fun Updateing(progress: Int) {
        handler.post {
            try {
                JzActivity.getControlInstance().showDiaLog(false, false, object : SetupDialog(R.layout.update_dialog) {
                    var temppass = ""
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {

                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.findViewById<TextView>(R.id.tit).text = JzActivity.getControlInstance().getRootActivity().resources.jzString(R.string.app_updating) + "$progress%"
                    }
                },"update_dialog")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun Finish(a: Boolean) {
        if (a) {
            handler.post {
                JzActivity.getControlInstance().closeDiaLog()
                val intent = Intent(JzActivity.getControlInstance().getRootActivity().applicationContext, KtActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        } else {
            Cmd_Og.HandShake(this)
            Cmd_Og.GetHard()
        }
    }

    var fal = 0
    override fun result(position: Int) {
        when (position) {
            1 -> {
                handler.post {
                    JzActivity.getControlInstance()
                        .showDiaLog(false, false, object : SetupDialog(R.layout.update_dialog) {
                            override fun dismess() {

                            }

                            var temppass = ""
                            override fun keyevent(event: KeyEvent): Boolean {
                                if (event.action == KeyEvent.ACTION_UP) {
                                    Log.e("keycode", "" + event.keyCode)
                                    if (event.keyCode == 19 || event.keyCode == 20 || event.keyCode == 21 || event.keyCode == 22 || event.keyCode == 66) {
                                        if (event.keyCode == 19) {
                                            temppass = ""
                                        }
                                        temppass += "${event.keyCode}"
                                        if (temppass == "1920212266") {
                                            val intent = Intent(Settings.ACTION_SETTINGS)
                                            JzActivity.getControlInstance().getRootActivity().startActivity(intent)
                                        }
                                        if (temppass.length > 20) {
                                            temppass = ""
                                        }
                                        Log.e("keycode", "" + temppass)
                                    }
                                }
                                return false
                            }

                            override fun setup(rootview: Dialog) {

                            }
                        },"update_dialog")
                }

                var mcu = if(File(activity!!.applicationContext.filesDir.path + "/update.x2").exists()) "init" else "no"
                if (fal > 3) {
                    mcu = "no"
                    Log.e("update", "update重臨開始")
                }
                Cmd_Og.WriteBootloader(JzActivity.getControlInstance().getRootActivity(), 132, mcu, this)
                fal+=1
            }
            -1 -> {
                Cmd_Og.HandShake(this)
            }
            2 -> {
                Cmd_Og.GetVerion(this)
            }
        }
    }
}
