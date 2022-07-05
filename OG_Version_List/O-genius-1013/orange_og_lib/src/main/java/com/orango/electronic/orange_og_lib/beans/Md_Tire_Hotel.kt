package com.orango.electronic.orange_og_lib.beans

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import java.text.SimpleDateFormat
import java.util.*

class Md_Tire_Hotel(){
    companion object{
        var domain="${serverRout}/tireHotelApi"
        //最後一次紀錄數量
        var lastCount get() = JzActivity.getControlInstance().getPro("lastcount",0)
        set(value) {JzActivity.getControlInstance().setPro("lastcount",value)}
        //取得Schedule數量
        fun getScheduleCount():Int?{
            val map: MutableMap<String, String> = mutableMapOf()
            map["account"]=PublicBean.admin
            map["request"] = "GetScheduleCount"
            map["time-start"] = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(Date())
            map["time-stop"] = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(Date().calDay(+1))
            val data= domain.postRequest(1000*10,Gson().toJson(map))
            if(data==null){
                return null
            }else{
                val map2:MutableMap<String,String> = Gson().fromJson(data,object :TypeToken<MutableMap<String,String>>(){}.type)
                return if(map2["count"]?.toInt()!=null){
                    if(lastCount!=map2["count"]!!.toInt()){
                        lastCount=map2["count"]!!.toInt()
                        JzActivity.getControlInstance().getHandler().post {
                            val page=JzActivity.getControlInstance().findFragByTag("Frag_home")
                            if(page is JzFragement){
                                page.viewInit()
                            }
                            OgPublic.getInstance.playBeepSoundAndVibrate() }
                    }
                    map2["count"]?.toInt()
                }else{
                    null
                }
            }
        }
        //取得Schedule
        fun getSchedule(startDate:Date=Date()):String?{
            val map: MutableMap<String, String> = mutableMapOf()
            map["request"] = "GetSchedule"
            map["account"] = PublicBean.admin
            map["time-start"] = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(startDate)
            map["time-stop"] =
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(startDate.calDay(1))
            val data =domain.postRequest(
                    1000 * 60,
                    Gson().toJson(map)
                )
            return data
        }
    }
}

fun Date.calDay(a: Int): Date {
    val rightNow = Calendar.getInstance()
    rightNow.time = this
    rightNow.add(Calendar.DAY_OF_YEAR, a)
    return rightNow.time
}