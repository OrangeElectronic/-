package com.orango.electronic.orange_og_lib.Dialog

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.jzutil.*
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import kotlinx.android.synthetic.main.dia_check_data.view.*
import kotlinx.android.synthetic.main.dia_check_data.view.linear
import kotlinx.android.synthetic.main.dia_check_data.view.spinner5
import kotlinx.android.synthetic.main.dia_check_data.view.spinner6
import java.util.ArrayList

class Dia_Check_Data(var item: Md_UserInforMation) : JzFragement(R.layout.dia_check_data) {
    data class Md_UserInforMation(
        var admin: String? = null,
        var storeType: String? = null,
        var company: String? = null,
        var firstNmae: String? = null,
        var lastName: String? = null,
        var telPhone: String? = null,
        var area: String? = null,
        var state: String? = null,
        var city: String? = null,
        var street: String? = null,
        var areaNO: String? = null,
        var country: String? = null,
        var countrySelect:String? = null
    )

    var Arealist = ArrayList<String>()
    var Arealist2 = ArrayList<String>()



    companion object {
        fun getUSer_InfoMation(): Md_UserInforMation? {
            val obj = "Md_UserInforMation".getObject<Md_UserInforMation>()
            val needUpdate = "needUpdate".getObject<Md_UserInforMation>()
            Thread {
                TaskHandler.newInstance.runTaskOnce("getUSer_InfoMation", runner {
                    if (needUpdate == null || obj == null) {
                        val map: MutableMap<String, Any> = mutableMapOf()
                        map["request"] = "getUserInfo"
                        map["account"] = PublicBean.admin
                        val result =
                            "${serverRout}/orangeJsonApi".postRequest(
                                60 * 1000,
                                Gson().toJson(map)
                            )
                        Log.e("Md_UserInforMation","Md_UserInforMation:${result}")
                        if (result != null) {
                            val jsonre: MutableMap<String, Any> = Gson().fromJson(
                                result,
                                object : TypeToken<MutableMap<String, Any>>() {}.type
                            )
                            if (jsonre["result"] == "true") {
                                val mdInfo: Md_UserInforMation = Gson().fromJson(
                                    jsonre["data"].toString(),
                                    object : TypeToken<Md_UserInforMation>() {}.type
                                )
                                mdInfo.storeObject("Md_UserInforMation")
                            }
                        }
                    } else {
                        TaskHandler.newInstance.runTaskOnce("needUpdate", runner {
                            val map: MutableMap<String, Any> = mutableMapOf()
                            map["request"] = "updateUserInfo"
                            map["account"] = PublicBean.admin
                            map["data"] = Gson().toJson(needUpdate).stringToUnicode()!!
                            val result =
                                "${serverRout}/orangeJsonApi".postRequest(
                                    60 * 1000,
                                    Gson().toJson(map)
                                )
                            if (result != null) {
                                val jsonre: MutableMap<String, Any> = Gson().fromJson(
                                    result,
                                    object : TypeToken<MutableMap<String, Any>>() {}.type
                                )
                                if (jsonre["result"] == "true") {
                                    "Md_UserInforMation".deleteObject()
                                    "needUpdate".deleteObject()
                                }
                            }
                        })
                    }
                })
            }.start()
            return obj
        }
    }

    override fun viewInit() {

        JzActivity.getControlInstance().toggleActionBar(false)
        rootview.fixLanguage()
        rootview.firstname.setText(item.firstNmae)
        rootview.lastname.setText(item.lastName)
        rootview.company.setText(item.company)
        rootview.phone.setText(item.telPhone)
        rootview.streat.setText(item.street)
        rootview.city.setText(item.city)
        rootview.state.setText(item.state)
        rootview.zpcode.setText(item.areaNO)
        rootview.counrty.setText(item.country)
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        Arealist2.add(resources!!.jzString(R.string.Distributor))
        Arealist2.add(resources!!.jzString(R.string.Retailer))
        val arrayAdapter = ArrayAdapter<String>(
            JzActivity.getControlInstance().getRootActivity(),
            R.layout.spinner,
            Arealist
        )
        val arrayAdapter2 = ArrayAdapter<String>(
            JzActivity.getControlInstance().getRootActivity(),
            R.layout.spinner,
            Arealist2
        )
        rootview.spinner5.adapter = arrayAdapter
        rootview.spinner6.adapter = arrayAdapter2
        rootview.linear.setOnClickListener {
            JzActivity.getControlInstance().getRootActivity().HideKeyBoard()
        }
        for (i in rootview.listView()) {
            if (i is EditText) {
                if (i.text.toString() == "NA") {
                    i.setText("")
                }
            }
        }
        rootview.next.setOnClickListener {
            for (i in rootview.listView()) {
                if (i is EditText) {
                    if (i.text.isEmpty()) {
                        JzActivity.getControlInstance().toast("jz.489".getFix())
                        return@setOnClickListener
                    }
                }
            }
            val a = Md_UserInforMation(
                PublicBean.admin,
                arrayOf("經銷商", "輪胎行")[Arealist2.indexOf(rootview.spinner6.selectedItem.toString())],
                rootview.company.text.toString(),
                rootview.firstname.text.toString(),
                rootview.lastname.text.toString(),
                rootview.phone.text.toString(),
                rootview.spinner5.selectedItem.toString(),
                rootview.state.text.toString(),
                rootview.city.text.toString(),
                rootview.streat.text.toString(),
                rootview.zpcode.text.toString(),
                rootview.counrty.text.toString(),
                item.countrySelect
            )
            a.storeObject("needUpdate")
            a.storeObject("Md_UserInforMation")
            JzActivity.getControlInstance().hideKeyBoard()
            OgPublic.getInstance.resrart()
        }

    }

}