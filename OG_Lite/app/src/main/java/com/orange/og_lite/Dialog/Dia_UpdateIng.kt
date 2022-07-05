package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import android.widget.TextView
import com.orange.og_lite.Util.fixLanguage
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.callback
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.callback.Update_C
import com.orange.og_lite.Util.jzString
class Dia_UpdateIng:SetupDialog(R.layout.update_dialog) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
     return false
    }

    override fun setup(rootview: Dialog) {
        Thread{
            TaskHandler.newInstance.runTaskOnce("updateing", runner{
                Util_FileDowload.ChechUpdate(JzActivity.getControlInstance().getRootActivity(),object :
                    Update_C {
                    override fun Finish(a: Boolean) {
                        JzActivity.getControlInstance().getHandler().post {
                            JzActivity.getControlInstance().closeDiaLog("Dia_UpdateIng")
                            if (a){
                                JzActivity.getControlInstance().setPro("Firebasetitle","nodata")
                            }
                        }
                    }

                    override fun Updateing(progress: Int) {
                        JzActivity.getControlInstance().getHandler().post {
                            rootview.findViewById<TextView>(R.id.tit).text = rootview.context.resources.jzString(R.string.app_updating) + "$progress%"
                        }
                    }
                })
            })
        }.start()

    }

}