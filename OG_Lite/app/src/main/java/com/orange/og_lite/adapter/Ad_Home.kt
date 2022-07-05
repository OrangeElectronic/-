package com.orange.og_lite.adapter

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.og_lite.Frag.Frag_SettingPager_Setting
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.beans.HomeItem
import com.orange.og_lite.beans.PublicBeans
import kotlinx.android.synthetic.main.adapter_ad_home.view.*

class Ad_Home(val myitem: HomeItem) : JzAdapter(R.layout.adapter_ad_home)
{
    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.mView.button4.setBackgroundResource(myitem.item[position])

            holder.mView.textView9.text = (myitem.item2[position])

            holder.mView.textView9.setAlpha(myitem.item3[position])
        holder.mView.Home_R.setOnClickListener {
            if(myitem.change[position] != null)
            {
                PublicBeans.position = myitem.changePlace[position]
                    JzActivity.getControlInstance()
                        .changePage(myitem.change[position]!!, myitem.tag[position], true)
                    if(PublicBeans.position == PublicBeans.線上訂購){
                    val a= JzActivity.getControlInstance().getPro("Languages","en")
                    var uti="http://simple-sensor.com"
                    when(a){
                        "tw"->{ uti="http://simple-sensor.com"}
                        "zh"->{ uti="http://simple-sensor.com"}
                        "de"->{ uti="http://orange-rdks.de"}
                        "en"->{ uti="http://simple-sensor.com"}
                        "it"->{
                            JzActivity.getControlInstance().changePage(Frag_SettingPager_Setting(),"Frag_SettingPager_Setting",true)
                            return@setOnClickListener
                        }
                        "sk"->{ uti="http://simple-sensor.com"}
                        "da"->{ uti="http://simple-sensor.com"}
                    }
                    try {
                        val uri = Uri.parse(uti)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        mMainActivity.startActivity(intent)
                    }catch ( e: Exception){}
                }

                    //act.DaiLogDismiss()
                }
            }

    }

    override fun sizeInit(): Int {
         return myitem.item2.size
    }

}