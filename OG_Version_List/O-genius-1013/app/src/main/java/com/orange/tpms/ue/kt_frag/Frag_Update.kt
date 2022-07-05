package com.orange.tpms.ue.kt_frag


import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange.tpms.ue.dialog.Dia_UpdateIng
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__update.view.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Update : RootFragement(R.layout.fragment_frag__update) {

    override fun viewInit() {
        rootview.fixLanguage()
        val tmpInfo = JzActivity.getControlInstance().getRootActivity().packageManager.getApplicationInfo("com.orange.homescreem", -1)
        val size = File(tmpInfo.sourceDir).length()
        rootview.size.text = "$size"
        rootview.iv_check.isSelected = GetPro("AutoUpdate", true)
        rootview.iv_check.setOnClickListener {
            if (GetPro("AutoUpdate", true)) {
                SetPro("AutoUpdate", false)
            } else {
                SetPro("AutoUpdate", true)
            }
            rootview.iv_check.isSelected = GetPro("AutoUpdate", true)
        }
        rootview.check.setOnClickListener {
            JzActivity.getControlInstance().showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        }
        if (PublicBean.Update) {
            JzActivity.getControlInstance().showDiaLog(false, false, Dia_UpdateIng(), "Dia_UpdateIng")
        }
    }

}
