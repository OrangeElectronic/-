package com.orango.electronic.orange_og_lib.Frag


import android.app.Dialog
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.orange_og_lib.Callback.Register_C
import com.orango.electronic.orange_og_lib.HttpCommand.Fuction
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.WifiConnector.WifiConnectHelper
import kotlinx.android.synthetic.main.fragment_frag__register.view.*
import java.util.*

//FileDowload.HaveData(JzActivity.getControlInstance().getRootActivity(), this)
class Frag_Register :
    JzFragement(R.layout.fragment_frag__register), Register_C {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.cancel.setOnClickListener {
            JzActivity.getControlInstance().goBack()
        }
        rootview.next.setOnClickListener {
            register()
        }
        email = rootview.findViewById(R.id.email)
        password = rootview.findViewById(R.id.password)
        repeatpassword = rootview.findViewById(R.id.repeatpassword)
        serialnumber = rootview.findViewById(R.id.serialnumber)
        firstname = rootview.findViewById(R.id.firstname)
        lastname = rootview.findViewById(R.id.lastname)
        company = rootview.findViewById(R.id.company)
        phone = rootview.findViewById(R.id.phone)
        streat = rootview.findViewById(R.id.streat)
        city = rootview.findViewById(R.id.city)
        state = rootview.findViewById(R.id.state)
        zpcode = rootview.findViewById(R.id.zpcode)
        Store = rootview.findViewById(R.id.spinner6)
        AreaSpinner = rootview.findViewById(R.id.spinner5)
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        Arealist2.add(resources!!.jzString(R.string.Distributor))
        Arealist2.add(resources!!.jzString(R.string.Retailer))
        val arrayAdapter = ArrayAdapter<String>(JzActivity.getControlInstance().getRootActivity(), R.layout.spinner, Arealist)
        val arrayAdapter2 = ArrayAdapter<String>(JzActivity.getControlInstance().getRootActivity(), R.layout.spinner, Arealist2)
        AreaSpinner.adapter = arrayAdapter
        Store.adapter = arrayAdapter2
        rootview.linear.setOnClickListener { JzActivity.getControlInstance().getRootActivity().HideKeyBoard() }
    }

    override fun WifiError() {
        handler.post {
            WifiConnectHelper().switchWifi(JzActivity.getControlInstance().getRootActivity(), false)
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance().toast(resources.jzString(R.string.nointernet))
        }
    }

    override fun Result(a: Boolean) {
        handler.post { JzActivity.getControlInstance().closeDiaLog() }
        if (a) {
            handler.post {
                JzActivity.getControlInstance().showDiaLog(false, false, object :
                    SetupDialog(R.layout.update_dialog) {
                    override fun dismess() {

                    }

                    override fun keyevent(event: KeyEvent): Boolean {
                        return false
                    }

                    override fun setup(rootview: Dialog) {
                    }
                }, "update_dialog")
            }
            JzActivity.getControlInstance().setPro("admin",email.text.toString())
            JzActivity.getControlInstance().setPro("password",password.text.toString())
            OgPublic.signSuccess!!.result(true)
        } else {
            handler.post {
                JzActivity.getControlInstance().toast(resources!!.jzString(R.string.be_register))
            }
        }
    }
    lateinit var AreaSpinner: Spinner
    lateinit var Store: Spinner
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatpassword: EditText
    lateinit var serialnumber: EditText
    lateinit var firstname: EditText
    lateinit var lastname: EditText
    lateinit var company: EditText
    lateinit var phone: EditText
    lateinit var streat: EditText
    lateinit var city: EditText
    lateinit var state: EditText
    lateinit var zpcode: EditText
    var Arealist = ArrayList<String>()
    var Arealist2 = ArrayList<String>()
    fun register() {
        val email = email.text.toString()
        val password = password.text.toString()
        val repeatpassword = repeatpassword.text.toString()
        val firstname = firstname.text.toString()
        val lastname = lastname.text.toString()
        val company = company.text.toString()
        val phone = phone.text.toString()
        val streat = streat.text.toString()
        val city = city.text.toString()
        val state = state.text.toString()
        val country = rootview.spinner5.selectedItem.toString()
        val storetype = Store.selectedItem.toString()
        val zpcode = zpcode.text.toString()
        if (!password.equals(repeatpassword)) {
            JzActivity.getControlInstance().toast(resources.jzString(R.string.confirm_password))
            return
        }
        if(email.isEmpty()){
            return
        }
        JzActivity.getControlInstance()
            .showDiaLog(false, false, object : SetupDialog(R.layout.normal_dialog) {
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                }
            }, "normal_dialog")
        Thread {
            if (storetype.equals(resources!!.jzString(R.string.Distributor))) {
                Fuction.Register(
                    email,
                    password,
                    "" + OgPublic.deviceID,
                    "Distributor",
                    company,
                    firstname,
                    lastname,
                    phone,
                    state,
                    city,
                    streat,
                    zpcode,
                    this,
                    "OGenius"
                    , country
                )
            } else {
                Fuction.Register(
                    email,
                    password,
                    "" + OgPublic.deviceID,
                    "Retailer",
                    company,
                    firstname,
                    lastname,
                    phone,
                    state,
                    city,
                    streat,
                    zpcode,
                    this,
                    "OGenius"
                    , country
                )
            }
        }.start()
    }

}

interface register {
    fun result(a: Boolean)
}
