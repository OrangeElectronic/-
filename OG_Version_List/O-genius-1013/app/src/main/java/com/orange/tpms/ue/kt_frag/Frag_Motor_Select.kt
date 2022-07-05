package com.orange.tpms.ue.kt_frag

import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.frag_motor_select.view.*

class Frag_Motor_Select(var upload: Boolean) : JzFragement(R.layout.frag_motor_select) {
    var readable = arrayOf(false, false)

    var fwToggle: Boolean = false
        set(value) {
            field = value
            rootview.fw.setImageResource(if (field) R.mipmap.img_motorcyclewheel_select else R.mipmap.img_motorcycle_wheel_normal)
            rootview.imageView39.setImageResource(if (field) R.mipmap.icon_click else R.mipmap.icon_not_selected)
            readable[0] = field
        }
    var rwToggle: Boolean = false
        set(value) {
            field = value
            rootview.rw.setImageResource(if (field) R.mipmap.img_motorcyclewheel_select else R.mipmap.img_motorcycle_wheel_normal)
            rootview.imageView43.setImageResource(if (field) R.mipmap.icon_click else R.mipmap.icon_not_selected)
            readable[1] = field
        }

    override fun viewInit() {
        rootview.title2.text =
            "${PublicBean.selectMmy.displayMake}/${PublicBean.selectMmy.displayModel}/${PublicBean.selectMmy.displayYear}"
        rootview.fixLanguage()
        rootview.fw.setOnClickListener {
            fwToggle = !fwToggle
        }
        rootview.rw.setOnClickListener {
            rwToggle = !rwToggle
        }
        rootview.nextBt.setOnClickListener {
            if (!readable.contains(true)) {
                JzActivity.getControlInstance().toast("請先選擇燒錄位置!")
                return@setOnClickListener
            }
            PublicBean.readable = readable
            JzActivity.getControlInstance().changePage(
                Frag_Function_OBD_ID_copy(upload),
                "Frag_Function_OBD_ID_copy",
                true
            )
        }
        rootview.fb.setOnClickListener { fwToggle = !fwToggle }
        rootview.rb.setOnClickListener { rwToggle = !rwToggle }
    }
}