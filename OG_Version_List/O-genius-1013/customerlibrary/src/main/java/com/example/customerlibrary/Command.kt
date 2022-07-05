package com.example.customerlibrary

import android.os.Handler
import com.example.customerlibrary.beans.Bs_Contact
import com.example.customerlibrary.beans.Messageitem
import com.example.customerlibrary.beans.PublicBeans
import com.example.customerlibrary.beans.updateContact
import com.example.customerlibrary.callback.AddLove_C
import com.example.customerlibrary.callback.GetPost_C
import com.example.customerlibrary.callback.Socket_C
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.tool.UnicodeUtil
import com.orango.electronic.jzutil.addParameters
import com.orango.electronic.jzutil.getWebResource
import org.jsoup.Jsoup
import java.lang.Thread.sleep

class Command {


    companion object{
        private var instance:Command ? = null
        var server = "http://192.168.0.13:8080"
        fun getInstance():Command{
            if(instance==null){
                instance=Command()
            }
            return instance!!
        }
    }
    var handler= Handler()
    fun GetMessageItem(take: String, it: Messageitem, caller: GetPost_C) {
        Thread {
            val originsize = it.id.size
            try {
                object : Socket_C(115) {
                    override fun init() {
                        writeUTF(take)
                        var a = readUTF()
                        while (a != "完畢") {
                            it.add(
                                a,
                                readUTF(),
                                readUTF(),
                                readUTF(),
                                readUTF(),
                                readUTF(),
                                readUTF()
                            )
                            a = readUTF()
                        }
                        if (it.id.size == originsize) {
                            it.button = true
                        }
                        it.success = true
                        handler.post { caller.Result(true) }

                    }
                }.start()
            } catch (e: Exception) {
                sleep(1000)
                e.printStackTrace()
                it.success = false
                handler.post { caller.Result(false) }
            }
        }.start()
    }
//    fun GetMail(take: String, it: Messageitem, ad: String, caller: GetPost_C) {
//        Thread{
//            val originsize = it.admin.size
//            try {
//                val re = "${server}/Api_Customer_Message".addParameters(
//                    arrayOf("serial", "id"),
//                    arrayOf(
//                        ShareBeans.serialNumber,
//                        if (message.size >= 50) message.get(message.size - 1).id else "-1"
//                    )
//                ).getWebResource(5000)
//            } catch (e: Exception) {
//                sleep(1000)
//                e.printStackTrace()
//                it.success = false
//                handler.post { caller.Result(false) }
//            }
//        }.start()
//
//    }
    interface createResult{
        fun result(a:Boolean)
    }
    fun creatAdmin( admin:String,caller:createResult){
        Thread{
            try{
                object :Socket_C(3){
                    override fun init() {
                        writeUTF(admin)
                        val a = readUTF()=="完畢" && downDB()
                        handler.post { caller.result(a)}
                    }
                }.start()
            }catch (e:java.lang.Exception){
                handler.post { caller.result(false)}
                e.printStackTrace()
            }
        }.start()
    }
    fun GetDevice(take:String,item:ArrayList<Bs_Contact>, caller: updateContact) {
        Thread{
            val originalSize=item.size
            try {
                object :Socket_C(1){
                    override fun init() {
                        writeUTF(take)
                        var a = readUTF()
                        while (a != "完畢"){
                            item.add(Bs_Contact(a,readUTF(),"",0))
                            a = readUTF()
                        }
                        handler.post { caller.update(originalSize==item.size,true)}
                    }}.start()
            } catch (e: Exception) {
                sleep(1000)
                e.printStackTrace()
                handler.post {  caller.update(originalSize==item.size,false)}
            }
        }.start()
    }
    fun SendMail(ad: String, file: String, Message: String,caller:GetPost_C) {

        Thread{
            try {
                object :Socket_C(116){
                    override fun init() {
                        writeUTF(ad)
                        writeUTF(file)
                        writeUTF(UnicodeUtil.stringToUnicode(Message))
                        val a=readInt() == 1
                        handler.post { caller.Result(a) }
                    }}.start()
            } catch (e: Exception) {
                sleep(1000)
                e.printStackTrace()
                handler.post { caller.Result(false) }
            }
        }.start()
    }
    fun downDB():Boolean{
        val doc = Jsoup.connect( "${serverRout}/Orange Cloud/CustomServer/").get()
        doc.attr("href");
        val array=doc.getElementsByTag("a")
        for(i in 1 until array.size){
            if (PublicBeans.instance.nowDbVersion==array[i].text()) {
                return true
            }else{
                PublicBeans.instance.robotOnline.close()
                if(PublicBeans.instance.robotOnline.init_ByUrl("${serverRout}/Orange%20Cloud/CustomServer/${array[i].text()}")){
                    PublicBeans.instance.robotOnline.create()
                    PublicBeans.instance.nowDbVersion=array[i].text()
                }else{return false}
            }
        }
        return false
    }
//    fun CheckRobotUpdate():Boolean{
//        try {
//
//        }catch (e:java.lang.Exception){e.printStackTrace()}
//    }

    fun refreshMail(take: String, it: Messageitem, ad: String,caller:GetPost_C) {
        Thread{
            val originsize = it.admin.size
            try {
                object :Socket_C(135){
                    override fun init() {
                        writeUTF(ad)
                        writeUTF(take)
                        var a = readUTF()
                        while (a != "完畢") {
                            it.insert(a, readUTF(), readUTF(), readUTF(), readUTF(), readUTF(), readUTF())
                            a = readUTF()
                        }
                        if (originsize == it.admin.size) {
                            it.button = true
                        }
                        it.success = true
                        handler.post { caller.Result(true) }
                    }

                    override fun error() {
                        caller.Result(false)
                    }
                }.start()
            } catch (e: Exception) {
                sleep(1000)
                e.printStackTrace()
                it.success = false
                handler.post { caller.Result(false) }
            }
        }.start()
    }

    fun havemessage(caller: AddLove_C) {
        Thread{
            try {
                object :Socket_C(117){
                    override fun init() {
                        var a=readInt()
                        handler.post { caller.result(a) }
                    }

                    override fun error() {
                        caller.result(-1)
                    }
                }.start()
            } catch (e: Exception) {
                sleep(1000)
                e.printStackTrace()
                handler.post {  caller.result(-1)}

            }
        }.start()
    }
}