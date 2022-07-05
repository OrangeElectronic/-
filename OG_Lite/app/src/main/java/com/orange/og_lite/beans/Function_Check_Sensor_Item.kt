package com.orange.og_lite.beans

class Function_Check_Sensor_Item
{
    var ID = ArrayList<String>()
    var Psi = ArrayList<String>()
    var C = ArrayList<String>()
    var BAT = ArrayList<String>()
    var VOLT = ArrayList<String>()
    var focus = 1
    fun add(ID:String,Psi:String,C:String,BAT:String,VOLT:String)
    {
        this.ID.add(ID)
        this.Psi.add(Psi)
        this.C.add(C)
        this.BAT.add(BAT)
        this.VOLT.add(VOLT)
    }
}