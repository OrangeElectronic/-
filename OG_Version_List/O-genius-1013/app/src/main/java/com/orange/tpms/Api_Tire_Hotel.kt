package com.orange.tpms

import android.util.Log
import com.example.jztaskhandler.runner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout

object Api_Tire_Hotel {
    fun request(plate: String, caller: requestBack) {
        Thread {
            val map: MutableMap<String, String> = mutableMapOf()
            map["request"] = "getSensorData"
            map["serial-Number"] = PublicBean.SerialNum
            map["plate-Number"] = plate
            val data = "${serverRout}/tireHotelApi".postRequest(
                1000 * 15,
                Gson().toJson(map)
            )
            if (data == null) {
                caller.reback(false)
            } else {
                val jsonMap: ArrayList<triggerModel> =
                    Gson().fromJson(data, object : TypeToken<ArrayList<triggerModel>>() {}.type)
                caller.reback(jsonMap)
            }
        }.start()
    }
    //取得合作類型
    var requestType="NA"
    fun getRequestID(callback:()->Unit){
        Thread{
            if(requestType!="NA"){callback()
                return@Thread
            }
            val results = "${serverRout}/SelectMysql".postRequest(
                20 * 1000,
                "SELECT hotelID FROM orange_userdata.new_user_infomation where account='${PublicBean.admin}'"
            )
            var data: ArrayList<MutableMap<String, Any>>? = null
            if (results != null) {
                Log.e("topMVersion", results)
                data = Gson().fromJson(results,
                    object : TypeToken<ArrayList<MutableMap<String, Any>>>() {}.type)
                requestType=data!![0]["hotelID"].toString()
                Log.e("topMVersion", data[0]["hotelID"].toString())
                callback()
            } else {
                Log.e("topMVersion", "getNull")
                callback()
            }
        }.start()

    }

}

interface requestBack {
    fun reback(a: Any)
}

class triggerModel(
    var id: String,
    var tem: String,
    var pre: String,
    var bat: String,
    var plateNumber: String,
    var location: String,
    var make: String,
    var model: String,
    var year: String,
    var wheelPosition: String,
    var serial: String,
    var sensorid: String,
    var time: String,
    var tire_depth: String
)