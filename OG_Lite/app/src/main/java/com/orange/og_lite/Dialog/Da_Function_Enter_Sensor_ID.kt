package com.orange.og_lite.Dialog

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Function_Enter_Sensor_ID
import com.orange.og_lite.beans.Function_Enter_Sensor_ID_Item
import com.orange.og_lite.callback.Dia_SelectBaclk
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString

class Da_Function_Enter_Sensor_ID(var callback: Dia_SelectBaclk) : SetupDialog(R.layout.da_function_enter_sensor_id)
{

    val mMainActivity= JzActivity.getControlInstance().getRootActivity() as MainActivity

    var myitem = Function_Enter_Sensor_ID_Item()
    lateinit var adapter: Ad_Function_Enter_Sensor_ID

    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {

        myitem= Function_Enter_Sensor_ID_Item()
        adapter= Ad_Function_Enter_Sensor_ID(myitem,callback)

        rootview.findViewById<RecyclerView>(R.id.enter_sensor_id_re).layoutManager = LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.findViewById<RecyclerView>(R.id.enter_sensor_id_re).adapter = adapter
        myitem.add(R.mipmap.img_program_trigger_read,mMainActivity.resources.jzString(R.string.app_sensor_info_read), View.GONE,"",Function_Enter_Sensor_ID_Item.Sensor_Read)
        myitem.add(R.mipmap.icon_scan,mMainActivity.resources.jzString(R.string.Scan_Code), View.VISIBLE,"jz.314".getFix(),Function_Enter_Sensor_ID_Item.Scan_Code)
        myitem.add(R.mipmap.icon_key_in,mMainActivity.resources.jzString(R.string.app_key_in), View.GONE,"",Function_Enter_Sensor_ID_Item.Key_In)
        adapter.notifyDataSetChanged()
        Function_Enter_Sensor_ID_Item.選擇讀ID感測器 = 0
    }

}