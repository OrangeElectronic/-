package com.orange.tpms.ue.kt_frag


import android.app.Dialog
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.service.ChangePageService
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.hardware.HardwareApp
import com.orango.electronic.orange_og_lib.Command.Cmd_Og
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.data_loading.*
import kotlinx.android.synthetic.main.fragment_frag__scan.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Scan : RootFragement(R.layout.fragment_frag__scan) {
    private var dataReceiver: HardwareApp.DataReceiver? = null
    override fun viewInit() {
        refresh=true
        rootview.fixLanguage()
        when(PublicBean.ScanType){
            PublicBean.掃描Mmy->{
                rootview.iv_scan_tips.setImageResource(R.mipmap.iv_scan_mmy)
                rootview.tv_info_tips.text = "jz.64".getFix()
            }
            PublicBean.檢查傳感器->{
                rootview.iv_scan_tips.setImageResource(R.mipmap.iv_scan_sensorid)
                rootview.tv_info_tips.text = "jz.27".getFix()
            }
        }
        init()
    }

    override fun onKeyScan() {
        if(run){return}
        run=true
        HardwareApp.getInstance().scan()
        JzActivity.getControlInstance().closeDiaLog()
        JzActivity.getControlInstance().showDiaLog(false,true, object : SetupDialog(R.layout.data_loading) {
            override fun dismess() {

            }

            override fun keyevent(event: KeyEvent): Boolean {
                if(event.keyCode==4){
                    Cmd_Og.cancel=true
                }
            return false
            }

            override fun setup(rootview: Dialog) {
                rootview.pass.visibility=View.VISIBLE
                rootview.pass.text=resources!!.jzString(R.string.app_scaning)
            }

        },"data_loading")

        Thread{
            Thread.sleep(5000)
            handler.post { JzActivity.getControlInstance().closeDiaLog() }
            run=false
        }.start()
    }
fun init(){
    HardwareApp.getInstance().switchScan(true)
    dataReceiver = object : HardwareApp.DataReceiver {
        override fun scanReceive() {

        }
        override fun scanMsgReceive(content: String) {
            JzActivity.getControlInstance().closeDiaLog()
            Log.v("yhd-", "backToLastFrag:$content")
                GoOk(content)
        }

        override fun uart2MsgReceive(content: String) {

        }
    }
    HardwareApp.getInstance().addDataReceiver(dataReceiver)
}
    fun GoOk(content:String){
        if(!TextUtils.isEmpty(content)){
            if(PublicBean.ScanType== PublicBean.掃描Mmy){
                if (!content.contains("**")) {
                    JzActivity.getControlInstance().toast(resources!!.jzString(R.string.app_invalid_mmy_qrcode))
                } else {
                    Log.d("Scan","")
                    val dataArray = content.split("\\*\\*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if(dataArray.size==2){
                        PublicBean.GoOk(dataArray.get(0))
                        ChangePageService.changePage()
                    }
                }
            }else if(PublicBean.ScanType== PublicBean.掃描Sensor){

            }else{
                JzActivity.getControlInstance().toast(resources!!.jzString(R.string.app_content_empty))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            HardwareApp.getInstance().switchScan(false)
            HardwareApp.getInstance().removeDataReceiver(dataReceiver)
        }catch (e:Exception){e.printStackTrace()}
    }
}
