package com.jianzhi.customer_service

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jianzhi.customer_service.beans.PublicBeans
import com.jianzhi.customer_service.message
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.jzutil.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ServerCommand() : ServerInTerface {
    var demo = false

    val handler: Handler get() = JzActivity.getControlInstance().getHandler()

    companion object {
        var serial:String=""
        var instance: ServerInTerface? = null
        var server = "http://192.168.0.13:8080"
        fun controlInstance(): ServerInTerface {
            if (instance == null) {
                instance = ServerCommand()
            }
            return instance!!
        }
    }


    override fun getLanguageVersion(callback: ServerInTerface.vertifycallback) {
        start()

    }

    @SuppressLint("SimpleDateFormat")
    override fun getMessage(
        message: ArrayList<message>,
        callback: ServerInTerface.vertifycallback
    ) {
        Thread {
            if (demo) {
                Thread.sleep(2000)
                handler.post {
                    stop()
                    message.add(
                        message(
                            "1",
                            "sam28124",
                            "子腦溢右右拉".stringToUnicode()!!,
                            "${server}/getFile?path=/Users/jianzhi.wang/IdeaProjects/.sqeng/_2020062808_20_31_031",
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                    "2020-06-26 19:03:59"
                                )
                            ),
                            "head",
                            "Doctor1"
                        )
                    )
                    message.add(
                        message(
                            "1",
                            serial,
                            "哈哈哈哈".stringToUnicode()!!,
                            "${server}/getFile?path=/Users/jianzhi.wang/IdeaProjects/.sqeng/_2020062808_20_31_031",
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                    "2020-06-26 19:03:59"
                                )
                            ),
                            "head",
                            "Doctor1"
                        )
                    )
                    callback.success()
                }
            } else {
                val re = "${server}/Api_Customer_Message".addParameters(
                    arrayOf("serial", "id"),
                    arrayOf(
                        serial,
                        if (message.size >= 50) message.get(message.size - 1).id else "-1"
                    )
                ).getWebResource(5000)
//                val lanVersion = "${server}/robotVersion".getWebResource(5000)
//                if (lanVersion == null ) {
//                    handler.post {
//                        stop()
//                        callback.internetError()
//                    }
//                    return@Thread
//                } else if (lanVersion != PublicBeans.instance.nowDbVersion) {
//                    val data = "${server}/getRoBot".getWebResource(1000 * 30)
//                    if (data == null) {
//                        handler.post {
//                            callback.internetError()
//                        }
//                        return@Thread
//                    }
//                    Looper.prepare()
//                    data.storeFile("lan")
//                    PublicBeans.instance.robotDB.close()
//                    PublicBeans.instance.robotDB.dbinit("lan".getFile()!!.inputStream())
//                    PublicBeans.instance.nowDbVersion = lanVersion
//                    PublicBeans.instance.robotDB.create()
//                }
                handler.post {
                    if (re == null) {
                        interNetError()
                        callback.internetError()
                    } else {
                        val Dt: ArrayList<message> =
                            Gson().fromJson(re, object : TypeToken<ArrayList<message>>() {}.type)
                        message.addAll(Dt)
                        callback.success()
                    }
                }
            }
        }.start()
    }


    @SuppressLint("SimpleDateFormat")
    override fun getTopMessage(
        message: ArrayList<message>,
        callback: ServerInTerface.vertifycallback
    ) {
        Thread {
            if (demo) {
                Thread.sleep(2000)
                handler.post {
                    message.add(
                        message(
                            "1",
                            "sam28124",
                            "子腦溢右右拉".stringToUnicode()!!,
                            "${server}/getFile?path=/Users/jianzhi.wang/IdeaProjects/.sqeng/_2020062808_20_31_031",
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                    "2020-06-26 19:03:59"
                                )
                            ),
                            "head",
                            "Doctor1"
                        )
                    )
                    message.add(
                        message(
                            "1",
                            serial,
                            "哈哈哈哈".stringToUnicode()!!,
                            "${server}/getFile?path=/Users/jianzhi.wang/IdeaProjects/.sqeng/_2020062808_20_31_031",
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                    "2020-06-26 19:03:59"
                                )
                            ),
                            "head",
                            "Doctor1"
                        )
                    )
                    callback.success()
                }
            } else {
                val re = "${server}/Api_Top_Message".addParameters(
                    arrayOf("serial", "id"),
                    arrayOf(serial, if (message.size > 0) message[0].id else "-1")
                ).getWebResource(5000)
                handler.post {
                    if (re == null) {
//                        callback.internetError()
                    } else {
                        val Dt: ArrayList<message> =
                            Gson().fromJson(re, object : TypeToken<ArrayList<message>>() {}.type)
                        message.addAll(0, Dt)
                        callback.success()
                    }
                }
            }
        }.start()
    }

    override fun uploadImage(uri: Uri, callback: ServerInTerface.vertifycallback) {
        start()
        Thread {
            if (demo) {
                handler.post { callback.success() }
            } else {
                val iStream = JzActivity.getControlInstance().getRootActivity()
                    .contentResolver.openInputStream(uri)
                val re = "${server}/UploadImage".postRequest(20000, iStream!!.readBytes())
                Log.e("response", re.toString())
                if (re == null || re == "false") {
                    handler.post {
                        stop()
                        callback.vertifyFalse()
                    }
                    return@Thread
                }
                val postMessage = "${server}/InsertMessage".postRequest(
                    20 * 1000, "".addParameters(
                        arrayOf("tableName", "serial", "message", "file"),
                        arrayOf(
                            serial,
                            serial,
                            "",
                            "/getFile?path=$re"
                        )
                    ).substring(1)
                )
                handler.post {
                    stop()
                    when (postMessage) {
                        "true" -> {
                            callback.success()
                        }
                        "false" -> {
                            callback.vertifyFalse()
                        }
                        null -> {
                            callback.internetError()
                        }
                    }
                }

            }
        }.start()
    }

    override fun sendMessage(message: String, callback: ServerInTerface.vertifycallback) {
        start()
        Thread {
            val postMessage = "${server}/InsertMessage".postRequest(
                20 * 1000, "".addParameters(
                    arrayOf("tableName", "serial", "message", "file"),
                    arrayOf(
                        serial,
                        serial,
                        message.stringToUnicode()!!,
                        "nodata"
                    )
                ).substring(1)
            )
            handler.post {
                stop()
                when (postMessage) {
                    "true" -> {
                        callback.success()
                    }
                    "false" -> {
                        callback.vertifyFalse()
                    }
                    null -> {
                        callback.internetError()
                    }
                }
            }
        }.start()
    }

    fun interNetError() {
        handler.post {
//            JzActivity.getControlInstance().toast("jz.98")
        }
    }

    fun start() {
        handler.post {
            JzActivity.getControlInstance().showDiaLog(R.layout.progress, false, false, "progress")
        }
    }

    fun stop() {
        handler.post {
            JzActivity.getControlInstance().closeDiaLog("progress")
        }
    }

}

interface demoRun {
    fun run() {}
}

