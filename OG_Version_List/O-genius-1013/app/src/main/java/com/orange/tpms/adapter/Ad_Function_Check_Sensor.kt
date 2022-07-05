package com.orange.tpms.adapter

import android.view.View
import android.widget.RelativeLayout
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.tpms.widget.CarWidget
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.R
import com.orango.electronic.orange_og_lib.Util.OggUtils
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.adapter_ad_function_check_sensor_read.view.*
import kotlinx.android.synthetic.main.fragment_frag__check__sensor__read.view.*

class Ad_Function_Check_Sensor(val myitem: ArrayList<SensorData>, var carWidget: CarWidget, var motor: RelativeLayout) :
    JzAdapter(R.layout.adapter_ad_function_check_sensor_read) {
    var wheelCount = PublicBean.wheelCount()
    var isDec = PublicBean.isDec()
    var focus = 0
    var wheelP = arrayOf(
        "LF",
        "RF",
        "RR",
        "LR",
        "SP"
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.title.visibility = if (position == 0) View.VISIBLE else View.GONE
        holder.mView.title.textView63.text = SensorData.getPre().replace(":","")
        holder.mView.title.textView64.text = SensorData.getTem().replace(":","")
        holder.mView.setOnClickListener {
            focus = position
            this.notifyDataSetChanged()
        }
        if (focus == position) {
            holder.mView.Vehice_ID_text.setBackgroundResource(com.orange.tpms.R.color.green)
        } else {
            holder.mView.Vehice_ID_text.setBackgroundResource(com.orange.tpms.R.color.white)
        }
        if (position < myitem.size && myitem[position].success) {
            if (isDec) {
                var id = myitem[position].id
                while (id.length < 8) {
                    id = "0${id}"
                }
                holder.mView.Vehice_ID_text.text = "" + OggUtils.byteArrayToInt(id.hexToByte())
            } else {
                holder.mView.Vehice_ID_text.text = myitem[position].id
            }
            holder.mView.Psi_text.text = myitem[position].kpa.toString()
            holder.mView.C_text.text =
                if (!myitem[position].有無胎溫) "NA" else myitem[position].c.toString()
            if (myitem[position].有無電壓) {
                holder.mView.BAT_text.text = "${myitem[position].vol}"
            } else {
                holder.mView.BAT_text.text = myitem[position].bat
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
            holder.mView.Psi_text.text = ""
            holder.mView.C_text.text = ""
            holder.mView.BAT_text.text = ""
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