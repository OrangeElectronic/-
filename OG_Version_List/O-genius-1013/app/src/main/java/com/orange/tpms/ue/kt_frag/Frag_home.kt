package com.orange.tpms.ue.kt_frag


import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JavaScriptInterFace
import com.jianzhi.glitter.JsInterFace
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.Api_Tire_Hotel
import com.orange.tpms.R
import com.orange.tpms.RequestFun
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.Ad_Home
import com.orange.tpms.adapter.HomeItem
import com.orange.tpms.app.TPMSApp
import com.orange.tpms.ue.dialog.EmptyDialog
import com.orange.tpms.ue.tire_hotel.Frag_Hotel_First_Info
import com.orange.tpms.ue.tire_hotel.Frag_TireHotel_List
import com.orange.tpms.utils.DataCleanManager
import com.orango.electronic.orange_og_lib.Command.Cmd_Rfid
import com.orango.electronic.orange_og_lib.OgPublic
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.*
import com.orango.electronic.orange_og_lib.beans.Md_Tire_Hotel
import kotlinx.android.synthetic.main.activity_frag_home.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map.Entry


class Frag_home : RootFragement(R.layout.activity_frag_home) {
    var homeItem = ArrayList<HomeItem>()
    var adapter = Ad_Home(homeItem)
    override fun viewInit() {
        rootview.fixLanguage()
        refresh = true
        homeItem.clear()
        homeItem.add(
            HomeItem("jz.231".getFix(),
                R.drawable.bt_check_sensor_selecter,
                {
                    PublicBean.position = PublicBean.檢查傳感器
                    JzActivity.getControlInstance()
                        .changePage(Frag_Select_Mmy(), "Frag_CheckSensor", true)
                })
        )
        homeItem.add(
            HomeItem(resources.jzString(R.string.app_home_program_sensor_phone),
                R.drawable.bt_program_sensor_selecter,
                {
                    PublicBean.position = PublicBean.燒錄傳感器
                    JzActivity.getControlInstance()
                        .changePage(Frag_Select_Mmy(), "Frag_Program_Sensor", true)
                })
        )
        homeItem.add(
            HomeItem(resources.jzString(R.string.app_home_id_copy), R.drawable.bt_id_copy_selecter,
                {
                    PublicBean.position = PublicBean.複製傳感器
                    JzActivity.getControlInstance()
                        .changePage(Frag_Select_Mmy(), "Frag_Id_Copy", true)
                })
        )
//        homeItem.add(
//            HomeItem("OSOM", R.drawable.bt_hotel, {
//                DataCleanManager().cleanCache(context)
//                GlitterActivity.setUp("file:///android_asset/TireStorage", appName = "TireStorage")
////                    GlitterActivity.setUp("http://192.168.43.61/tireHotel", appName = "TireStorage")
//                val intent = Intent(activity, GlitterActivity::class.java)
//                startActivity(intent)
//            })
//        )
        homeItem.add(
            HomeItem("jz.538".getFix(), R.drawable.bt_hotel, {
                BluetoothAdapter.getDefaultAdapter().enable()
                if(!TPMSApp.TestMode){
                    Systool.openGPS(true)
                }
                Systool.hookWebView()
                GlitterActivity.setUp("file:///android_asset/TireStorage", appName = "TireStorage")
                val intent = Intent(activity, GlitterActivity::class.java)
                startActivity(intent)
            })
        )
        if (JzActivity.getControlInstance().getLanguage() == Locale("de")) {
            homeItem.add(
                HomeItem("jz.478".getFix(), R.drawable.bt_tire_hotel_selecter,
                    {
                        PublicBean.position = PublicBean.TireHotel
                        EmptyDialog(R.layout.data_loading).show()
                        Api_Tire_Hotel.getRequestID {
                            if(Api_Tire_Hotel.requestType=="NA"){
                                handler.post {
                                    JzActivity.getControlInstance().closeDiaLog()
                                    JzActivity.getControlInstance().toast("jz.210".getFix())
                                }
                            }else{
                                handler.post{
                                    JzActivity.getControlInstance().closeDiaLog()
                                    if (Frag_Hotel_First_Info.isInital||(Api_Tire_Hotel.requestType!="00")) {
                                        JzActivity.getControlInstance()
                                            .changePage(Frag_TireHotel_List(), "Frag_TireHotel_List", true)
                                    } else {
                                        JzActivity.getControlInstance()
                                            .changePage(Frag_Hotel_First_Info(), "Frag_Hotel_First_Info", true)
                                    }
                                }

                            }
                        }
                    }) { Md_Tire_Hotel.lastCount }
            )

        }
        if (FileDowload.country != "JP") {
            homeItem.add(
                HomeItem(resources.jzString(R.string.idcopyobd2), R.drawable.bt_relearn_copy,
                    {
                        PublicBean.position = PublicBean.ID_COPY_OBD
                        JzActivity.getControlInstance()
                            .changePage(Frag_Select_Mmy(), "Frag_Obd", true)
                    })
            )
            homeItem.add(
                HomeItem(resources.jzString(R.string.app_home_obdii_relearna),
                    R.drawable.bt_obdii_relearn_selecter,
                    {
                        PublicBean.position = PublicBean.OBD_RELEARM
                        JzActivity.getControlInstance()
                            .changePage(Frag_Select_Mmy(), "Frag_Obd", true)
                    })
            )
        }


        homeItem.add(
            HomeItem(resources.jzString(R.string.Relearn_Procedure), R.drawable.bt_relearn,
                {
                    PublicBean.position = PublicBean.學碼步驟
                    JzActivity.getControlInstance()
                        .changePage(Frag_Select_Mmy(), "Frag_Relearm", true)
                })
        )
        if ((JzActivity.getControlInstance().getLanguage() != Locale("it"))&&(JzActivity.getControlInstance().getLanguage() != Locale("de"))) {
            homeItem.add(
                HomeItem(resources.jzString(R.string.Online_shopping),
                    R.drawable.bt_online_shopping_selecter,
                    {
                        PublicBean.position = PublicBean.Go_Web
                        JzActivity.getControlInstance()
                            .changePage(Frag_WebView(), "Frag_WebView", true)
                    })
            )
        }
        if ((JzActivity.getControlInstance()
                .getLanguage() == Locale("en") || JzActivity.getControlInstance()
                .getLanguage() == Locale("zh"))&&(OgPublic.admin=="orangerd"||OgPublic.admin=="orangeus")
        ) {
            val area=if(JzActivity.getControlInstance().getLanguage() == Locale("en")) "US" else "EU"
            homeItem.add(
                HomeItem("jz.509".getFix(), R.drawable.bt_ship, {
                        GlitterActivity.setUp("file:///android_asset/appData", appName = "appData",parameter = "?country=${area}")
                        val intent = Intent(activity, GlitterActivity::class.java)
                        startActivity(intent)
                })
            )
            if((JzActivity.getControlInstance()
                    .getLanguage() == Locale("en"))&&(OgPublic.admin=="orangeus")){
                homeItem.add(
                    HomeItem("jz.619".getFix(), R.drawable.bt_ship_add, {
                        GlitterActivity.setUp("file:///android_asset/appData", appName = "appData",parameter = "?country=${area}&Add_Ship=true")
                        val intent = Intent(activity, GlitterActivity::class.java)
                        startActivity(intent)
                    }))
            }
        }
        if(JzActivity.getControlInstance().getPro("RFID", false)){
            homeItem.add(
                HomeItem("RFID", R.drawable.btn_rfid,
                    {
                        Systool.hookWebView()
                        EmptyDialog(R.layout.data_loading).show()
                        Thread{
                            Cmd_Rfid.startRfid()
                            Thread.sleep(2000)
                            handler.post {
                                JzActivity.getControlInstance().closeDiaLog()
                                GlitterActivity.setUp("file:///android_asset/rfID", appName = "appData")
                                val intent = Intent(activity, GlitterActivity::class.java)
                                startActivity(intent)
                            }
                        }.start()
                    })
            )
        }

        homeItem.add(
            HomeItem(resources.jzString(R.string.Setting), R.drawable.bt_setting_selecter,
                {
                    PublicBean.position = PublicBean.設定
                    JzActivity.getControlInstance().changePage(Frag_Setting(), "Frag_Setting", true)
                })
        )

//        homeItem.add(
//            HomeItem("車牌辨識", R.drawable.bt_serch_selecter,
//                View.OnClickListener {
//                    PublicBean.position = PublicBean.車牌辨識
//                    JzActivity.getControlInstance().changePage(
//                        Frag_Plate_Recognize(),
//                        "Frag_Plate_Recognize",
//                        true
//                    )
//                })
//        )
//        homeItem.add(HomeItem(resources.jzString(R.string.Cloud_information),R.drawable.bt_cloud_information_selecter,
//            View.OnClickListener { }))
//        homeItem.add(HomeItem(resources.jzString(R.string.app_user_manual),R.drawable.bt_users_manual_selecter,
//            View.OnClickListener { }))
        val checkShipManager={
            val data= mutableMapOf<String,Any>()
            data["account"]=PublicBean.admin
            data["type"]="OG"
            RequestFun("PublicLogic","checkShipManager").request(data)
        }
        Thread{checkShipManager()}.start()
        rootview.re.layoutManager = GridLayoutManager(context, 2)
        rootview.re.adapter = adapter
    }

    override fun onLeft() {
        FocusReset(-1)
    }

    override fun onRight() {
        FocusReset(1)
    }

    var focus = 0
    override fun onTop() {
        FocusReset(-2)
    }

    override fun onDown() {
        FocusReset(2)
    }

    fun FocusReset(re: Int) {
        if (focus + re >= 0 && focus + re < homeItem.size) {
            focus += re
        }
        adapter.focus = focus
        rootview.re.scrollToPosition(focus)
        adapter.notifyDataSetChanged()
    }

    override fun enter() {
        homeItem[focus].onClickListener.onClick(null)
    }


}

