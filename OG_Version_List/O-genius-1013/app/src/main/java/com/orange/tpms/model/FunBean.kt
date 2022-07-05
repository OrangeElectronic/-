package com.orange.tpms.model

import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.ue.dialog.TwoReprogram

class FunBean
{
    companion object{
        fun ReProgram(reProgram_times:Int) {
            if (reProgram_times == 2) {
                JzActivity.getControlInstance().showDiaLog(false, false,
                    TwoReprogram(), "program_false"
                )
            }
        }
    }
}