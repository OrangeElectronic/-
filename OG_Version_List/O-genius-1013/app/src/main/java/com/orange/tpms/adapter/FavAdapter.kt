package com.orange.tpms.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.service.ChangePageService
import com.orange.tpms.ue.activity.KtActivity
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.favorite
import java.util.*

class FavAdapter(private var name: ArrayList<favorite>, val go: Boolean) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FavAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text =
            name.get(position).make + "/" + name.get(position).model + "/" + name.get(position).year
        if (go) {
            holder.delete.visibility = View.GONE
            holder.mView.setOnClickListener {
                PublicBean.selectMmy.make=name[position].make
                PublicBean.selectMmy.model=name[position].model
                PublicBean.selectMmy.year=name[position].year
                PublicBean.selectMmy.displayMake=name[position].make
                PublicBean.selectMmy.displayModel=name[position].model
                PublicBean.selectMmy.displayYear=name[position].year
                Log.e(
                    "select",
                    PublicBean.selectMmy.make + PublicBean.selectMmy.model + PublicBean.selectMmy.year
                )
                ChangePageService.changePage()
            }
        } else {
            holder.delete.visibility = View.VISIBLE
            holder.delete.setOnClickListener {
               PublicBean.deleteFavorite(name[position].make, name[position].model, name[position].year)
                name=PublicBean.getFavorite()
                this.notifyDataSetChanged()
            }
        }


    }

    override fun getItemCount(): Int = name.size

    inner class ViewHolder(val mView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
        val name: TextView = mView.findViewById(R.id.textView22)
        val delete: ImageView = mView.findViewById(R.id.imageView2)
        override fun toString(): String {
            return super.toString() + " ''"
        }
    }

}