package com.orange.og_lite.beans

import com.google.gson.Gson
import com.orange.og_lite.Util.Util_FileDowload
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.jzutil.storeObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

class FileJsonVersion(var country:String,var type:String="OG") :Serializable{
    var mmyVersion = "no"
    var lanVersion =   "no"
    var bleVersion =   "no"
    var mcuVerion =   "no"
    var apkVersion =   "no"
    var bootloaderVersion = "1.0"
    var s19List = mutableMapOf<String,String>()
    var s18List = mutableMapOf<String,String>()
    var obdList = mutableMapOf<String,String>()
    companion object{
        fun getLocal():FileJsonVersion{
            val a= "DataListVersion".getObject<FileJsonVersion>()
            return a ?: FileJsonVersion(Util_FileDowload.country)
        }
    }
}


fun FileJsonVersion.storeLocal(){
    this.storeObject("DataListVersion")
}

fun FileJsonVersion.storeOnline(){
    this.storeObject("OnlineDataVersion")
}