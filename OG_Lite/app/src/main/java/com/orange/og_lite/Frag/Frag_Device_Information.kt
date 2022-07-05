package com.orange.og_lite.Frag

import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.og_lite.Util.fixLanguage
import com.jianzhi.jzblehelper.FormatConvert
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.adapter.Ad_Device
import com.orange.og_lite.adapter.Device
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.beans.FileJsonVersion
import com.orange.og_lite.beans.PublicBeans
import com.orango.electronic.jzutil.getObject
import kotlinx.android.synthetic.main.device_information.view.*
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__my_favorite.*
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__sign_in.view.*

class Frag_Device_Information:JzFragement(R.layout.device_information){
    var click=0
    override fun viewInit() {
        var tempData = "DataListVersion".getObject<FileJsonVersion>()
        val item=ArrayList<Device>()
        item.add(Device("jz.397".getFix(),"O-Genius Lite"))
        item.add(Device("jz.414".getFix(),String(FormatConvert.StringHexToByte(PublicBeans.MCU_NUMBER))))
        item.add(Device("jz.59".getFix(), tempData!!.mmyVersion))
        item.add(Device("jz.399".getFix(), ""+PublicBeans.versionCodes))
        item.add(Device("jz.56".getFix(), ""+PublicBeans.SN))
        item.add(Device("jz.447".getFix(), ""+PublicBeans.bleVersion))
        item.add(Device("jz.392".getFix(), ""+JzActivity.getControlInstance().getPro("lanName","no")))
        rootview.re.layoutManager=LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.re.adapter=Ad_Device(item)
        rootview.imageView26.setOnClickListener {
            click+=1
            if(click==10){
                click=0
                PublicBeans.beta=!PublicBeans.beta
                Util_FileDowload.clearInit()
                JzActivity.getControlInstance().goMenu()
            }
        }
    }

}