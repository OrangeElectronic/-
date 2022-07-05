package com.orange.og_lite.Frag

import android.app.Dialog
import android.view.KeyEvent
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.R
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.callback.Update_C
import com.orange.og_lite.Util.fixLanguage
import com.jianzhi.jzblehelper.FormatConvert
import com.orange.og_lite.Util.getFix
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.adapter.Ad_Device
import com.orange.og_lite.adapter.Device
import com.orange.og_lite.beans.PublicBeans
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orange.tpms.ue.dialog.Dia_check_update
import kotlinx.android.synthetic.main.device_information.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Update(var auto:Boolean) : JzFragement(R.layout.device_information) {

    override fun viewInit() {
        val item=ArrayList<Device>()
        item.add(Device("jz.397".getFix(),"O-Genius Lite"))
        item.add(Device("jz.414".getFix(),String(FormatConvert.StringHexToByte(PublicBeans.MCU_NUMBER))))
        item.add(Device("jz.59".getFix(), PublicBeans.DataVersion))
        item.add(Device("jz.399".getFix(), ""+PublicBeans.versionCodes))
        item.add(Device("jz.56".getFix(), ""+PublicBeans.SN))
        item.add(Device("jz.447".getFix(), ""+PublicBeans.bleVersion))
        item.add(Device("jz.392".getFix(), ""+JzActivity.getControlInstance().getPro("lanName","no")))

        rootview.re.layoutManager= LinearLayoutManager(JzActivity.getControlInstance().getRootActivity())
        rootview.re.adapter= Ad_Device(item)

        if(Util_FileDowload.needUpdate()){
           JzActivity.getControlInstance()
               .showDiaLog(false, false, Dia_check_update(), "Dia_check_update")
        }else{
            JzActivity.getControlInstance().toast("jz.214".getFix())
        }
    }

}
