package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import com.orange.og_lite.beans.Function_Check_Sensor_Item
import com.orange.og_lite.beans.SensorData
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.jzString
import kotlinx.android.synthetic.main.adapter_ad_function_check_sensor_read.view.*

class Ad_Function_Check_Sensor(val myitem: Function_Check_Sensor_Item) :
    JzAdapter(R.layout.adapter_ad_function_check_sensor_read) {
    var position = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            holder.mView.WH_text.text = ""
            holder.mView.Vehice_ID_text.text = "ID"
            //holder.mView.Psi_text.text = holder.mView.context.resources.jzString(R.string.app_psi)
            holder.mView.Psi_text.text = "${SensorData.getPre()}".replace(":", "")
            //holder.mView.C_text.text = holder.mView.context.resources.jzString(R.string.app_c)
            holder.mView.C_text.text = "${SensorData.getTem()}".replace(":", "")
            holder.mView.BAT_text.text =
                holder.mView.context.resources.jzString(R.string.app_bat).replace(":", "")
            holder.mView.Volt_text.text =
                holder.mView.context.resources.jzString(R.string.app_voltage).replace(":", "")
//            holder.mView.WH_text.setBackgroundResource(R.color.qr_code_background_color)
//            holder.mView.WH_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            holder.mView.Vehice_ID_text.setBackgroundResource(R.color.qr_code_background_color)
            holder.mView.Vehice_ID_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            holder.mView.Psi_text.setBackgroundResource(R.color.qr_code_background_color)
            holder.mView.Psi_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            holder.mView.C_text.setBackgroundResource(R.color.qr_code_background_color)
            holder.mView.C_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            holder.mView.BAT_text.setBackgroundResource(R.color.qr_code_background_color)
            holder.mView.BAT_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            holder.mView.Volt_text.setBackgroundResource(R.color.qr_code_background_color)
            holder.mView.Volt_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
        } else {
            holder.mView.Vehice_ID_text.setBackgroundResource(R.color.white)
            holder.mView.Vehice_ID_text.setTextColor(holder.mView.context.resources.getColor(R.color.text_black))
            holder.mView.Psi_text.setBackgroundResource(R.color.white)
            holder.mView.Psi_text.setTextColor(holder.mView.context.resources.getColor(R.color.text_black))
            holder.mView.C_text.setBackgroundResource(R.color.white)
            holder.mView.C_text.setTextColor(holder.mView.context.resources.getColor(R.color.text_black))
            holder.mView.BAT_text.setBackgroundResource(R.color.white)
            holder.mView.BAT_text.setTextColor(holder.mView.context.resources.getColor(R.color.text_black))
            holder.mView.Volt_text.setBackgroundResource(R.color.white)
            holder.mView.Volt_text.setTextColor(holder.mView.context.resources.getColor(R.color.text_black))

            holder.mView.setOnClickListener {
                if (position != 0) {
                    myitem.focus = position
                    notifyDataSetChanged()
                }
            }

            holder.mView.Sensor_Data.layoutParams.height = 150
            holder.mView.Vehice_ID_text.text = myitem.ID[position]
            holder.mView.Psi_text.text = myitem.Psi[position]
            holder.mView.C_text.text = myitem.C[position]
            holder.mView.BAT_text.text = myitem.BAT[position]
            holder.mView.Volt_text.text = myitem.VOLT[position]
            when (position) {
                1 -> {
                    holder.mView.WH_text.text =
                        holder.mView.context.resources.jzString(R.string.app_fl)
                }
                2 -> {
                    holder.mView.WH_text.text =
                        holder.mView.context.resources.jzString(R.string.app_fr)
                }
                3 -> {
                    holder.mView.WH_text.text =
                        holder.mView.context.resources.jzString(R.string.app_rr)
                }
                4 -> {
                    holder.mView.WH_text.text =
                        holder.mView.context.resources.jzString(R.string.app_rl)
                }
                5 -> {
                    holder.mView.WH_text.text = "SP"
                }
            }

            if (myitem.focus == position) {
                holder.mView.Vehice_ID_text.setBackgroundResource(R.color.green)
                holder.mView.Vehice_ID_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
                holder.mView.Psi_text.setBackgroundResource(R.color.green)
                holder.mView.Psi_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
                holder.mView.C_text.setBackgroundResource(R.color.green)
                holder.mView.C_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
                holder.mView.BAT_text.setBackgroundResource(R.color.green)
                holder.mView.BAT_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
                holder.mView.Volt_text.setBackgroundResource(R.color.green)
                holder.mView.Volt_text.setTextColor(holder.mView.context.resources.getColor(R.color.white))
            }
        }

    }

    override fun sizeInit(): Int {
        return myitem.VOLT.size
    }

}