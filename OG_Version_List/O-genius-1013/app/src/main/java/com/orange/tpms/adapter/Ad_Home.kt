package com.orange.tpms.adapter

import android.view.View
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.data_loading_false.view.*
import kotlinx.android.synthetic.main.homeitem.view.*

class Ad_Home(var size: ArrayList<HomeItem>) : JzAdapter(R.layout.homeitem) {
    var focus = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.fixLanguage()
        holder.mView.imag.setImageDrawable(JzActivity.getControlInstance().getRootActivity().getDrawable(size[position].img))
        holder.mView.setOnClickListener {
            size[position].onClickListener.onClick(null)
        }
        holder.mView.imag.isSelected=focus==position
        holder.mView.name.text = size[position].name
        var itemCount=size[position].itemCount()
        if(itemCount == 0){
            holder.mView.itemCount.visibility=View.GONE
        }else{
            holder.mView.itemCount.text=itemCount.toString()
            holder.mView.itemCount.visibility=View.VISIBLE
        }
    }

    override fun sizeInit(): Int {
        return size.size
    }
}

class HomeItem(var name: String, var img: Int,var onClickListener: View.OnClickListener,var itemCount:()->Int={0})