package com.orange.og_lite.Frag

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.og_lite.Util.jzString
import com.example.jztaskhandler.TaskHandler
import com.example.jztaskhandler.runner
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.*
import com.orange.og_lite.Dialog.Da_Scan_ble
import com.orange.og_lite.OgCommand.Companion.tx_memory
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.adapter.Ad_Function_Check_Sensor
import com.orange.og_lite.adapter.connectBack
import com.orange.og_lite.beans.Function_Check_Sensor_Item
import com.orange.og_lite.beans.ObdBeans
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.beans.SensorData
import kotlinx.android.synthetic.main.fragment_frag__function__show__read.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_Function_Check_Sensor_Read : JzFragement(R.layout.fragment_frag__function__show__read) {

    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity
    //var Data = ArrayList<TextView>()

    var myitem = Function_Check_Sensor_Item()
    lateinit var adapter: Ad_Function_Check_Sensor
    var canRead=true
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.Read_MMY_Title.text = PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year

        adapter = Ad_Function_Check_Sensor(myitem)

        rootview.ShowReadView.layoutManager = LinearLayoutManager(context)
        rootview.ShowReadView.adapter = adapter
        myitem.add("-----", "","","","")
        myitem.add("", "","","","")
        myitem.add("", "","","","")
        myitem.add("", "","","","")
        myitem.add("", "","","","")
        if(PublicBeans.wheelCount() == 5) {
            myitem.add("", "","","","")
        }

        adapter.notifyDataSetChanged()

        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }

        rootview.read.setOnClickListener {
            if(!canRead){return@setOnClickListener}
            canRead=false
            if(myitem.focus==-1){return@setOnClickListener}
            TaskHandler.newInstance.runTaskOnce("read", runner {
                Command.onProgram=true
                BleManager.toggleReadSensor=false
                Command.getSensorData(
                    myitem,
                    object : Command.Companion.callback {
                        override fun result(a: Boolean) {
                            canRead=true
                            BleManager.toggleReadSensor=true
                            Command.onProgram=false
                            if(!myitem.ID.contains("") ){
                                rootview.read.visibility = View.GONE
                                rootview.menu.setBackgroundResource(R.mipmap.btn_rectangle)}
                                adapter.notifyDataSetChanged()
                        }
                    })
            })

        }

    }

}
