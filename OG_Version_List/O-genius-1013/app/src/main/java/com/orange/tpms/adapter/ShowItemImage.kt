package com.orange.tpms.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orango.electronic.orange_og_lib.PublicBean
import com.orange.tpms.ue.kt_frag.Frag_SelectModle
import com.orange.tpms.ue.kt_frag.kt_splash
import com.orange_electronic.orangeobd.mmySql.Util_MmySql_module
import java.util.*


class ShowItemImage(var makes: ArrayList<Util_MmySql_module>) : RecyclerView.Adapter<ShowItemImage.ViewHolder>() {
    var focus = 0


    internal var a = true



    // 建立ViewHolder

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val simpleDraweeView: ImageView

        init {
            simpleDraweeView = itemView.findViewById(R.id.make_item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.make_grid_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mipmapId = holder.itemView.context.resources!!.getIdentifier(
            makes[position].image,
            "mipmap",
            JzActivity.getControlInstance().getRootActivity().packageName
        )
        if(makes[position].image=="icon"&& kt_splash.isHella){
            mipmapId=holder.itemView.context.resources!!.getIdentifier(
                "hellaicon",
                "mipmap",
                JzActivity.getControlInstance().getRootActivity().packageName
            )
        }
        Glide.with(holder.itemView.context).load(mipmapId)
            .centerCrop().into(holder.simpleDraweeView)
        if (position == focus) {
            holder.simpleDraweeView.background = JzActivity.getControlInstance().getRootActivity().getDrawable(R.drawable.bg_32414e_r8)
        } else {
            holder.simpleDraweeView.background = JzActivity.getControlInstance().getRootActivity().getDrawable(R.color.white)
        }
        holder.simpleDraweeView.setOnTouchListener { v, event ->
            Log.e("event", event.toString())
            if (event.action == MotionEvent.ACTION_MOVE) {
                holder.simpleDraweeView.background = JzActivity.getControlInstance().getRootActivity().getDrawable(R.drawable.bg_32414e_r8)
            } else {
                holder.simpleDraweeView.background = JzActivity.getControlInstance().getRootActivity().getDrawable(R.color.white)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                PublicBean.selectMmy.make = makes[position].make
                PublicBean.selectMmy.displayMake = makes[position].make
                JzActivity.getControlInstance().changePage(Frag_SelectModle(), "Frag_SelectModle", true)
            }
            true
        }
    }


    //    }


    override fun getItemCount(): Int {

        return makes.size

    }

}