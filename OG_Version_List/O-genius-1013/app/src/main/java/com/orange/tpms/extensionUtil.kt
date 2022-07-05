package com.orange.tpms

import android.content.pm.PackageManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.Util.FileDowload
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ExtensionUtil(){
   companion object{
       /**
        * 判斷app是否已被下載
        * */
       fun appInstalledOrNot(uri: String): Boolean {
           val pm: PackageManager = JzActivity.getControlInstance().getRootActivity().packageManager
           try {
               pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
               return true
           } catch (e: PackageManager.NameNotFoundException) {
           }
           return false
       }



   }
}
/**
 * Glitter請求
 * */
     class RequestFun(var routName:String,var functionName:String){
    fun request(data:MutableMap<String,Any>):MutableMap<String,Any>?{
        val map:MutableMap<String,Any> = mutableMapOf()
        map["routName"]=routName
        map["functionName"]=functionName
        map["data"]=data
        val string="${FileDowload.serverRout}/PostApi".postRequest(20*1000, Gson().toJson(map))
        return if(string==null){
            null
        }else{
            Gson().fromJson(string,object :TypeToken<MutableMap<String,Any>>(){}.type)
        }
    }
 }
fun postRequest2(
    url: String,
    timeout: Int,
    dataArray: ByteArray,
    uploadProgress: (a: Int) -> Unit = {},
    downloadProgress: (a: Int) -> Unit = {}
): String? {
    try {
        val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = timeout
        conn.readTimeout = timeout
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.doInput = true;
        conn.setRequestProperty("Content-Type","text/plain")
        conn.setRequestProperty("User-Agent","PostmanRuntime/7.26.8")
        conn.setRequestProperty("Connection","keep-alive")

        val inputStream = dataArray.inputStream()
        val wr = DataOutputStream(conn.outputStream)
        val buffer = ByteArray(1024)
        val length = inputStream.available()
        var uploaded = 0L
        inputStream.use {
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read
                wr.write(buffer, 0, read)
                uploadProgress((uploaded * 100 / length).toInt())
                wr.flush()
            }
        }
        wr.close()
        val reader = DataInputStream(conn.inputStream)
        var strBuf = ""
        var downLoad = 0L
        reader.use {
            var read: Int
            while (reader.read(buffer).also { read = it } != -1) {
                downLoad += read
                strBuf += String(buffer.copyOfRange(0, read))
                if (reader.available() > 0) {
                    downloadProgress((downLoad * 100 / reader.available()).toInt())
                }

            }
        }
        reader.close()
        return strBuf.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
//POST String
fun String.postRequest2(
    timeout: Int,
    postString: String,
    uploadProgress: (a: Int) -> Unit = {},
    downloadProgress: (a: Int) -> Unit = {}
): String? {
    return postRequest2(
        this,
        timeout,
        postString.toByteArray(),
        uploadProgress,
        downloadProgress
    )
}
