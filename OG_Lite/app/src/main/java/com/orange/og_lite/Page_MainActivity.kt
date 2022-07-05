package com.orange.og_lite

import android.app.Dialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzFragement
import com.orange.jzchi.jzframework.callback.SetupDialog
import com.orange.og_lite.Dialog.Da_Logout
import com.orange.og_lite.Frag.Frag_MainActivity_Home
import com.orange.og_lite.Frag.Frag_SettingPager_Set_Languages
import com.orange.og_lite.Util.Util_FileDowload
import com.orange.og_lite.beans.PublicBeans
import com.orange.og_lite.callback.Update_C
import kotlinx.android.synthetic.main.page_activity.view.*
import android.R.attr.bottom
import android.R.attr.right
import android.R.attr.top
import android.R.attr.left
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.orange.og_lite.Util.jzString
import com.orange.og_lite.Util.fixLanguage
import com.orange.og_lite.Command.Companion.updateOgMcu
import com.orange.og_lite.Dialog.Da_InsertAndRemove_tool
import com.orange.og_lite.Frag.Frag_Function_Program
import com.orange.og_lite.Frag.Frag_Show_Memory
import com.orange.og_lite.Util.getFix


class Page_MainActivity : JzFragement(R.layout.page_activity) {

    val mMainActivity = JzActivity.getControlInstance().getRootActivity() as MainActivity

    companion object{
        var toggleMenu:(a:Boolean)->Unit = {}
    }
    override fun viewInit() {
        toggleMenu={
            handler.post {
                val it2=it
                if(it){
                    rootview.LeftTopButton.setImageResource(R.mipmap.menu)
                }else{
                    rootview.LeftTopButton.setImageResource(R.mipmap.btn_back_normal)
                }
                rootview.LeftTopButton.setOnClickListener {
                    if(it2){
                        JzActivity.getControlInstance().goMenu()
                    }else{
                        JzActivity.getControlInstance().goBack()
                    }
                }
            }
        }
        rootview.LeftTopButton.visibility = View.GONE
        rootview.RightTopButton.visibility = View.GONE
        rootview.MainTitle.text = ""
        rootview.LeftTopButton.setOnClickListener { }

        if (JzActivity.getControlInstance().getPro("admin", "nodata") == "nodata") {
            JzActivity.getControlInstance().changePage(
                Frag_SettingPager_Set_Languages(
                    0
                ),  "Frag_SettingPager_Set_Languages_first", false
            )
        } else {
            rootview.frage.setBackgroundColor(resources.getColor(R.color.backgroung))
            JzActivity.getControlInstance()
                .changePage(Frag_MainActivity_Home(),"Frag_MainActivity_Home", false)
        }
    }

    fun setMenuBt() {
        rootview.LeftTopButton.setImageResource(R.mipmap.menu)
        rootview.LeftTopButton.setOnClickListener { JzActivity.getControlInstance().goMenu() }
    }

    fun changePageListener(tag: String, frag: Fragment) {
        Log.e("switch", tag)
        if (frag is JzFragement) {
            Thread {
                while (!frag.haveRootView()) {
                }
                handler.post { frag.rootview.fixLanguage() }
            }.start()
        }
        if(PublicBeans.beta){
            rootview.toolbar.setBackgroundColor(resources.getColor(R.color.Blue))
        }else{
            rootview.toolbar.setBackgroundColor(resources.getColor(R.color.title_color))
        }
        val nowpage = JzActivity.getControlInstance().getNowPage()
        if (nowpage is Frag_Function_Program && nowpage.haveProgram) {
            setMenuBt()
        } else {
            rootview.LeftTopButton.setImageResource(R.mipmap.btn_back)
            rootview.LeftTopButton.setOnClickListener { JzActivity.getControlInstance().goBack() }
        }
        rootview.LeftTopButton.visibility = View.VISIBLE
        rootview.RightTopButton.visibility = View.GONE
        when (JzActivity.getControlInstance().getPro("battery", "03")) {
            "00" -> {
                rootview.RightTopButton.setImageResource(R.mipmap.icon_replace)
            }
            "01" -> {
                rootview.RightTopButton.setImageResource(R.mipmap.icon_low)
            }
            "02" -> {
                rootview.RightTopButton.setImageResource(R.mipmap.icon_ok)
            }
            "03" -> {
                rootview.RightTopButton.setImageResource(R.mipmap.icon_full)
            }
            "FF" -> {
                JzActivity.getControlInstance()
                    .showDiaLog(R.layout.replace_battery, true, false, "replace_battery")
            }
        }



        when(Util_FileDowload.country){
            "EU"->{
                rootview.imageView17.setImageResource(R.mipmap.icon_eu)
            }
            "US"->{
                rootview.imageView17.setImageResource(R.mipmap.icon_na)
            }
            "TW"->{
                rootview.imageView17.setImageResource(R.mipmap.icon_tw)
            }
        }


        if (mMainActivity.manager.Ble_Helper.isConnect()) {
            rootview.RightTopButton.visibility = View.VISIBLE

        }

        when (tag) {
            "Frag_SettingPager_Set_Languages_first" -> {
                rootview.MainTitle.text = resources.jzString(R.string.AreaLanguage)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Set_Languages_first_Live" -> {
                //rootview.MainTitle.text = resources.jzString(R.string.AreaLanguage)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_PrivaryPolicy_first" -> {
                rootview.MainTitle.text = resources.jzString(R.string.app_setting_privacy_policy)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Sign_in" -> {
                rootview.MainTitle.text = resources.jzString(R.string.app_sign_in)
                rootview.LeftTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Registration" -> {
                rootview.MainTitle.text = resources.jzString(R.string.Registration)
            }

            "Frag_SettingPager_ResetPass" -> {
                rootview.MainTitle.text = resources.jzString(R.string.Reset_Password)
            }

            "Frag_MainActivity_Home" -> {
                rootview.LeftTopButton.visibility = View.GONE
                rootview.MainTitle.text = "O-Genius Lite"
                rootview.RightTopButton.visibility = View.VISIBLE
            }

            "Frag_Function_Selection" -> {
                when (PublicBeans.position) {
                    PublicBeans.OBD複製 -> {
                        rootview.MainTitle.text = resources.jzString(R.string.idcopyobd)
                    }
                    PublicBeans.OBD學碼 -> {
                        rootview.MainTitle.text =
                            resources.jzString(R.string.app_home_obdii_relearn)
                    }
                    PublicBeans.燒錄 -> {
                        rootview.MainTitle.text = "jz.12".getFix()
                    }
                    PublicBeans.複製ID -> {
                        rootview.MainTitle.text = resources.jzString(R.string.ID_COPY)
                    }
                    PublicBeans.讀取 -> {
                        rootview.MainTitle.text = resources.jzString(R.string.app_home_check_sensor)
                    }
                }
            }
        }

        when (tag) {
            "Frag_SettingPager_Setting" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.app_setting)
            }

            "Frag_SettingPager_MyFavorite" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.app_my_favorite)
            }

            "Frag_SettingPager_AddMyFavorite" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.app_my_favorite)
            }

            "Frag_SettingPager_Set_Languages_Live" -> {
                rootview.RightTopButton.visibility = View.GONE
            }

            "Frag_SettingPager_Set_Languages" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.AreaLanguage)
            }

            "Frag_SettingPager_Update" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.update)
            }

            "Frag_SettingPager_Set_Up_Password" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.Reset_Password)
            }

            "Frag_SettingPager_PrivaryPolicy" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = resources.jzString(R.string.app_setting_privacy_policy)
            }

            "Frag_Device_Information" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = "jz.246".getFix()
            }

            "Frag_Auto_Lock" -> {
                rootview.RightTopButton.visibility = View.GONE
                rootview.MainTitle.text = "jz.248".getFix()
            }

        }

//        if(rootview.RightTopButton.visibility == View.GONE && rootview.LeftTopButton.visibility != View.GONE)
//        {
//            rootview.MainTitle.setPadding(-200,0,0,0)
//        }
//        else if(rootview.RightTopButton.visibility == View.GONE && rootview.LeftTopButton.visibility == View.GONE)
//        {
//            rootview.MainTitle.setPadding(-100,0,0,0)
//        }
//        else
//        {
//            rootview.MainTitle.setPadding(0,0,0,0)
//
//        }

    }
}