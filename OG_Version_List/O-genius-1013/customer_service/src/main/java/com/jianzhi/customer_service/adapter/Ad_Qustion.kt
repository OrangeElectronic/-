package com.jianzhi.customer_service.adapter

import android.view.View
import com.example.customerlibrary.Dialog.GetSelection
import com.jianzhi.customer_service.beans.PublicBeans
import com.jianzhi.customer_service.R
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import kotlinx.android.synthetic.main.ad_quastion.view.*

class Ad_Qustion(var qustion: ArrayList<Qustion>) :JzAdapter(R.layout.ad_quastion){
    override fun sizeInit(): Int {
           return qustion.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.mView.setOnClickListener {  }
        if(position==qustion.size-1){
            holder.mView.textView6.visibility= View.VISIBLE
            holder.mView.textView3.visibility= View.GONE
            holder.mView.imageView3.visibility= View.GONE
        }else{
            holder.mView.setOnClickListener {
                JzActivity.getControlInstance().showBottomSheetDialog(true,false, GetSelection(
                    PublicBeans.instance.getQustion(qustion[position].id)),"GetSelection")
//                for(i in PublicBeans.instance.getQustion(qustion[position].id)){
//                    Log.e("取得問題",i.name)
//                }
            }
            holder.mView.textView3.text=qustion[position].text
            holder.mView.imageView3.setImageResource(qustion[position].img)
            holder.mView.textView3.visibility= View.VISIBLE
            holder.mView.imageView3.visibility= View.VISIBLE
            holder.mView.textView6.visibility= View.GONE
        }

    }

}
class Qustion(var id:String,var text:String,var img:Int)