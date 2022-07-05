package com.orange.og_lite.beans

class Bs_SoftKeyboard
{
    var num1 = ArrayList<String>()
    var num2 = ArrayList<String>()
    var num3 = ArrayList<String>()
    var num4= ArrayList<String>()

    fun add(num1:String,num2:String,num3:String,num4:String)
    {
        this.num1.add(num1)
        this.num2.add(num2)
        this.num3.add(num3)
        this.num4.add(num4)
    }

        var Key_In=""
        var MainSelect=0
        var EndSelect=0
}