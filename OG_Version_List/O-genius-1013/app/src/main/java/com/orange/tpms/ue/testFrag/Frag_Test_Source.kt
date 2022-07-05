package com.orange.tpms.ue.testFrag

import android.app.Dialog
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Util.VibMediaUtil
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import kotlinx.android.synthetic.main.frag_test_engineer.*
import kotlinx.android.synthetic.main.frag_test_engineer.view.*

class Frag_Test_Source : JzFragement(R.layout.frag_test_engineer) {
    var run=false
    lateinit var vibMediaUtil: VibMediaUtil
    var lastSuccess: Int
        get() {
            return JzActivity.getControlInstance().getPro("lastSuccess", 0)
        }
        set(value) {
            JzActivity.getControlInstance().setPro("lastSuccess", value)
        }
    var lastFalse: Int
        get() {
            return JzActivity.getControlInstance().getPro("lastFalse", 0)
        }
        set(value) {
            JzActivity.getControlInstance().setPro("lastFalse", value)
        }

    override fun viewInit() {
        vibMediaUtil = VibMediaUtil(activity)
        rootview.textView6.text = "上次成功紀錄-${lastSuccess}\n上次失敗紀錄-${lastFalse}"
        rootview.button10.setOnClickListener {
            run=!run
            rootview.button10.setText(if(run)  "結束測試" else "開始測試")
            if(run){
                lastFalse=0
                lastSuccess=0
                start()
            }
        }
    }

    fun start(){
        Thread{
            while(run){
                handler.post {
                    JzActivity.getControlInstance().showDiaLog( false, true, object : SetupDialog(R.layout.data_loading) {
                        override fun keyevent(event: KeyEvent): Boolean {
                            if(event.keyCode==4){
                                Cmd_Og.cancel=true
                            }
                            return false
                        }

                        override fun setup(rootview: Dialog) {
                        }

                        override fun dismess() {
                        }
                    },"data_loading")
                }
                val a = Cmd_Og.GetId(rootview.editText5.text.toString(), rootview.editText.text.toString())
                vibMediaUtil.playVibrate()
                if(a.success){
                    lastSuccess+=1
                }else{
                    lastFalse+=1
                }
                handler.post {
                    JzActivity.getControlInstance().closeDiaLog()
                    textView6.text = "上次成功紀錄-${lastSuccess}\n上次失敗紀錄-${lastFalse}"
                }
                Thread.sleep(2000)
            }
        }.start()
    }
}