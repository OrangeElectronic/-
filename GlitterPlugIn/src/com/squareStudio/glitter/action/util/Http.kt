package com.squareStudio.glitter.action.util

import java.io.DataInputStream
import java.net.HttpURLConnection
import java.net.URL

object Http {
    fun getRequest(
        url: String, timeout: Int, downloadProgress: (a: Int) -> Unit = {}
    ): String? {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout
            conn.readTimeout = timeout
            conn.requestMethod = "GET"
            conn.doInput = true;
            val buffer = ByteArray(1024)
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
}