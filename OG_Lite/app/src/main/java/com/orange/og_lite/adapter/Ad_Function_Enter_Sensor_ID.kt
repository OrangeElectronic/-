package com.orange.og_lite.adapter

import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import com.orange.og_lite.beans.Function_Enter_Sensor_ID_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Dia_SelectBaclk
import kotlinx.android.synthetic.main.adapter_ad_function_enter_sensor_id.view.*

class Ad_Function_Enter_Sensor_ID(val myitem:Function_Enter_Sensor_ID_Item,var callback: Dia_SelectBaclk):JzAdapter(R.layout.adapter_ad_function_enter_sensor_id)
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==0){
            holder.mView.firstbt.visibility= View.VISIBLE
            holder.mView.two_button.visibility=View.GONE
            holder.mView.imageView16.visibility = View.GONE
            holder.mView.textView46.text=myitem.item2[position]
        }else{
            holder.mView.firstbt.visibility= View.GONE
            holder.mView.two_button.visibility=View.VISIBLE
            holder.mView.imageView16.visibility = View.VISIBLE
        }
        holder.mView.Enter_sensor_ID_img.setImageResource(myitem.item[position])
        holder.mView.Enter_sensor_ID_title.text = myitem.item2[position]
        holder.mView.Enter_sensor_ID_title2.visibility = myitem.item3[position]
        holder.mView.Enter_sensor_ID_title2.text = myitem.item4[position]
        holder.mView.two_button.setOnClickListener {
            JzActivity.getControlInstance().closeDiaLog()
            when(myitem.condition[position])
            {
                Function_Enter_Sensor_ID_Item.Scan_Code ->
                {
                    PublicBeans.selectWay=PublicBeans.SCAN
                }
                Function_Enter_Sensor_ID_Item.Sensor_Read ->
                {
                    PublicBeans.selectWay=PublicBeans.READ
                }
                Function_Enter_Sensor_ID_Item.Key_In ->
                {
                    PublicBeans.selectWay=PublicBeans.KEY_IN
                }

            }
            callback.back()
        }
        holder.mView.firstbt.setOnClickListener {
            holder.mView.two_button.performClick()
        }

    }

    override fun sizeInit(): Int {
        return myitem.item.size
    }

}