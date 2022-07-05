package com.orange.og_lite.beans

import android.widget.TextView
import java.util.ArrayList

class ObdBeans {
    var idcount = 8
    var Tire_img = ArrayList<TextView>()
    var OldSemsor = ArrayList<String>()
    var writeList = ArrayList<String>()
    var wheelPosition = ArrayList<String>()
    var NewSensor = ArrayList<String>()
    var state = ArrayList<Int>()
    var CanEdit = false

    fun add(OldSemsor: String, NewSensor: String, state: Int,wheelPosition:String) {
        this.OldSemsor.add(OldSemsor)
        this.NewSensor.add(NewSensor)
        this.state.add(state)
        this.wheelPosition.add(wheelPosition)
    }

    fun replaceOldSensor(a:String,b:String){
        for(i in 0 until NewSensor.size){
            if(NewSensor[i]==a){
                NewSensor[i]=b
            }
        }
    }

    fun clear() {
        OldSemsor.clear()
        NewSensor.clear()
        state.clear()
    }

    fun getReadable():Int{
        var i=0
        return wheelPosition.count()
    }


    fun getNewSensorReader():ArrayList<String>{
        return NewSensor
    }

    companion object {
        var PROGRAM_WAIT = 2
        var PROGRAM_SUCCESS = 0
        var PROGRAM_FALSE = 1
        var Selectfocus=0
        var SelectTire=-1
    }
}
