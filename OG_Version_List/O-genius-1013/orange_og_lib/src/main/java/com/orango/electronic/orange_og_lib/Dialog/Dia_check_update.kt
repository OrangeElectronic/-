package com.orange.tpms.ue.dialog

import android.app.Dialog
import android.view.KeyEvent
import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.FileDowload
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.beans.FileJsonVersion
import kotlinx.android.synthetic.main.check_update.*
import java.lang.Exception

class Dia_check_update : SetupDialog(R.layout.check_update) {
    override fun dismess() {

    }

    override fun keyevent(event: KeyEvent): Boolean {
        return false
    }

    override fun setup(rootview: Dialog) {
        var text = ""
        if (OgPublic.beta) {
            try {
                val item = "DataListVersion".getObject<FileJsonVersion>()!!
                val itemOnline = "OnlineDataVersion".getObject<FileJsonVersion>()!!
                if ("${OgPublic.versionCodes}.apk" != itemOnline.apkVersion) {
                    text += "${OgPublic.versionCodes}.apk -> ${itemOnline.apkVersion}\n"
                }
                if (item.lanVersion != itemOnline.lanVersion) {
                    text += "${item.lanVersion} -> ${itemOnline.lanVersion}\n"
                }
                if ("${OgPublic.MCU_NUMBER}.x2" != itemOnline.mcuVerion) {
                    text += "${OgPublic.MCU_NUMBER}.x2 -> ${itemOnline.mcuVerion}\n"
                }
                if (item.mmyVersion != itemOnline.mmyVersion) {
                    text += "${item.mmyVersion} -> ${itemOnline.mmyVersion}\n"
                }
                if (item.s18List != itemOnline.s18List) {
                    for (i in itemOnline.s18List) {
                        if (item.s18List[i.key] != i.value) {
                            text += "${item.s18List[i.key]} -> ${i.value}\n"
                        }
                    }
                }
                if (item.s19List != itemOnline.s19List) {
                    for (i in itemOnline.s19List) {
                        if (item.s19List[i.key] != i.value) {
                            text += "${item.s19List[i.key]} -> ${i.value}\n"
                        }
                    }
                }
                if (item.obdList != itemOnline.obdList) {
                    for (i in itemOnline.obdList) {
                        if (item.obdList[i.key] != i.value) {
                            text += "${item.obdList[i.key]} -> ${i.value}\n"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            rootview.content.text = text
        } else {
            rootview.content.visibility = View.GONE
        }
        rootview.cancel.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
        }
        rootview.yes.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            JzActivity.getControlInstance()
                .showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        }
    }
}