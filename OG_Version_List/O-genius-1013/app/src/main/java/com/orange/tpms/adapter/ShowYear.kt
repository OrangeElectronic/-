package com.orange.tpms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orange.tpms.R
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.PublicBean
import java.util.*


class ShowYear(private val years: ArrayList<String>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ShowYear.ViewHolder>() {
    var focus=0
    var favorite= ArrayList<String>()
    lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.text_item.text=years[position]
        if (position == focus) {
            holder.mView.background = context.getDrawable(R.color.color_orange)
        } else {
            holder.mView.background = context.getDrawable(R.color.color_unite_bg)
        }
        holder.mView.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_MOVE){
                holder.mView.background = context.getDrawable(R.color.color_orange)
            }else{
                holder.mView.background = context.getDrawable(R.color.color_unite_bg)
            }
            if(event.action == MotionEvent.ACTION_UP){
                PublicBean.selectMmy.year=years[position]
                PublicBean.selectMmy.displayYear=years[position]
                PublicBean.setFavorite(
                    PublicBean.selectMmy.make,
                    PublicBean.selectMmy.model,
                    PublicBean.selectMmy.year)
                ChangePageService.changePage()
            }
            true
        }
    }

    override fun getItemCount(): Int = years.size

    inner class ViewHolder(val mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
        val text_item: TextView = mView.findViewById(R.id.text_item)
        override fun toString(): String {
            return super.toString() + " '" + text_item.text + "'"
        }
    }
}