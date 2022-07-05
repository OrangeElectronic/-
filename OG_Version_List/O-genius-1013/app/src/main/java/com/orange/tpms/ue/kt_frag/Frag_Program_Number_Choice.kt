package com.orange.tpms.ue.kt_frag


import android.text.InputFilter
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.ue.dialog.Dia_Hint
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange.tpms.utils.KeyboardUtil
import com.orange.tpms.utils.OggUtils
import com.orange.tpms.utils.number_filter
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__program__sensor_information.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Frag_Program_Number_Choice :
    RootFragement(R.layout.fragment_frag__program__sensor_information) {
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.tv_mmy_title.text =
            "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        rootview.et_number.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        rootview.et_number.addTextChangedListener(number_filter(rootview.et_number, 2, context!!))
        KeyboardUtil.hideEditTextKeyboard(rootview.et_number)
        rootview.bt_start.setOnClickListener {
            enter()
        }
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        rootview.et_number.requestFocus()
    }


    override fun onTop() {
        rootview.et_number.requestFocus()
    }

    override fun onDown() {
        rootview.et_number.requestFocus()
    }

    override fun onLeft() {
        rootview.et_number.requestFocus()
        rootview.et_number.setSelection(0)
    }

    override fun onRight() {
        rootview.et_number.requestFocus()
        rootview.et_number.setSelection(rootview.et_number.text.toString().length)
    }

    override fun enter() {
        OggUtils.hideKeyBoard(activity)
        if (rootview.et_number.text.isEmpty()) {
            JzActivity.getControlInstance().showDiaLog(true,false,Dia_Hint("jz.519".getFix()),"Dia_Hint")
//            PublicBean.ProgramNumber = 1
//            JzActivity.getControlInstance().changePage(Frag_New_ProgramDetail(), "Frag_Program_Detail", true)
        } else {
            PublicBean.ProgramNumber = Integer.valueOf(rootview.et_number.text.toString())
            JzActivity.getControlInstance().changePage(Frag_New_ProgramDetail(), "Frag_Program_Detail", true)
        }
    }
}
