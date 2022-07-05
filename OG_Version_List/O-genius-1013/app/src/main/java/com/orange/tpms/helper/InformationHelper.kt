package com.orange.tpms.helper

import android.app.Activity
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.tpms.R
import com.orange.tpms.model.InformationBean
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion

import java.util.ArrayList

class InformationHelper : BaseHelper() {

    lateinit var informationList: MutableList<InformationBean>//信息列表

    /* *********************************  获取自动锁定列表  ************************************** */

    private var onGetInformationListener: OnGetInformationListener? = null

    /**
     * 读取信息列表
     */
    fun getInformation(context: Activity) {
        preRequestNext()
        val data="DataListVersion".getObject<FileJsonVersion>()!!
        informationList = ArrayList()//数据源
        informationList.add(
            InformationBean(
                context.resources!!.jzString(R.string.infomation_name),
                "O-Genius"
            )
        )
        informationList.add(
            InformationBean(
                context.resources!!.jzString(R.string.infomation_module),
                "V1.0"
            )
        )
        informationList.add(
            InformationBean(
                context.resources!!.jzString(R.string.infomation_serial_number),
                OgPublic.deviceID
            )
        )
        informationList.add(
            InformationBean(
                context.resources!!.jzString(R.string.infomation_version),
                "" + OgPublic.versionCodes
            )
        )
        informationList.add(
            InformationBean(
                "jz.59".getFix(),data.mmyVersion
            )
        )
        informationList.add(InformationBean("jz.414".getFix(), OgPublic.MCU_NUMBER))
        informationList.add(InformationBean("jz.66".getFix(), FileDowload.country))
        informationList.add(InformationBean("jz.52".getFix(), data.lanVersion))
        informationList.add(InformationBean("jz.58".getFix(), OgPublic.hardWareVersion))
        getInformationNext(informationList)
        finishRequestNext()
    }

    fun getInformationNext(arrayList: List<InformationBean>) {
        if (onGetInformationListener != null) {
            runMainThread { onGetInformationListener!!.onGetInformation(arrayList) }
        }
    }

    fun setOnGetInformationListener(onGetInformationListener: OnGetInformationListener) {
        this.onGetInformationListener = onGetInformationListener
    }

    interface OnGetInformationListener {
        fun onGetInformation(arrayList: List<InformationBean>)
    }
}
