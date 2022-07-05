package com.example.customerlibrary.callback

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.customerlibrary.AdminBeans
import com.example.customerlibrary.R
import com.orange.jzchi.jzframework.JzActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import javax.net.SocketFactory
import android.R.attr.port
import android.R.attr.host
import com.orange.jzchi.jzframework.callback.SetupDialog
import kotlinx.android.synthetic.main.error.*
import java.net.InetSocketAddress


abstract class Socket_C(val function: Int) {
    companion object {
        var counter = 0
        var ip="35.240.51.141"
    }
    var act=JzActivity.getControlInstance().getRootActivity()
    lateinit var s: Socket
    lateinit var br: DataInputStream
    lateinit var ps: DataOutputStream
    fun writeUTF(a: String) {
        ps.writeUTF(a)
    }

    fun writeInt(a: Int) {
        ps.writeInt(a)
    }

    fun readUTF(): String {
        return br.readUTF()
    }

    fun readInt(): Int {
        return br.readInt()
    }

    //開始請求
    fun start() {
        try {
            Log.e("Socket", "請求開始" + function)
            ShowProgress(true)
            counter++
            val s = SocketFactory.getDefault().createSocket()
            val remoteaddr = InetSocketAddress(ip, 10001)
            s.connect(remoteaddr, 10*1000);
            br = DataInputStream(s.getInputStream())
            ps = DataOutputStream(s.getOutputStream())
            ps.writeUTF(AdminBeans.admin)
            ps.writeUTF(AdminBeans.password)
            ps.writeInt(function)
            init()
            stop()
        } catch (e: Exception) {
            e.printStackTrace()
            counter--
            error()
        }
    }

    open fun error() {
        act.handler.post {
            ShowProgress(false)
            JzActivity.getControlInstance().showDiaLog(false,false,object :SetupDialog(R.layout.error){
                override fun dismess() {
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return false
                }

                override fun setup(rootview: Dialog) {
                    rootview.retry.setOnClickListener {
                        rootview.dismiss()
                        Thread { start() }.start()
                    }
                    rootview.cancel.setOnClickListener {
                        rootview.dismiss()
                    }
                }
            },"error")
        }
    }

    //請求完畢
    fun stop() {
        counter--
        Log.e("Socket", "請求完畢${function}/" + counter)
        act.handler.postDelayed({
            if (counter <= 0) {
                ShowProgress(false)
            }
        },500)
    }

    //代碼處理
    abstract fun init()


    fun ShowProgress(show: Boolean) {
        if (show) {
            act.handler.post {
                JzActivity.getControlInstance().showDiaLog(R.layout.progress,false,false,"progress")
            }
        } else {
            act.handler.post {
            JzActivity.getControlInstance().closeDiaLog("progress")
            }
        }
    }
}