package com.orango.electronic.orange_og_lib.beans

import androidx.lifecycle.MutableLiveData
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.getFix
import java.math.BigDecimal

class SensorData(var wheelPosition:String="",var wheelString: String="") {
    interface callback {
        fun run()
    }
    /*
    * 是否能執行操作
    * */
    var isBlock=false
    /*
        * 是否執行cancel
        * */
    var cancel=false
    var idViewModel = MutableLiveData<String>()
    var id: String = ""
        set(value) {
            field = value
            JzActivity.getControlInstance().getHandler().post { idViewModel.value = value }
        }

    init {
        JzActivity.getControlInstance().getHandler().post {
            idViewModel.observe(JzActivity.getControlInstance().getNowPage()!!, androidx.lifecycle.Observer {
                call.run()
            })
        }
    }
    //存入sensor的RFID
    var rfIdData=""
    //存入sensor的StorageData
    var storageData=""
    //NA/S2/S3
    var senesorType = "NA"
    var reKpa= 0F
    var kpa = 0F
        get() {
            var cas=JzActivity.getControlInstance().getPro("Pre", -1)
            if(cas==-1){
                when(FileDowload.country){
                    "EU"->{
                        cas=1
                    }
                    "US","NA"->{
                        cas=0
                    }
                    "TW"->{
                        cas=0
                    }
                    "JP"->{
                        cas=2
                    }
                }

            }
            when (cas) {
                0 -> {
                    val c = BigDecimal(field * 0.14F.toDouble())
                    val f1 = c.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
                    return f1
                }
                2 -> {
                    return field
                }
                1 -> {
                    val c = BigDecimal(field * 0.01F.toDouble())
                    val f1 = c.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
                    return f1
                }
            }
            return field
        }
    set(value) {
        reKpa=value
        field=value
    }
    var c = 0
        get() {
            var cas=JzActivity.getControlInstance().getPro("Tem", -1)
            if(cas==-1){
                cas = when(FileDowload.country){
                    "US","NA"->{
                        JzActivity.getControlInstance().setPro("Tem", 1)
                        1
                    }
                    else->{
                        JzActivity.getControlInstance().setPro("Tem", 0)
                        0
                    }
                }
            }
            when (cas) {
                0 -> {
                    return field
                }
                1 -> {
                    return (field * 9 / 5) + 32
                }
            }
            return field
        }
    var idcount = PublicBean.getIdcount()
    var bat = ""
        get() {
            return if (field == "1") "ok" else if(有無電池) "low" else "NA"
        }
    var vol:Float = 0F
    get() {
        val v = BigDecimal(field /10.toDouble())
        val f1 = v.setScale(1, BigDecimal.ROUND_HALF_UP).toFloat()
        return f1
    }
    var success = false
    var 有無胎溫 = false
    var 有無電池 = false
    var 有無電壓 = false
    var programResult = STATE_NORMAL

    companion object {
        var call=object :callback{
            override fun run() {
            }
        }
        var STATE_NORMAL = 0//正常状态
        var STATE_SUCCESS = 1//成功状态
        var STATE_FAILED = 2//失败状态
        fun getTem(): String {
            var cas=JzActivity.getControlInstance().getPro("Tem", -1)
            if(cas==-1){
                cas = when(FileDowload.country){
                    "US","NA"->{
                        JzActivity.getControlInstance().setPro("Tem", 1)
                        1
                    }
                    else->{
                        JzActivity.getControlInstance().setPro("Tem", 0)
                        0
                    }
                }
            }
            when (cas) {
                0 -> {
                    return "jz.375".getFix() + ":"
                }
                1 -> {
                    return "jz.376".getFix() + ":"
                }
            }
            return "jz.375".getFix() + ":"
        }

        fun getPre(): String {
            var cas=JzActivity.getControlInstance().getPro("Pre", -1)
            if(cas==-1){
                when(FileDowload.country){
                    "EU"->{
                        cas=1
                    }
                    "US","NA"->{
                        cas=0
                    }
                    "TW"->{
                        cas=0
                    }
                    "JP"->{
                        cas=2
                    }
                }
            }
            JzActivity.getControlInstance().setPro("Pre",cas)
            when (JzActivity.getControlInstance().getPro("Pre", 2)) {
                0 -> {
                    return "jz.377".getFix() + ":"
                }
                1 -> {
                    return "jz.378".getFix() + ":"
                }
                2 -> {
                    return "jz.379".getFix() + ":"
                }
            }
            return "jz.379".getFix() + ":"
        }
    }
}
