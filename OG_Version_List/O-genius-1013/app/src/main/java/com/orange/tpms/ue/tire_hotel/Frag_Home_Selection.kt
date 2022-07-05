package com.orange.tpms.ue.tire_hotel

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jianzhi.jzblehelper.callback.ConnectResult
import com.orange.jzchi.jzframework.JzActivity
import com.orange.jzchi.jzframework.JzAdapter
import com.orange.jzchi.jzframework.JzFragement
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orange.tpms.adapter.HomeItem
import com.orange.tpms.ue.activity.KtActivity
import com.orange.tpms.ue.kt_frag.*
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import com.orango.electronic.orange_og_lib.Util.jzString
import kotlinx.android.synthetic.main.activity_frag_home.view.*
import kotlinx.android.synthetic.main.frag_tire_home_selection.view.*
import kotlinx.android.synthetic.main.frag_tire_home_selection.view.re
import kotlinx.android.synthetic.main.homeitem.view.*
import kotlinx.android.synthetic.main.tire_home_list.view.*

class Frag_Home_Selection : RootFragement(R.layout.frag_tire_home_selection) {
    var adapter = home_list()
    var homeItem = ArrayList<HomeItem>()
    override fun viewInit() {
        rootview.fixLanguage()
        rootview.re.layoutManager = LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false)
        rootview.re.adapter = adapter
        rootview.mmytext.text=PublicBean.selectMmy.make+"/"+PublicBean.selectMmy.model+"/"+PublicBean.selectMmy.year
        homeItem.add(
            HomeItem(resources.jzString(R.string.app_home_check_sensor),
                R.drawable.bt_check_sensor_selecter,
                {
                PublicBean.position = PublicBean.檢查傳感器
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(selectFinish={
                            JzActivity.getControlInstance().changePage(Frag_Check_Sensor_Read(true),"Frag_Check_Sensor_Read",true)
                        }),
                        "Frag_Check_Sensor_Read",
                        true
                    )
                })
        )
        homeItem.add(
            HomeItem(resources.jzString(R.string.app_home_program_sensor_phone),
                R.drawable.bt_program_sensor_selecter,
                {
                    PublicBean.ProgramNumber=4
                    PublicBean.position = PublicBean.燒錄傳感器
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(selectFinish={
                            JzActivity.getControlInstance().changePage(Frag_New_ProgramDetail(true),"Frag_Program_Detail",true)
                        }),
                        "Frag_Program_Sensor",
                        true
                    )
                })
        )
        homeItem.add(
            HomeItem(resources.jzString(R.string.app_home_id_copy), R.drawable.bt_id_copy_selecter,
                {
                    PublicBean.position = PublicBean.複製傳感器
                    if(PublicBean.wheelCount()==2){
                        JzActivity.getControlInstance().changePage(
                            Frag_Motor_Select(false),
                            "Frag_Id_Copy",
                            true
                        )
                    }else{
                        JzActivity.getControlInstance().changePage(
                            Frag_Function_ID_Copy_Selection(true),
                            "Frag_Id_Copy",
                            true
                        )
                    }
                })
        )
        homeItem.add(
            HomeItem(resources.jzString(R.string.idcopyobd2), R.drawable.bt_relearn_copy,
                {
                    PublicBean.position = PublicBean.ID_COPY_OBD
                    val blemanager =
                        (JzActivity.getControlInstance().getRootActivity() as KtActivity).bleManager
                    blemanager.scan(ConnectResult {
                        if (it) {
                            if(PublicBean.wheelCount()==2){
                                JzActivity.getControlInstance().changePage(
                                    Frag_Motor_Select(false),
                                    "Frag_Obd",
                                    true
                                )
                            }else{
                                JzActivity.getControlInstance().changePage(
                                    Frag_Function_ID_Copy_Selection(false),
                                    "Frag_Obd",
                                    true
                                )
                            }
                        }
                        JzActivity.getControlInstance().closeDiaLog()
                    })

                })
        )

//        if(PublicBean.getRelearmMode()=="3"){
//            homeItem.add(
//                HomeItem(resources.jzString(R.string.app_home_obdii_relearna),
//                    R.drawable.bt_obdii_relearn_selecter,
//                    View.OnClickListener {
//                        PublicBean.position = PublicBean.OBD_RELEARM
//                        JzActivity.getControlInstance().changePage(Frag_Function_ID_Copy_Selection(true),"Frag_Function_ID_Copy_Selection",true)
//                    })
//            )
//        }
        rootview.menu.setOnClickListener {
            JzActivity.getControlInstance().goMenu()
        }
        adapter.notifyDataSetChanged()
    }

    inner class home_list : JzAdapter(R.layout.tire_home_list) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mView.textView30.text = homeItem[position].name
            holder.mView.imageView31.setImageDrawable(
                resources.getDrawable(
                    homeItem[position].img,
                    null
                )
            )
            holder.mView.setOnClickListener {
                homeItem[position].onClickListener.onClick(view)
            }
            holder.mView.imageView31.isSelected=focus==position
        }

        override fun sizeInit(): Int {
            return homeItem.size
        }
    }

    var focus = 0
    override fun onTop() {
        FocusReset(-1)
    }

    override fun onDown() {
        FocusReset(1)
    }

    override fun enter() {
        super.enter()
        homeItem[focus].onClickListener.onClick(rootview)
    }
    fun FocusReset(re: Int) {
        if (focus + re >= 0 && focus + re < homeItem.size) {
            focus += re
        }
        rootview.re.scrollToPosition(focus)
        adapter.notifyDataSetChanged()
    }
}