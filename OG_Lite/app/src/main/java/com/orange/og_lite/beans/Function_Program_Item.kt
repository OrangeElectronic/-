package com.orange.og_lite.beans

import android.widget.ImageView

class Function_Program_Item
{
    var rowcount = 6
    var idcount = 8
    var readable= arrayOf(false,false,false,false,false)
    var programId = ArrayList<String>()
    var state = ArrayList<Int>()
    var 溫度 = ArrayList<String>()
    var 壓力 = ArrayList<String>()
    var 電力 = ArrayList<String>()
    fun add(b:String, state: Int,tem:String,pre:String,bat:String)
    {
        溫度.add(tem)
        壓力.add(pre)
        電力.add(bat)
        programId.add(b)
        this.state.add(state)
    }

    companion object {
        var PROGRAM_WAIT = 2
        var PROGRAM_SUCCESS = 0
        var PROGRAM_FALSE = 1
    }
}