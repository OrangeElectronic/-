package com.orange.tpms.model

import android.widget.TextView
import com.orango.electronic.orange_og_lib.beans.SensorData
import java.util.ArrayList

class ObdBeans {
    var rowcount = 8
    var idcount = 8
    //選擇的欄位
    var selectRows=-1
    var Tire_img = ArrayList<TextView>()
    var OldSemsor = ArrayList<SensorData>()
    var wheelPosition = ArrayList<String>()
    var NewSensor = ArrayList<SensorData>()
    var state = ArrayList<Int>()
    var CanEdit = false

    fun add(OldSemsor: SensorData, NewSensor: SensorData, state: Int, wheelPosition:String) {
        this.OldSemsor.add(OldSemsor)
        this.NewSensor.add(NewSensor)
        this.state.add(state)
        this.wheelPosition.add(wheelPosition)
    }

    fun replaceOldSensor(a: SensorData, b: SensorData){
        for(i in 0 until NewSensor.size){
            if(NewSensor[i].id==a.id){
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


    fun getNewSensorReader():ArrayList<SensorData>{
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
