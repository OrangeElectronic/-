package com.orange.tpms

import android.util.Log
import com.google.gson.Gson
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.jzutil.postRequest
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URL

object testMain {
    var s19List = mutableMapOf<String,String>()
    var s18List = mutableMapOf<String,String>()
    @JvmStatic
    fun main(args: Array<String>) {
//        val file = File("test.csv")
//        val fw = FileWriter(file)
//        val bw = BufferedWriter(fw)
//        bw.write("Domain,Total")
//        bw.newLine();
//        bw.write("\nApplication,Total")
//        bw.newLine();
//        bw.close()
//        fw.close()
//        val map:MutableMap<String,Any> = mutableMapOf()
//        map["routName"]="PublicLogic"
//        map["functionName"]="sendSensorData"
//        val sensorList:ArrayList<MutableMap<String,Any>> = arrayListOf()
//        for (a in 0 until 4){
//            val sensorData:MutableMap<String,Any> = mutableMapOf()
//            sensorData["id"]="12345678"
//            sensorData["tem"]="28"
//            sensorData["kpa"]="123"
//            sensorList.add(sensorData)
//        }
//        val data:MutableMap<String,Any> = mutableMapOf()
//        data["sensorList"]=sensorList
//        map["data"]=data
//        println( "http://127.0.0.1/PostApi".postRequest(3000, Gson().toJson(map)))

//        val checkSum= "00 0C 00 13 01 00 00 00 20 60 00 E2 00 47 48 E1 A2 DB 0A 41 03 56 78".replace(" ","").hexToByte().sum()
//
//
//        println(   Integer.toHexString(checkSum))
//        val bytes:ByteArray = byteArrayOf((0x32).toByte(),(0xB9).toByte())
//        println("\\u53f0\\u7063\\u002d\\u4e09\\u7fa9\\u9109\\u5c16\\u8c50\\u516c\\u8def\\u0033\\u0030\\u0038".unicodeToString())
//        System.out.println(  (bytes[1].toInt() and 0xFF) - (bytes[0].toInt() and 0xFF))
        //Log.e("filterCaptcha","ssasasa")
//        "https://ifconfig.me/".getWebResource(1000*10)!!.split("ip_address\">")[1].split("</strong>")[0]
//        s18List["sss"]="ss"
//        s19List["sss"]="ss"
//        try {
//            // UrlRequest.ignoreSsl()
//            val conn = URL("${serverRout}/Orange%20Cloud/Beta/Drive/OG/APP%20Software/237.apk").openConnection() as HttpURLConnection
//            conn.connectTimeout = 1000 * 10
//            val `is` = conn.inputStream
//            val fos = FileOutputStream("test.apk")
//            val contentsize=conn.contentLength;
//            Log.e("contentSize","${contentsize}")
//            System.out.println("contentSize$contentsize")
//            val bufferSize = 8192
//            var prread = 0.0
//            val buf = ByteArray(bufferSize)
//            while (true) {
//                val read = `is`.read(buf)
//                prread += read.toDouble()
//                if (read == -1) {
//                    break
//                }
//                fos.write(buf, 0, read)
//            }
//            `is`.close()
//            fos.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("錯誤", e.message)
//        }
//        System.out.println("https://ifconfig.me/".getWebResource(1000*10)!!)
    }
    fun contentSize(){
        try {
            // UrlRequest.ignoreSsl()
            val conn = URL("${serverRout}/Orange%20Cloud/Beta/Drive/OG/APP%20Software/237.apk").openConnection() as HttpURLConnection
            conn.connectTimeout = 1000 * 10
            val `is` = conn.inputStream
            val fos = FileOutputStream(JzActivity.getControlInstance().getRootActivity().applicationContext.filesDir.path + "/sasa.srec")
            val contentsize=conn.contentLength;
            Log.e("contentSize","$contentsize")
            System.out.println("contentSize->$contentsize")
            val bufferSize = 8192
            var prread = 0.0
            val buf = ByteArray(bufferSize)
            while (true) {
                val read = `is`.read(buf)
                prread += read.toDouble()/1024/1024
                if (read == -1) {
                    break
                }
                fos.write(buf, 0, read)
            }
            System.out.println("contentSizeProgress->$prread")
            `is`.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("錯誤", e.message.toString())
        }
    }
    private fun filterCaptcha(s: String): String? {
        val temp=s.replace(" ","-").replace("[^A-Z0-9-]".toRegex(), "?")
        if(checkCount(temp)==1 && !temp.contains("?")){
            return temp}
        return null
    }
    fun checkCount(a:String):Int{
        var count=0
        for(i in a){
            if(i=="-".elementAt(0)){count+=1}
        }
        return count
    }
}
