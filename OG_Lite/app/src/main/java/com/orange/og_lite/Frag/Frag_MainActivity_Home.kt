package com.orange.og_lite.Frag

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange.og_lite.Util.jzString
import com.jzsql.lib.mmySql.InitCaller
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.MainActivity
import com.orange.og_lite.R
import com.orange.og_lite.adapter.Ad_Home
import com.orange.og_lite.beans.HomeItem
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Command
import com.orange.og_lite.Util.getFix
import kotlinx.android.synthetic.main.check_update.*
import kotlinx.android.synthetic.main.fragment_frag_home.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
class Frag_MainActivity_Home : JzFragement(R.layout.fragment_frag_home) {
    val mMainActivity=JzActivity.getControlInstance().getRootActivity() as MainActivity
    var myitem= HomeItem()
    lateinit var adapter: Ad_Home
    var havemmy=false

    override fun viewInit() {
        rootview.fixLanguage()
        refresh=true
        myitem=HomeItem()
        adapter= Ad_Home(myitem)

        rootview.HomeButtonView.layoutManager= GridLayoutManager(
            activity,
            2
        ) as RecyclerView.LayoutManager?
        rootview.HomeButtonView.adapter=adapter
        myitem.add(
            R.mipmap.btn_check_sensor_n, resources.jzString(R.string.app_home_check_sensor),1f,
            Frag_Function_Selection(0),"",PublicBeans.讀取)
        myitem.add(
            R.mipmap.btn_program_n, "jz.12".getFix(),1f,
            Frag_Function_Selection(0),"Frag_Function_Selection",PublicBeans.燒錄)
        myitem.add(
            R.mipmap.btn_id_copy_n, resources.jzString(R.string.ID_COPY),1f,
            Frag_Function_Selection(0),"Frag_Function_Selection",PublicBeans.複製ID)
        myitem.add(
            R.mipmap.btn_obdii_relearn_n, resources.jzString(R.string.app_home_obdii_relearn),1f,
            Frag_Function_Selection(1),"Frag_Function_Selection",PublicBeans.OBD學碼)
        myitem.add(
            R.mipmap.btn_id_copy_by_obd_n, resources.jzString(R.string.idcopyobd),1f,
            Frag_Function_Selection(1),"Frag_Function_Selection",PublicBeans.OBD複製)
if(JzActivity.getControlInstance().getPro("Language","Italiano")!="Italiano"){
    myitem.add(R.mipmap.btn_online_shopping_n, resources.jzString(R.string.app_home_online_shopping),1f, Frag_MainActivity_Home(),"Frag_MainActivity_Home",PublicBeans.線上訂購)
}
        myitem.add(
            R.mipmap.btn_relearnprocedure_p, "jz.135".getFix(),1f,
            Frag_Function_Selection(1),"Frag_Function_Selection",PublicBeans.原廠學碼步驟)
        myitem.add(
            R.mipmap.btn_setting_n, resources.jzString(R.string.app_setting),1f,
            Frag_SettingPager_Setting(),"Frag_SettingPager_Setting",0)
        myitem.add(R.mipmap.btn_users_manual_p, resources.jzString(R.string.app_user_manual),0.5f, null,"",0)
        adapter.notifyDataSetChanged()
//        if(!havemmy){db_download()}
    }

    override fun onResume() {
        super.onResume()
    }

}
