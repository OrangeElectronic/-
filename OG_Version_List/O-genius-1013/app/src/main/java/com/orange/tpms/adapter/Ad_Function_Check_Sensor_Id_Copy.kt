package com.orange.tpms.adapter

import android.view.View
import android.widget.RelativeLayout
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.tpms.R
import com.orange.tpms.ue.kt_frag.Frag_Check_Sensor_Read_IdCopy_Selection
import com.orange.tpms.widget.CarWidget
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.OggUtils
import com.orango.electronic.orange_og_lib.Util.getFix
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.ad_function_check_sensor_copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__copy.view.*
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.*
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.fw
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.read
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.rw

class Ad_Function_Check_Sensor_Id_Copy(
    val myitem: ArrayList<SensorData>,
    var carWidget: CarWidget,
    var motor: RelativeLayout,
    var frag: Frag_Check_Sensor_Read_IdCopy_Selection
) :
    JzAdapter(R.layout.ad_function_check_sensor_copy) {
    var wheelCount = PublicBean.wheelCount()
    var isDec = PublicBean.isDec()

    var focus :Int =0
        set(value) {
            field=value
            frag.handler.post {
                frag.rootview.read.text = if(value==-1) "jz.117".getFix() else "jz.404".getFix()
            }
    }

    var wheelP = arrayOf(
        "LF",
        "RF",
        "RR",
        "LR",
        "SP"
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.title.visibility = if (position == 0) View.VISIBLE else View.GONE
        holder.mView.setOnClickListener {
            if(focus==position){
                if(myitem.any { it.id.isNotEmpty() }){focus=-1}
            }else{
                focus = position
            }
            this.notifyDataSetChanged()
        }
        if (focus == position) {
            holder.mView.Vehice_ID_text.setBackgroundResource(com.orange.tpms.R.color.green)
            holder.mView.Vehice_ID_text.setTextColor(
                JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.white)
            )
        } else {
            holder.mView.Vehice_ID_text.setBackgroundResource(com.orange.tpms.R.color.white)
            holder.mView.Vehice_ID_text.setTextColor(
                JzActivity.getControlInstance().getRootActivity().resources.getColor(R.color.black)
            )
        }
        if (position < myitem.size && myitem[position].id.isNotEmpty()) {
            if (isDec) {
                var id = myitem[position].id
                while (id.length < 8) {
                    id = "0${id}"
                }
                holder.mView.Vehice_ID_text.text = "" + OggUtils.byteArrayToInt(id.hexToByte())
            } else {
                holder.mView.Vehice_ID_text.text = myitem[position].id
            }
            val arrp = arrayOf(
                CarWidget.CAR_LOCATION.TOP_LEFT,
                CarWidget.CAR_LOCATION.TOP_RIGHT,
                CarWidget.CAR_LOCATION.BOTTOM_RIGHT,
                CarWidget.CAR_LOCATION.BOTTOM_LEFT
            )
            if (myitem.size < 6) {
                if (wheelP.indexOf(myitem[position].wheelPosition) < 4) {
                    carWidget.setCarStatus(
                        arrp[wheelP.indexOf(myitem[position].wheelPosition)],
                        CarWidget.CAR_STATUS.NORMAL
                    )
                }
            }
        } else {
            holder.mView.Vehice_ID_text.text = ""
            val arrp = arrayOf(
                CarWidget.CAR_LOCATION.TOP_LEFT,
                CarWidget.CAR_LOCATION.TOP_RIGHT,
                CarWidget.CAR_LOCATION.BOTTOM_RIGHT,
                CarWidget.CAR_LOCATION.BOTTOM_LEFT
            )
            if (myitem.size < 6) {
                if (wheelP.indexOf(myitem[position].wheelPosition) < 4) {
                    carWidget.setCarStatus(
                        arrp[wheelP.indexOf(myitem[position].wheelPosition)],
                        if (focus != position) CarWidget.CAR_STATUS.NOTINIT else CarWidget.CAR_STATUS.SELECT
                    )
                }
            }
        }
        when (position) {
            0 -> {
                if (wheelCount == 2) {
                    if (position < myitem.size && myitem[position].success) {
                        motor.fw.setImageResource(R.mipmap.img_motorcycle_wheel_g)
                    } else {
                        motor.fw.setImageResource(if (focus != position) R.mipmap.img_motorcycle_wheel_normal else R.mipmap.img_motorcyclewheel_select)
                    }

                }
            }
            1 -> {
                if (wheelCount == 2) {
                    if (position < myitem.size && myitem[position].success) {
                        motor.rw.setImageResource(R.mipmap.img_motorcycle_wheel_g)
                    } else {
                        motor.rw.setImageResource(if (focus != position) R.mipmap.img_motorcycle_wheel_normal else R.mipmap.img_motorcyclewheel_select)
                    }
                }
            }
        }
        if (myitem[position].wheelString.isNotEmpty()) {
            if (PublicBean.wheelCount() == 2) {
                holder.mView.WH_text.text = arrayOf("F", "R")[position]
            } else {
                holder.mView.WH_text.text = myitem[position].wheelString
            }
        }


    }

    override fun sizeInit(): Int {
        return myitem.size
    }

}