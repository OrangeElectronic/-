package com.orange.og_lite.adapter

import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.R
import kotlinx.android.synthetic.main.adapter_device.view.*

class Device(var name:String,var version:String)
class Ad_Device(var item:ArrayList<Device>) :JzAdapter(R.layout.adapter_device){
    override fun sizeInit(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.mView.name.text=item[position].name
        holder.mView.version.text=item[position].version
    }

}