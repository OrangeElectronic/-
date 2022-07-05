package com.jianzhi.customer_service.adapter

import android.view.View
import com.example.customerlibrary.beans.selectBeabs
import com.facebook.drawee.gestures.GestureDetector
import com.jianzhi.customer_service.R
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import kotlinx.android.synthetic.main.adapter_qustion.view.*
import kotlinx.android.synthetic.main.in_message.view.*
import java.util.*

class Ad_Select(var selectBeabs: selectBeabs,val click: View.OnClickListener) :JzAdapter(R.layout.adapter_qustion){
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.mView.image.setActualImageResource(selectBeabs.image[position])
        holder.mView.textView5.text=selectBeabs.text[position]
        holder.mView.setOnClickListener{
            click.onClick(it)
        }
    }

    override fun sizeInit(): Int {
       return selectBeabs.name.size
    }
}