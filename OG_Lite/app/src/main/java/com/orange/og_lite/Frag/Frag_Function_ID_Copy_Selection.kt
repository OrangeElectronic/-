package com.orange.og_lite.Frag

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement

import com.orange.og_lite.R
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.fragment_frag__function__id__copy__selection.view.*

/**
 * A simple [Fragment] subclass.
 */

class Frag_Function_ID_Copy_Selection :
    JzFragement(R.layout.fragment_frag__function__id__copy__selection) {

    var readable = ArrayList<Boolean>()

    override fun viewInit() {
        for (i in 0 until PublicBeans.wheelCount()) {
            readable.add(true)
        }
        ChangeReadable()
        rootview.title.text =
            PublicBeans.make + "/" + PublicBeans.model + "/" + PublicBeans.year
        rootview.lf_R.setOnClickListener {
            readable[0] = !readable[0]
            ChangeReadable()
        }
        rootview.lr_R.setOnClickListener {
            readable[1] = !readable[1]
            ChangeReadable()
        }
        rootview.rr_R.setOnClickListener {
            readable[2] = !readable[2]
            ChangeReadable()
        }
        rootview.rf_R.setOnClickListener {
            readable[3] = !readable[3]
            ChangeReadable()
        }
        rootview.spare_tire_L.setOnClickListener {
            readable[4] = !readable[4]
            ChangeReadable()
        }
        if (PublicBeans.wheelCount() != 5) {
            rootview.spare_tire_L.visibility = View.GONE
        }
        rootview.All_L.setOnClickListener {

            if (!readable.contains(false)) {
                for (i in 0 until PublicBeans.wheelCount()) {
                    readable[i] = false
                }
            } else {
                for (i in 0 until PublicBeans.wheelCount()) {
                    readable[i] = true
                }
            }

            ChangeReadable()
        }

        rootview.menu.setOnClickListener { JzActivity.getControlInstance().goMenu() }

        rootview.textView49.setOnClickListener {
            if (!readable.contains(true)) {
                JzActivity.getControlInstance().toast("請先選擇燒錄位置!")
                return@setOnClickListener
            }
            PublicBeans.readable = readable.toTypedArray()
            JzActivity.getControlInstance().changePage(
                Frag_Function_OBD_ID_copy(),

                "Frag_Function_OBD_ID_copy",
                true
            )
        }
    }

//    override fun enter() {
//        super.enter()
//        rootview.textView49.performClick()
//    }
    var lastMax=true
    fun ChangeReadable() {
        Log.e("readable.filter", "" + readable.filter { it }.size)
        if (readable.filter { it }.size == readable.size-1 && lastMax) {
            Log.e("readable.filter", "init" )
            readable = ArrayList(readable.map { it.not() })
        }
        if (readable[0] && readable[1] && readable[2] && readable[3]  && (if(readable.size==5) readable[4] else true)) {
            lastMax=true
            rootview.lf_img.setBackgroundResource(R.mipmap.icon_tire_cancel)
            rootview.lr_img.setBackgroundResource(R.mipmap.icon_tire_cancel)
            rootview.rr_img.setBackgroundResource(R.mipmap.icon_tire_cancel)
            rootview.rf_img.setBackgroundResource(R.mipmap.icon_tire_cancel)
            rootview.img_all.setBackgroundResource(R.mipmap.img_wheel_n)
            if (readable.size == 5) {
                rootview.img_spare_tire.setBackgroundResource(R.mipmap.icon_tire_cancel)
            }
        } else {
            lastMax=false
            rootview.lf_img.setBackgroundResource(if (readable[0]) R.mipmap.img_wheel_n else R.mipmap.icon_tire_cancel)
            rootview.lr_img.setBackgroundResource(if (readable[1]) R.mipmap.img_wheel_n else R.mipmap.icon_tire_cancel)
            rootview.rr_img.setBackgroundResource(if (readable[2]) R.mipmap.img_wheel_n else R.mipmap.icon_tire_cancel)
            rootview.rf_img.setBackgroundResource(if (readable[3]) R.mipmap.img_wheel_n else R.mipmap.icon_tire_cancel)
            if (readable.size == 5) {
                rootview.img_spare_tire.setBackgroundResource(if (readable[4]) R.mipmap.img_wheel_n else R.mipmap.icon_tire_cancel)
            }
            rootview.img_all.setBackgroundResource(R.mipmap.icon_tire_cancel)
        }
        rootview.img_all.setBackgroundResource(if (readable.contains(false)) R.mipmap.icon_tire_cancel else R.mipmap.img_wheel_n)
    }

}