package com.orango.electronic.orange_og_lib.Frag


import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.Callback.Sign_In_C
import com.orango.electronic.orange_og_lib.HttpCommand.Fuction
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.WifiConnector.WifiConnectHelper
import com.orango.electronic.orange_og_lib.WifiConnector.WifiUtils
import kotlinx.android.synthetic.main.activity_sign_in.view.*
import kotlinx.android.synthetic.main.data_loading.*


class Frag_Sign_in : JzFragement(R.layout.activity_sign_in), Sign_In_C {
    override fun viewInit() {
        rootview.fixLanguage()
        admin=rootview.findViewById(R.id.editText3)
        password=rootview.findViewById(R.id.editText4)
        (rootview.findViewById(R.id.button4) as Button).setOnClickListener {
            val admin=admin.text.toString()
            val password=password.text.toString()
            if(admin.isEmpty()||password.isEmpty()){
                JzActivity.getControlInstance().toast("jz.285".getFix())
                return@setOnClickListener
            }
            JzActivity.getControlInstance().showDiaLog(false,true, object :SetupDialog(R.layout.data_loading) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if(event.keyCode==4){
                    }
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.pass.visibility=View.VISIBLE
                }

            },"data_loading")
            Thread{
                TaskHandler.newInstance.runTaskOnce("signin", runner{
                    Fuction.ValidateUser(admin,password,this)
                })
            }.start()
        }
        (rootview.findViewById(R.id.textView26) as TextView).setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Reset_Ps(),"Frag_Reset_Ps",true)
        }
        (rootview.findViewById(R.id.imageView22) as ImageView).setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Reset_Ps(),"Frag_Reset_Ps",true)
        }
        rootview.bt_register.setOnClickListener {
            JzActivity.getControlInstance().changePage(Frag_Register(),"Frag_Register",true)
        }
    }

    override fun wifierror() {
        handler.post {
            JzActivity.getControlInstance().toast(resources.jzString(R.string.signfall))
            WifiUtils.getInstance(JzActivity.getControlInstance().getRootActivity()).closeWifi()
            JzActivity.getControlInstance().changePage(Frag_Wifi(
                object : connectBack {
                    override fun result(a: Boolean) {
                        if (a) {
                            JzActivity.getControlInstance().goBack()
                        }
                    }
                }),"Frag_Wifi",true)
        }
    }

    override fun result(a: Boolean) {
        handler.post { JzActivity.getControlInstance().closeDiaLog() }
        if(a){
            handler.post { JzActivity.getControlInstance().showDiaLog(false,false, object :SetupDialog(R.layout.update_dialog) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                   return false
                }

                override fun setup(rootview: Dialog) {
                }

            },"update_dialog")
                JzActivity.getControlInstance().setPro("admin",admin.text.toString())
                JzActivity.getControlInstance().setPro("password",password.text.toString())
                OgPublic.signSuccess!!.result(true)
            }
        }else{handler.post { JzActivity.getControlInstance().toast(resources!!.jzString(R.string.signfall)) } }
    }



    lateinit var admin:EditText
    lateinit var password:EditText
}
