package com.orango.electronic.orange_og_lib.Frag


import android.app.Dialog
import android.view.KeyEvent
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.Callback.Reset_C
import com.orango.electronic.orange_og_lib.HttpCommand.Fuction
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__reset__ps.view.*

class Frag_Reset_Ps : JzFragement(R.layout.fragment_frag__reset__ps), Reset_C {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.button.setOnClickListener {
            val admin = rootview.editText2.text.toString()
            if (admin.isEmpty()) {
                JzActivity.getControlInstance().toast(resources!!.jzString(R.string.app_content_empty))
                return@setOnClickListener
            }
            JzActivity.getControlInstance()
                .showDiaLog(false, false, object : SetupDialog(R.layout.update_dialog) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                        rootview.findViewById<TextView>(R.id.tit).text =
                            resources!!.jzString(R.string.app_data_uploading)
                    }

                }, "update_dialog")
            Thread {
                TaskHandler.newInstance.runTaskOnce("ResetPassword", runner {
                    Fuction.ResetPassword(admin, this)
                })
            }.start()
        }
    }

    override fun Result(a: Boolean) {
        handler.post {
            JzActivity.getControlInstance().closeDiaLog()
            if (a) {
                JzActivity.getControlInstance().changePage(Frag_GoReset(),  "Frag_GoReset", false)
            } else {
                JzActivity.getControlInstance().toast(resources!!.jzString(R.string.nointernet))
            }
        }

    }
}
