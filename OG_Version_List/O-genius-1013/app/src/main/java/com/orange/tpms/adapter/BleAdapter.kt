package com.orange.tpms.adapter

import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.orango.electronic.orange_og_lib.Util.jzString
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.BleManager.Companion.connectBlename
import com.orange.tpms.R
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.selectble.view.*

class BleAdapter(var device:ArrayList<BluetoothDevice>):JzAdapter(R.layout.selectble){
    override fun sizeInit(): Int {
       return device.size
    }
var callback:ConnectResult ?= null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.fixLanguage()
       if(device[position].name==null){
           holder.mView.textView.text="UNKNOWN"
       }else{holder.mView.textView.text=device[position].name}
        holder.mView.textView2.text=device[position].address
        holder.mView.setOnClickListener {
            if(device[position].name!=null){
                connectBlename=device[position].name
            }
            (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.disconnect()
            if(callback==null){
                (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.connect(device[position].address,20
                ) {
                }
            }else{
                (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager.BleHelper.connect(device[position].address,20,callback!!)
            }

            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance().showDiaLog(false,false,object :SetupDialog(R.layout.data_loading){
                override fun dismess() {

                }

                override fun keyevent(event: KeyEvent): Boolean {
                    if(event.keyCode==4){
                        Cmd_Og.cancel=true
                    }
                 return false
                }

                override fun setup(rootview: Dialog) {
                  val tit=rootview.findViewById<TextView>(R.id.pass)
                    tit.visibility= View.VISIBLE
                    tit.text=JzActivity.getControlInstance().getRootActivity().resources!!.jzString(R.string.app_connecting)
                }
            },"data_loading")
        }
    }

}