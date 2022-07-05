package com.orango.electronic.orange_og_lib.beans

import com.orango.electronic.jzutil.getObject
import com.orango.electronic.jzutil.storeObject
import com.orango.electronic.orange_og_lib.Util.FileDowload
import java.io.Serializable
import java.lang.Exception

class FileJsonVersion(var country:String,var type:String="OG") :Serializable{
    var mmyVersion = "no"
    var lanVersion =   "no"
    var mcuVerion =   "no"
    var apkVersion =   "no"
    var s19List = mutableMapOf<String,String>()
    var s18List = mutableMapOf<String,String>()
    var obdList = mutableMapOf<String,String>()
    var obdList2 = mutableMapOf<String,String>()
    companion object{
        fun getLocal():FileJsonVersion{
            try {
                return "DataListVersion".getObject<FileJsonVersion>() ?: FileJsonVersion(FileDowload.country)
            }catch (e:Exception){e.printStackTrace()
            return FileJsonVersion(FileDowload.country)}
        }
    }
}


fun FileJsonVersion.storeLocal(){
    this.storeObject("DataListVersion")
}

fun FileJsonVersion.storeOnline(){
    this.storeObject("OnlineDataVersion")
}