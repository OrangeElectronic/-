package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.os.Looper
import android.view.KeyEvent
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.Callback.Update_C
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Dialog.Dia_Check_WiFi
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString

class Dia_UpdateIng:SetupDialog(R.layout.update_dialog) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
     return false
    }

    override fun setup(rootview: Dialog) {
        Thread{
            Looper.prepare()
            Cmd_Og.hardVersion
            FileDowload.ckeckNewVersion()
            if(FileDowload.needUpdate()){
                FileDowload.ChechUpdate(JzActivity.getControlInstance().getRootActivity(),object :
                    Update_C {
                    override fun Finish(a: Boolean) {
                        Thread.sleep(1000)
                        JzActivity.getControlInstance().getHandler().post {
                            JzActivity.getControlInstance().closeDiaLog()
                            if (a){
                                JzActivity.getControlInstance().setPro("Firebasetitle","nodata")
                                OgPublic.getInstance.resrart()
                            }else{
                                JzActivity.getControlInstance().showDiaLog(false,false,Dia_Check_WiFi(),"Dia_Check_WiFi")
                            }
                        }
                    }

                    override fun Updateing(progress: Int) {
                        JzActivity.getControlInstance().getHandler().post {
                            rootview.findViewById<TextView>(R.id.tit).text = rootview.context.resources!!.jzString(R.string.app_updating) + "$progress%"
                        }
                    }
                })
            }else{
                JzActivity.getControlInstance().getHandler().post {
                    JzActivity.getControlInstance().toast("jz.214".getFix())
                    JzActivity.getControlInstance().closeDiaLog()
                }
            }
            Looper.loop()
        }.start()
    }

}