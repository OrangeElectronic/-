package com.orange.og_lite.beans

import com.orange.jzchi.jzframework.JzActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString
import java.math.BigDecimal

class SensorData {
    var id = ""

    var kpa = 0F
        get() {
            when (JzActivity.getControlInstance().getPro("Pre", 1)) {
                0 -> {
                    val c = BigDecimal(field * 0.145F.toDouble())
                    val f1 = c.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
                    return f1
                }
                1 -> {
                    return field
                }
                2 -> {
                    val c = BigDecimal(field * 0.01F.toDouble())
                    val f1 = c.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
                    return f1
                }
            }
            return field
        }
    var c =0
        get(){
            when(JzActivity.getControlInstance().getPro("Tem",0)){
                0->{
                    return field
                }
                1->{
                    return (field*9/5)+32
                }
            }
            return field}
    var idcount = 0
    var bat = ""

    var vol = 0
    var success = false
    var 有無胎溫 = false
    var 有無電池 = false
    var 有無電壓 = false


companion object{
    fun getTem():String{
        when(JzActivity.getControlInstance().getPro("Tem",0)){
            0->{return "jz.375".getFix()+":"}
            1->{return "jz.376".getFix()+":"}
        }
        return "jz.375".getFix()+":"
    }
    fun getPre():String{
        when(JzActivity.getControlInstance().getPro("Pre",1)){
            0->{return "jz.377".getFix()+":"}
            1->{return "jz.379".getFix()+":"}
            2->{return "jz.378".getFix()+":"}
        }
        return "379".getFix()+":"
    }
}
}
