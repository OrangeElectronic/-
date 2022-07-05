package com.example.customerlibrary.beans

class Bs_Contact(var admin:String,var pick:String,var head:String,var needRead:Int)
interface updateContact{
    fun update(isBt:Boolean,success:Boolean)
}