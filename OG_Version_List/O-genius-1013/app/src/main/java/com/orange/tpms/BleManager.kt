package com.orange.tpms

import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jzandroidwidget.Callback.runner
import com.example.jzandroidwidget.JzTool
import com.jianzhi.jzblehelper.BleHelper
import com.jianzhi.jzblehelper.callback.BleCallBack
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.jianzhi.jzblehelper.models.BleBinary
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.adapter.BleAdapter
import com.orange.tpms.app.TPMSApp
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.kt_frag.Frag_Manager
import com.orango.electronic.orange_og_lib.Command.Cmd_Og.StringHexToByte
import com.orange.tpms.utils.RxCommand
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.Systool
import com.orango.electronic.orange_og_lib.Util.jzString
import kotlinx.android.synthetic.main.activity_kt.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BleManager(var context: Context) : BleCallBack {
    interface connectResult {
        fun result(a: Boolean)
    }

    companion object {
        var nowble = 0
        var connectBlename = ""
    }

    override fun onDisconnect() {
        Log.e("JzBleMessage", "藍牙斷線")
    }

    var RxChannel = "00008D81-0000-1000-8000-00805F9B34FB"
    var TxChannel = "00008D82-0000-1000-8000-00805F9B34FB"
    var BleHelper: BleHelper = BleHelper(context, this)
    var handler = Handler()
    var devices = ArrayList<BluetoothDevice>()
    var adapter: BleAdapter = BleAdapter(devices)


    override fun needGPS() {
        BleHelper.stopScan()
    }

    override fun onConnectFalse() {
        JzTool.newInstance.runTaskTimer("onConnectSuccess", 0.02, runner {
            BleHelper.closeBle()
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance()
                .toast(context.resources!!.jzString(R.string.app_connected_failed))
            Log.d("JzBleMessage", "藍牙連線失敗")
            val mMainActivity = JzActivity.getControlInstance().getActionBar()
            if (mMainActivity is Frag_Manager) {
                handler.post {
                    when (PublicBean.position) {
                        PublicBean.ID_COPY_OBD -> {
                            mMainActivity.rootview.logout.visibility = View.GONE
                        }

                        PublicBean.OBD_RELEARM -> {
                            mMainActivity.rootview.logout.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    override fun onConnectSuccess() {
        (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.stopScan()
        JzTool.newInstance.runTaskTimer("onConnectSuccess", 0.02) {
            Log.d("JzBleMessage", "藍牙連線")
            //val mainfrag = JzActivity.getControlInstance().getActionBar()
            val mMainActivity = JzActivity.getControlInstance().getActionBar()
            if (mMainActivity is Frag_Manager) {
                handler.post {
                    if (connectBlename.contains("OBD")) {
                        if (PublicBean.position == PublicBean.ID_COPY_OBD || PublicBean.position == PublicBean.OBD_RELEARM) {
                            mMainActivity.logout.visibility = View.VISIBLE
                            mMainActivity.logout.setImageResource(R.mipmap.icon_obdii)
                        }
                    } else if (connectBlename.contains("PAD")) {
                        Thread {
                            (JzActivity.getControlInstance().getRootActivity() as KtActivity).BleCommand.Setserial()
                        }.start()
                        if (PublicBean.position == PublicBean.PAD_COPY || PublicBean.position == PublicBean.PAD_PROGRAM) {
                            mMainActivity.rootview.logout.visibility = View.VISIBLE
                            mMainActivity.rootview.logout.setImageResource(R.mipmap.icon_link_pad)
                        }
                    }
                }
            }
        }
    }

    override fun onConnecting() {
        Log.e("JzBleMessage", "藍牙正在連線中")
    }

    override fun requestPermission(permission: ArrayList<String>) {
        Log.d("JzBleMessage", "requestPermission")
    }

    var lastRx = ""
    override fun rx(a: BleBinary) {
        if (lastRx == a.readHEX()) {
            return
        }
        lastRx = a.readHEX()
        BleHelper.RxData += a.readHEX()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
        Cmd_Og.tx_memory.append("RX " + time + "  :\n" + BleHelper.RxData + "\n")
        RxCommand.RX(StringHexToByte(BleHelper.RxData))
        Log.e("JzBleMessage", "收到藍牙消息${a.readHEX()}")

    }

    override fun scanBack(device: BluetoothDevice, scanRecord: BleBinary, rssi: Int) {
        if (!devices.contains(device) && device.name != null && device.name!!.toLowerCase().contains("obd")) {
            devices.add(device)
            adapter.notifyDataSetChanged()
        }
    }


    override fun tx(b: BleBinary) {
        lastRx = ""
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
        Cmd_Og.tx_memory.append("TX " + time + "  :\n" + b.readHEX() + "\n")
        Log.e("JzBleMessage", "傳送藍牙消息${b.readHEX()}")
    }

    fun scan(callback: ConnectResult) {
        if(!TPMSApp.TestMode){
            Systool.openGPS(true)
        }
        BleHelper.openBle()
        BleHelper.disconnect()
        devices.clear()
        adapter.callback = callback
        adapter.notifyDataSetChanged()
        Thread {
            var fal = 0
            while (!BleHelper.startScan()) {
                fal += 1
                if (fal == 3) {
                    break
                }
                Thread.sleep(500)
            }
        }.start()

        JzActivity.getControlInstance()
            .showDiaLog(false, false, object : SetupDialog(R.layout.activity_scan_ble) {
                override fun dismess() {
                    if(!TPMSApp.TestMode){
                        Systool.openGPS(false)
                    }
                }

                override fun keyevent(event: KeyEvent): Boolean {
                    return true
                }

                override fun setup(rootview: Dialog) {
                    rootview.findViewById<RecyclerView>(R.id.re).layoutManager =
                        LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
                    rootview.findViewById<RecyclerView>(R.id.re).adapter = adapter
                    rootview.findViewById<Button>(R.id.close).setOnClickListener {
                        BleHelper.stopScan()
                        BleHelper.disconnect()
                        JzActivity.getControlInstance().closeDiaLog()
                        if(!TPMSApp.TestMode){
                            Systool.openGPS(false)
                        }
                    }
                }
            }, "activity_scan_ble")
    }
}