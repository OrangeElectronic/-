package com.orange.tpms.adapter

import android.text.InputFilter
import android.util.Log
import android.view.View
import com.airbnb.lottie.utils.Logger.error
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.tpms.R
import com.orange.tpms.ue.kt_frag.Frag_New_ProgramDetail
import com.orange.tpms.utils.KeyboardUtil
import com.orango.electronic.orange_og_lib.Callback.textchange
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.OggUtils
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.beans.SensorData
import kotlinx.android.synthetic.main.item_program_detail.view.*

class Ad_Program_Adpter(var sensorData: ArrayList<SensorData>, var frag: Frag_New_ProgramDetail):JzAdapter(R.layout.item_program_detail) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.mView.toper.textView66.text = SensorData.getPre().replace(":","")
            holder.mView.toper.textView67.text = SensorData.getTem().replace(":","")
            holder.setIsRecyclable(false)
            if (position == 0) {
                holder.mView.toper.visibility = View.VISIBLE
                holder.mView.toper.fixLanguage()
            } else {
                holder.mView.toper.visibility = View.GONE
            }
            if (position == itemCount - 1) {
                holder.mView.view18.visibility = View.VISIBLE
            }
            //全部大写
            holder.mView.Tire_new_id.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(8))
            //不显示软键盘
            KeyboardUtil.hideEditTextKeyboard(holder.mView.Tire_new_id)
            holder.mView.Tire_text.text = (position + 1).toString()
            if(frag.upload){
                holder.mView.Tire_text.text = sensorData[position].wheelString
            }
            val state = sensorData[position].programResult
            if (state == SensorData.STATE_NORMAL) {
                holder.mView.situation.visibility = View.GONE
                holder.mView.situation.setImageResource(R.color.white)
            } else if (state == SensorData.STATE_SUCCESS) {
                holder.mView.situation.visibility = View.VISIBLE
                holder.mView.situation.setImageResource(R.mipmap.icon_correct)
            } else if (state == SensorData.STATE_FAILED) {
                holder.mView.situation.visibility = View.VISIBLE
                holder.mView.situation.setImageResource(R.mipmap.error)
            }
            holder.mView.Tire_new_id.setText(sensorData[position].id)
            holder.mView.Tire_new_id.textchange = textchange {
                OggUtils.hide_keyboard_from(holder.mView.Tire_new_id);
                sensorData[position].id = holder.mView.Tire_new_id.text!!.toString()
            }
            if(state == SensorData.STATE_SUCCESS){
                holder.itemView.pre.text = if (sensorData[position].success) sensorData[position].kpa.toString() else "NA"
                holder.itemView.tem.text = if (sensorData[position].success && sensorData[position].有無胎溫) sensorData[position].c.toString() else "NA"
                holder.itemView.bat.text = if (sensorData[position].success && sensorData[position].有無電池) sensorData[position].bat else "NA"
            }else{
                holder.itemView.pre.text = ""
                holder.itemView.tem.text = ""
                holder.itemView.bat.text = ""
            }
        }catch (e:Exception){}
    }

    override fun sizeInit(): Int {
        return PublicBean.ProgramNumber
    }
}