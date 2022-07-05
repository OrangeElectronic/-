package com.orange.og_lite.Frag

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.og_lite.Util.jzString
import com.orange.jzchi.jzframework.JzFragement
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Setting
import com.orange.og_lite.beans.SettingItem
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Util.getFix
import kotlinx.android.synthetic.main.fragment_frag__setting_pager__setting.view.*

/**
 * A simple [Fragment] subclass.
 */
class Frag_SettingPager_Setting : JzFragement(R.layout.fragment_frag__setting_pager__setting) {

    var myitem= SettingItem()
    lateinit var adapter: Ad_Setting

    override fun viewInit() {
        rootview.fixLanguage()
        myitem=SettingItem()
        adapter= Ad_Setting(myitem)

        rootview.SettingButtonView.layoutManager= GridLayoutManager(
            activity,
            1
        ) as RecyclerView.LayoutManager?
        rootview.SettingButtonView.adapter=adapter

        myitem.add(
            R.mipmap.btn_favourite_n,resources.jzString(R.string.app_my_favorite) ,"- "+resources.jzString(R.string.Add_or_remove_vehicles), View.VISIBLE,
            Frag_SettingPager_MyFavorite(),"Frag_SettingPager_MyFavorite")
        myitem.add(
            R.mipmap.btn_area_and_language,resources.jzString(R.string.AreaLanguage),"" , View.GONE,
            Frag_SettingPager_Set_Languages(1),"Frag_SettingPager_Set_Languages")
        myitem.add(
            R.mipmap.btn_device_information,"jz.246".getFix() ,"", View.VISIBLE,
            Frag_Device_Information(),"Frag_Device_Information")
        myitem.add(
            R.mipmap.btn_updata,"jz.153".getFix(),"", View.GONE ,
            Frag_SettingPager_Update(false),"Frag_SettingPager_Update")
        myitem.add(
            R.mipmap.btn_set_password,resources.jzString(R.string.Reset_Password),"" , View.GONE ,
            Frag_SettingPager_Set_Up_Password(),"Frag_SettingPager_Set_Up_Password")
        myitem.add(
            R.mipmap.btn_unit,resources.jzString(R.string.app_unit),"" , View.GONE ,
            Frag_SettingPager_Unit(),"Frag_SettingPager_Unit")
        myitem.add(
            R.mipmap.btn_auto_off,"jz.248".getFix(),"", View.GONE ,
            Frag_Auto_Lock(),"Frag_Auto_Lock")
        myitem.add(
            R.mipmap.btn_reset,resources.jzString(R.string.app_reset) ,"", View.GONE ,
            Frag_SettingPager_PrivaryPolicy(1),"reset")
        myitem.add(
            R.mipmap.btn_privacy_policy,"jz.71".getFix(),"", View.GONE ,
            Frag_SettingPager_PrivaryPolicy(1),"Frag_SettingPager_PrivaryPolicy")
        myitem.add(
            R.mipmap.btn_log_out,resources.jzString(R.string.Log_out) ,"", View.GONE ,
            Frag_SettingPager_PrivaryPolicy(1),"logout")

        adapter.notifyDataSetChanged()

    }
}
