package com.orange.tpms.ue.kt_frag

import com.jianzhi.customer_service.beans.PublicBeans
import com.jianzhi.customer_service.frag.Frag_UserMessage
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.ue.dialog.Dia_check_update
import com.orange.tpms.utils.WifiUtils
import com.orango.electronic.jzutil.CalculateTime
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.orange_og_lib.Frag.Frag_Wifi
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Frag.connectBack
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.FileDowload.serverRout
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import kotlinx.android.synthetic.main.frag_customer_service.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Frag_Customer_Service : JzFragement(R.layout.frag_customer_service) {
    override fun viewInit() {
        rootview.fixLanguage()
        PublicBeans.instance
        rootview.gochat.setOnClickListener {
            JzActivity.getControlInstance().showDiaLog(R.layout.data_loading,false,false,"data_loading")
                    if(WifiUtils.getInstance(JzActivity.getControlInstance().getRootActivity()).isOpenWifi){
                        goPage()
                    }else{
                        JzActivity.getControlInstance().changePage(Frag_Wifi(
                            object : connectBack {
                                override fun result(a: Boolean) {
                                    JzActivity.getControlInstance().closeDiaLog()
                                    if (a) {
                                        goPage()
                                    }
                                }
                            }),"Frag_Wifi",true)
                    }
        }
        rootview.apkversion.text = "${OgPublic.versionCodes}"
        rootview.dbversion.text = "${OgPublic.DataVersion}"
        rootview.mcuversion.text = OgPublic.MCU_NUMBER
        rootview.textView34.text = "O-Genius \n${OgPublic.DataVersion}"
        if(JzActivity.getControlInstance().getPro("updateDate","NA")=="NA"){
            rootview.updatedate.text=resources!!.jzString(R.string.is_latest)
        }else{
            rootview.updatedate.text=resources!!.jzString(R.string.update)+"\n"+calculateTime(JzActivity.getControlInstance().getPro("updateDate","NA"))
        }
        rootview.updateim.text="jz.214".getFix()
        if (FileDowload.needUpdate()) {
            val data = "DataListVersion".getObject<FileJsonVersion>()!!
            val dataOnline = "OnlineDataVersion".getObject<FileJsonVersion>()!!
            rootview.updateim.text="jz.213".getFix()
            rootview.updateim.setOnClickListener {
                JzActivity.getControlInstance()
                    .showDiaLog(false, false, Dia_check_update(), "Dia_check_update")
            }
            if( (dataOnline.mcuVerion != "${OgPublic.MCU_NUMBER}.x2")){
                rootview.mcuversion.setTextColor(resources!!.getColor(R.color.color_red))
            }
            if(dataOnline.mmyVersion != data.mmyVersion){
                rootview.dbversion.setTextColor(resources!!.getColor(R.color.color_red))
            }
            if("${OgPublic.versionCodes}.apk" != dataOnline.apkVersion){
                rootview.apkversion.setTextColor(resources!!.getColor(R.color.color_red))
            }
        }
    }
    
    fun goPage(){
        PublicBeans.instance.robotDB=OgPublic.getInstance.onlinelanDB
        JzActivity.getControlInstance().changePage(
            Frag_UserMessage(OgPublic.admin,"${serverRout}",0),
            "Frag_UserMessage",
            true
        )
    }
    fun calculateTime(a:String): String {
        if (JzActivity.getControlInstance().getLanguage() != null) {
            JzActivity.getControlInstance().setLanguage(JzActivity.getControlInstance().getLanguage()!!)
        }
        val nowTime = System.currentTimeMillis() // 获取当前时间的毫秒数
        var msg: String = "jz.222".getFix()
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 指定时间格式
        var setTime: Date? = null // 指定时间
        try {
            setTime = sdf.parse(a) // 将字符串转换为指定的时间格式
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val reset = setTime!!.time // 获取指定时间的毫秒数
        val dateDiff = nowTime - reset
        if (dateDiff < 0) {
            msg = "jz.222".getFix()
        } else {
            val dateTemp1 = dateDiff / 1000 // 秒
            val dateTemp2 = dateTemp1 / 60 // 分钟
            val dateTemp3 = dateTemp2 / 60 // 小时
            val dateTemp4 = dateTemp3 / 24 // 天数
            val dateTemp5 = dateTemp4 / 30 // 月数
            val dateTemp6 = dateTemp5 / 12 // 年数
            if (dateTemp6 > 0) {
                msg = "jz.227".getFix().replace("1", dateTemp6.toString())
            } else if (dateTemp5 > 0) {
                msg = "jz.226".getFix().replace("1", dateTemp5.toString())
            } else if (dateTemp4 > 0) {
                msg = "jz.221".getFix().replace("1", dateTemp4.toString())
            } else if (dateTemp3 > 0) {
                msg = "jz.225".getFix().replace("1", dateTemp3.toString())
            } else if (dateTemp2 > 0) {
                msg =  "jz.220".getFix().replace("1", dateTemp2.toString())
            } else if (dateTemp1 > 0) {
                msg = "jz.222"
            }
        }
        return msg
    }
}