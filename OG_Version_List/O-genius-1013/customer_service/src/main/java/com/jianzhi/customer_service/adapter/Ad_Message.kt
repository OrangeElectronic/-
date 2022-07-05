package com.jianzhi.customer_service.adapter

import android.view.View
import com.jianzhi.customer_service.Page_Show_Image
import com.jianzhi.customer_service.R
import com.jianzhi.customer_service.ServerCommand
import com.jianzhi.customer_service.beans.getFix
import com.jianzhi.customer_service.frag.Frag_UserMessage
import com.jianzhi.customer_service.message
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orango.electronic.jzutil.CalculateTime
import com.orango.electronic.jzutil.unicodeToString
import kotlinx.android.synthetic.main.in_message.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Ad_Message(var item: ArrayList<message>) : JzAdapter(R.layout.in_message) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.me.visibility= View.GONE
        holder.mView.you.visibility= View.GONE
        if(item[position].admin==ServerCommand.serial){
            holder.mView.me.visibility= View.VISIBLE
            holder.mView.meimage.visibility=if(item[position].file=="nodata") View.GONE else View.VISIBLE
            holder.mView.pome.text=item[position].message.unicodeToString()
            if(item[position].file!="nodata"){
                holder.mView.imageme.setImageURI(ServerCommand.server+item[position].file)
            }
            holder.mView.meimage.setOnClickListener {
                JzActivity.getControlInstance().changePage(Page_Show_Image(ServerCommand.server+item[position].file),"Page_Show_Image",true)
            }
            holder.mView.timeme.text= calculateTime(item[position].time)
        }else{
            holder.mView.you.visibility=View.VISIBLE
            holder.mView.name.text=item[position].pick
            holder.mView.po.text=item[position].message.unicodeToString()
             holder.mView.time.text= calculateTime(item[position].time)
            holder.mView.youimage.visibility=if(item[position].file=="nodata") View.GONE else View.VISIBLE
            if(item[position].file!="nodata"){
                holder.mView.image.setImageURI(ServerCommand.server+item[position].file)
            }
            holder.mView.youimage.setOnClickListener {
                JzActivity.getControlInstance().changePage(Page_Show_Image(ServerCommand.server+item[position].file),"Page_Show_Image",true)
            }
        }
if(position >= 50){
    val frag=JzActivity.getControlInstance().getNowPage()
    if(frag is Frag_UserMessage){
        frag.getMessage()
    }
}
    }

    override fun sizeInit(): Int {
        return item.size
    }
   fun calculateTime(a:String): String {
        if (JzActivity.getControlInstance().getLanguage() != null) {
            JzActivity.getControlInstance().setLanguage(JzActivity.getControlInstance().getLanguage()!!)
        }
        val nowTime = System.currentTimeMillis() // 获取当前时间的毫秒数
        var msg: String = "jz.222".getFix()
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 指定时间格式
        var setTime: Date? = null // 指定时间
        try {
            setTime = sdf.parse(a) // 将字符串转换为指定的时间格式
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val reset = setTime!!.time // 获取指定时间的毫秒数
        val dateDiff = nowTime - reset
        if (dateDiff < 0) {
            msg = "jz.222".getFix()
        } else {
            val dateTemp1 = dateDiff / 1000 // 秒
            val dateTemp2 = dateTemp1 / 60 // 分钟
            val dateTemp3 = dateTemp2 / 60 // 小时
            val dateTemp4 = dateTemp3 / 24 // 天数
            val dateTemp5 = dateTemp4 / 30 // 月数
            val dateTemp6 = dateTemp5 / 12 // 年数
            if (dateTemp6 > 0) {
                msg = "jz.227".getFix().replace("1", dateTemp6.toString())
            } else if (dateTemp5 > 0) {
                msg = "jz.226".getFix().replace("1", dateTemp5.toString())
            } else if (dateTemp4 > 0) {
                msg = "jz.221".getFix().replace("1", dateTemp4.toString())
            } else if (dateTemp3 > 0) {
                msg = "jz.225".getFix().replace("1", dateTemp3.toString())
            } else if (dateTemp2 > 0) {
                msg =  "jz.220".getFix().replace("1", dateTemp2.toString())
            } else if (dateTemp1 > 0) {
                msg = "jz.222".getFix()
            }
        }
        return msg
    }
}