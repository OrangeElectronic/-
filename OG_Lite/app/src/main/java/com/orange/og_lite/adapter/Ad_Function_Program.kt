package com.orange.og_lite.adapter

import android.text.InputFilter
import android.util.Log
import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Dialog.Da_SoftKeyboard
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.R
import com.orange.og_lite.Util.KeyboardUtil
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.beans.Function_Program_Item
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.textchange
import kotlinx.android.synthetic.main.adapter_ad_function_program.view.*
import kotlinx.android.synthetic.main.fragment_frag__function__program.view.*

class Ad_Function_Program(val frag: Frag_Function_Program, val myitem: Function_Program_Item) :
    JzAdapter(R.layout.adapter_ad_function_program) {

    var position=-1
    var focus = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.mView.pre.text = myitem.壓力[position]
        holder.mView.tem.text = myitem.溫度[position]
        holder.mView.bat.text = myitem.電力[position]
        holder.mView.Tire_text.text = "${position + 1}"
        holder.mView.Tire_new_id.setText(myitem.programId[position])
        //holder.mView.Tire_check.setBackgroundResource(myitem.item3[position])

//        if (PublicBeans.selectWay == PublicBeans.KEY_IN) {
//            holder.mView.Tire_new_id.isEnabled = true
//            holder.mView.Tire_new_id.setTextIsSelectable(true)
//            holder.mView.Tire_new_id.isFocusable = true
//        }
//        if (PublicBeans.selectWay != PublicBeans.KEY_IN)
//        {
//            holder.mView.Tire_new_id.isEnabled = false
//            holder.mView.Tire_new_id.setTextIsSelectable(false)
//            holder.mView.Tire_new_id.isFocusable = false
//        }
        holder.mView.Tire_new_id.isEnabled = true
        holder.mView.Tire_new_id.setTextIsSelectable(false)
        holder.mView.Tire_new_id.isFocusable = false

        if (PublicBeans.selectWay == PublicBeans.SCAN) {
            holder.mView.background_r.visibility = View.VISIBLE
            holder.mView.background_r.setOnClickListener {

                PublicBeans.Program_position = position

                PublicBeans.getSensor(object : PublicBeans.sensorBack {
                    override fun callback(content: String) {
                        frag.insert(content, "NA", "NA", "NA")
                    }
                })
            }
        }
        else
        {
            //holder.mView.background_r.visibility = View.GONE
        }

        holder.mView.Tire_new_id.textchange = textchange {
            Log.e("isEmpty","true")
            myitem.programId[position] = holder.mView.Tire_new_id.text.toString()
            frag.checkComplete()
        }

        //holder.mView.Tire_new_id.isFocusable = PublicBeans.selectWay == PublicBeans.KEY_IN

        if (PublicBeans.selectWay == PublicBeans.KEY_IN) {

            holder.mView.background_r.setOnClickListener {
//                if (position == focus) {
//                    focus = -1
//                    this.notifyDataSetChanged()
//                    return@setOnClickListener
//                }
                focus = position
                this.position = position
                //Log.e("this.position",this.position.toString())

                PublicBeans.DialogKeyboard = true

                this.notifyDataSetChanged()
            }


            Log.e("focus","Da_SoftKeyboard : "+ focus)
            if(focus == position && focus!=-1)
            {
                Log.e("focus","Da_SoftKeyboard")
                holder.mView.Tire_new_id.isEnabled = true

                if (PublicBeans.selectWay == PublicBeans.KEY_IN && PublicBeans.DialogKeyboard) {

                    holder.mView.Tire_new_id.isFocusable = true
                    holder.mView.Tire_new_id.setTextIsSelectable(true)
                    holder.mView.Tire_new_id.requestFocus()
                    PublicBeans.DialogKeytext = holder.mView.Tire_new_id.text.toString()

                    JzActivity.getControlInstance().closeDiaLog("Da_SoftKeyboard")
                    JzActivity.getControlInstance().showBottomSheetDialog(true,true, Da_SoftKeyboard(this),"Da_SoftKeyboard")
                }
            }

            //this.notifyDataSetChanged()
        }

//        if(!PublicBeans.DialogKeyboard)
//        {
//            PublicBeans.DialogKeyboard = true
//        }

        when (myitem.state[position]) {
            Function_Program_Item.PROGRAM_WAIT -> {
                holder.mView.pre.text = ""
                holder.mView.tem.text = ""
                holder.mView.bat.text = ""
                holder.mView.situation.visibility = View.GONE
                holder.mView.situation.setImageResource(R.color.white)
            }
            Function_Program_Item.PROGRAM_SUCCESS -> {
                holder.mView.situation.visibility = View.VISIBLE
                holder.mView.situation.setImageResource(R.mipmap.icon_check_sensor_ok)
            }
            Function_Program_Item.PROGRAM_FALSE -> {
                holder.mView.pre.text = "NA"
                holder.mView.situation.visibility = View.VISIBLE
                holder.mView.situation.setImageResource(R.mipmap.icon_check_sensor_fail)
            }
        }

        holder.mView.Tire_new_id.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(myitem.idcount))
        KeyboardUtil.hideEditTextKeyboard(holder.mView.Tire_new_id)
        //holder.mView.Vehice_ID.filters= arrayOf<InputFilter>(InputFilter.LengthFilter(beans.idcount))
    }

    override fun sizeInit(): Int {
        return myitem.rowcount
    }

}